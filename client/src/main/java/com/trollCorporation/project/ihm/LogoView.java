package com.trollCorporation.project.ihm;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.trollCorporation.project.utils.ImageLoader;

public class LogoView extends JPanel {
	
	private static final long serialVersionUID = 8454329494131543657L;
	private Image logo;
	
	public LogoView() {
		this.setOpaque(false);
		logo = ImageLoader.prepareImage("images/logo2.jpg");
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(logo, 0, 0, this);
	}
}
