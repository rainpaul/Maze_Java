

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
//Class to check format of every input 
public class CheckInput {
	//Check if a string is a positive integer
	public static boolean bIsPositiveInteger (String s) {
		try {
			int iInput = Integer.parseInt(s);
			if (iInput >= 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	//Check if a string is a positive integer and return it, otherwise show the error message
	public static int iGetInt (String sMessage) {
		int iResult = 0;
		boolean bValid = false;
		while (!bValid) {
			String sInput = JOptionPane.showInputDialog(sMessage);
			if (bIsPositiveInteger(sInput)) {
				iResult = Integer.parseInt(sInput);
				bValid = true;
			}
			else {
				JOptionPane.showMessageDialog(null,"Input should be a positive integer, please!");
			}
		}
		return iResult;
	}
	//If the map text file has error, show which line has error to user
	public static void checkMapFile( ArrayList<Integer> aliErrorLines) {
		String sError = "";
		if (aliErrorLines.size() == 1) {
			sError = "1";
		}
		else {
			for (int i = 0; i < aliErrorLines.size(); i++ ) {
				sError += Integer.toString(aliErrorLines.get(i) + 1) + "\n";
			}
		}
		JOptionPane.showMessageDialog(null,"Input file is corrupted, please check following line(s): \n"
				+ sError );
	}
	//Get the row and column of the map
	public static ArrayList<Integer> aliGetRowCol () {
		ArrayList<Integer> aliRowCol = new ArrayList<Integer>();
		aliRowCol.add(CheckInput.iGetInt("Please specify the row of maze: "));
		aliRowCol.add(CheckInput.iGetInt("Please specify the column of maze: "));
		return aliRowCol;
	}
	//Load the map on frame
	public static void loadingMap(JFrame frame, int ROW, int COLUMN) {
		//For testing Reset button, we will replace following whole code with the code above
		int iInputMaze = JOptionPane.showConfirmDialog(null, "Do you want to load map file?\n Otherwise please"
				+ " put input for the maze!\n"
				+ "I really recommend you to just load map file\n"
				+ "(the file is included in this package, same folder)");
		if (iInputMaze == JOptionPane.YES_OPTION) {
			Draw.drawMaze(true);
		}
		else if (iInputMaze == JOptionPane.NO_OPTION) {
			ArrayList<Integer> aliRowCol = aliGetRowCol();
			Maze.ROW = aliRowCol.get(0);
			Maze.COLUMN = aliRowCol.get(1);
			Draw.drawMaze(false);
		}
		else if (iInputMaze == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}
		
//		Draw.drawMaze(true);
	}
}
