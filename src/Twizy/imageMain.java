package Twizy;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class imageMain {
	//Pass: 1,2,4,5,7,8,9,10
	//FAIL: 3

	public static int num=1;

	//static Mat imag = null;

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Images only 
		List<Integer> radiusList = new ArrayList<>();
		List<Integer> speedList = new ArrayList<>();
		boolean accurateSpeed = false;
		boolean lastRadius = false;

		Mat imgOrigin = roadDetect.LectureImage("Twizy_assets/p"+num+".jpg");
		roadDetect.ImShow("Road Image: p"+num, imgOrigin );
		List<Integer> id = roadDetect.detectionImages(imgOrigin,speedList,accurateSpeed,radiusList,lastRadius);
	}
}
