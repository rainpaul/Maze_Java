//Keep those library in case we go back with the file dialog
import java.awt.FileDialog;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.swing.JFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//For importing
public class Import {
	//Get the file content and build the string array list for map
	public static ArrayList<String> alsImport() {
		//For testing reset button, comment out below code
		FileDialog fd = new FileDialog(new JFrame());
		fd.setVisible(true);
		File[] f = fd.getFiles();
		
		if(f.length > 0){
		    System.out.println(fd.getFiles()[0].getAbsolutePath());
		}
		
		//For testing, hard code the path
//		File[] f = new File[1];
//		String sPath = "";
//		String sCurDir = System.getProperty("user.dir");
//        
//        sPath = sCurDir +  "/src/main/java/server/map/mapDB/";
//        File tempF =new File(sPath );
//		if (!tempF.isDirectory()) {
//			sPath = sCurDir;
//		}
//		sPath += "map2018-11-20-12-24-939_ideal.txt";
//		f[0] = new File(sPath);
//		
		String sFileContent = "";
		try {
			FileReader frMap = new FileReader(f[0]);
			int iRaw = frMap.read();
			while (iRaw != -1) {
				char cRaw = (char)(iRaw);
				sFileContent += String.valueOf(cRaw);
				iRaw = frMap.read();
			}
			frMap.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String[] asFileContent = sFileContent.split("\n");
		ArrayList<String> alsResult = new ArrayList<String>();
		for (int i = 0; i < asFileContent.length; ++i) {
			alsResult.add(asFileContent[i]);
		}
		return alsResult;
	}
	//Check the format of input file before importing, collect which lines are corrupted
	public static ArrayList<Integer> aliCheckInputFile(ArrayList<String> alsMapFile) {
		ArrayList<Integer> aliResult = new ArrayList<Integer>();
		//Input file should have more than 2 lines
		if (alsMapFile.size() > 2) {
			//Check the 1st line (map dimension info)
			String[] asRowCol = alsMapFile.get(0).split(",");
			if (asRowCol.length == 2) {
				if (CheckInput.bIsPositiveInteger(asRowCol[0]) && CheckInput.bIsPositiveInteger(asRowCol[1])){
					boolean bCheckLine = true;
					for (int i = 1; i < alsMapFile.size(); ++i) {
						String[] asLine = alsMapFile.get(i).split(",");
						//Check if following line should have 3 elements(row, column, status)
						if (asLine.length == 3) {
							if (CheckInput.bIsPositiveInteger(asLine[0]) && 
									CheckInput.bIsPositiveInteger(asLine[1])) {
								continue;
							}
							else {
								aliResult.add(i);
								bCheckLine = false;
								continue;
							}
						}
						else {
							aliResult.add(i);
							bCheckLine = false;
							continue;
						}
					}
					if (bCheckLine) {
						aliResult.add(0);
						return aliResult;
					}
					else {
						return aliResult;
					}
				}
			}
		}
		aliResult.add(-1);
		return aliResult;
	}
}
