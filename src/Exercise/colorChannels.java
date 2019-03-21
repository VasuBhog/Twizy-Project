package Exercise;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

public class colorChannels {
	
	//read file
	public static Mat LectureImage(String file) {
		File f = new File(file);
		Mat m = Imgcodecs.imread(f.getAbsolutePath());
		return m;
	}
	
	//Display matrix in graphical window
	public static void ImShow(String title, Mat img){
		  MatOfByte matOfByte = new MatOfByte();
		  Imgcodecs.imencode(".png", img, matOfByte);
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
	
	
	//splitting image up into RGB
	public static void RGB(){
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		  Mat m= LectureImage("assets/bgr.png");
		  Vector<Mat> channels = new Vector<Mat>();
		  Core.split(m,channels); //break a matrix of an image
		  //for(int i=0; i<channels.size();i++) {
			  //ImShow(Integer.toString(i),channels.get(i));
		  //}
		  //BGR order
		  Mat dst = Mat.zeros(m.size(), m.type());
		  Vector<Mat> chans = new Vector<Mat>();
		  Mat empty = Mat.zeros(m.size(), CvType.CV_8UC1);
		  for (int i=0; i < channels.size(); i++) {
			  //ImShow(Integer.toString(i),channels.get(i));
			  chans.removeAllElements();
			  for (int j=0; j<channels.size();j++) {
				  if(j != i) {
					chans.add(empty);
				  }else {
					  chans.add(channels.get(i));
				  }
			  }
			  Core.merge(chans, dst);
			  ImShow(Integer.toString(i),dst);
		  }
	}
	
	//Change the color space - getting HSV (Hue-Saturation-Value)
	public static void BGR_HSV(){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("assets/hsv.png");
		Mat output = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, output, Imgproc.COLOR_BGR2HSV);
		ImShow("HSV",output);
		Vector<Mat> channels = new Vector<Mat>();
		Core.split(output, channels);
		double hsv_values[][] = {{1,255,255},{179,1,255},{179,0,1}};
		for (int i=0; i<3; i++) {
			ImShow(Integer.toString(i)+"-HSV",channels.get(i));
			Mat chans[] = new Mat[3];
			for(int j=0;j<3;j++) {
				Mat empty = Mat.ones(m.size(), CvType.CV_8UC1);
				Mat comp = Mat.ones(m.size(), CvType.CV_8UC1);
				Scalar v = new Scalar(hsv_values[i][j]);
				Core.multiply(empty, v, comp);
				chans[j] = comp;
			}
			chans[i] = channels.get(i);
			Mat dst = Mat.zeros(output.size(),output.type());
			Mat res = Mat.zeros(dst.size(),dst.type());
			Core.merge(Arrays.asList(chans), dst);
			Imgproc.cvtColor(dst, res, Imgproc.COLOR_HSV2BGR);
			ImShow(Integer.toString(i),res);

		}
	}
	
	//Thresholding of an image by color
	public static void single_threshold() {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("assets/circles.jpg");
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img = new Mat();
		Core.inRange(hsv_image, new Scalar(0,100,100), new Scalar(10,255,255), threshold_img);
		ImShow("Circles NOT with smoothing",threshold_img);
		Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9,9), 2,2);
		ImShow("Circles with smoothing",threshold_img);
	}
	
	//Multi threshold - for multiple objects 
	public static Mat multi_threshold(String file) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage(file);
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img1 = new Mat();//red - orange
		Mat threshold_img2 = new Mat(); //red
		Mat threshold_img3 = new Mat(); //yellow
		Mat threshold_img4 = new Mat(); //black
		Mat threshold_img5 = new Mat(); //blue
		Mat threshold_img6 = new Mat(); //purple
		Mat threshold_img = new Mat();
		//ImShow("Circles",m);
		//ImShow("HSV",hsv_image);
		//orange color - hsv range
		Core.inRange(hsv_image, new Scalar(0,100,100), new Scalar(10,255,255), threshold_img1);
		//red color - hsv range
		Core.inRange(hsv_image, new Scalar(160,100,100), new Scalar(179,255,255), threshold_img2);
		//yellow color - hsv range
		Core.inRange(hsv_image, new Scalar(20,100,100), new Scalar(65,255,255), threshold_img3);
		//black color - hsv range
		Core.inRange(hsv_image, new Scalar(0,0,0), new Scalar(180,255,30), threshold_img4);
		//blue color - hsv range
		Core.inRange(hsv_image, new Scalar(110,50,50), new Scalar(130,255,255), threshold_img5);
		//purple color - hsv range
		Core.inRange(hsv_image, new Scalar(130,100,50), new Scalar(160,255,255), threshold_img6);
		//Combining the images 
		Core.bitwise_or(threshold_img1, threshold_img2, threshold_img); //orange and red circle
		Core.bitwise_or(threshold_img, threshold_img3, threshold_img); //previous circle and yellow circle
		Core.bitwise_or(threshold_img, threshold_img4, threshold_img); //pre-circle and black
		Core.bitwise_or(threshold_img, threshold_img5, threshold_img); //pre-circle and blue 
		Core.bitwise_or(threshold_img, threshold_img6, threshold_img); //pre-circle and blue 
		//ImShow("Circles NOT with smoothing",threshold_img);
		Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9,9), 2,2);
		ImShow("Circles with smoothing",threshold_img);
		return threshold_img;

	}
	
	//Edge Detection - contour is border between two objects in an image
	
	public static void extract(String file) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage(file);
		ImShow("Circles",m);
		Mat hsv_image = Mat.zeros(m.size(),m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img = multi_threshold(file);
		//System.out.println(threshold_img);
		ImShow("Circles with Smoothing", threshold_img);
		int thresh = 100;
		Mat canny_output = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt4 hierarchy = new MatOfInt4();
		Imgproc.Canny( threshold_img, canny_output, thresh, thresh*2);
		Imgproc.findContours( canny_output, contours, hierarchy,Imgproc.RETR_EXTERNAL, 
		           Imgproc.CHAIN_APPROX_SIMPLE);
		Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
		Random rand = new Random();
		for( int i = 0; i< contours.size(); i++ ) {
		  Scalar color = new Scalar( rand.nextInt(255 - 0 + 1) , rand.nextInt(255 - 0 + 1), 
		                rand.nextInt(255 - 0 + 1) );
		  Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
		}
		ImShow("Contours",drawing);
	}
	
	//Detect Circles 
		public static Mat detect_circles(Mat img, String file) {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
			Mat m = LectureImage(file);
			Mat hsv_image = Mat.zeros(m.size(), m.type());
			Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
			Mat threshold_img1 = new Mat();//red - orange
			Mat threshold_img2 = new Mat(); //red
			Mat threshold_img3 = new Mat(); //yellow
			Mat threshold_img4 = new Mat(); //black
			Mat threshold_img5 = new Mat(); //blue
			Mat threshold_img6 = new Mat(); //purple
			Mat threshold_img = new Mat();
			//ImShow("Circles",m);
			//ImShow("HSV",hsv_image);
			//orange color - hsv range
			Core.inRange(hsv_image, new Scalar(0,100,100), new Scalar(10,255,255), threshold_img1);
			//red color - hsv range
			Core.inRange(hsv_image, new Scalar(160,100,100), new Scalar(179,255,255), threshold_img2);
			//yellow color - hsv range
			Core.inRange(hsv_image, new Scalar(20,100,100), new Scalar(65,255,255), threshold_img3);
			//black color - hsv range
			Core.inRange(hsv_image, new Scalar(0,0,0), new Scalar(180,255,30), threshold_img4);
			//blue color - hsv range
			Core.inRange(hsv_image, new Scalar(110,50,50), new Scalar(130,255,255), threshold_img5);
			//purple color - hsv range
			Core.inRange(hsv_image, new Scalar(130,100,50), new Scalar(160,255,255), threshold_img6);
			
			//Combining the images 
			Core.bitwise_or(threshold_img1, threshold_img2, threshold_img); //orange and red circle
			//Core.bitwise_or(threshold_img, threshold_img3, threshold_img); //previous circle and yellow circle
			//Core.bitwise_or(threshold_img, threshold_img4, threshold_img); //pre-circle and black
			//Core.bitwise_or(threshold_img, threshold_img5, threshold_img); //pre-circle and blue 
			//Core.bitwise_or(threshold_img, threshold_img6, threshold_img); //pre-circle and blue 
			//ImShow("Circles NOT with smoothing",threshold_img);
			Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9,9), 2,2);
			//ImShow("Circles with smoothing",threshold_img);
			return threshold_img;
		}
		
		public static List<MatOfPoint> detect_contour(Mat img, String file) {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
			Mat m = LectureImage(file);
			//ImShow("Circles",m);
			Mat hsv_image = Mat.zeros(m.size(),m.type());
			Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
			Mat threshold_img = detect_circles(hsv_image,file);
			//System.out.println(threshold_img);
			//ImShow("Circles with Smoothing", threshold_img);
			int thresh = 100;
			Mat canny_output = new Mat();
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			MatOfInt4 hierarchy = new MatOfInt4();
			Imgproc.Canny( threshold_img, canny_output, thresh, thresh*2);
			Imgproc.findContours( canny_output, contours, hierarchy,Imgproc.RETR_EXTERNAL, 
			           Imgproc.CHAIN_APPROX_SIMPLE);
			Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
			Random rand = new Random();
			for( int i = 0; i< contours.size(); i++ ) {
			  Scalar color = new Scalar( rand.nextInt(255 - 0 + 1) , rand.nextInt(255 - 0 + 1), 
			                rand.nextInt(255 - 0 + 1) );
			  Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
			}
			ImShow("Contours",drawing);
			return contours;
		}
	
	//Recognize Shapes Contours
	public static Mat shape_contour(String file) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage(file);
		ImShow("Circles and Rectangles",m);
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		ImShow("HSV",hsv_image);
		Mat threshold_img = detect_circles(hsv_image,file);
		ImShow("Smoothing",threshold_img);
		List<MatOfPoint> contours = detect_contour(threshold_img,file);
		
		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		for (int c=0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			matOfPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
			if ((contourArea/(Math.PI*radius[0]*radius[0])) >= 0.8){
				Imgproc.circle(m,center,(int)radius[0], new Scalar(0,255,0),2);
			}
		}
		ImShow("Detection of Red Circles",m);
		return m;
	}
	
	
	
	//Template matching - matching image with other image
	public static void template(String file) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage(file);
		ImShow("Ball",m);
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		ImShow("HSV",hsv_image);
		Mat threshold_img = detect_circles(hsv_image,file);
		ImShow("Smoothing",threshold_img);
		List<MatOfPoint> contours = detect_contour(threshold_img,file);
		
		//template matching 
		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		for (int c=0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			matOfPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
			if ((contourArea/(Math.PI*radius[0]*radius[0])) >= 0.8){
				Imgproc.circle(m,center,(int)radius[0], new Scalar(0,255,0),2);
				Rect rect = Imgproc.boundingRect(contour);
				Imgproc.rectangle(m, new Point(rect.x,rect.y),
						new Point(rect.x+rect.width,rect.y+rect.height),
						new Scalar(0,255,0),2);
				Mat tmp = m.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
				Mat ball = Mat.zeros(tmp.size(), tmp.type());
				tmp.copyTo(ball);
				ImShow("Ball",ball);
			}
		}
	}
	
	public static void feature_detect(String file, String file2) {
		//SURF Detector
				Mat firstImg = Imgcodecs.imread(file); //image 1 small image
				Mat secondImg = Imgcodecs.imread(file2); //image 2 is final template
				if (firstImg.empty() || secondImg.empty()){
					System.err.println("Cannot read images!");
					System.exit(0);
				}
				
				//DETECT keypoints using SURF detector
				double hessianThreshold = 400;
				int nOctaves = 4, nOctaveLayers = 3;
				boolean extended = false, upright = false;
				SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
				MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
				MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
				Mat descriptors1 = new Mat();
				Mat descriptors2 = new Mat();
				detector.detectAndCompute(firstImg, new Mat(), keypoints1, descriptors1);
				detector.detectAndCompute(secondImg, new Mat(), keypoints2, descriptors2);
				detector.detect(firstImg, keypoints1);
				
				//SURF FEATURES of First Image
				Mat surfImg1 = firstImg;
				Features2d.drawKeypoints(surfImg1, keypoints1, surfImg1);
				ImShow("Surf Keypoints",surfImg1);
				
				//SURF Features of Second Image
				Mat surfImg2 = secondImg;
				Features2d.drawKeypoints(surfImg2, keypoints2, surfImg2);
				ImShow("Surf Keypoints",surfImg2);
		
	}
	
	public static void matching(String file, String file2) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage(file);
		ImShow("Ball",m);
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		ImShow("HSV",hsv_image);
		Mat threshold_img = detect_circles(hsv_image,file);
		ImShow("Smoothing",threshold_img);
		List<MatOfPoint> contours = detect_contour(threshold_img,file);
		
		//template matching 
		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		for (int c=0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			matOfPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
			if ((contourArea/(Math.PI*radius[0]*radius[0])) >= 0.8){
				Imgproc.circle(m,center,(int)radius[0], new Scalar(0,255,0),2);
				Rect rect = Imgproc.boundingRect(contour);
				Imgproc.rectangle(m, new Point(rect.x,rect.y),
						new Point(rect.x+rect.width,rect.y+rect.height),
						new Scalar(0,255,0),2);
				Mat tmp = m.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
				Mat ball = Mat.zeros(tmp.size(), tmp.type());
				tmp.copyTo(ball);
				ImShow("Ball",ball);
			
		
				//SURF Detector
				Mat firstImg = ball; //image 1 small image
				Mat secondImg = Imgcodecs.imread(file2); //image 2 is final template
				if (firstImg.empty() || secondImg.empty()){
					System.err.println("Cannot read images!");
					System.exit(0);
				}
				
				
				//DETECT keypoints using SURF detector
				double hessianThreshold = 400;
				int nOctaves = 4, nOctaveLayers = 3;
				boolean extended = false, upright = false;
				SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
				MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
				MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
				Mat descriptors1 = new Mat();
				Mat descriptors2 = new Mat();
				detector.detectAndCompute(firstImg, new Mat(), keypoints1, descriptors1);
				detector.detectAndCompute(secondImg, new Mat(), keypoints2, descriptors2);
				detector.detect(firstImg, keypoints1);
				
				
				//Matching Descriptor vectors with BruteForce
				DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
				List<MatOfDMatch> knnMatches = new ArrayList<>();
				matcher.knnMatch(descriptors1, descriptors2, knnMatches,2);
				
				
				//find good matches
				//-- Filter matches using the Lowe's ratio test
		        float ratioThresh = 0.7f;
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
				Features2d.drawMatches(firstImg, keypoints1, secondImg, keypoints2, goodMatches, imgMatches);
				//show detected matches
				ImShow("Matches of the Images",imgMatches);
			}
		}
	}

	
	
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//RGB(); //prints out RGB result
		//BGR_HSV();
		//single_threshold();
		//multi_threshold("assets/circles_rectangles.jpg");
		//extract("assets/circles.jpg");
		//shape_contour("assets/Billard_Balls.jpg");
		//template("assets/Billard_Balls.jpg");
		//feature_detect("assets/Ball_three.png","assets/Billard_Balls.jpg");							//Get Images Keypoints using SURF
		matching("assets/Ball_three.png","assets/Billard_Balls.jpg"); 		//Matching of the Images
	}
}
