package com.seu.platform.util;

import lombok.experimental.UtilityClass;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_dnn;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_dnn.Net;

import java.io.File;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-10 10:58
 */
@UtilityClass
public class OpenCvUtil {
    public static void main(String[] args) {

        String modelPath = "/Users/cjl1996/Downloads/yolov5n-7-k5.onnx";
        String imagePath = "/Users/cjl1996/Downloads/673999.png";
        File file1 = new File(imagePath);
        System.out.println(file1.exists());
        Mat image = opencv_imgcodecs.imread(imagePath);
        File file = new File(modelPath);
        System.out.println(file.exists());
        Net net = opencv_dnn.readNetFromONNX(modelPath);
        Size size = new Size(660, 660);
        opencv_imgproc.resize(image, image, size);
        Mat blob = opencv_dnn.blobFromImage(image);
        net.setInput(image);
        Mat detections = net.forward();
        int numDetections = detections.rows();
        opencv_core.print(detections);
        System.out.println(11);
    }
}
