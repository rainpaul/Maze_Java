import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;

public class useMaze {

	/**
	 * Launch the application.
	 */	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
				    // If Nimbus is not available, you can set the GUI to another look and feel.
				}
				try {
					Maze window = new Maze();
					window.frame.setVisible(true);
					//Set the JFram to center
					window.frame.setLocationRelativeTo(null);
					JOptionPane.showMessageDialog(Maze.frame, "<html><font color=green>Please set the target by keep"
							+ " clicking the <font color=rgb(0,255,255)>Blue</font> node"
							+ " until its color change to <font color=black>Black</font>, then click Solve, magic will happen :v</font></html>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
}
