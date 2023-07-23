package client;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-22
 * Time: 23:52
 */
public class Mail {
    //创建一封邮件
    Properties props=new Properties();
    //用于链接邮件服务器的参数配置(发送邮件时才需要用到)
    Session session=Session.getInstance(props);     //根据参数配置，创建会话对象
    MimeMessage message=new MimeMessage(session);   //创建邮件对象
    //发件人
}
