package com.trollCorporation.domain.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.trollCorporation.common.exceptions.AlreadyExistsUserException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.domain.exceptions.DbConnectionException;
import com.trollCorporation.domain.users.UserEntity;
import com.trollCorporation.domain.users.UsersDao;

public class UsersServicesImpl implements UsersServices {
	
	@Autowired
	private UsersDao usersDao;
	
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	public final User getUserByName(final String username) throws ConnectionException {
		return getUserByNameWithRetry(username, 0);
	}
		
	private final User getUserByNameWithRetry(final String username, final int retry) 
			throws ConnectionException {
		User user = new User(username);
		try {
			UserEntity userDao = usersDao.findUserByUsername(username);
			if (userDao != null) {
				user.setEncryptedPassword(userDao.getPassword());
				user.setEmail(userDao.getEmail());
			}
		} catch (Exception e) {
			if (e instanceof DbConnectionException) {
				if (retry < 5) {
					try { Thread.sleep((long)(Math.random()*retry*1000)+1000L); }
					catch (InterruptedException e1) {/*do nothing*/}
					return getUserByNameWithRetry(username, retry+1);	
				}
			}
			throw new ConnectionException("Problem while trying to fetch user info in DB", e);
		}
		return user;
	}

	public final void register(final User user) throws AlreadyExistsUserException, RegistrationException {
		if (user != null && user.getName() != null) {
			UserEntity userEntity = null;
			try {
				userEntity = usersDao.findUserByUsername(user.getName());
			} catch (Exception e) {
				throw new RegistrationException("Problem while trying to know if user exists!", e);
			}
			if (userEntity != null) {
				throw new AlreadyExistsUserException();
			}
			UserEntity userDao = new UserEntity();
			userDao.setUsername(user.getName());
			userDao.setPassword(user.getPassword());
			userDao.setEmail(user.getEmail());
			usersDao.register(userDao);
		}
	}
}
