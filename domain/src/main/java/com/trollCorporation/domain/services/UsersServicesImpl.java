package com.trollCorporation.domain.services;

import com.trollCorporation.common.model.User;
import com.trollCorporation.domain.users.UserDao;
import com.trollCorporation.domain.users.UsersFinder;
import com.trollCorporation.domain.users.UsersFinderImpl;

public class UsersServicesImpl implements UsersServices {
	
//	@EJB
//	UserDao userDao;
	
	private UsersFinder usersFinder = new UsersFinderImpl();
	
	private static UsersServices singleton;
	
	private UsersServicesImpl() {
	}
	
	public synchronized static UsersServices getInstance() {
		if (singleton == null) {
			synchronized (UsersServicesImpl.class) {
				if (singleton == null) {
					singleton = new UsersServicesImpl();
				}
			}
		}
		return singleton;
	}

	public User getUserByName(String username) {
		UserDao userDao = usersFinder.findUserByUsername(username);
		User user = new User(username);
		if (userDao != null) {
			user.setEncryptedPassword(userDao.getPassword());
			user.setEmail(userDao.getEmail());
		}
		return user;
	}
}
