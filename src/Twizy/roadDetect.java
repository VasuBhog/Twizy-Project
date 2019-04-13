package Twizy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SURF;


import org.opencv.imgcodecs.Imgcodecs;

public class roadDetect {
		//read file
	
		private static List<findCircle> findCircle = new ArrayList<findCircle>();
		private static List<Integer> res = new ArrayList<Integer>();

		public static Mat LectureImage(String file) {
			File f = new File(file);
			Mat m = Imgcodecs.imread(f.getAbsolutePath());
			return m;
		}
		
		//Display matrix in graphical window
		public static void ImShow(String title, Mat img){
			  MatOfByte matOfByte = new MatOfByte();
			  Imgcodecs.imencode(".jpg", img, matOfByte);
			  byte[] byteArray = matOfByte.toArray();
			  BufferedImage bufImage = null;
			  try {
				  InputStream in = new ByteArrayInputStream(byteArray);
				  bufImage = ImageIO.read(in);
				  JFrame frame = new JFrame();
				  frame.setTitle(title);
				  frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
				  frame.pack();
				  frame.setVisible(true);
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		}
		
		public static Mat HSV(Mat img) {
			Mat hsv = new Mat(img.size(),img.type());
			Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
			//ImShow("HSV",hsv);
			return(hsv);
		}
		
		//Detect Circles - uses HSV function
		public static Mat detect_circles(Mat img) {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
			
			//delete noise
			Imgproc.medianBlur(img, img, 3);
			Mat hsv_image = HSV(img);
			Mat threshold_img = new Mat();
			Mat threshold_img1 = new Mat();//lower red hue range (orange)
			Mat threshold_img2 = new Mat(); //higher red hue range 
			//Mat threshold_img3 = new Mat(); //yellow
			//Mat threshold_img4 = new Mat(); //black
			//Mat threshold_img5 = new Mat(); //blue
			//Mat threshold_img6 = new Mat(); //purple
			//ImShow("Circles",m);
			//ImShow("HSV",hsv_image);
			
			//orange color - hsv range 0 - 3 
			Core.inRange(hsv_image, new Scalar(0,100,100), new Scalar(6,255,255), threshold_img1);
			//red color - hsv rang  173 - 180
			Core.inRange(hsv_image, new Scalar(160,100,100), new Scalar(180,255,255), threshold_img2);
			
			//yellow color - hsv range
			//Core.inRange(hsv_image, new Scalar(20,100,100), new Scalar(65,255,255), threshold_img3);
			//black color - hsv range
			//Core.inRange(hsv_image, new Scalar(0,0,0), new Scalar(180,255,30), threshold_img4);
			//blue color - hsv range
			//Core.inRange(hsv_image, new Scalar(110,50,50), new Scalar(130,255,255), threshold_img5);
			//purple color - hsv range
			//Core.inRange(hsv_image, new Scalar(130,100,50), new Scalar(160,255,255), threshold_img6);

			//Combining the images 
			Core.bitwise_or(threshold_img2, threshold_img1, threshold_img); //orange and red circle
			
			//Core.bitwise_or(threshold_img, threshold_img3, threshold_img); //previous circle and yellow circle
			//Core.bitwise_or(threshold_img, threshold_img4, threshold_img); //pre-circle and black
			//Core.bitwise_or(threshold_img, threshold_img5, threshold_img); //pre-circle and blue 
			//Core.bitwise_or(threshold_img, threshold_img6, threshold_img); //pre-circle and blue 
			//ImShow("Circles NOT with smoothing",threshold_img);
			Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9,9), 2,2);
			//ImShow("Circles with smoothing",threshold_img);
			return threshold_img;
		}
		
		public static List<MatOfPoint> detect_contour(Mat img) {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
			Mat m = detect_circles(img);
			Mat circleMat = new Mat();
			
			Imgproc.HoughCircles(m, circleMat, Imgproc.CV_HOUGH_GRADIENT, 2, 100, 100, 90, 0, 1000);
			List<MatOfPoint> contours = show_contour(m);

			for (int x = 0; x < circleMat.cols(); x++) {
				Imgproc.drawContours(img, contours, x, new Scalar(255,255,0));
			}
			return(contours);
		}
			
		public static List<MatOfPoint> show_contour(Mat threshold_img){
			int thresh = 100;
			Mat canny_output = new Mat();
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			MatOfInt4 hierarchy = new MatOfInt4();
			Imgproc.Canny(threshold_img, canny_output, thresh, thresh*2);
			Imgproc.findContours(canny_output, contours, hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			
			Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
			Random rand = new Random();
			
			for( int i = 0; i< contours.size(); i++ ) {
				Scalar color = new Scalar( rand.nextInt(255 - 0 + 1) , rand.nextInt(255 - 0 + 1), 
						rand.nextInt(255 - 0 + 1) );
				Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
			}
		
			//ImShow("Contours",drawing);
			return contours;
		}
		
		
		//Recognize Shapes Contours
		public static int trace_contour(Mat img, List<MatOfPoint> contours) {
			int numCircle = 0;
			MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
			float[] radius = new float[1];
			Point center = new Point();
			for (int c=0; c < contours.size(); c++) {
				MatOfPoint contour = contours.get(c);
				double contourArea = Imgproc.contourArea(contour);
				matOfPoint2f.fromList(contour.toList());
				Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
				if ((contourArea/(Math.PI*radius[0]*radius[0])) >= .8 && radius[0]>15){
					//center2?
					Point center2 = new Point(center.x,center.y);
					//Change Trace color
					Imgproc.circle(img,center,(int)radius[0], new Scalar(0,255,0),1,8,0);
		
					findCircle.add(new findCircle(center2,(int)radius[0]));
					//System.out.println(radius[0]);
					//radiusList.add(radius[0]);

					numCircle++;
				}
				
			}
			//ImShow("Detection of Red Circles",img);
			return numCircle;
		}
		
		public static Mat extract_Circle(Mat src, Point center, int radius) {

			Mat mask = new Mat(src.rows(), src.cols(), CvType.CV_8U, Scalar.all(0));
			
			Imgproc.circle(mask, center, radius, new Scalar(255,255,255), -1, 8, 0 );
			
			//gets only the circle
			Mat masked = new Mat();
			src.copyTo( masked, mask );
			
			Mat thresh = new Mat();
			Imgproc.threshold( mask, thresh, 1, 255, Imgproc.THRESH_BINARY );

			List<MatOfPoint> contours = new ArrayList<>();
			Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

			Rect rect = Imgproc.boundingRect(contours.get(0));
			Mat cropped = masked.submat(rect);
			//ImShow("Cropped",cropped);
			return cropped;

		}
		
		public static MatOfDMatch matching(Mat ref, Mat img) {
			Mat resizedImg = new Mat();
			Size sz = new Size(154,154);
			Imgproc.resize(ref, ref, sz);
			//resize the image
			int interpolation = Imgproc.INTER_CUBIC;
			Imgproc.resize(img, resizedImg,sz,0,0,interpolation);
			
			Mat imgGray = new Mat(resizedImg.rows(), resizedImg.cols(), resizedImg.type());
			Imgproc.cvtColor(resizedImg, imgGray, Imgproc.COLOR_BGR2GRAY);
			Core.normalize(imgGray, imgGray, 0, 255, Core.NORM_MINMAX);
			
			Mat refGray = new Mat(ref.rows(), ref.cols(), ref.type());
			Imgproc.cvtColor(ref, refGray, Imgproc.COLOR_BGR2GRAY);
			Core.normalize(refGray, refGray, 0, 255, Core.NORM_MINMAX);
			
			//SURF Detector - MATCHING 
			if (imgGray.empty() || refGray.empty()){
				System.err.println("Cannot read images!");
				System.exit(0);
			}
			//DETECT keypoints using SURF detector
			double hessianThreshold = 900;
			int nOctaves = 4, nOctaveLayers = 3;
			boolean extended = false, upright = false;
			SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
			
			MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
			MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
			Mat descriptors1 = new Mat();
			Mat descriptors2 = new Mat();
			
			detector.detectAndCompute(refGray, new Mat(), keypoints1, descriptors1);
			detector.detectAndCompute(imgGray, new Mat(), keypoints2, descriptors2);
			//detector.detect(resizedImg, keypoints1);

			//Matching Descriptor vectors with BruteForce
			DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
			List<MatOfDMatch> knnMatches = new ArrayList<>();
			matcher.knnMatch(descriptors1, descriptors2, knnMatches,2);

			//find good matches
			//-- Filter matches using the Lowe's ratio test
			float ratioThresh = 0.76f;
			List<DMatch> listOfGoodMatches = new ArrayList<>();
			for (int i = 0; i < knnMatches.size(); i++) {
				if (knnMatches.get(i).rows() > 1) {
					DMatch[] matches = knnMatches.get(i).toArray();
					if (matches[0].distance < ratioThresh * matches[1].distance) {
						listOfGoodMatches.add(matches[0]);
					}
				}
			}
			MatOfDMatch goodMatches = new MatOfDMatch();
			goodMatches.fromList(listOfGoodMatches);

			//draw matches
			Mat imgMatches = new Mat();
			Features2d.drawMatches(ref, keypoints1, resizedImg, keypoints2, goodMatches, imgMatches);
			int row = goodMatches.rows();
			//System.out.println(row);
			//ImShow("",imgMatches);
			return(goodMatches);
		}	

		
		public static List<Integer> matchingImages(Mat cropped, List<Integer> speedList, Boolean accurateSpeed, Boolean lastRadius) {
			List<DMatch> matchImage;
			int[] speed = {30,50,70,90,110};
			int[] nbMatchSpeed = new int[speed.length+1];
			int indiceMatchSpeed=-1;
			int maxMatchSpeed=-1;
			
			//Keypoints that match
			for(int i=0;i<speed.length+1;i++) {	
				//prints out matches for each speed
				if(i<speed.length) {
					//call matching
					matchImage=matching(roadDetect.LectureImage("Twizy_assets/ref"+speed[i]+".jpg"), cropped).toList();
					nbMatchSpeed[i]=matchImage.size();
					//System.out.println("Speed " +speed[i]+" : "+nbMatchSpeed[i] + " Matched Keypoints");
				} else {
					//prints matches for the double road sign
					matchImage=matching(roadDetect.LectureImage("Twizy_assets/refdouble.jpg"), cropped).toList();
					nbMatchSpeed[i]=matchImage.size();
					//System.out.println("Double Road Sign"+" : " + nbMatchSpeed[i] + " Matched Keypoints");
				}
				
				//max speed to get output
				if(nbMatchSpeed[i]>maxMatchSpeed) {
					indiceMatchSpeed=i;
					maxMatchSpeed=nbMatchSpeed[i];
				}
				
			}

			res.add(indiceMatchSpeed);
			speedList.add(speed[indiceMatchSpeed]);

//			int totalSpeed = speedList.size();
//			int occurences30 = Collections.frequency(speedList, 30);
//			int occurences50 = Collections.frequency(speedList, 50);
//			int occurences70 = Collections.frequency(speedList, 70);
//			int occurences90 = Collections.frequency(speedList, 90);
//			int occurences110 = Collections.frequency(speedList, 110);
//
//			System.out.println(speedList);
//			System.out.println("\nFrequency 30: " + occurences30+"\n");
//			System.out.println("\nFrequency 50: " + occurences50+"\n");
//			System.out.println("\nFrequency 70: " + occurences70+"\n");
//			System.out.println("\nFrequency 90: " + occurences90+"\n");
//			System.out.println("\nFrequency 110: " + occurences110+"\n");
//			System.out.println(speedList);
			
//			if(occurences30 > 10) {
//				System.out.println("\nSpeed Found: 30\n");
//				speedList.clear();
//				
//			}else if (occurences50 >= 1) {
//				System.out.println("\nSpeed Found: 50\n");
//				speedList.clear();
//				
//			}else if(occurences70 > 1) {
//				System.out.println("\nSpeed Found: 70\n");
//				speedList.clear();
//				
//			}else if(occurences90 > 1) {
//				System.out.println("\nSpeed Found: 90\n");
//				speedList.clear();
//				
//			}else if(occurences110 > 1) {
//				System.out.println("\nSpeed Found: 110\n");
//				speedList.clear();
//			}
			
			if(indiceMatchSpeed==speed.length) {
				System.out.println("\nDouble Road Sign Found!\n");
			} else {
				System.out.println("\nSpeed Found: " +speed[indiceMatchSpeed]+" Km/h\n");
			}
				
			return(res);
		}

		public static List<Integer> detectionImages(Mat img, List<Integer> speedList, Boolean accurateSpeed, List<Integer> radiusList, Boolean lastRadius) {

			res = new ArrayList<Integer>();
			findCircle = new ArrayList<findCircle>();
			
			Mat imgOri = new Mat();
			img.copyTo(imgOri);

			List<MatOfPoint> panel = roadDetect.detect_contour(img);

			trace_contour(imgOri,panel);
			

			for(int x=0;x<findCircle.size();x++) {
				
				Mat cropped=extract_Circle(imgOri,findCircle.get(x).center,findCircle.get(x).radius);
				radiusList.add(findCircle.get(x).radius);
				res  = matchingImages(cropped,speedList,accurateSpeed,lastRadius);
			}
			
			if(findCircle.size() == 0) {
				
			}
			//ImShow("Detected Panel", imgOri);
			
			return(res);
		}
		
		public static boolean traceOnly(Mat img) {

			findCircle = new ArrayList<findCircle>();
			boolean newCircle = false;
			int num = 0;

			List<MatOfPoint> panel = roadDetect.detect_contour(img);

			num=trace_contour(img,panel);
			
			if(num<1) {
				newCircle = true;
			}
			
			//ImShow("Detected Panel", imgOri);
			
			return newCircle;
		}
		
		
		
		
}