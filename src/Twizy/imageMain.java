package Twizy;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class imageMain {
	//Pass: 1,2,4,5,7,8,9,10
	//FAIL: 3,7

	public static int num=10;

	//static Mat imag = null;

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Images only 
		List<Integer> radiusList = new ArrayList<>();
		List<Integer> speedList = new ArrayList<>();
		boolean accurateSpeed = false;
		boolean lastRadius = false;
		boolean imShow = false;

		Mat imageNew = roadDetect.LectureImage("Twizy_assets/p"+num+".jpg");
		List<Integer> id = roadDetect.detectionImages(imageNew,speedList,accurateSpeed,radiusList,lastRadius, imShow);
		//roadDetect.ImShow("Road Image: p"+num, imageNew);

	}
}
