import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
//To draw the map
public class Draw {
	//Draw points base on the array list of string (points info)
	public static void drawPoints(ArrayList<String> alsResult) {
		Maze.alpPoints = new ArrayList<Point>();
		if (alsResult.size() > 0) {
			//The 1st line of string array list is the map dimension
			String[] asDimension = alsResult.get(0).split(",");
			Maze.ROW = Integer.parseInt(asDimension[0]);
			Maze.COLUMN = Integer.parseInt(asDimension[1]);
		}	
		//Go through map points
		for (int x = 0; x < Maze.ROW ; ++x) {
			for (int y = 0; y < Maze.COLUMN; ++y) {
				JButton btn = new JButton(y + "," + x);
				//For clicking the point case, switch between Normal, Barrier and
				//Target( status = Normal)
				btn.setName("1");
				Point pTemp = new Point();
				pTemp.btnT = btn;
				pTemp.iX = y;
				pTemp.iY = x;
				pTemp.sStatus = "normal";
				pTemp.coBg = Color.CYAN;  
				pTemp.btnT.setBackground(pTemp.coBg);
				//Set points to be Barrier or Normal base on the input 
				Point.modifyPoint(pTemp,alsResult);
				Maze.pnlPoint.add(pTemp.btnT);
				pTemp.btnT.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton jb = (JButton) e.getSource();
						//Now we used the name that we set previously to go between 1 2 3
						int iCurrent =Integer.parseInt(jb.getName()) ;
						//If the target is set already, just go between 1 2
						if (Maze.bHasTarget ) {
							if (iCurrent > 2)
								iCurrent = 1;
						}
						//Otherwise, go between 1 2 3
						else {
							if (iCurrent > 3)
								iCurrent = 1;
						}
						switch (iCurrent ) {
						//Case 1: Barrier
						case 1 :
							if (pTemp.coBg == Color.BLACK) {
								Maze.bHasTarget = false;
							}
							if (pTemp.coBg == Color.WHITE) {
								Maze.bStartSet = false;
							}
							Point.Barrier(pTemp);
							break;
						//Case 2: Normal
						case 2:
							if (pTemp.coBg == Color.BLACK) {
								Maze.bHasTarget = false;
							}
							if (pTemp.coBg == Color.WHITE) {
								Maze.bStartSet = false;
							}
							Point.Normal(pTemp);
							break;
						//Case 3: Target
						case 3:
							if (Maze.bHasTarget == false) {
								pTemp.coBg = Color.BLACK;
								Maze.iXTarget = pTemp.iX;
								Maze.iYTarget = pTemp.iY;
								pTemp.btnT.setBackground(pTemp.coBg);
								pTemp.sStatus = "normal";
								Maze.bHasTarget = true;
								break;
							}
							else {
								JOptionPane.showMessageDialog(null,"We can choose only 1 target at 1 time,"
										+ "please");
							}	
//						case 4: Starting point
//							if (Maze.bStartSet == false) {
//								pTemp.coBg = Color.WHITE;
//								Maze.iXStart = pTemp.iX;
//								Maze.iYStart = pTemp.iY;
//								pTemp.btnT.setBackground(pTemp.coBg);
//								pTemp.sStatus = "normal";
//								Maze.bStartSet = true;
//								break;
//							}
//							else {
//								JOptionPane.showMessageDialog(null,"We can choose only 1 start at 1 time,"
//										+ "please");
//							}
						default:				
							break;
						}	
						//Increase the index and set it back to the button
						int iNext = iCurrent + 1;
						String sNext = Integer.toString(iNext);
						jb.setName(sNext);
					}
				});
				Maze.alpPoints.add(pTemp);
			}	
		}
	}
	//Draw the map with 2 options:
	//True: get input from file
	//False: get input from user (user has to draw it by themselves
	public static ArrayList<Point> drawMaze( Boolean bImport) {
		ArrayList<String> alsResult = new ArrayList<String>();
		if (bImport) {
			alsResult = Import.alsImport();
			ArrayList<Integer> aliCheck = Import.aliCheckInputFile(alsResult);
			//If the input is good to go
			if (aliCheck.get(0) == 0) {
				if (alsResult.size() > 0) {
					String[] asDimension = alsResult.get(0).split(",");
					Maze.ROW = Integer.parseInt(asDimension[0]);
					Maze.COLUMN = Integer.parseInt(asDimension[1]);
				}
			}
			else {
				CheckInput.checkMapFile(aliCheck);
				System.exit(0);
			}
		}
		Maze.pnlPoint = new JPanel();
		Maze.pnlPoint.setLayout(new GridLayout(Maze.ROW,Maze.COLUMN));
		Maze.frame.add(Maze.pnlPoint,BorderLayout.CENTER);
		Maze.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		Draw.drawPoints( alsResult);
		//A panel to store buttons
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new GridLayout(4, 4));
		//Add the button panel to the south
		Maze.frame.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
		//Solve the map
		JButton btnSolve = new JButton("<html><b>Solve</b></html>");
		pnlButtons.add(btnSolve );
		btnSolve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Maze.solveMaze();
			}
		});
		//Reset the map for a new path finding process
		JButton btnReset = new JButton("<html><b>Reset</b></html>");
		pnlButtons.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			@Override	
			public void actionPerformed(ActionEvent arg0) {
				Maze.resetMaze();
			}
		});
		//Import the file to build the map
		JButton btnImport = new JButton("Import file to map");
		btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Import function
				Maze.importMaze();
			}
		});
		//For debug: set default Enter to Reset
		Maze.frame.getRootPane().setDefaultButton(btnSolve);
		pnlButtons.add(btnImport);
		//Export map to file
		JButton btnExport = new JButton("Export map to file");
		pnlButtons.add(btnExport);
		btnExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Export.exportToFile();	
			}
		});
		
		//Export to JPEG
		JButton btnExportToJPEG = new JButton("Export to jpeg file");
		btnExportToJPEG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Maze.exportMapToJPEG();
			}
		});
		pnlButtons.add(btnExportToJPEG);
		//Type target
		JButton btnTypeTarget = new JButton("Type target point");
		pnlButtons.add(btnTypeTarget);
		btnTypeTarget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TypeTarget is for the case user type it here
//				Maze.typeTarget();
				//SetTargetFromAL is for the case we get input from file
//				ArrayList<String> alsInput = new ArrayList<>();
				//For testing, assign a new string array list {3,5} (Since setTargetFromAL
				// will be invoke lately
//				alsInput.add("3");
//				alsInput.add("5");
//				Maze.setTargetFromAL(alsInput);
				Maze.typeTarget();
			}
		});
		//Close this frame
		JButton btnClose = new JButton("<html><b>Close</b></html>");
		pnlButtons.add(btnClose);
		btnClose.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				Maze.frame.dispose();
//				System.exit(0);
			}
		});
		return Maze.alpPoints;	
	}
}
