package client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 20:45
 */
public class ResourceMgr {
     public static BufferedImage tankL,tankU,tankR,tankD;
     public static BufferedImage bulletL,bulletU,bulletR,bulletD;
     public static BufferedImage[] explodes=new BufferedImage[16];

     static {
         try {
             tankU = ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/BadTank1.png"));
             tankL =ImageUtil.rotateImage(tankU,-90);
             tankR =ImageUtil.rotateImage(tankU,90);
             tankD =ImageUtil.rotateImage(tankU,180);


             bulletU = ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/bulletU.png"));
             bulletL= ImageUtil.rotateImage(bulletU,-90);
             bulletR= ImageUtil.rotateImage(bulletU,90);
             bulletD= ImageUtil.rotateImage(bulletU,180);


             for (int i = 0; i < 16; i++) {
                 explodes[i]= ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/e"+(i+1)+".gif"));
             }

         }catch (IOException e){
             e.printStackTrace();
         }
     }
}
