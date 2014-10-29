package com.trollCorporation.common.model;

import java.io.Serializable;

public class ListUsersOperation extends Operation implements Serializable {
	
	private static final long serialVersionUID = 13073507508498909L;
	private ActiveUsers activeUsers = new ActiveUsers();
	
	public ListUsersOperation() {
		super(OperationType.CHATBOX_USERS_LISTING);
	}
	
	public ListUsersOperation(final ActiveUsers users) {
		super(OperationType.CHATBOX_USERS_LISTING);
		this.activeUsers = users;
	}
	
	public ActiveUsers getUsers() {
		return activeUsers;
	}
	
	public void setUsers(ActiveUsers users) {
		this.activeUsers = users;
	}

}
