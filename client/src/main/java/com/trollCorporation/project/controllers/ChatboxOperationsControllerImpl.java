package com.trollCorporation.project.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.trollCorporation.common.model.ActiveUsers;
import com.trollCorporation.common.model.ListUsersOperation;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.project.exceptions.ConnectionException;
import com.trollCorporation.services.ConnectionToServer;

public class ChatboxOperationsControllerImpl implements ChatboxOperationsController {

	private ConnectionToServer connection;
	private boolean interrupted;
	private Set<Observer> observers = new HashSet<Observer>();
	private boolean lastMessageChanged;
	private Message lastMessage;
	private boolean activeUsersChanged;
	private ActiveUsers activeUsers;
	
	public ChatboxOperationsControllerImpl() {
		this.interrupted = false;
		this.connection = new ConnectionToServer();
		new ChatboxControllerThread().start();
	}
	
	public ChatboxOperationsControllerImpl(final String address, final int port) {
		this.interrupted = false;
		this.connection = new ConnectionToServer(address, port);
		new ChatboxControllerThread().start();
	}
	
	public boolean sendMessage(final String message) {
		try {
			connection.sendMessage(message);
			return true;
		} catch (ConnectionException e) {
			return false;
		}
	}
	
	public boolean isLastMessageChanged() {
		return lastMessageChanged;
	}
	
	public String getMessage() {
		return lastMessage.getMessageValue();
	}
	
	public boolean isActiveUsersChanged() {
		return activeUsersChanged;
	}
	
	public List<String> getActiveUsers() {
		List<String> usersToStringList = new ArrayList<String>();
		for (User user : activeUsers.getUsers()) {
			usersToStringList.add(user.getName());
		}
		return usersToStringList;
	}
	
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}
	
	private void notifyObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}

	private class ChatboxControllerThread extends Thread {

		public void run() {
			//TEMPORAIRE
			String name = null;
			while(name == null || name.isEmpty()) {
				name = JOptionPane.showInputDialog("What's your name ?");
			}
			sendMessage(name);
			//FIN TEMPORAIRE
			while (!interrupted) {
				try {
					lastMessageChanged = false;
					activeUsersChanged = false;
					Operation operation = connection.getOperation();
					switch(operation.getOperationType()) {
					case CHATBOX_MAILING :
						if (operation instanceof MessageOperation) {
							lastMessage = ((MessageOperation) operation).getMessage();
							lastMessageChanged = true;
						}
						break;
					case CHATBOX_USERS_LISTING :
						if (operation instanceof ListUsersOperation) {
							activeUsers = ((ListUsersOperation) operation).getUsers();
							activeUsersChanged = true;
						}
						break;
					default :
						//unknow case
					}
					if (lastMessageChanged || activeUsersChanged) {
						notifyObservers();
					}
				} catch (ConnectionException e) {
					interrupted = true;
				}
			}
		}
	}
}
