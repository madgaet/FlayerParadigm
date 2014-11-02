package com.trollCorporation.project.ihm;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class BannerView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8454329494131543657L;

	public BannerView() {
		JLabel title = new JLabel("Bienvenue !");
		this.setBorder(new LineBorder(Color.DARK_GRAY));
		this.setBackground(Color.GREEN);
		this.add(title);
	}
}
