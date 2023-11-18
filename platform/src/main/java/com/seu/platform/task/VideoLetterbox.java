package com.seu.platform.task;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class VideoLetterbox {

    private Size newShape = new Size(640, 640);
    public void setSize(String input) {
        if ("yolov5s".equals(input)|| "yolov8m".equals(input)) {
            newShape = new Size(640, 640);
        } else if ("best".equals(input)) {
            newShape = new Size(1088, 1088);
        }
    }
    //    private Size newShape = new Size(1088, 1088);
    private final double[] color = new double[]{114,114,114};
    private final Boolean auto = false;
    private final Boolean scaleUp = true;
    private Integer stride = 32;

    private double ratio;
    private double dw;
    private double dh;

    public double getRatio() {
        return ratio;
    }

    public double getDw() {
        return dw;
    }

    public Integer getWidth() {
        return (int) this.newShape.width;
    }

    public Integer getHeight() {
        return (int) this.newShape.height;
    }

    public double getDh() {
        return dh;
    }

    public void setNewShape(Size newShape) {
        this.newShape = newShape;
    }

    public void setStride(Integer stride) {
        this.stride = stride;
    }

    public VideoLetterbox(Size newShape) {
        this.newShape = newShape;
    }

    public Mat letterbox(Mat im) { // 调整图像大小和填充图像，使满足步长约束，并记录参数
        // 获取原始图像的尺寸
        int originalHeight = im.rows();
        int originalWidth = im.cols();
        // 选择较长的边作为正方形图像的边长
        int maxDimension = Math.max(originalHeight, originalWidth);

        // 创建一个黑色的正方形画布
        Mat squareCanvas = Mat.zeros(maxDimension, maxDimension, im.type());

        // 计算要将原始图像置于正中间时的起始坐标
        int x = (maxDimension - originalWidth) / 2;
        int y = (maxDimension - originalHeight) / 2;

        // 将原始图像复制到正方形画布的中央
        Mat roi = squareCanvas.submat(y, y + originalHeight, x, x + originalWidth);
        im.copyTo(roi);

        // 缩放图像到网络输入大小 (640, 640)
        Mat resizedImage = new Mat();
        Size sz = new Size(this.newShape.width, this.newShape.height);
        Imgproc.resize(squareCanvas, resizedImage, sz);

        // 更新缩放比例和填充信息
        this.ratio = this.newShape.width / maxDimension;
        this.dw = x * this.ratio;
        this.dh = y * this.ratio;

        // 返回缩放后的图像
        return resizedImage;
    }
}
