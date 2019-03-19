package Twizy;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class main {
	//Pass: 1,2,4,5,7,8,9,10
	//FAIL: 3,7(only one right) error after fixes 
	
	public static int num=3;

	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat imgOrigin = roadDetect.LectureImage("Twizy_assets/p"+num+".jpg");
		roadDetect.ImShow("Road Image: p"+num, imgOrigin );
		List<Integer> id = roadDetect.detectionImages(imgOrigin);
		
		Mat imgOri = roadDetect.LectureImage("Twizy_assets/p"+num+".jpg");
	}
}
