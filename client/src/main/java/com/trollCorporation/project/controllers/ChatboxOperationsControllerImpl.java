package com.trollCorporation.project.controllers;

import java.util.List;

import javax.swing.JOptionPane;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.operations.MessageOperation;
import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.services.ConnectionToServer;

public class ChatboxOperationsControllerImpl implements ChatboxOperationsController {

	private static ChatboxOperationsController singleton;
	private Observer observer;
	private boolean lastMessageChanged;
	private Message lastMessage;
	
	private ConnectionToServer getConnection() throws ConnectionException {
		return ConnectionToServer.getInstance();
	}

	private ChatboxOperationsControllerImpl() {
	}
	
	public synchronized static ChatboxOperationsController getInstance() 
		throws ConnectionException {
		if (singleton == null) {
			synchronized (ConnectionToServer.class) {
				if (singleton == null) {
					singleton = new ChatboxOperationsControllerImpl();
				}
			}
		}
		return singleton;
	}
	
	public boolean sendMessage(final String sender, final String message) {
		try {
			if (message != null && !message.trim().isEmpty()) {
				Message messageObj = new Message(message, sender);
				MessageOperation messageOperation = new MessageOperation(messageObj);
				getConnection().sendOperation(messageOperation);
				return true;
			}
		} catch (ConnectionException e) {
			JOptionPane.showMessageDialog(null, "Verify your connection!");
		}
		return false;
	}
	
	public boolean sendMessage(final String sender, final String message, final List<String> receivers) {
		try {
			if (message != null && !message.trim().isEmpty()) {
				Message messageObj = new Message(message, sender);
				MessageOperation messageOperation = new MessageOperation(messageObj);
				messageOperation.setTargets(receivers);
				getConnection().sendOperation(messageOperation);
				return true;
			}
		} catch (ConnectionException e) {
			JOptionPane.showMessageDialog(null, "Verify your connection!");
		}
		return false;
	}
	
	public boolean isLastMessageChanged() {
		return lastMessageChanged;
	}
	
	public void setMessage(final Message message) {
		lastMessageChanged = true;
		this.lastMessage = message;
		notifyObservers();
	}
	
	public Message getMessage() {
		lastMessageChanged = false;
		return lastMessage;
	}
	
	//Unique observer but replace old one
	public boolean addObserver(Observer observer) {
		this.observer = observer;
		notifyObservers();
		return true;
	}
	
	public boolean removeObserver(Observer observer) {
		if (this.observer.equals(observer)) {
			this.observer = null;
			return true;
		}
		return false;
	}
	
	private void notifyObservers() {
		if (this.observer != null) {
			observer.update();
		}
	}

}
