

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;

public class Point {
	JButton btnT;
	public int iX;
	public int iY;
	public Color coBg;
	public String sStatus;
	public int iStepToGoal;
	//For debug purpose, create a new boolean var
	public boolean bReachToPath = false;
	
	//Modify toString function
	public String toString() {
		String sResult = "";
		sResult = 
			  "X is: " + Integer.toString(iX) + "\n" + 
			  "Y is: " + Integer.toString(iY)+ "\n" +
			  "Reached to goal: " + Boolean.toString(bReachToPath) + "\n" +
			  "Step to goal: " + Integer.toString(iStepToGoal) + "\n" + 
				"Status is: "+ sStatus + "\n";
		return sResult;
	}
	
	//The string to show when exporting point
	public String sExport() {
		String sResult = "";
		sResult = Integer.toString(iX)+ "," + Integer.toString(iY) +","+ sStatus + "\n";
		return sResult;
	}
	//Find a point on maze base on the input x and y
	public static Point pFindPoint (int x, int y) {

		Point pResult = new Point();
		for (int i = 0; i < Maze.alpPoints.size() ; ++i) {
			if ( x ==  Maze.alpPoints.get(i).iX && y == Maze.alpPoints.get(i).iY ) {
				return Maze.alpPoints.get(i);
			}
		}
		return pResult;
	}
	//Draw the point and set its status base on the array list string input
	public static void modifyPoint (Point pInput, ArrayList<String> alsMap) {
		for (int i = 0; i < alsMap.size(); ++i) {
			String[] asLine = alsMap.get(i).split(",");
			if (pInput.iX == Integer.parseInt(asLine[0])) {
				if (pInput.iY == Integer.parseInt(asLine[1])) {
					pInput.sStatus = asLine[2];
					if (pInput.sStatus.compareTo("barrier")==0) {
						pInput.coBg = Color.red;
						pInput.btnT.setBackground(pInput.coBg);
						return;
					}
					else if (pInput.sStatus.compareTo("path")==0) {
						pInput.coBg = Color.GREEN;
						pInput.btnT.setBackground(pInput.coBg);
						return;
					}
				}
			}
		}
	}
	//Set a point to be target
	public static void Target(Point p) {
		p.coBg = Color.BLACK;
		p.btnT.setBackground(p.coBg);
		p.sStatus = "path";
	}
	//Set a point to be path
	public static void Path(Point pInput) {
		pInput.coBg = Color.GREEN;
		pInput.btnT.setBackground(pInput.coBg);
		pInput.sStatus = "path";
	}
	//Set a point to be normal
	public static void Normal(Point pInput) {
		pInput.coBg = Color.CYAN;
		pInput.btnT.setBackground(pInput.coBg);
		pInput.sStatus = "normal";
	}
	//Set a point to be visited
	public static void Visited(Point pInput) {
		//For debug: try to hide the visited path
//		pInput.coBg = Color.ORANGE;
		pInput.coBg = Color.CYAN;
		pInput.btnT.setBackground(pInput.coBg);
		pInput.sStatus = "visited";
	}
	//Set a point to be barrier
	public static void Barrier(Point pInput) {
		pInput.coBg = Color.RED;
		pInput.btnT.setBackground(pInput.coBg);
		pInput.sStatus = "barrier";
	}
	//Set a point to be start
	public static void Start(Point pInput) {
		pInput.coBg = Color.WHITE;
		pInput.btnT.setBackground(pInput.coBg);
		pInput.sStatus = "path";
	}
	//Get the distance between 2 points
	public static double dDistance(Point pA, Point pB) {
		double dResult = 0;
		int iEdgeX = Math.abs(pA.iX - pB.iX);
		int iEdgeY = Math.abs(pA.iY - pB.iY);
		dResult = Math.sqrt(Math.pow(iEdgeX, 2) + Math.pow(iEdgeY, 2));
		dResult = iEdgeX * iEdgeY;
		return dResult;
	}
	//Get the barrier between 2 X of 2 points
	public static boolean bBarrierBetweenX(int iSame, int iPointStart, int iPointTarget) {
		if (iPointStart > iPointTarget) {
			int iTemp = iPointStart;
			iPointStart = iPointTarget;
			iPointTarget = iTemp;
		}
		for (int i = iPointStart + 1; i < iPointTarget; ++i) {
			Point pTemp = Point.pFindPoint(iSame, i);
			if (pTemp.sStatus.compareTo("barrier") == 0)
				return true;
		}
		return false;
	}
	//Get the barrier between 2 Y of 2 points
	public static boolean bBarrierBetweenY(int iSame, int iPointStart, int iPointTarget) {
		if (iPointStart > iPointTarget) {
			int iTemp = iPointStart;
			iPointStart = iPointTarget;
			iPointTarget = iTemp;
		}
		for (int i = iPointStart + 1; i < iPointTarget; ++i) {
			Point pTemp = Point.pFindPoint(i, iSame);
			if (pTemp.sStatus.compareTo("barrier") == 0)
				return true;
		}
		return false;
	}
	//Try to find the nearest points
	public static Point pNearest (ArrayList<Point> alpInput) {
		Point pTemp = alpInput.get(0);
		Point pTarget = new Point();
		pTarget.iX = Maze.iXTarget;
		pTarget.iY = Maze.iYTarget;
		//If there are 3 points, remove the farest points
		if (alpInput.size() == 3) {
			if (dDistance(alpInput.get(0), pTarget) > dDistance(alpInput.get(1), pTarget)) {
				alpInput.remove(alpInput.get(0));
			}
			else if (dDistance(alpInput.get(1), pTarget) > dDistance(alpInput.get(2), pTarget)) {
				alpInput.remove(alpInput.get(1));
			}
			else {
				alpInput.remove(alpInput.get(2));
			}
		}
		//If there are 2 points, try to find the nearest point
		if (alpInput.size() == 2) {
			if ((alpInput.get(0).iX == pTarget.iX && bBarrierBetweenX(pTarget.iX, alpInput.get(0).iY,pTarget.iY)) ||
					(alpInput.get(0).iY == pTarget.iY && bBarrierBetweenY(pTarget.iY, alpInput.get(0).iX,pTarget.iX ))	){
					return alpInput.get(1);
			}
			else if ((alpInput.get(1).iX == pTarget.iX && bBarrierBetweenX(pTarget.iX, alpInput.get(1).iY,pTarget.iY)) ||
					(alpInput.get(1).iY == pTarget.iY && bBarrierBetweenY(pTarget.iY, alpInput.get(1).iX,pTarget.iX ))	) {
				return alpInput.get(0);
			}
			else {
				if (alpInput.get(0).iX == 5 && alpInput.get(0).iY == 12) {
					System.out.println("");
				}
				for (int i= 1; i < alpInput.size(); ++i) {
					if (dDistance(alpInput.get(i), pTarget) <= dDistance(pTemp, pTarget)) {
						pTemp = alpInput.get(i);
					}
				}	
			}
		}
		return pTemp;
	}
}

