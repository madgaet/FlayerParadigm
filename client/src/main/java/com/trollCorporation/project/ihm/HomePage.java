package com.trollCorporation.project.ihm;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.trollCorporation.common.exceptions.ConnectionException;

public class HomePage extends JFrame {

	private static final long serialVersionUID = -1090911947475162016L;
	private static final int MIN_PAGE_WIDTH = 800;
	
	private Dimension pageDimension;

	public HomePage(final String username) throws ConnectionException {
		this.pageDimension = Toolkit.getDefaultToolkit().getScreenSize();
		createHomePage();
		this.setTitle(username);
		this.setSize(pageDimension);
		this.setMaximumSize(pageDimension);
		this.setMinimumSize(new Dimension(MIN_PAGE_WIDTH, 600));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void createHomePage() throws ConnectionException {
		Box homePage = Box.createVerticalBox();
		//Banner
		Box banner = Box.createHorizontalBox();
		BannerView jpBanner = new BannerView();
		jpBanner.setMinimumSize(new Dimension(MIN_PAGE_WIDTH, BannerView.MIN_BANNER_HEIGHT));
		jpBanner.setPreferredSize(new Dimension(pageDimension.width, pageDimension.height/18));
		banner.add(jpBanner);
		homePage.add(banner);
		
		//Container under banner
		Box container = Box.createVerticalBox();
		//Home
		Box home = Box.createHorizontalBox();
		JPanel jpHomeView = new HomeView();
		jpHomeView.setPreferredSize(new Dimension(pageDimension.width, pageDimension.height/2));
		home.add(jpHomeView);
		container.add(home);
		
		//Chat
		Box chat = Box.createHorizontalBox();
		Dimension chatDimension = new Dimension(pageDimension.width, pageDimension.height/3);
		JPanel jpChatView = new ChatView(chatDimension);
		jpChatView.setPreferredSize(chatDimension);
		chat.add(jpChatView);
		container.add(chat);
		
		homePage.add(container);
		//add the home page to the page
		this.add(homePage);
	}
	
}
