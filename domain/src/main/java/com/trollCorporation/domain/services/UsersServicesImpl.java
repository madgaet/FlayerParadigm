package com.trollCorporation.domain.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.trollCorporation.common.exceptions.AlreadyExistsUserException;
import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.domain.users.UserEntity;
import com.trollCorporation.domain.users.UsersDao;

public class UsersServicesImpl implements UsersServices {
	
	@Autowired
	private UsersDao usersDao;
	
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	public User getUserByName(String username) {
		UserEntity userDao = usersDao.findUserByUsername(username);
		User user = new User(username);
		if (userDao != null) {
			user.setEncryptedPassword(userDao.getPassword());
			user.setEmail(userDao.getEmail());
		}
		return user;
	}

	public void register(User user) throws AlreadyExistsUserException, RegistrationException {
		if (user != null && user.getName() != null) {
			if (usersDao.findUserByUsername(user.getName()) != null) {
				throw new AlreadyExistsUserException();
			} else {
				UserEntity userDao = new UserEntity();
				userDao.setUsername(user.getName());
				userDao.setPassword(user.getPassword());
				userDao.setEmail(user.getEmail());
				usersDao.register(userDao);
			}
		}
	}
}
