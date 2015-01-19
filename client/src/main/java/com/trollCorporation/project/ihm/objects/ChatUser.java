package com.trollCorporation.project.ihm.objects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatUser {
	
	private String user;
	private JButton userButtons;
	private JPanel drawnChatBoxes;
	private JTextPane readTextArea;
	private JTextArea writeTextArea;
	
	private List<String> textMessages = new ArrayList<String>();
	
	public ChatUser(final String user) {
		this.user = user;
	}

	public final String getUser() {
		return user;
	}

	public final void setUser(String user) {
		this.user = user;
	}

	public final JButton getUserButtons() {
		return userButtons;
	}

	public final void setUserButtons(JButton userButtons) {
		this.userButtons = userButtons;
	}

	public final JPanel getDrawnChatBoxes() {
		return drawnChatBoxes;
	}

	public final void setDrawnChatBoxes(JPanel drawnChatBoxes) {
		this.drawnChatBoxes = drawnChatBoxes;
	}

	public final JTextPane getReadTextArea() {
		return readTextArea;
	}

	public final void setReadTextArea(JTextPane textArea) {
		this.readTextArea = textArea;
	}
	
	public final JTextArea getWriteTextArea() {
		return writeTextArea;
	}

	public final void setWriteTextArea(JTextArea textArea) {
		this.writeTextArea = textArea;
	}
	
	/**
	 * @param received by opposition to sent.
	 */
	public final void addTextMessage(final String message, final boolean received) {
		if (readTextArea != null) {
			textMessages.add(message);
			StyledDocument doc = readTextArea.getStyledDocument();
			SimpleAttributeSet style = new SimpleAttributeSet();
			try {
				if (received) {
					Color brown = new Color(205, 140, 85);
					StyleConstants.setForeground(style, brown);
					doc.insertString(doc.getLength(), message, style);
				} else {
					StyleConstants.setForeground(style, Color.BLUE);
					doc.insertString(doc.getLength(), message, style);
				}
			} catch (BadLocationException e) {};
			readTextArea.revalidate();
			if (userButtons != null && received) {
				userButtons.setBackground(new Color(155,155,0,125));
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) {
			return false;
		}
		ChatUser other = (ChatUser) obj;
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}
	
	
}
