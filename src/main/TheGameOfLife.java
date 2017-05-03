package main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;

public class TheGameOfLife {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("The Game Of LIfe");
		frame.setLayout(new BorderLayout());
		
		GLCapabilities caps = new GLCapabilities(null);
		
		GamePanel panel = new GamePanel(caps);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.requestFocusInWindow();
		frame.setVisible(true);		
		
		panel.run();
	}
}







