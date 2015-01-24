package com.trollCorporation.project.controllers;

import java.util.List;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.ListUsers;
import com.trollCorporation.common.model.operations.FriendsRequestOperation;
import com.trollCorporation.common.utils.Observable;

public interface FriendsController extends Observable {
	
	void sendAddFriendsRequest(String username) throws ConnectionException;
	void sendRemoveFriendsRequest(String username) throws ConnectionException;
	boolean isActiveUsersChanged();
	void setActiveFriends(ListUsers activeUsers);
	List<String> getActiveFriends();
	void setFriendsList(ListUsers activeUsers);
	List<String> getFriendsList();
	//inform the user has received a friend request
	void notifyFriendRequest(FriendsRequestOperation operation);
}
