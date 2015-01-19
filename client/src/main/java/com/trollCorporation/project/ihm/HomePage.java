package com.trollCorporation.project.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.trollCorporation.common.exceptions.ConnectionException;

public class HomePage extends JFrame {

	private static final long serialVersionUID = -1090911947475162016L;
	public static final int MIN_PAGE_WIDTH = 800;
	public static final int MIN_PAGE_HEIGHT = 600;
	
	private Dimension pageDimension;
	private String username;
	
	private JPanel jpHomeView;
	private JPanel jpChatView;

	public HomePage(final String username) throws ConnectionException {
		this.pageDimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.username = username;
		this.setTitle(username);
		this.setLayout(new BorderLayout());
		createHomePage();
		this.setSize(pageDimension);
		this.setMaximumSize(pageDimension);
		this.setMinimumSize(new Dimension(MIN_PAGE_WIDTH, MIN_PAGE_HEIGHT));
		this.addComponentListener(new ResizeListener());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void createHomePage() throws ConnectionException {
		JPanel homePage = new JPanel(new BorderLayout());
		//Banner
		JPanel banner = new JPanel(new BorderLayout());
		BannerView jpBanner = new BannerView();
		jpBanner.setMinimumSize(new Dimension(MIN_PAGE_WIDTH, BannerView.BANNER_HEIGHT));
		jpBanner.setMaximumSize(new Dimension(pageDimension.width, BannerView.BANNER_HEIGHT));
		banner.add(jpBanner, BorderLayout.CENTER);
		homePage.add(banner, BorderLayout.NORTH);
		
		//Container under banner
		int containerHeight = pageDimension.height - BannerView.BANNER_HEIGHT;
		//Home
		jpHomeView = new HomeView();
		homePage.add(jpHomeView, BorderLayout.CENTER);
		
		//Chat
		Dimension chatDimension = new Dimension(pageDimension.width/5, containerHeight);
		jpChatView = new ChatView(chatDimension, username);
		homePage.add(jpChatView, BorderLayout.EAST);
		
		//add the home page to the page
		this.add(homePage, BorderLayout.CENTER);
	}
	
	private class ResizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			Dimension newDim = e.getComponent().getSize();
			newDim.width = (int) Math.round(newDim.width/5)-5;
			newDim.height = newDim.height - BannerView.BANNER_HEIGHT;
			
			jpChatView.setSize(newDim);
			jpChatView.setPreferredSize(newDim);
		}
	}
	
}
