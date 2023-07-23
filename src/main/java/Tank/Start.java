package Tank;

import Tank.net.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 13:38
 */
public class Start extends JFrame {
    //窗口长宽
    int width=800;
    int height=800;
    //中间面板
    JLayeredPane jLayeredPane;
    JPanel panel1,panel2,panel3;
    //游戏开始按钮,退出按钮
    JLabel startLab,exitLab;
    //游戏标题
    JLabel title;

    public static void main(String[] args) {
       Start start =new Start();
           start.launch();
        new Thread(() -> new Audio("audio//war1.wav").loop()).start();

    }

    //窗口的启动方法
    public void launch(){

        //标题
        setTitle("tank war");
        //窗口初始大小
        setSize(width,height);
        //使屏幕居中
        setLocationRelativeTo(null);
        //添加关闭事件
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //用户不能调整大小
        setResizable(false);
        //使窗口可见
        setVisible(true);

        startLab=new JLabel("开始游戏");
        exitLab=new JLabel("退出游戏");
        title=new JLabel("坦克大战");
        jLayeredPane=new JLayeredPane();
        panel1=new JPanel();
        panel2=new JPanel();
        panel3=new JPanel();
        panel1.setBackground(Color.BLACK);
        panel1.setBounds(0,0,width,height);
        panel2.setBackground(Color.GRAY);
        panel2.setBounds(40,40,width-80,height-80);
        panel3.setBackground(Color.white);
        panel3.setBounds(80,80,width-160,height-160);
        panel3.setLayout(null);

        this.setContentPane(jLayeredPane);
        jLayeredPane.add(panel1,JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.add(panel2,JLayeredPane.PALETTE_LAYER);

        //设置标题
        JLabel j1 = new JLabel("tank war");
        //用户文本
        JLabel userLa = new JLabel();
        userLa.setText("用户名：");
        userLa.setSize(60, 50);
        userLa.setLocation(100, 80);

        //密码文本
        JLabel pwdLa = new JLabel();
        pwdLa.setText("密码：");
        pwdLa.setSize(50, 50);
        pwdLa.setLocation(100, 120);

        //用户输入框
        JTextField userTxt = new JTextField();
        userTxt.setSize(100, 20);
        //this.setSize(width, height)
        userTxt.setLocation(170, 95);

        //密码输入框
        JPasswordField pwdTxt = new JPasswordField();
        pwdTxt.setSize(100, 20);
        pwdTxt.setLocation(170, 135);

        //确认按钮
        JButton sureBt = new JButton("登录");
        sureBt.setSize(60, 25);
        sureBt.setLocation(135, 260);
        sureBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //将登录窗口隐藏
                changeVision();

                //向玩家邮箱发送登录提示
//                try {
//                    Mail mail = new Mail();
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }

                //开启坦克大战
                TankFrame tankFrame = TankFrame.INSTANCE;
                tankFrame.launch();
                tankFrame.setVisible(true);

                //新开一个线程用来播放背景音乐
                new Thread(() -> new Audio("audio//war1.wav").loop()).start();

                //另一个线程用来刷新游戏界面
                new Thread() {
                    public void run() {

                        new Thread(() -> {
                            while (true) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                tankFrame.repaint();
                            }
                        }).start();

                        //connect to the server
                        Client.INSTANCE.connect();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } // 每秒10帧
                    }
                }.start();
            }
        });

        //退出按钮
        JButton quitBt = new JButton("退出");
        quitBt.setSize(60, 25);
        quitBt.setLocation(240, 260);
        quitBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel3.add(userLa);
        panel3.add(pwdLa);
        panel3.add(userTxt);
        panel3.add(pwdTxt);
        panel3.add(sureBt);
        panel3.add(quitBt);
        panel3.add(j1);

        jLayeredPane.add(panel3,JLayeredPane.POPUP_LAYER);

    }
   public void changeVision(){
        this.setVisible(false);
   }
}
