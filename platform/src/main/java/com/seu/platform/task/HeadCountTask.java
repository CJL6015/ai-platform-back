package com.seu.platform.task;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.util.ImageUtil;
import com.seu.platform.util.ODResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.FloatBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private final ProcessLinePictureHistService processLinePictureHistService;
    ExecutorService executorService = new ThreadPoolExecutor(9, 9,
            1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(10000),
            new ThreadFactoryBuilder().setNamePrefix("opencv-").build());

    @Value("${model.model-path}")
    private String modelPath;

    @Value("${model.out-dir}")
    private String outDir;

    private ThreadLocal<Object[]> threadLocal;


    @PostConstruct
    public void init() {
        nu.pattern.OpenCV.loadLocally();
        threadLocal = ThreadLocal.withInitial(() -> {
            Object[] model = new Object[2];
            OrtEnvironment environment = OrtEnvironment.getEnvironment();
            model[0] = environment;
            OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
            OrtSession session = null;
            try {
                session = environment.createSession(modelPath, sessionOptions);
            } catch (OrtException e) {
                throw new RuntimeException(e);
            }
            model[1] = session;
            return model;
        });
        test();
    }

    /**
     * 人员检测定时任务,每秒扫描一次数据库
     */
//    @Scheduled(fixedRate = 1000)
    public void doTask() {
        List<ProcessLinePictureHist> pendingChecks = processLinePictureHistService.getPendingChecks(CAMERA_COUNT);
        if (CollUtil.isEmpty(pendingChecks)) {
            log.info("未获取待检测图片");
        } else {
            log.info("本次待检测图片为:{}", pendingChecks);
        }
        for (ProcessLinePictureHist pendingCheck : pendingChecks) {
            executorService.execute(() -> detection(pendingCheck));
        }
    }

    public void test() {
        String path = "C:\\work\\model\\p1.jpg";
        String outPath = "C:\\work\\model\\p2.jpg";
        for (int i = 0; i < 60; i++) {
            executorService.execute(() -> {
                try {
                    getPeopleCount(path, outPath);
                } catch (OrtException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    public void detection(ProcessLinePictureHist pendingCheck) {
        String picturePath = pendingCheck.getPicturePath();
        String outPath = outDir + picturePath.substring(picturePath.lastIndexOf("\\"));
        try {
            int peopleCount = getPeopleCount(picturePath, outPath);
            pendingCheck.setDetectionPicturePath(outPath);
            pendingCheck.setUpdateTime(new Date());
            pendingCheck.setPeopleCount(peopleCount);
            processLinePictureHistService.updateById(pendingCheck);
        } catch (OrtException e) {
            log.error("人员检测异常,path:{}", picturePath, e);
        }
    }

    private int getPeopleCount(String picturePath, String outPath) throws OrtException {
        OrtSession session = (OrtSession) threadLocal.get()[1];
        long t1 = System.currentTimeMillis();
        Mat image = Imgcodecs.imread(picturePath);
        Mat outImage = Imgcodecs.imread(picturePath);
        long t2 = System.currentTimeMillis();
        log.info("加载图片耗时:{}", (t2 - t1));
        VideoLetterbox letterbox = new VideoLetterbox();
        image = letterbox.letterbox(image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);
        image.convertTo(image, CvType.CV_32FC1, 1. / 255);
        float[] whc = new float[3 * 640 * 640];
        image.get(0, 0, whc);
        float[] chw = ImageUtil.whc2cwh(whc);

        OrtEnvironment environment = (OrtEnvironment) threadLocal.get()[0];
        FloatBuffer inputBuffer = FloatBuffer.wrap(chw);
        OnnxTensor tensor = OnnxTensor.createTensor(environment, inputBuffer, new long[]{1, 3, 640, 640});


        HashMap<String, OnnxTensor> stringOnnxTensorHashMap = new HashMap<>();
        stringOnnxTensorHashMap.put(session.getInputInfo().keySet().iterator().next(), tensor);

        // 运行推理
        // 模型推理本质是多维矩阵运算，而GPU是专门用于矩阵运算，占用率低，如果使用cpu也可以运行，可能占用率100%属于正常现象，不必纠结。
        OrtSession.Result output = session.run(stringOnnxTensorHashMap);
        long t3 = System.currentTimeMillis();
        log.info("预测时间：{}", (t3 - t2));
        // 得到结果,缓存结果
        float[][] outputData = ((float[][][]) output.get(0).getValue())[0];
        int peopleCount = 0;
        for (float[] x : outputData) {
            if (x[6] < 0.25) {
                continue;
            }
            ODResult odResult = new ODResult(x);
            // 画框
            Point topLeft = new Point((odResult.getX0() - letterbox.getDw()) / letterbox.getRatio(), (odResult.getY0() - letterbox.getDh()) / letterbox.getRatio());
            Point bottomRight = new Point((odResult.getX1() - letterbox.getDw()) / letterbox.getRatio(), (odResult.getY1() - letterbox.getDh()) / letterbox.getRatio());
            Scalar color = new Scalar(255, 0, 0);

            Imgproc.rectangle(outImage, topLeft, bottomRight, color, 5);
            // 框上写文字
            String boxName = "person";
            Point boxNameLoc = new Point((odResult.getX0() - letterbox.getDw()) / letterbox.getRatio(), (odResult.getY0() - letterbox.getDh()) / letterbox.getRatio() - 3);
            peopleCount++;
            // 也可以二次往视频画面上叠加其他文字或者数据，比如物联网设备数据等等
            Imgproc.putText(outImage, boxName, boxNameLoc, Imgproc.FONT_HERSHEY_SIMPLEX, 10, color, 20);

        }
        Imgcodecs.imwrite(outPath, outImage);
        long t4 = System.currentTimeMillis();
        log.info("检测完成,耗时:{}", (t4 - t1));
        return peopleCount;
    }
}
