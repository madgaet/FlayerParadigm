package com.trollCorporation.project.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.ListUsers;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.operations.FriendsListUsersOperation;
import com.trollCorporation.common.model.operations.FriendsRequestOperation;
import com.trollCorporation.common.model.operations.ListUsersOperation;
import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.services.ConnectionToServer;

public class FriendsControllerImpl implements FriendsController {
	
	private static FriendsController singleton;
	
	private Observer observer;
	private boolean activeUsersChanged;
	private ListUsers activeFriends;
	private ListUsers friends;
	
	private ConnectionToServer getConnection() throws ConnectionException {
		return ConnectionToServer.getInstance();
	}

	private FriendsControllerImpl() {
		requestFriendsList();
	}
	
	private void requestFriendsList() {
		try {
			ListUsersOperation listUsersOperation = new FriendsListUsersOperation();
			getConnection().sendOperation(listUsersOperation);
		} catch (ConnectionException e) {
			//should not
		}
	}
	
	public synchronized static FriendsController getInstance() 
		throws ConnectionException {
		if (singleton == null) {
			synchronized (ConnectionToServer.class) {
				if (singleton == null) {
					singleton = new FriendsControllerImpl();
				}
			}
		}
		return singleton;
	}
	
	public void setFriendsList(final ListUsers friends) {
		this.friends = new ListUsers();
		this.friends.setUsers(friends.getUsers());
	}
	
	public List<String> getFriendsList() {
		List<String> usersToStringList = new ArrayList<String>();
		if (friends != null) {
			for (User user : friends.getUsers()) {
				usersToStringList.add(user.getName());
			}
		}
		return usersToStringList;
	}
	
	
	public boolean isActiveUsersChanged() {
		return activeUsersChanged;
	}
	
	public void setActiveFriends(final ListUsers activeUsers) {
		activeUsersChanged = true;
		this.activeFriends = activeUsers;
		notifyObservers();
	}
	
	public List<String> getActiveFriends() {
		activeUsersChanged = false;
		List<String> usersToStringList = new ArrayList<String>();
		if (activeFriends != null) {
			for (User user : activeFriends.getUsers()) {
				usersToStringList.add(user.getName());
			}
		}
		return usersToStringList;
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

	public void sendAddFriendsRequest (String username) throws ConnectionException {
		FriendsRequestOperation friendRequest = new FriendsRequestOperation(username, true);
		getConnection().sendOperation(friendRequest);
	}
	
	public void sendRemoveFriendsRequest (String username) throws ConnectionException {
		FriendsRequestOperation friendRequest = new FriendsRequestOperation(username, false);
		getConnection().sendOperation(friendRequest);
	}
	
	public void notifyFriendRequest(final FriendsRequestOperation friendRequest) {
		if (friendRequest.isAddRequest()) {
			String username = friendRequest.getUsername();
			int response = JOptionPane.showConfirmDialog(null, username + " wants to add you as friend. Do you agree?",
					"Friend Request from " + username, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			try {
				if (response == JOptionPane.YES_OPTION) {
					sendAddFriendsRequest(username);
				}
				if (response == JOptionPane.NO_OPTION) {
					sendRemoveFriendsRequest(username);
				}
			} catch (ConnectionException c) {}
		}
	}

	
}
