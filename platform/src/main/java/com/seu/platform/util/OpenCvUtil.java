package com.seu.platform.util;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import lombok.experimental.UtilityClass;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-10 10:58
 */
@UtilityClass
public class OpenCvUtil {
    public static void main(String[] args) throws OrtException, IOException {
        nu.pattern.OpenCV.loadLocally();
        String modelPath = "/Users/cjl1996/Downloads/yolov5s1.onnx";
        String imagePath = "/Users/cjl1996/Downloads/673999.png";
        String outPath = "/Users/cjl1996/Downloads/yolov.png";
        OrtEnvironment environment = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        OrtSession session = environment.createSession(modelPath, sessionOptions);
        Mat image = Imgcodecs.imread(imagePath);
        Mat outImage = Imgcodecs.imread(imagePath);
        VideoLetterbox letterbox = new VideoLetterbox();
        image = letterbox.letterbox(image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);
        image.convertTo(image, CvType.CV_32FC1, 1. / 255);
        float[] whc = new float[3 * 640 * 640];
        image.get(0, 0, whc);
        float[] chw = ImageUtil.whc2cwh(whc);


        FloatBuffer inputBuffer = FloatBuffer.wrap(chw);
        OnnxTensor tensor = OnnxTensor.createTensor(environment, inputBuffer, new long[]{1, 3, 640, 640});


        HashMap<String, OnnxTensor> stringOnnxTensorHashMap = new HashMap<>();
        stringOnnxTensorHashMap.put(session.getInputInfo().keySet().iterator().next(), tensor);

        // 运行推理
        // 模型推理本质是多维矩阵运算，而GPU是专门用于矩阵运算，占用率低，如果使用cpu也可以运行，可能占用率100%属于正常现象，不必纠结。
        OrtSession.Result output = session.run(stringOnnxTensorHashMap);

        // 得到结果,缓存结果
        float[][] outputData = ((float[][][]) output.get(0).getValue())[0];
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

            // 也可以二次往视频画面上叠加其他文字或者数据，比如物联网设备数据等等
            Imgproc.putText(outImage, boxName, boxNameLoc, Imgproc.FONT_HERSHEY_SIMPLEX, 10, color, 20);
            System.out.println(odResult + "   " + boxName);

        }
        Imgcodecs.imwrite(outPath, outImage);
        System.out.println(11);
    }

}
