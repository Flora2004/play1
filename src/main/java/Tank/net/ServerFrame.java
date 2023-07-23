package Tank.net;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 10:02
 */
public class ServerFrame extends JFrame {
    public static final ServerFrame INSTANCE=new ServerFrame();

    Button btnStart=new Button("start");
    TextArea taLeft=new TextArea();
    TextArea taRight=new TextArea();
    Server server=new Server();
    DefaultMutableTreeNode tank;
    DefaultMutableTreeNode alive,dead;
    JTree tree;
    JScrollPane scrollPane;

    public ServerFrame(){
        this.setSize(1600,600);
        this.setLocation(300,30);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p=new JPanel(new GridLayout(1,3));
        p.add(taLeft);
        p.add(taRight);
        this.getContentPane().add(p);

        tank=new DefaultMutableTreeNode("tank");
        alive=new DefaultMutableTreeNode("alive");
        dead=new DefaultMutableTreeNode("dead");

        tank.add(alive);
        tank.add(dead);

        tree=new JTree(tank);
        tree.setCellRenderer(new DefaultTreeCellRenderer());

        scrollPane=new JScrollPane(tree);

        p.add(scrollPane);
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
