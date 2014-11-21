package com.trollCorporation.project.ihm.connection;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ConnectionPage extends JFrame {

	private static final long serialVersionUID = -3298364427023876210L;
	private static final int PAGEWIDTH = 800;
	private static final int PAGEHEIGHT = 450;
	private static final int FONTSIZE = 32;
	private static final int TABBLEDWIDTH = 500;
	private static final int TABBLEDHEIGHT = 150;

	private JPanel infoPanel;
	private JLabel infoLabel;
	private ConnectionView connectionView;
	private RegistrationView registrationView;

	public ConnectionPage() {
		this.add(createConnectionPage());
		this.setSize(PAGEWIDTH, PAGEHEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private Box createConnectionPage() {
		Box page = Box.createVerticalBox();
		page.setAlignmentX(Box.LEFT_ALIGNMENT);
		
		//Page title
		Box topBox = Box.createHorizontalBox();
		JLabel title = new JLabel("Welcome to the Flayer Paradigm");
		title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONTSIZE));
		topBox.add(title);
		page.add(topBox);
		
		Box centerBox = Box.createHorizontalBox();
		//Tabbled page (Connection or Registration)
		JTabbedPane tabbledPane = new JTabbedPane();
		tabbledPane.setSize(TABBLEDWIDTH, TABBLEDHEIGHT);
		tabbledPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		tabbledPane.addChangeListener(new TabChangeListener());
		
		connectionView = new ConnectionView(this);
		registrationView = new RegistrationView(this);
		tabbledPane.addTab("Connection", createConnectionPanel());
		tabbledPane.addTab("Registration", createRegistrationPanel());
		centerBox.add(tabbledPane);
		
		//info panel
		centerBox.add(createInfoPanel());
		
		page.add(centerBox);
		
		return page;
	}

	private Box createConnectionPanel() {
		Box connPanelBox = Box.createVerticalBox();
		connPanelBox.setAlignmentX(LEFT_ALIGNMENT);
		connPanelBox.setSize(TABBLEDWIDTH, TABBLEDHEIGHT);

		connPanelBox.add(new JLabel("<html><b>Connection</b></html>"));

		Box connView = connectionView.getView();
		connPanelBox.add(connView);

		return connPanelBox;
	}

	private Box createRegistrationPanel() {
		Box registerPanelBox = Box.createVerticalBox();
		registerPanelBox.setAlignmentX(LEFT_ALIGNMENT);
		registerPanelBox.setSize(TABBLEDWIDTH, TABBLEDHEIGHT);

		registerPanelBox.add(new JLabel("<html><b>Registration</b></html>"));

		Box registerView = registrationView.getView();
		registerPanelBox.add(registerView);

		return registerPanelBox;
	}

	private JPanel createInfoPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(25, 0, 200, 0));
		infoPanel = new JPanel();
		infoLabel = new JLabel();
		infoPanel.add(infoLabel);
		panel.add(infoPanel);
		return panel;
	}

	void setErrorMessage(final String message) {
		setMessage(Color.RED, message);
	}

	void setSuccessMessage(final String message) {
		setMessage(Color.GREEN, message);
	}

	void setInfoMessage(final String message) {
		setMessage(Color.ORANGE, message);
	}

	private void setMessage(final Color color, final String message) {
		infoPanel.setBackground(color);
		infoLabel.setText(message);
	}

	private class TabChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent changeEvent) {
			//JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
			//int index = sourceTabbedPane.getSelectedIndex();
			reset();
		}
	}
	
	public void reset() {
		if (infoPanel != null && infoLabel != null) {
			infoLabel.setText("");
			infoPanel.setBackground(null);
		}
		connectionView.reset();
		registrationView.reset();
	}
}
