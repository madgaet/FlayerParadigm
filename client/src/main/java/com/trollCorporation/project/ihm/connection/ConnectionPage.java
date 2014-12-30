package com.trollCorporation.project.ihm.connection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import com.trollCorporation.project.ihm.LogoView;
import com.trollCorporation.project.utils.ImageLoader;

public class ConnectionPage extends JFrame {

	private static final long serialVersionUID = -3298364427023876210L;
	private static final int PAGEWIDTH = 800;
	private static final int PAGEHEIGHT = 600;
	private static final int TABBLEDWIDTH = 500;
	private static final int TABBLEDHEIGHT = 450;

	private JPanel infoPanel;
	private JLabel infoLabel;
	private JTabbedPane tabbledPane;
	
	private ConnectionView connectionView;
	private RegistrationView registrationView;

	public ConnectionPage() {
		this.setSize(PAGEWIDTH, PAGEHEIGHT);
		JPanel page = new ConnectionPanel();
		page.setLayout(new BorderLayout());
		page.add(createConnectionPage(), BorderLayout.CENTER);
		this.getContentPane().add(page);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private class ConnectionPanel extends JPanel {
		private static final long serialVersionUID = -7077719103791550081L;
		private Image backgroundImage;

		public ConnectionPanel() {
			Dimension panelDim = new Dimension(PAGEWIDTH, PAGEHEIGHT);
			this.setPreferredSize(panelDim);
			backgroundImage = ImageLoader.prepareImage("images/home2.jpg");
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, this);
		}
	}
	

	private Box createConnectionPage() {
		Box page = Box.createVerticalBox();
		page.setAlignmentX(Box.LEFT_ALIGNMENT);
		page.setOpaque(false);
		
		//Page title
		Box topBox = Box.createHorizontalBox();
		topBox.setPreferredSize(new Dimension(PAGEWIDTH, PAGEHEIGHT-TABBLEDHEIGHT));
		topBox.setOpaque(true);
		topBox.setBackground(Color.WHITE);
		JPanel logo = new LogoView();
		topBox.add(logo);
		page.add(topBox);
		
		Box centerBox = Box.createHorizontalBox();
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(false);
		//Tabbled page (Connection or Registration)
		tabbledPane = new JTabbedPane();
		tabbledPane.setPreferredSize(new Dimension(TABBLEDWIDTH, TABBLEDHEIGHT));
		tabbledPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		tabbledPane.addChangeListener(changeEvent -> {changeTab(changeEvent);});
		
		connectionView = new ConnectionView(this);
		registrationView = new RegistrationView(this);
		tabbledPane.addTab("Connection", createConnectionPanel());
		tabbledPane.addTab("Registration", createRegistrationPanel());
		centerPanel.add(tabbledPane, BorderLayout.CENTER);
		
		//info panel
		centerPanel.add(createInfoPanel(), BorderLayout.EAST);
		centerBox.add(centerPanel);
		page.add(centerBox);
		
		return page;
	}

	private Box createConnectionPanel() {
		Box connPanelBox = Box.createVerticalBox();
		connPanelBox.setPreferredSize(new Dimension(TABBLEDWIDTH, TABBLEDHEIGHT));
		
		JPanel title = new JPanel();
		title.setOpaque(false);
		JLabel titleText = new JLabel("<html><br><h1>Connection</h1></html>");
		title.add(titleText);
		connPanelBox.add(title);

		Box connView = connectionView.getView();
		connPanelBox.add(connView);

		return connPanelBox;
	}

	private Box createRegistrationPanel() {
		Box registerPanelBox = Box.createVerticalBox();
		registerPanelBox.setPreferredSize(new Dimension(TABBLEDWIDTH, TABBLEDHEIGHT));

		JPanel title = new JPanel();
		title.setOpaque(false);
		JLabel titleText = new JLabel("<html><br><h1>Registration</h1></html>");
		title.add(titleText);
		registerPanelBox.add(title);

		Box registerView = registrationView.getView();
		registerPanelBox.add(registerView);

		return registerPanelBox;
	}

	private JPanel createInfoPanel() {
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setPreferredSize(new Dimension(PAGEWIDTH-TABBLEDWIDTH,TABBLEDHEIGHT));
		container.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
		infoPanel = new JPanel(new BorderLayout());
		infoPanel.setPreferredSize(new Dimension((PAGEWIDTH-TABBLEDWIDTH-20), TABBLEDHEIGHT/2));
		infoPanel.setBackground(new Color(0,0,0,0));
		infoLabel = new JLabel();
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(infoLabel, BorderLayout.CENTER);
		container.add(infoPanel);
		return container;
	}

	void setErrorMessage(final String message) {
		Color red = new Color((float) 1, (float) 0, (float) 0, (float) 0.5);
		setMessage(red, message);
	}

	void setSuccessMessage(final String message) {
		Color green = new Color((float) 0, (float) 1, (float) 0, (float) 0.5);
		setMessage(green, message);
	}

	void setInfoMessage(final String message) {
		Color orange = new Color((float) 1, (float) 0.6, (float) 0, (float) 0.5);
		setMessage(orange, message);
	}
	
	void enableTabChange(boolean active) {
		tabbledPane.setEnabled(active);
	}

	private void setMessage(final Color color, final String message) {
		infoPanel.setBackground(color);
		infoLabel.setText(message);
		repaint();
	}
	
	public void changeTab(ChangeEvent e) {
		reset();
	}
	
	public void reset() {
		if (infoPanel != null) { 
			infoPanel.setBackground(new Color(0,0,0,0));
		}
		if (infoLabel != null) {
			infoLabel.setText("");
		}
		connectionView.reset();
		registrationView.reset();
		repaint();
	}
}
