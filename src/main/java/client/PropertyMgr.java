package client;

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
