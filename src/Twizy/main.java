package Twizy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.xfeatures2d.SURF;

public class main {
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//VIDEO Section
		JFrame jframe = new JFrame("Detection of Panel in the Video");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
				
		List<Integer> radiusList = new ArrayList<>();
		List<Integer> speedList = new ArrayList<>();
		List<Integer> idVideo = new ArrayList<>();
		
		//Radius
		int currentRadius = 0;
		int previousRadius = 0;
		boolean lastRadius = false;
		int radiusCount = 0;
		
		//Speed
		boolean accurateSpeed = false; 
		int speedCount = 0;
		int currentSpeed = 0;
		int previousSpeed = 0;
		
		boolean newPanel = false;
		boolean imShow = false;
		
		VideoCapture camera = new VideoCapture("Twizy_assets/ourVid.avi");
		
		while (camera.read(frame)) {		
			//checks to see Radius is filled
			//System.out.println(radiusList);
			if(newPanel || idVideo.isEmpty()) {
				idVideo = roadDetect.detectionImages(frame,speedList,accurateSpeed,radiusList,lastRadius,imShow);
				newPanel = false;
				//System.out.println(speedList);
			}else {
				boolean trace = roadDetect.traceOnly(frame,imShow);
				newPanel = trace;
			}	
			
			ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();
		}
	}

	public static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}
}

