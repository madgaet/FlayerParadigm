package com.trollCorporation.common.model.operations;

public class FriendsListUsersOperation extends ListUsersOperation {

	private static final long serialVersionUID = -4555488942599110772L;

	private boolean activeFriendsList;
	
	public FriendsListUsersOperation () {
		super();
	}
	
	public boolean isActiveFriendsList() {
		return activeFriendsList;
	}
	
	public void setActiveFriendsList(boolean activeFriendsList) {
		this.activeFriendsList = activeFriendsList;
	}
}
