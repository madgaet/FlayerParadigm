package com.trollCorporation.project.ihm;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;

public class HomePage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1090911947475162016L;
	private Dimension pageDimension;

	public HomePage(final String username) {
		this.pageDimension = Toolkit.getDefaultToolkit().getScreenSize();
		createHomePage(username);
		this.setTitle(username);
		this.setSize(pageDimension);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void createHomePage(final String username) {
		Box homePage = Box.createVerticalBox();
		//Banner
		Box banner = Box.createHorizontalBox();
		JPanel jpBanner = new BannerView();
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
		ChatboxOperationsController msgController = new ChatboxOperationsControllerImpl(username);
		Dimension chatDimension = new Dimension(pageDimension.width, pageDimension.height/3);
		JPanel jpChatView = new ChatView(msgController, chatDimension);
		jpChatView.setPreferredSize(chatDimension);
		chat.add(jpChatView);
		container.add(chat);
		
		homePage.add(container);
		//add the home page to the page
		this.add(homePage);
	}
	
}
