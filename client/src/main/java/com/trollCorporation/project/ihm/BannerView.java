package com.trollCorporation.project.ihm;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BannerView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8454329494131543657L;

	public BannerView() {
		JLabel title = new JLabel("Bienvenue !");
		this.add(title);
	}
}
