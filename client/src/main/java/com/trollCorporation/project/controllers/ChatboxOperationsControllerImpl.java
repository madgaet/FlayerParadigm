package com.trollCorporation.project.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.ActiveUsers;
import com.trollCorporation.common.model.ListUsersOperation;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.services.ConnectionToServer;

public class ChatboxOperationsControllerImpl implements ChatboxOperationsController {

	private static ChatboxOperationsController singleton;
	private Observer observer;
	private boolean lastMessageChanged;
	private Message lastMessage;
	private boolean activeUsersChanged;
	private ActiveUsers activeUsers;
	
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
	
	public void getFriendsList() {
		try {
			ListUsersOperation listUsersOperation = new ListUsersOperation();
			getConnection().sendOperation(listUsersOperation);
		} catch (ConnectionException e) {
			//should not
		}
	}
	
	public boolean sendMessage(final String message) {
		try {
			if (message != null && !message.trim().isEmpty()) {
				Message messageObj = new Message(message);
				MessageOperation messageOperation = new MessageOperation(messageObj);
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
	
	public String getMessage() {
		lastMessageChanged = false;
		return lastMessage.getMessageValue();
	}
	
	public boolean isActiveUsersChanged() {
		return activeUsersChanged;
	}
	
	public void setActiveUsers(final ActiveUsers activeUsers) {
		activeUsersChanged = true;
		this.activeUsers = activeUsers;
		notifyObservers();
	}
	
	public List<String> getActiveUsers() {
		activeUsersChanged = false;
		List<String> usersToStringList = new ArrayList<String>();
		for (User user : activeUsers.getUsers()) {
			usersToStringList.add(user.getName());
		}
		return usersToStringList;
	}
	
	//Unique observer 
	public boolean addObserver(Observer observer) {
		if (this.observer == null) {
			this.observer = observer;
			notifyObservers();
			return true;
		} else {
			//observer not unique
			return false;
		}
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
