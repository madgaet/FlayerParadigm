package com.trollCorporation.project.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.trollCorporation.project.utils.ImageLoader;

public class HomeView extends JPanel {

	private static final long serialVersionUID = -2566415999396216862L;

	private Image backgroundImg;
	
	public HomeView() {
		backgroundImg = ImageLoader.prepareImage("images/home2.jpg");
		this.setBorder(new LineBorder(Color.RED));
		this.setBackground(Color.BLUE);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImg, 0, 0, this);
	}
}
