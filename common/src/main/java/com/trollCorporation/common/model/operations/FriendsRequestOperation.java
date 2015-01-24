package com.trollCorporation.common.model.operations;

import com.trollCorporation.common.model.enums.OperationType;

public class FriendsRequestOperation extends Operation {

	private static final long serialVersionUID = -6995759886943793670L;
	private String username;
	/** To tell if it is a adding request or removing request */
	private boolean addRequest;

	public FriendsRequestOperation(String username, boolean addRequest) {
		super(OperationType.FRIENDS_REQUEST);
		this.username = username;
		this.addRequest = addRequest;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAddRequest() {
		return addRequest;
	}	

}
