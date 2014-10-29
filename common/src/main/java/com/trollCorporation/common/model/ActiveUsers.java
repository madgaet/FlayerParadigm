package com.trollCorporation.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActiveUsers implements Serializable {
	
	private static final long serialVersionUID = 8649208931494270619L;
	private List<User> users = new ArrayList<User>();
	
	public ActiveUsers() {}
	public ActiveUsers(List<User> users) {
		if (users != null) {
			this.users = users;
		}
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(final List<User> users) {
		this.users = users;
	}
}
