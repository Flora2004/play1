/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-22
 * Time: 15:15
 */
import javax.swing.*;

public class ConfirmDialogExample {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Confirmation Dialog Example");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton button = new JButton("Show Confirm Dialog");
            button.addActionListener(e -> {
                // 显示确认对话框，询问用户是否继续
                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Do you want to continue?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                // 根据用户的选择做出相应的处理
                if (result == JOptionPane.YES_OPTION) {
                    System.out.println("User chose 'Yes'.");
                } else if (result == JOptionPane.NO_OPTION) {
                    System.out.println("User chose 'No'.");
                } else if (result == JOptionPane.CLOSED_OPTION) {
                    System.out.println("User closed the dialog without making a choice.");
                }
            });

            frame.add(button);
            frame.setVisible(true);
        });
    }
}
