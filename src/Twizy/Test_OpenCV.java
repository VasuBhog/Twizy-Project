package Twizy;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Test_OpenCV{
   public static void main( String[] args ){
	  System.out.println(Core.NATIVE_LIBRARY_NAME);
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      Mat mat = Mat.eye( 2, 2, CvType.CV_8UC3 ); //all pictures stored in a MAT
      System.out.println( "mat = " + "\n" + mat.dump() );
   }
}
