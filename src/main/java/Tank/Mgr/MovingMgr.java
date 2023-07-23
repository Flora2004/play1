package Tank.Mgr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-23
 * Time: 19:15
 */
public class MovingMgr {
    public  BufferedImage[] explodes=new BufferedImage[16];
    public BufferedImage out(int i){
        try {
                explodes[i] = ImageIO.read(MovingMgr.class.getClassLoader().getResourceAsStream("images/e" + (i + 1) + ".gif"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return explodes[i];
    }
}
