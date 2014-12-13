package com.trollCorporation.common.model.operations;

import java.io.Serializable;

import com.trollCorporation.common.model.ActiveUsers;

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
