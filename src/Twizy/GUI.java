package Twizy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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

public class GUI extends JFrame{
	
	static boolean imShow = false;
	static boolean hsvShow = false;

	static String pnum;
	JPanel panel;
	static JComboBox cb;
	static JLabel imageTest;
	
	
	public static void Interface() {
		int num=1;
		//Images only 
		List<Integer> radiusList = new ArrayList<>();
		List<Integer> speedList = new ArrayList<>();
		boolean accurateSpeed = false;
		boolean lastRadius = false;

		//Setting up images
		

		//creating Jframe instances Section
		JPanel panel = new JPanel();
		JFrame jframe = new JFrame("GUI for Road Signs");
		
		//exit on close
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
				
		//Create Widgets
		//ComboBox initializaiton 
		String refImages[] = new String[] {"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
		JComboBox<String> cb = new JComboBox<>(refImages);
		cb.setBounds(0,5,100,100);
		
		//image
		String pnum = cb.getSelectedItem().toString();
		JLabel imageTest = new JLabel(new ImageIcon("Twizy_assets/"+pnum+".jpg"));
		imageTest.setBounds(0, 0, 360, 360);
		
		
		//Detected Road Sign Initialization
		JButton speedDetect = new JButton("Detect Road Sign");
		speedDetect.setBounds(50, 300, 150, 50);
		
		
		//Detected Road Sign Button Click
		speedDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imShow = true; 
				//display the dialog
				String pnum = cb.getSelectedItem().toString();
				
				//Output items
				JLabel imageTest = new JLabel(new ImageIcon("Twizy_assets/"+pnum+".jpg"));
				imageTest.setBounds(500, 50, 360, 360);
				Mat imgOrigin = roadDetect.LectureImage("Twizy_assets/" + pnum +".jpg");
				roadDetect.ImShow("Road Image: "+pnum, imgOrigin );
				List<Integer> id = roadDetect.detectionImages(imgOrigin,speedList,accurateSpeed,radiusList,lastRadius,imShow);
			}
		});
		
		//HSV Button Initialization 
		JButton hsv = new JButton("HSV");
		hsv.setBounds(50, 100, 100, 50);
		
		//HSV Button Click
		hsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hsvShow = true;
				//display the dialog
				String pnum = cb.getSelectedItem().toString();
				System.out.println(pnum);
				Mat imgOrigin = roadDetect.LectureImage("Twizy_assets/"+pnum+".jpg");
				roadDetect.HSV(imgOrigin, hsvShow);
			}
		});
		
		//Detect Circles Button Initialization 
		JButton contour = new JButton("Contour");
		contour.setBounds(100, 400, 150, 50);
				
		//HSV Button Click
		contour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hsvShow = true;
				//display the dialog
				String pnum = cb.getSelectedItem().toString();
				System.out.println(pnum);
				Mat imgOrigin = roadDetect.LectureImage("Twizy_assets/"+pnum+".jpg");
				roadDetect.HSV(imgOrigin, hsvShow);
			}
		});
		
		//setting the Jframe options
		jframe.setSize(400, 400);
		jframe.getContentPane().setLayout(new FlowLayout());
		
		
		//Add gui elements 
		jframe.add(cb); //combobox
		jframe.add(imageTest);
		jframe.add(speedDetect); //detected sign
		jframe.add(hsv);		//output value
		jframe.add(contour);
		
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
		
	}
	

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(cb.getSelectedItem().equals("Select")) {
		}
		JOptionPane.showMessageDialog(this, cb.getSelectedItem());
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Interface();
		
		
	}
}