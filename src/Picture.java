import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Picture {
	   public static void main(String[] args){
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		  Mat m= Highgui.imread("opencv.png");
		  //System.out.println("height of the picture: " + m.height());
		  //System.out.println("width of the picture: " + m.width());
		  //System.out.println(m.channels());
		  for(int i=0; i< m.height();i++) {
			  for(int j=0; j<m.width();j++) {
				  double[] BGR = m.get(i, j);
				  if(BGR[0]==255 && BGR[1]==255 && BGR[2] == 255) {
					  System.out.print(".");
				  }else {
					  System.out.print("+");
				  }
			  }
			  System.out.println();
		  }
	   } 
}
