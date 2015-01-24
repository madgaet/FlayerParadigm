package com.trollCorporation.common.model.operations;

import java.io.Serializable;

import com.trollCorporation.common.model.ListUsers;
import com.trollCorporation.common.model.enums.OperationType;

public class ListUsersOperation extends Operation implements Serializable {
	
	private static final long serialVersionUID = 13073507508498909L;
	private ListUsers listUsers = new ListUsers();
	
	public ListUsersOperation() {
		super(OperationType.USERS_LISTING);
	}
	
	public ListUsersOperation(final ListUsers users) {
		super(OperationType.USERS_LISTING);
		this.listUsers = users;
	}
	
	public ListUsers getUsers() {
		return listUsers;
	}
	
	public void setUsers(ListUsers users) {
		this.listUsers = users;
	}

}
