import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
//For exporting
public class Export {
	//Export to file
	public static void exportToFile() {
		//Get current time in milisecond to build the file name
		long lMilSec = System.currentTimeMillis();
	    SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd-hh-mm-S");
	    Date resultdate = new Date(lMilSec);
		String sPath = System.getProperty("user.dir") + 
				"\\main\\java\\server\\map\\mapDB\\" ;
		File fTestPath = new File(sPath);
		//If the above path does not exist, just use the user current dir
		if (!fTestPath.isDirectory()) {
			sPath = System.getProperty("user.dir");
		}
		sPath += "map" + sdf.format(resultdate)  + ".txt";
		File fOutput = new File(sPath );
		try {
			FileWriter fw = new FileWriter(fOutput);
			fw.write( Integer.toString(Maze.ROW) + "," +Integer.toString(Maze.COLUMN) + "\n" );
			for (int i = 0; i < Maze.alpPoints.size(); ++i) {
				//Write the point to file with specific format
				fw.write(Maze.alpPoints.get(i).sExport());
			}
			JOptionPane.showMessageDialog(null,"Succefully export file to " + sPath);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
