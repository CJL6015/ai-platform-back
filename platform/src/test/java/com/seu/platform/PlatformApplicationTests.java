package com.seu.platform;

import cn.hutool.core.io.FileUtil;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_dnn;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_dnn.Net;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.bytedeco.opencv.global.opencv_core.CV_8U;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_UNCHANGED;

@SpringBootTest
class PlatformApplicationTests {

    @Test
    void contextLoads() {
        String modelPath = "C:\\work\\民爆\\yolov5n-7-k5.onnx";
        String imagePath = "C:\\Users\\陈小黑\\Pictures\\Feedback\\{5E4348FF-52B7-47BA-9D02-D4DC34914017}\\Capture001.png";
        byte[] bytes = FileUtil.readBytes(imagePath);
        File file1 = new File(imagePath);
        System.out.println(file1.exists());
        Mat image = opencv_imgcodecs.imread(imagePath,IMREAD_UNCHANGED);
        File file = new File(modelPath);
        System.out.println(file.exists());
        Net net = opencv_dnn.readNetFromONNX(modelPath);
        Mat blob = opencv_dnn.blobFromImage(image, 1.0, new Size(640, 640),
                new Scalar(0), true, false, CV_8U);
        net.setInput(blob);
        Mat detections = net.forward();
        int numDetections = detections.rows();
        opencv_core.print(detections);
        System.out.println(11);
    }

}
