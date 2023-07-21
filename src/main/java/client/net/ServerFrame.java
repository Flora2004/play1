package client.net;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 10:02
 */
public class ServerFrame extends Frame {
    public static final ServerFrame INSTANCE=new ServerFrame();

    Button btnStart=new Button("start");
    TextArea taLeft=new TextArea();
    TextArea taRight=new TextArea();
    Server server=new Server();

    public ServerFrame(){
        this.setSize(1600,600);
        this.setLocation(300,30);
//        this.add(btnStart,BorderLayout.NORTH);
        Panel p=new Panel(new GridLayout(1,2));
        p.add(taLeft);
        p.add(taRight);
        this.add(p);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.serverStart();
    }
    public void updateServerMsg(String string){
        this.taLeft.setText(taLeft.getText()+string+"\n");
    }
    public void updateClientMsg(String string){
        this.taRight.setText(taRight.getText()+string+"\n");
    }
}
