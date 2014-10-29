package com.trollCorporation.project.ihm;

import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JFrame;

import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;

public class HomePage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1090911947475162016L;

	public HomePage() {
		createHomePage();
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void createHomePage() {
		Box homePage = Box.createVerticalBox();
		//Banner
		Box banner = Box.createHorizontalBox();
		banner.add(new BannerView());
		homePage.add(banner);
		
		//Home
		Box home = Box.createHorizontalBox();
		home.add(new HomeView());
		homePage.add(home);
		
		//Chat
		Box chat = Box.createHorizontalBox();
		ChatboxOperationsController msgController = new ChatboxOperationsControllerImpl();
		chat.add(new ChatView(msgController));
		homePage.add(chat);
		
		//add the home page to the page
		this.add(homePage);
	}
	
}
