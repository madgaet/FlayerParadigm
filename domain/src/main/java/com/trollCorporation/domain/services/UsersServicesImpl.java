package com.trollCorporation.domain.services;

import javax.ejb.Stateless;

import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.domain.ejb.configurations.EjbContext;
import com.trollCorporation.domain.users.UserEntity;
import com.trollCorporation.domain.users.UsersDao;

@Stateless
public class UsersServicesImpl implements UsersServices {
	
	private UsersDao usersDao 
		= (UsersDao) EjbContext.getMappedImpl(UsersDao.class.getName());
	
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
		UserEntity userDao = usersDao.findUserByUsername(username);
		User user = new User(username);
		if (userDao != null) {
			user.setEncryptedPassword(userDao.getPassword());
			user.setEmail(userDao.getEmail());
		}
		return user;
	}

	public void register(User user) throws RegistrationException {
		UserEntity userDao = new UserEntity();
		userDao.setUsername(user.getName());
		userDao.setPassword(user.getPassword());
		userDao.setEmail(user.getEmail());
		usersDao.register(userDao);
	}
}
