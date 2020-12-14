package cc.eguid.cv.videoRecorder;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.*;

import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;

public class FaceDetectionTest {

    /**
     * 人脸检测-eguid
     * @param cascadeClassifierXml 基于Haar特征的cascade正面人脸分类器
     * @param width 图像宽度
     * @param height 图像高度
     */
    public static void faceDetection(String cascadeClassifierXml,Integer width,Integer height) throws Exception, InterruptedException {
        // 开启摄像头，获取图像（得到的图像为frame类型，需要转换为mat类型进行检测和识别）
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        if(width!=null&&width>1&&height!=null&&height>1) {
            grabber.setImageWidth(width);
            grabber.setImageHeight(height);
        }
        grabber.start();

        if(width==null||height==null) {
            height=grabber.getImageHeight();
            width=grabber.getImageWidth();
        }

        CanvasFrame canvas = new CanvasFrame("人脸检测");// 新建一个预览窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setVisible(true);
        canvas.setFocusable(true);
        //窗口置顶
        if(canvas.isAlwaysOnTopSupported()) {
            canvas.setAlwaysOnTop(true);
        }
        Frame frame =null;

        // 读取opencv人脸检测器
        opencv_objdetect.CascadeClassifier cascade = new opencv_objdetect.CascadeClassifier(
                "/Users/mac/IdeaProjects/easyCV/lbpcascade_frontalface_improved.xml");

        for(;canvas.isVisible()&&(frame=grabber.grab())!=null;) {

            frame = grabber.grab();

            OpenCVFrameConverter.ToMat convertor = new OpenCVFrameConverter.ToMat();//用于类型转换
            opencv_core.Mat scr = convertor.convertToMat(frame);//将获取的frame转化成mat数据类型«
            opencv_core.Mat grayscr = new opencv_core.Mat();
            opencv_core.Mat face = new opencv_core.Mat();
            opencv_core.Mat roi = new opencv_core.Mat();
            cvtColor(scr, grayscr, COLOR_BGRA2GRAY);//摄像头是彩色图像，所以先灰度化下
            opencv_imgproc.equalizeHist(grayscr, grayscr);//均衡化直方图

            // 检测到的人脸
            opencv_core.RectVector faces = new opencv_core.RectVector();
            cascade.detectMultiScale(grayscr, faces);

            // 遍历人脸
            for (int i = 0; i < faces.size(); i++) {
                opencv_core.Rect face_i = faces.get(i);
                //绘制人脸矩形区域，scalar色彩顺序：BGR(蓝绿红)
                rectangle(scr, face_i, new opencv_core.Scalar(0, 255, 0, 1));

                int pos_x = Math.max(face_i.tl().x() - 10, 0);
                int pos_y = Math.max(face_i.tl().y() - 10, 0);
                // 在人脸矩形上方绘制提示文字
                putText(scr, "face", new opencv_core.Point(pos_x, pos_y),
                        FONT_HERSHEY_PLAIN, 1.0, new opencv_core.Scalar(0, 255, 0, 2.0));
            }

            canvas.showImage(frame);// 获取摄像头图像并放到窗口上显示，frame是一帧视频图像
            Thread.sleep(40);// 40毫秒刷新一次图像
        }
        cascade.close();
        canvas.dispose();
        grabber.close();// 停止抓取
    }

    public static void main(String[] args) throws Exception, InterruptedException {
        faceDetection("haarcascade_frontalface_alt.xml",960,540);
    }

}
