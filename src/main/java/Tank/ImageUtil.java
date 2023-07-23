package Tank;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-18
 * Time: 9:47
 */
public class ImageUtil {
    /**
     * 旋转图像的方法。
     * 将输入的 BufferedImage 对象按照指定角度进行旋转，并返回旋转后的新 BufferedImage 对象。
     *
     * @param bufferedimage 要旋转的原始 BufferedImage 对象。
     * @param degree        旋转角度，单位为度（degree），正值表示顺时针旋转，负值表示逆时针旋转。
     * @return 旋转后的新 BufferedImage 对象。
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
                                            final int degree) {
        int w = bufferedimage.getWidth();// 获取图像的宽度
        int h = bufferedimage.getHeight();// 获取图像的高度
        int type = bufferedimage.getColorModel().getTransparency();// 获取图像的透明度类型
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);// 设置图像渲染插值方式为双线性插值
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 按照指定角度旋转图像
        graphics2d.drawImage(bufferedimage, 0, 0, null);// 在新图像上绘制原始图像
        graphics2d.dispose();// 释放绘图资源
        return img;// 返回旋转后的新 BufferedImage 对象
    }
}
