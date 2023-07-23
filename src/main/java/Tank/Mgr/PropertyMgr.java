package Tank.Mgr;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Description:利用配置文件更好的测试程序
 * User: angel
 * Date: 2023-07-18
 * Time: 13:51
 */
public class PropertyMgr {
    static Properties props=new Properties();
    static {
        try {
            //找到同一目录下的config文档后录入配置文件
            props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Object get(String key){
        if(props==null){
            return null;
        }
        return props.get(key);
    }

    //int getInt(key)
    //getString(key)
}
