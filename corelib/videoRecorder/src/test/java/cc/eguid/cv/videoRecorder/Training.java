package cc.eguid.cv.videoRecorder;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;



public class Training {

    public void training(String[] args) throws Exception  {
        //准备两个人的训练图片，每个人脸十张
        //需要注意，训练的图片必须是相同大小的灰度图

        //读取图片保存到mat
        Mat y1 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0000.pgm",0);
        Mat y2 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0001.pgm",0);
        Mat y3 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0002.pgm",0);
        Mat y4 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0003.pgm",0);
        Mat y5 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0004.pgm",0);
        Mat y6 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0005.pgm",0);
        Mat y7 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0006.pgm",0);
        Mat y8 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0007.pgm",0);
        Mat y9 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0009.pgm",0);
        Mat y10 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0010.pgm",0);

        Mat y11 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0011.pgm",0);
        Mat y12 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0012.pgm",0);
        Mat y13 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0013.pgm",0);
        Mat y14 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0014.pgm",0);
        Mat y15 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0015.pgm",0);
        Mat y16 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0016.pgm",0);
        Mat y17 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0017.pgm",0);
        Mat y18 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0018.pgm",0);
        Mat y19 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0019.pgm",0);
        Mat y20 = opencv_imgcodecs.imread("/Users/mac/Downloads/FaceDB/data/BioID_0020.pgm",0);

        MatVector images =new MatVector(20);//一共20个训练样本
        Mat lables = new Mat(20,1,CV_32SC1);//对应20个标签值
        //写入标签值，前十个为1，后十个为2
        IntBuffer lablesBuf = lables.createBuffer();
        lablesBuf.put(0, 1);
        lablesBuf.put(1, 1);
        lablesBuf.put(2, 1);
        lablesBuf.put(3, 1);
        lablesBuf.put(4, 1);
        lablesBuf.put(5, 1);
        lablesBuf.put(6, 1);
        lablesBuf.put(7, 1);
        lablesBuf.put(8, 1);
        lablesBuf.put(9, 1);
        lablesBuf.put(10, 2);
        lablesBuf.put(11, 2);
        lablesBuf.put(12, 2);
        lablesBuf.put(13, 2);
        lablesBuf.put(14, 2);
        lablesBuf.put(15, 2);
        lablesBuf.put(16, 2);
        lablesBuf.put(17, 2);
        lablesBuf.put(18, 2);
        lablesBuf.put(19, 2);

        //写入图片
        images.put(0,y1);
        images.put(1,y2);
        images.put(2,y3);
        images.put(3,y4);
        images.put(4,y5);
        images.put(5,y6);
        images.put(6,y7);
        images.put(7,y8);
        images.put(8,y9);
        images.put(9,y10);
        images.put(10,y11);
        images.put(11,y12);
        images.put(12,y13);
        images.put(13,y14);
        images.put(14,y15);
        images.put(15,y16);
        images.put(16,y17);
        images.put(17,y18);
        images.put(18,y19);
        images.put(19,y20);

        //创建人脸分类器，有Fisher、Eigen、LBPH，选哪种自己决定，这里使用FisherFaceRecognizer
        FaceRecognizer fr = FisherFaceRecognizer.create();
        //训练
        fr.train(images, lables);
        //保存训练结果
        fr.save("FisherRecognize.xml");

        //读取训练出的xml文件
        fr.read("FisherRecognize.xml");
        //设置阈值，阈值为0则任何人都不认识，阈值特别大的时候任何人都认识（返回和样本最相似的结果，永远不会返回-1）
        //前面忘记说了，检测返回-1代表不能和训练结果匹配
        fr.setThreshold(3000.0);

    }


    public static void main(String[] args) throws Exception {
        FaceRecognizer fr = FisherFaceRecognizer.create();
        fr.read("FisherRecognize.xml");
        //*********************测试部分************************

        //开启摄像头，获取图像（得到的图像为frame类型，需要转换为mat类型进行检测和识别）
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setImageWidth(640);
        grabber.setImageHeight(480);
        grabber.start();

        CanvasFrame canvas = new CanvasFrame("人脸检测");//新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while (true) {
            if (!canvas.isEnabled()) {//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(0);//退出
            }
            Frame frame = grabber.grab();

            OpenCVFrameConverter.ToMat convertor = new OpenCVFrameConverter.ToMat();//用于类型转换
            Mat scr = convertor.convertToMat(frame);//将获取的frame转化成mat数据类型
            Mat grayscr = new Mat();
            Mat face = new Mat();
            Mat roi = new Mat();
            cvtColor(scr, grayscr, COLOR_BGRA2GRAY);//摄像头是彩色图像，所以先灰度化下
            equalizeHist(grayscr, grayscr);//均衡化直方图

            //读取opencv人脸检测器，参考我的路径改为自己的路径
            CascadeClassifier cascade = new CascadeClassifier(
                    "lbpcascade_frontalface_improved.xml");
            //检测人脸
            RectVector faces = new RectVector();
            cascade.detectMultiScale(grayscr, faces);

            IntPointer label = new IntPointer(1);
            DoublePointer confidence = new DoublePointer(1);

            //识别人脸
            for (int i = 0; i < faces.size(); i++) {
                Rect face_i = faces.get(i);
                rectangle(scr, face_i, new Scalar(0, 255, 0, 1));

                roi = new Mat(grayscr, face_i);
                resize(roi, face, new Size(384, 286));//我的训练样本是350*350，要对应的进行修改
                fr.predict(face, label, confidence);
                int predictedLabel = label.get(0);//得到识别的标签值

                //判断并显示
                if (predictedLabel == 1) {
                    String box_text = "people:YangPeng";
                    int pos_x = Math.max(face_i.tl().x() - 10, 0);
                    int pos_y = Math.max(face_i.tl().y() - 10, 0);
                    putText(scr, box_text, new Point(pos_x, pos_y),
                            FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0));
                } else if (predictedLabel == 2) {
                    int pos_x = Math.max(face_i.tl().x() - 10, 0);
                    int pos_y = Math.max(face_i.tl().y() - 10, 0);
                    // And now put it into the image:
                    putText(scr, "people:LiPengpeng", new Point(pos_x, pos_y),
                            FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0));
                } else {
                    int pos_x = Math.max(face_i.tl().x() - 10, 0);
                    int pos_y = Math.max(face_i.tl().y() - 10, 0);
                    // And now put it into the image:
                    putText(scr, "UnknownPeople!", new Point(pos_x, pos_y),
                            FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 0, 255, 2.0));
                }
            }

            //显示
            frame = convertor.convert(scr);//将检测结果重新的mat重新转化为frame
            canvas.showImage(frame);//获取摄像头图像并放到窗口上显示，frame是一帧视频图像
            /*try {
                Thread.sleep(20);//20毫秒刷新一次图像
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }
    }
}
