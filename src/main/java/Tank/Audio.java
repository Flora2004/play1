package Tank;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-18
 * Time: 14:45
 */
public class Audio {
    byte[] b = new byte[1024 * 1024 * 15];

    /**
     * 循环播放音频的方法。
     * 该方法会不断循环播放音频文件，直到程序手动终止。
     */
    public void loop() {
        try {

            while (true) {
                int len = 0;
                sourceDataLine.open(audioFormat, 1024 * 1024 * 15);// 打开音频输出设备通道
                sourceDataLine.start();// 启动音频输出设备通道
                audioInputStream.mark(12358946);// 标记音频流位置
                while ((len = audioInputStream.read(b)) > 0) {// 将音频数据写入音频输出设备通道，实现音频播放
                    sourceDataLine.write(b, 0, len);
                }
                audioInputStream.reset(); // 重置音频流到标记的位置

                sourceDataLine.drain();// 等待音频输出设备通道中的所有数据被播放完毕
                sourceDataLine.close();// 关闭音频输出设备通道
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AudioFormat audioFormat = null;// 音频格式
    private SourceDataLine sourceDataLine = null;// 音频输出设备通道
    private DataLine.Info dataLine_info = null;// 音频输出设备信息

    private AudioInputStream audioInputStream = null;// 音频输入流

    /**
     * 构造函数，用于初始化音频对象。
     * @param fileName 音频文件的文件名或路径。
     */
    public Audio(String fileName) {
        try {
            // 获取音频输入流
            audioInputStream = AudioSystem.getAudioInputStream(Audio.class.getClassLoader().getResource(fileName));
            audioFormat = audioInputStream.getFormat();// 获取音频格式
            dataLine_info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLine_info);
            //FloatControl volctrl=(FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            //volctrl.setValue(-40);//

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 播放音频的方法。
     * 该方法会播放一次音频文件。
     */
    public void play() {
        try {
            byte[] b = new byte[1024*5];
            int len = 0;
            sourceDataLine.open(audioFormat, 1024*5);// 打开音频输出设备通道
            sourceDataLine.start();// 启动音频输出设备通道
            audioInputStream.mark(12358946);// 标记音频流位置
            while ((len = audioInputStream.read(b)) > 0) {
                sourceDataLine.write(b, 0, len);// 将音频数据写入音频输出设备通道，实现音频播放
            }
            // audioInputStream.reset();// 不重置音频流，只播放一次

            sourceDataLine.drain();// 等待音频输出设备通道中的所有数据被播放完毕
            sourceDataLine.close();// 关闭音频输出设备通道

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void close() {
        try {
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Audio a = new Audio("audio/explode.wav");
        Audio a = new Audio("audio/war1.wav");
        a.loop();

    }
}
