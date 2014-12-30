package com.trollCorporation.project.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.trollCorporation.project.client.Game;

public class BannerView extends JPanel {

	private static final long serialVersionUID = 8454329494131543657L;

	public BannerView() {
		this.setLayout(new BorderLayout());
		JLabel title = new JLabel("Bienvenue !");
		JButton jbDisconnect = new JButton("Disconnect");
		jbDisconnect.addActionListener(e -> {disconnect(e);});
		this.setBorder(new LineBorder(Color.DARK_GRAY));
		this.setBackground(Color.GREEN);
		this.add(title, BorderLayout.CENTER);
		this.add(jbDisconnect, BorderLayout.EAST);
	}
	
	private void disconnect(ActionEvent e) {
		Game.getInstance().disconnect();
	}
}
