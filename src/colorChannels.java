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
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
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
		  Mat m= LectureImage("bgr.png");
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
	
	//Change the color space - getting HSV 
	public static void BGR_HSV(){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("hsv.png");
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
		Mat m = LectureImage("circles.jpg");
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img = new Mat();
		Core.inRange(hsv_image, new Scalar(0,100,100), new Scalar(10,255,255), threshold_img);
		ImShow("Circles NOT with smoothing",threshold_img);
		Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9,9), 2,2);
		ImShow("Circles with smoothing",threshold_img);
	}
	
	//Multi threshold 
	public static Mat multi_threshold(Mat img) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("img");
		Mat hsv_image = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		Mat threshold_img1 = new Mat();
		Mat threshold_img2 = new Mat();
		Mat threshold_img = new Mat();
		Core.inRange(hsv_image, new Scalar(0,100,100), new Scalar(10,255,255), threshold_img1);
		Core.inRange(hsv_image, new Scalar(160,100,100), new Scalar(179,255,255), threshold_img2);
		Core.bitwise_or(threshold_img1, threshold_img2, threshold_img);
		//ImShow("Circles NOT with smoothing",threshold_img3);
		Imgproc.GaussianBlur(threshold_img, threshold_img, new Size(9,9), 2,2);
		//ImShow("Circles with smoothing",threshold_img3);
		return threshold_img;
	}
	
	//Edge Detection - contour is border between two objects in an image
	
	public static void extract() {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("circles.jpg");
		ImShow("Circles",m);
		Mat hsv_image = Mat.zeros(m.size(),m.type());
		Imgproc.cvtColor(m, hsv_image, Imgproc.COLOR_BGR2HSV);
		ImShow("HSV", hsv_image);
		Mat threshold_img = multi_threshold(hsv_image);
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
	
	public static void main(String[] args){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		//RGB(); //prints out RGB result
		//BGR_HSV();
		//single_threshold();
		//multi_threshold();
		extract();
	}
}
