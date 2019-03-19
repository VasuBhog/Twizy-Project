//package Twizy;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.DMatch;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.MatOfDMatch;
//import org.opencv.core.MatOfInt4;
//import org.opencv.core.MatOfKeyPoint;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.features2d.DescriptorMatcher;
//import org.opencv.features2d.Features2d;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.xfeatures2d.SURF;
//
//public class Backup_Twizy {
//	
//	public static List<MatOfPoint> detect_contour(Mat img, String file) {
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
//		Mat m = LectureImage(file);
//		//ImShow("Circles",m);
//		Mat hsv_image = Mat.zeros(m.size(),m.type());
//		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
//		Mat threshold_img = detect_circles(hsv_image,file);
//		//System.out.println(threshold_img);
//		//ImShow("Circles with Smoothing", threshold_img);
//		int thresh = 100;
//		Mat canny_output = new Mat();
//		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		MatOfInt4 hierarchy = new MatOfInt4();
//		Imgproc.Canny( threshold_img, canny_output, thresh, thresh*2);
//		Imgproc.findContours( canny_output, contours, hierarchy,Imgproc.RETR_EXTERNAL, 
//		           Imgproc.CHAIN_APPROX_SIMPLE);
//		Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
//		Random rand = new Random();
//		for( int i = 0; i< contours.size(); i++ ) {
//		  Scalar color = new Scalar( rand.nextInt(255 - 0 + 1) , rand.nextInt(255 - 0 + 1), 
//		                rand.nextInt(255 - 0 + 1) );
//		  Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
//		}
//		//ImShow("Contours",drawing);
//		return contours;
//	}
//	
//	public static void extract(String file) {
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
//		Mat m = LectureImage(file);
//		ImShow("Roads",m);
//		Mat hsv_image = Mat.zeros(m.size(),m.type());
//		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
//		Mat threshold_img = multi_threshold(file);
//		//System.out.println(threshold_img);
//		ImShow("Road with Smoothing", threshold_img);
//		int thresh = 100;
//		Mat canny_output = new Mat();
//		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		MatOfInt4 hierarchy = new MatOfInt4();
//		Imgproc.Canny( threshold_img, canny_output, thresh, thresh*2);
//		Imgproc.findContours( canny_output, contours, hierarchy,Imgproc.RETR_EXTERNAL, 
//		           Imgproc.CHAIN_APPROX_SIMPLE);
//		Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
//		Random rand = new Random();
//		for( int i = 0; i< contours.size(); i++ ) {
//		  Scalar color = new Scalar( rand.nextInt(255 - 0 + 1) , rand.nextInt(255 - 0 + 1), 
//		                rand.nextInt(255 - 0 + 1) );
//		  Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
//		}
//		ImShow("Contours",drawing);
//	}
//	
//	//Recognize Shapes Contours
//	public static Mat shape_contour(String file) {
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
//		Mat m = LectureImage(file);
//		ImShow("Original Image",m);
//		Mat hsv_image = Mat.zeros(m.size(), m.type());
//		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
//		ImShow("HSV",hsv_image);
//		Mat threshold_img = detect_circles(hsv_image,file);
//		ImShow("Smoothing",threshold_img);
//		List<MatOfPoint> contours = detect_contour(threshold_img,file);
//		
//		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
//		float[] radius = new float[1];
//		Point center = new Point();
//		for (int c=0; c < contours.size(); c++) {
//			MatOfPoint contour = contours.get(c);
//			double contourArea = Imgproc.contourArea(contour);
//			matOfPoint2f.fromList(contour.toList());
//			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
//			if ((contourArea/(Math.PI*radius[0]*radius[0])) >= 0.8){
//				Imgproc.circle(m,center,(int)radius[0], new Scalar(0,255,0),2);
//			}
//		}
//		ImShow("Detection of Red Circles",m);
//		return m;
//	}
//	
//	//Template matching - matching image with other image
//	public static void template(String file) {
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
//		Mat m = LectureImage(file);
//		ImShow("Image",m);
//		Mat hsv_image = Mat.zeros(m.size(), m.type());
//		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
//		ImShow("HSV",hsv_image);
//		Mat threshold_img = detect_circles(hsv_image,file);
//		ImShow("Smoothing",threshold_img);
//		List<MatOfPoint> contours = detect_contour(threshold_img,file);
//		
//		//template matching 
//		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
//		float[] radius = new float[1];
//		Point center = new Point();
//		for (int c=0; c < contours.size(); c++) {
//			MatOfPoint contour = contours.get(c);
//			double contourArea = Imgproc.contourArea(contour);
//			matOfPoint2f.fromList(contour.toList());
//			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
//			if ((contourArea/(Math.PI*radius[0]*radius[0])) >= 0.8){
//				Imgproc.circle(m,center,(int)radius[0], new Scalar(0,255,0),2);
//				Rect rect = Imgproc.boundingRect(contour);
//				Imgproc.rectangle(m, new Point(rect.x,rect.y),
//						new Point(rect.x+rect.width,rect.y+rect.height),
//						new Scalar(0,255,0),2);
//				Mat tmp = m.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
//				Mat ball = Mat.zeros(tmp.size(), tmp.type());
//				tmp.copyTo(ball);
//				ImShow("Found Image",ball);
//			}
//		}
//	}
//	
//	public static void feature_detect(String file, String file2) {
//		//SURF Detector
//		Mat firstImg = Imgcodecs.imread(file); //image 1 small image
//		Mat secondImg = Imgcodecs.imread(file2); //image 2 is final template
//		if (firstImg.empty() || secondImg.empty()){
//			System.err.println("Cannot read images!");
//			System.exit(0);
//		}
//
//		//DETECT keypoints using SURF detector
//		double hessianThreshold = 400;
//		int nOctaves = 4, nOctaveLayers = 3;
//		boolean extended = false, upright = false;
//		SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
//		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
//		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
//		Mat descriptors1 = new Mat();
//		Mat descriptors2 = new Mat();
//		detector.detectAndCompute(firstImg, new Mat(), keypoints1, descriptors1);
//		detector.detectAndCompute(secondImg, new Mat(), keypoints2, descriptors2);
//		detector.detect(firstImg, keypoints1);
//
//		//SURF FEATURES of First Image
//		Mat surfImg1 = firstImg;
//		Features2d.drawKeypoints(surfImg1, keypoints1, surfImg1);
//		ImShow("Surf Keypoints",surfImg1);
//
//		//SURF Features of Second Image
//		Mat surfImg2 = secondImg;
//		Features2d.drawKeypoints(surfImg2, keypoints2, surfImg2);
//		ImShow("Surf Keypoints",surfImg2);
//
//	}
//	
//	public static void matching(String file, String file2) {
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
//		Mat m = LectureImage(file);
//		ImShow("Image",m);
//		Mat hsv_image = Mat.zeros(m.size(), m.type());
//		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
//		//ImShow("HSV",hsv_image);
//		Mat threshold_img = detect_circles(hsv_image,file);
//		//ImShow("Smoothing",threshold_img);
//		List<MatOfPoint> contours = detect_contour(threshold_img,file);
//
//		//template matching 
//		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
//		float[] radius = new float[1];
//		Point center = new Point();
//		for (int c=0; c < contours.size(); c++) {
//			MatOfPoint contour = contours.get(c);
//			double contourArea = Imgproc.contourArea(contour);
//			matOfPoint2f.fromList(contour.toList());
//			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
//			if ((contourArea/(Math.PI*radius[0]*radius[0])) >= 0.8){
//				Imgproc.circle(m,center,(int)radius[0], new Scalar(0,255,0),2);
//				Rect rect = Imgproc.boundingRect(contour);
//				Imgproc.rectangle(m, new Point(rect.x,rect.y),
//						new Point(rect.x+rect.width,rect.y+rect.height),
//						new Scalar(0,255,0),2);
//				Mat tmp = m.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
//				Mat ball = Mat.zeros(tmp.size(), tmp.type());
//				tmp.copyTo(ball);
//				
//				//ImShow("Found Image",ball);
//
//
//				//SURF Detector - MATCHING 
//				Mat firstImg = ball; //image 1 small image
//				Mat secondImg = Imgcodecs.imread(file2); //image 2 is final template
//				Mat resizedImg = new Mat();
//				Imgproc.resize(firstImg, resizedImg, secondImg.size());
//				
//				if (resizedImg.empty() || secondImg.empty()){
//					System.err.println("Cannot read images!");
//					System.exit(0);
//				}
//
//
//				//DETECT keypoints using SURF detector
//				double hessianThreshold = 500;
//				int nOctaves = 4, nOctaveLayers = 3;
//				boolean extended = false, upright = false;
//				SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
//				MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
//				MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
//				Mat descriptors1 = new Mat();
//				Mat descriptors2 = new Mat();
//				detector.detectAndCompute(resizedImg, new Mat(), keypoints1, descriptors1);
//				detector.detectAndCompute(secondImg, new Mat(), keypoints2, descriptors2);
//				detector.detect(resizedImg, keypoints1);
//				
//
//				//Matching Descriptor vectors with BruteForce
//				DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
//				List<MatOfDMatch> knnMatches = new ArrayList<>();
//				matcher.knnMatch(descriptors1, descriptors2, knnMatches,2);
//
//
//				//find good matches
//				//-- Filter matches using the Lowe's ratio test
//				float ratioThresh = 0.7f;
//				List<DMatch> listOfGoodMatches = new ArrayList<>();
//				for (int i = 0; i < knnMatches.size(); i++) {
//					if (knnMatches.get(i).rows() > 1) {
//						DMatch[] matches = knnMatches.get(i).toArray();
//						if (matches[0].distance < ratioThresh * matches[1].distance) {
//							listOfGoodMatches.add(matches[0]);
//						}
//					}
//				}
//				MatOfDMatch goodMatches = new MatOfDMatch();
//				goodMatches.fromList(listOfGoodMatches);
//
//				//draw matches
//				Mat imgMatches = new Mat();
//				Features2d.drawMatches(resizedImg, keypoints1, secondImg, keypoints2, goodMatches, 
//						imgMatches, Scalar.all(-1), Scalar.all(-1), new MatOfByte(), Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);
//				//show detected matches
//				//System.out.println(goodMatches.dump());
//				ImShow("Matches of the Images",imgMatches);
//				int row = goodMatches.rows();
//				System.out.println(row);
//			}	
//		}
//	}
//	
//	public static void main(String[] args){
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		//RGB(); //prints out RGB result
//		//BGR_HSV();
//		//single_threshold();
//		//multi_threshold("Twizy_assets/p1.jpg");
//		//extract("Twizy_assets/p1.jpg");
//		
//		//Gets the outline of the circles in the image
//		//shape_contour("Twizy_assets/p1.jpg");
//		
//		//Extracts the found images within the image
//		//template("Twizy_assets/image_with_hidden_signs.jpg");
//		
//		//Shows the SURF "Interest Points" Keypoints of the Image
//		//feature_detect("assets/Ball_three.png","assets/Billard_Balls.jpg");			//Get Images Keypoints using SURF
//		
//		//Compares the Extracts (template) with the original image
//		//needs to be changed
//		System.out.println("Matched Points of Speed 110: ");
//		matching("Twizy_assets/p3.jpg","Twizy_assets/speed110.jpg"); 	//Matching of the Images
//		
//		System.out.println("\n" + "Matched Points of Speed 30: ");
//		matching("Twizy_assets/p3.jpg","Twizy_assets/speed30.jpg"); 	//Matching of the Images
//		
//		//System.out.println("\n" + "Matched Points of Speed 50: ");
//		//matching("Twizy_assets/p3.jpg","Twizy_assets/speed50.jpg"); 	//Matching of the Images
//		
//		//System.out.println("\n" + "Matched Points of Speed 70: ");
//		//matching("Twizy_assets/p2.jpg","Twizy_assets/speed70.jpg"); 	//Matching of the Images
//		
//		//System.out.println("\n" + "Matched Points of Speed 90: ");
//		//matching("Twizy_assets/p2.jpg","Twizy_assets/speed90.jpg"); 	//Matching of the Images
//		
//		//System.out.println("\n" + "Matched Points of Speed Two Cars: ");
//		//matching("Twizy_assets/p2.jpg","Twizy_assets/two_cars.jpg"); 	//Matching of the Images
//	}
//	
//	
//}
//
//
//}
