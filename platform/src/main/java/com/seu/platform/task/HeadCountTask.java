package com.seu.platform.task;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.seu.platform.dao.entity.ProcessLinePictureHist1;
import com.seu.platform.dao.service.ProcessLinePictureHist1Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-28 10:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HeadCountTask {
    private static final int CAMERA_COUNT = 9;

    private static final float CONF_THRESHOLD = 0.5F;

    private static final float NMS_THRESHOLD = 0.55F;

    private static final double[] COLOR = {0, 0, 255};
    private static final String[] LABELS = {"person", "no_man"};

    private final ProcessLinePictureHist1Service processLinePictureHistService;

    private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(4, 4,
            1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(20),
            new ThreadFactoryBuilder().setNamePrefix("opencv-").build());

    private Set<Long> concurrentSet = ConcurrentHashMap.newKeySet();

    private Lock lock = new ReentrantLock();

    private Lock modelLock = new ReentrantLock();

    @Value("${model.model-path}")
    private String modelPath;

    @Value("${model.out-dir}")
    private String outDir;

    @Value("${model.input-dir}")
    private String inputDir;


    public static void xywh2xyxy(float[] bbox) {
        float x = bbox[0];
        float y = bbox[1];
        float w = bbox[2];
        float h = bbox[3];

        bbox[0] = x - w * 0.5f;
        bbox[1] = y - h * 0.5f;
        bbox[2] = x + w * 0.5f;
        bbox[3] = y + h * 0.5f;
    }

    public static int argmax(float[] a) {
        float re = -Float.MAX_VALUE;
        int arg = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] >= re) {
                re = a[i];
                arg = i;
            }
        }
        return arg;
    }

    public static List<float[]> nonMaxSuppression(List<float[]> bboxes, float iouThreshold) {

        List<float[]> bestBboxes = new ArrayList<>();

        bboxes.sort(Comparator.comparing(a -> a[4]));

        while (!bboxes.isEmpty()) {
            float[] bestBbox = bboxes.remove(bboxes.size() - 1);
            bestBboxes.add(bestBbox);
            bboxes = bboxes.stream().filter(a -> computeIOU(a, bestBbox) < iouThreshold).collect(Collectors.toList());
        }

        return bestBboxes;
    }

    public static float computeIOU(float[] box1, float[] box2) {

        float area1 = (box1[2] - box1[0]) * (box1[3] - box1[1]);
        float area2 = (box2[2] - box2[0]) * (box2[3] - box2[1]);

        float left = Math.max(box1[0], box2[0]);
        float top = Math.max(box1[1], box2[1]);
        float right = Math.min(box1[2], box2[2]);
        float bottom = Math.min(box1[3], box2[3]);

        float interArea = Math.max(right - left, 0) * Math.max(bottom - top, 0);
        float unionArea = area1 + area2 - interArea;
        return Math.max(interArea / unionArea, 1e-8f);

    }

    @PostConstruct
    public void init() {
        nu.pattern.OpenCV.loadLocally();
        test();
    }

    public void test() {
        try {
            String input = "C:\\work\\model\\p3.jpg";
            String output = "C:\\work\\model\\test_1.jpg";
            int peopleCount = getPeopleCount(input, output);
            System.out.println(peopleCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 人员检测定时任务,每秒扫描一次数据库
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Scheduled(fixedDelay = 100)
    public void doTask() {
        lock.lock();
        try {
            int activeCount = concurrentSet.size();
            if (activeCount > 20) {
                return;
            }
            List<ProcessLinePictureHist1> pendingChecks = processLinePictureHistService.getPendingChecks(CAMERA_COUNT, concurrentSet);
            if (CollUtil.isNotEmpty(pendingChecks)) {
                for (ProcessLinePictureHist1 pendingCheck : pendingChecks) {
                    Long id = pendingCheck.getId();
                    if (!concurrentSet.contains(id) && concurrentSet.size() < 20) {
                        concurrentSet.add(id);
                        executorService.execute(() -> detection(pendingCheck));
                    }
                }
            }
        } catch (Exception e) {
            log.error("提交任务异常", e);
        } finally {
            lock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void detection(ProcessLinePictureHist1 pendingCheck) {
        log.info("{}开始检测", pendingCheck.getId());
        String picturePath = pendingCheck.getPicturePath();
        String fileName = pendingCheck.getPicturePath().substring(picturePath.lastIndexOf("\\") + 1);
        String outPath = outDir + fileName;
        String inputPath = inputDir + fileName;
        try {
            int peopleCount = getPeopleCount(inputPath, outPath);
            pendingCheck.setDetectionPicturePath("http://114.55.245.123/api/static/images/" + fileName);
            pendingCheck.setPeopleCount(peopleCount);
            pendingCheck.setPeopleHasChecked(1);
            processLinePictureHistService.updateById(pendingCheck);
        } catch (Exception e) {
            log.error("人员检测异常,path:{}", picturePath, e);
        } finally {
            lock.lock();
            try {
                concurrentSet.remove(pendingCheck.getId());
            } finally {
                lock.unlock();
            }
        }
        log.info("{}检测结束", pendingCheck.getId());
    }


    private int getPeopleCount(String picturePath, String outPath) throws Exception {
        if (!new File(picturePath).exists()) {
            log.warn("{}不存在", picturePath);
            return 0;
        }

        OrtSession session = null;
        try (OrtEnvironment environment = OrtEnvironment.getEnvironment();
             OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        ) {
            modelLock.lock();
            try {
                session = environment.createSession(modelPath, sessionOptions);
            } finally {
                modelLock.unlock();
            }
            Mat image = Imgcodecs.imread(picturePath);
            Size originalSize = image.size();
            Size size = new Size(640, 640);
            Mat outImage = image.clone();
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);
            VideoLetterbox letterbox = new VideoLetterbox(size);
            image = letterbox.letterbox(image);

            double ratio = letterbox.getRatio();
            double dw = letterbox.getDw();
            double dh = letterbox.getDh();
            int rows = letterbox.getHeight();
            int cols = letterbox.getWidth();
            int channels = image.channels();
            // 将Mat对象的像素值赋值给Float[]对象
            float[] pixels = new float[channels * rows * cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double[] pixel = image.get(j,i);
                    for (int k = 0; k < channels; k++) {
                        // 这样设置相当于同时做了image.transpose((2, 0, 1))操作，切换了RGB到
                        pixels[rows*cols*k+j*cols+i] = (float) pixel[k]/255.0f;
                    }
                }
            }

            // 创建OnnxTensor对象
            long[] shape = {1L, (long) channels, (long) rows, (long) cols};
            OnnxTensor tensor = OnnxTensor.createTensor(environment, FloatBuffer.wrap(pixels), shape);


            HashMap<String, OnnxTensor> stringOnnxTensorHashMap = new HashMap<>();
            stringOnnxTensorHashMap.put(session.getInputInfo().keySet().iterator().next(), tensor);

            // 运行推理
            // 模型推理本质是多维矩阵运算，而GPU是专门用于矩阵运算，占用率低，如果使用cpu也可以运行，可能占用率100%属于正常现象，不必纠结。
            OrtSession.Result output = session.run(stringOnnxTensorHashMap);
            // 得到结果,缓存结果
            tensor.close();
            float[][] outputData = ((float[][][]) output.get(0).getValue())[0];
            if (modelPath.contains("yolov8m") || modelPath.contains("best")) {
                float[][] outputD = outputData;
                int numRows = outputD.length;
                int numCols = 0;

                // 计算输入数组的列数，并检查每行的列数是否一致
                for (int i = 0; i < numRows; i++) {
                    if (i == 0) {
                        numCols = outputD[i].length;
                    } else if (outputD[i].length != numCols) {
                        throw new IllegalArgumentException("Input array rows have different lengths.");
                    }
                }

                // 创建转置后的数组
                float[][] transposedArray = new float[numCols][numRows];

                // 执行转置
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        transposedArray[j][i] = outputD[i][j];
                    }
                }
                outputData = transposedArray;
            }
            Map<Integer, List<float[]>> class2Bbox = new HashMap<>();
            for (float[] bbox : outputData) {

                // center_x,center_y, width, height，score
                float score = bbox[4];
                if (score < CONF_THRESHOLD) {
                    continue;
                }

                // 获取标签
                float[] conditionalProbabilities = Arrays.copyOfRange(bbox, 5, bbox.length);
                int label = argmax(conditionalProbabilities);

                xywh2xyxy(bbox);

                // 去除无效结果
                if (bbox[0] >= bbox[2] || bbox[1] >= bbox[3]) {
                    continue;
                }

                class2Bbox.putIfAbsent(label, new ArrayList<>());
                class2Bbox.get(label).add(bbox);
            }

            List<Detection> detections = new ArrayList<>();
            if (CollUtil.isNotEmpty(class2Bbox)) {
                for (Map.Entry<Integer, List<float[]>> entry : class2Bbox.entrySet()) {
                    List<float[]> bboxes = entry.getValue();
                    bboxes = nonMaxSuppression(bboxes, NMS_THRESHOLD);
                    for (float[] bbox : bboxes) {
                        Integer key = entry.getKey();
                        String labelString = LABELS[0];
                        detections.add(new Detection(labelString, key, Arrays.copyOfRange(bbox, 0, 4), bbox[4]));
                    }
                }
                int minDwDh = Math.min(image.width(), image.height());
                int thickness = minDwDh / ODConfig.lineThicknessRatio;
                DecimalFormat df = new DecimalFormat("0.00");
                for (Detection detection : detections) {
                    float[] bbox = detection.getBbox();
                    // 画框
                    Point topLeft = new Point((bbox[0] - dw) / ratio, (bbox[1] - dh) / ratio);
                    Point bottomRight = new Point((bbox[2] - dw) / ratio, (bbox[3] - dh) / ratio);
                    Scalar color = new Scalar(COLOR);
                    Imgproc.rectangle(outImage, topLeft, bottomRight, color, thickness);
                    // 框上写文字
                    Point boxNameLoc = new Point((bbox[0] - dw) / ratio, (bbox[1] - dh) / ratio - 3 + 20);
                    Imgproc.putText(outImage, df.format(detection.confidence),
                            boxNameLoc, Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, thickness);
                }
            }
            int peopleCount = detections.size();
            Imgproc.resize(outImage, outImage, originalSize);
            Imgcodecs.imwrite(outPath, outImage);
            output.close();

            return peopleCount;
        } catch (Exception e) {
            log.error("{}检测异常", picturePath, e);
            throw new Exception(e);
        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
        }
    }
}
