package com.trollCorporation.domain.users;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface UsersFinder {
	
	List<UserDao> getUsers();
	
	UserDao findUserById(int id);
	UserDao findUserByUsername(String username);
}
