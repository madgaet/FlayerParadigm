package com.trollCorporation.project.ihm;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class HomeView extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2566415999396216862L;

	public HomeView() {
		this.setBorder(new LineBorder(Color.RED));
		this.setBackground(Color.BLUE);
		ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("images/home2.jpg"));
		JLabel imageLabel = new JLabel(img);
		this.add(imageLabel);
	}
}
