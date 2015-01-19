package com.trollCorporation.project.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.trollCorporation.project.client.Game;

public class BannerView extends JPanel {

	private static final long serialVersionUID = 8454329494131543657L;
	public static final int BANNER_HEIGHT = 130;
	private static final int MIN_BANNER_WIDTH = 800;

	private Box bannerBox;
	
	public BannerView() {
		bannerBox = Box.createHorizontalBox();
		this.setPreferredSize(null);
		bannerBox.setPreferredSize(new Dimension(MIN_BANNER_WIDTH, BANNER_HEIGHT));
		JPanel logo = new LogoView();
		bannerBox.add(logo);
		JButton jbDisconnect = new JButton("Disconnect");
		jbDisconnect.addActionListener(e -> {disconnect(e);});
		bannerBox.add(jbDisconnect);
		this.setBorder(new LineBorder(Color.DARK_GRAY));
		this.setBackground(Color.WHITE);
		this.add(bannerBox, BorderLayout.CENTER);
	}
	
	private void disconnect(ActionEvent e) {
		Game.getInstance().disconnect();
	}
	
	@Override
	public void setPreferredSize(Dimension d) {
		int width = MIN_BANNER_WIDTH;
		if (d != null) {
			width = Integer.max(MIN_BANNER_WIDTH, d.width);
		}
		int height = BANNER_HEIGHT;
		Dimension newDim = new Dimension(width, height);
		super.setPreferredSize(newDim);
	}
}
