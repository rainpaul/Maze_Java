
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
//Maze class with properties and functions
public class Maze {
	//This property is used to define if there is a way from source to destination
	public boolean bFounded;
	public static int ROW ;
	public static int COLUMN;
	public static int iXTarget ;
	public static int iYTarget;
	public static int iXStart;
	public static int iYStart;
	public static JFrame frame;
	public static boolean bHasTarget = false;
	public static boolean bStartSet = false;
	public static JPanel pnlPoint;
	public static ArrayList<Point> alpPoints;
	//This property is used for tracking how many steps from source to destination
	public static int iStep = 0;
	/**
	 * Create the application.
	 */
	public Maze() {
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		//The Nimbus for LookAndFeel to have color on Mac OS
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        frame = new JFrame();
        frame.setBounds(800, 800, 652, 628);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CheckInput.loadingMap(frame, ROW, COLUMN);
    }
	//Find the path for maze
	public static ArrayList<Integer> findMazePath( Point pInput, ArrayList<Integer> aliPathCount) {
		int x =  pInput.iX;
		int y = pInput.iY;
		String sStatus = pInput.sStatus;
		//If point co-ordinate is higher than maze dimension, return false
		if (x > Maze.COLUMN || y > Maze.ROW || x < 0 || y < 0) {
			aliPathCount.set(0, 0);
			return aliPathCount;
		}
		//If this point is barrier or visited already, return false
		else if ( sStatus.compareTo("visited") == 0 || sStatus.compareTo("barrier") == 0 ) {
			aliPathCount.set(0, 0);
			return aliPathCount;
		}
		//If this point is the target, return true
		else if (x == iXTarget && y == iYTarget  ) {
			Point.Target(pInput);
			aliPathCount.set(0,1);
			iStep = aliPathCount.get(1);
			iStep++;
			aliPathCount.set(1, iStep);
			pInput.bReachToPath = true;
//			System.out.println(Point.iPathCount(Maze.alpPoints));
			return aliPathCount;
		}
		//Otherwise, consider this point is part of the path
		else {
			Point.Path(pInput);
			aliPathCount.set(0,1);
			iStep = aliPathCount.get(1);
			iStep++;
			aliPathCount.set(1, iStep);
			ArrayList<Point> alp4Points = new ArrayList<>();
			//Right
			if (x < (Maze.COLUMN - 1)) {
				Point pRight = Point.pFindPoint(x+1, y);
				if (pRight.sStatus.compareTo("normal") == 0) {
					alp4Points.add(pRight);
				}		
			}
			//Left
			if (x > 0  ) {
				Point pLeft = Point.pFindPoint(x -1, y);
				if (pLeft.sStatus.compareTo("normal") == 0) {
					alp4Points.add(pLeft);
				}	
			}
			//Up
			if ( y > 0) {
				Point pDown = Point.pFindPoint(x, y-1);
				if ( pDown.sStatus.compareTo("normal") == 0 ) {
					alp4Points.add(pDown);	
				}			
			}
			//Down
			if (y < (Maze.ROW - 1)) {	
				Point pUp = Point.pFindPoint(x, y+1);
				if (pUp.sStatus.compareTo("normal") == 0 ) {
					alp4Points.add(pUp);
				}			
			}		
			if (alp4Points.size() == 1) {
				if( findMazePath(alp4Points.get(0), aliPathCount).get(0) == 1) {
					aliPathCount.set(0,1);
					iStep = aliPathCount.get(1);
					iStep++;
					aliPathCount.set(1, iStep);
					return aliPathCount;
				}
				else {
					Point.Visited(alp4Points.get(0));
					return findMazePath(pInput,aliPathCount);
				}		
			}
			//If we have more than 1 option, try to find the nearest point
			else if (alp4Points.size() > 1) {
				Point pNearest = Point.pNearest(alp4Points);
				if (findMazePath(pNearest, aliPathCount).get(0) == 1) {	
					aliPathCount.set(0,1);
					iStep = aliPathCount.get(1);
					iStep++;
					aliPathCount.set(1, iStep);
					return aliPathCount;
				}	
				else {
					Point.Visited(pNearest);
					return findMazePath(pInput, aliPathCount);
				}
			}		
		}
		Point.Visited(pInput);
		aliPathCount.set(0,0);
		return aliPathCount;	
	}
	//Solve the maze
	public static void solveMaze() {
		//Before solving, we need to reset the visited, path & target
		Maze.resetMaze();
		//Init an array list of integer for the return of findMazePath
		ArrayList<Integer> aliInt = new ArrayList<Integer>();
		aliInt.add(0);
		aliInt.add(0);
		 ArrayList<Integer> iAResult =  Maze.findMazePath( Maze.alpPoints.get(0),aliInt);
		 //For finding out the shortest path, print out the 1st element of the result
		 System.out.println(Integer.toString(iAResult.get(1)));
		 //If there is no path way from starting to destination, show message to user
		 if ( iAResult.get(0) == 0) {
			 JOptionPane.showMessageDialog(null,"There is no pathway to the target"
			 		+ ", please select another target!");
		 }
	}
	//Reset the maze for a new path finding process
	public static void resetMaze() {
		Maze.bHasTarget = false;
		Maze.iStep = 0;
		for (int i = 0; i < Maze.alpPoints.size(); ++i) {
			if (Maze.alpPoints.get(i).sStatus.compareTo("path") == 0 ||
					Maze.alpPoints.get(i).sStatus.compareTo("visited") == 0 ||
					Maze.alpPoints.get(i).coBg == Color.BLACK) {
				Point.Normal(Maze.alpPoints.get(i));
			}			
		}
	}
	//Get file content and import it to the Maze
	public static void importMaze() {
		Maze.frame.getContentPane().remove(Maze.pnlPoint);;
		Maze.alpPoints.clear();
		ArrayList<String> alsNewResult = Import.alsImport();
		String[] asDimension = alsNewResult.get(0).split(",");
		Maze.ROW = Integer.parseInt(asDimension[0]);
		Maze.COLUMN = Integer.parseInt(asDimension[1]);
		Maze.pnlPoint = new JPanel();
		Maze.pnlPoint.setLayout(new GridLayout(Maze.ROW,Maze.COLUMN));
		Maze.frame.add(Maze.pnlPoint,BorderLayout.CENTER);		
		Draw.drawPoints(alsNewResult);
		Maze.frame.validate();
		Maze.frame.repaint();
	}
	//Export maze to JPEG file
	public static void exportMapToJPEG() {
		 BufferedImage image = new BufferedImage(Maze.pnlPoint.getWidth(), 
				 Maze.pnlPoint.getHeight(), BufferedImage.TYPE_INT_RGB);
		 Graphics2D g = image.createGraphics();
		 Maze.pnlPoint.printAll(g);
		 g.dispose();
		 try {
			 String sPath = System.getProperty("user.dir") + 
			 "\\main\\java\\server\\map\\mapImage\\";
			 File fPath = new File(sPath);
			 if (!fPath.isDirectory()) {
				sPath = System.getProperty("user.dir");
			}
			long lMilSec = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd-hh-mm-S");
			Date resultdate = new Date(lMilSec);
			sPath += "\\mapImage" + sdf.format(resultdate) + ".jpeg" ; 
			ImageIO.write(image, "jpeg", new File(sPath));
			JOptionPane.showMessageDialog(null,"Succesfully export image to "+ sPath);
		 } catch (IOException exp) {
		     exp.printStackTrace();
		 }
	 }
	//Check if a string is integer or not
	public static boolean bIsInteger (String sInput) {
		try {
			Integer.parseInt(sInput);
			return true;			
		} catch (Exception e) {
			return false;
		}
	}
	//For user to type the target manually
	public static void typeTarget () {
		JFrame frTypeTarget = new JFrame("Target input: ");
		frTypeTarget.setVisible(true);
		frTypeTarget.setSize(400,100);
		frTypeTarget.setLocation(500,400);
		frTypeTarget.setDefaultCloseOperation(1);
		JPanel pnInput = new JPanel();
		frTypeTarget.getContentPane().add(pnInput);
		pnInput.setLayout(new GridLayout(3, 2));
		JLabel lbX = new JLabel("The row of target is: ");
		JTextField txfiX = new JTextField();
		txfiX.setText(Integer.toString(Maze.iXTarget));
		JLabel lbY = new JLabel("The column of target is: ");
		JTextField txfiY = new JTextField();
		txfiY.setText(Integer.toString(Maze.iYTarget));
		pnInput.add(lbX);
		pnInput.add(txfiX);
		pnInput.add(lbY);
		pnInput.add(txfiY);
		JButton btnDone = new JButton("Done");
		pnInput.add(btnDone);
		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sX = txfiX.getText();
				String sY = txfiY.getText();
				if (!sX.isEmpty() && !sY.isEmpty()) {
					if (bIsInteger(sX) && bIsInteger(sY)) {
						int iX = Integer.parseInt(sX);
						int iY = Integer.parseInt(sY);
						if(iX <= Maze.ROW && iY  <= Maze.COLUMN) {
							Maze.resetMaze();
							Point pTarget = Point.pFindPoint(iX,iY);
							//If the target is barrier, we try to find 1 of 8 points around to see
							// if any point is available
							if (pTarget.sStatus.compareTo("barrier") == 0) {
								for (int i = iX - 1; i <= iX + 1; ++i) {
									for (int y = iY - 1; y <= iY +1; ++y) {
										Point pTry = Point.pFindPoint(i, y);
										if (pTry != null && pTry.sStatus.compareTo("normal") == 0) {
											Point.Target(pTry);
											Maze.iXTarget = i;
											Maze.iYTarget = y;
											frTypeTarget.setVisible(false);
											JOptionPane.showMessageDialog(frTypeTarget, 
													"The target inputed from user(" + Integer.toString(iX)
													+ "," + Integer.toString(iY) +
													") is a wall, so we moved it to the nearest avaiable house"
													+" which is(" + Integer.toString(i) + "," +
													Integer.toString(y) + ")");
											return;
										}
									}
								}
							}
							else if (pTarget.sStatus.compareTo("normal") == 0) {
								Point.Target(pTarget);
								Maze.iXTarget = iX;
								Maze.iYTarget = iY;
								frTypeTarget.setVisible(false);	
							}
							else {
								JOptionPane.showMessageDialog(frTypeTarget, "Unable to locate this address");
							}
						}
						else {
							JOptionPane.showMessageDialog(frTypeTarget, "X and/or Y is oversize of the map, please double check!");
						}
					}
					else {
						JOptionPane.showMessageDialog(frTypeTarget, "Input should be integer, please!");
					}
				}
				else {
					JOptionPane.showMessageDialog(frTypeTarget, "Input cannot be empty, please!");
				}
			}
		});
	}
	//For the input we get from service
	public static void setTargetFromAL( ArrayList<String> aliInput) {
		try {
			String sX = aliInput.get(0);
			String sY = aliInput.get(1);
			if (!sX.isEmpty() && !sY.isEmpty()) {
				if (bIsInteger(sX) && bIsInteger(sY)) {
					int iX = Integer.parseInt(sX);
					int iY = Integer.parseInt(sY);
					if(iX <= Maze.ROW && iY  <= Maze.COLUMN) {
						Maze.resetMaze();
						Point pTarget = Point.pFindPoint(iX,iY);
						if (pTarget.sStatus.compareTo("barrier") == 0) {
							for (int i = iX - 1; i <= iX + 1; ++i) {
								for (int y = iY - 1; y <= iY +1; ++y) {
									Point pTry = Point.pFindPoint(i, y);
									if (pTry != null && pTry.sStatus.compareTo("normal") == 0) {
										Point.Target(pTry);
										Maze.iXTarget = i;
										Maze.iYTarget = y;
										JOptionPane.showMessageDialog(null, "The target inputed from user(" + Integer.toString(iX)
												+ "," + Integer.toString(iY) +
												") is a wall, so we moved it to the nearest avaiable house"
												+"which is(" + Integer.toString(i) + "," +
												Integer.toString(y) + ")");
										return;
									}
								}
							}
						}
						else if (pTarget.sStatus.compareTo("normal") == 0) {
							Point.Target(pTarget);
							Maze.iXTarget = iX;
							Maze.iYTarget = iY;	
						}
						else {
							JOptionPane.showMessageDialog(null, "Unable to locate this address");
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "X and/or Y is oversize of the map, please double check!");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Input should be integer, please!");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Input cannot be empty, please!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
