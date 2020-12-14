package cc.eguid.cv.videoRecorder;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.util.EnumSet;

/**
 * @描述 TODO
 * @创建时间 2020/12/14 16:27
 * @创建人 jiangchun
 */
public class RecordPushTest {

    /**
     * 转流器
     * @param inputFile
     * @param outputFile
     * @throws Exception
     * @throws org.bytedeco.javacv.FrameRecorder.Exception
     * @throws InterruptedException
     */
    public static void recordPush(String inputFile,String outputFile,int v_rs) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception, InterruptedException{

        FrameGrabber grabber =new FFmpegFrameGrabber(inputFile);
        try {
            grabber.start();
        } catch (Exception e) {
            throw e;
        }
        //一个opencv视频帧转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        Frame grabframe =grabber.grab();
        opencv_core.IplImage grabbedImage =null;
        if(grabframe!=null){
            System.out.println("取到第一帧");
            grabbedImage = converter.convert(grabframe);
        }else{
            System.out.println("没有取到第一帧");
        }
        //如果想要保存图片,可以使用 opencv_imgcodecs.cvSaveImage("hello.jpg", grabbedImage);来保存图片

        FrameRecorder recorder;
        try {
            recorder = FrameRecorder.createDefault(outputFile, 1280, 720);
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            throw e;
        }

        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264
        recorder.setFormat("flv");
        recorder.setFrameRate(v_rs);
        recorder.setGopSize(v_rs);

        try {
            recorder.start();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            System.out.println("录制器启动失败");
            throw e;

        }

        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        Frame rotatedFrame=converter.convert(grabbedImage);
        while (frame.isVisible() && (grabframe=grabber.grab()) != null) {
            frame.showImage(grabframe);
            if(rotatedFrame!=null){
                recorder.record(rotatedFrame);
            }
        }
        recorder.close();
        grabber.close();
    }

    public static void main(String[] args)
            throws Exception{

        String inputFile = "D:\\video\\easyCV\\recorde.flv";

        String outputFile = "rtmp://192.168.0.21/live/pushFlow";

        recordPush(inputFile, outputFile, 25);
    }

}
