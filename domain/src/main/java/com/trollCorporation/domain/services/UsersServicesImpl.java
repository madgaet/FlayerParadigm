package com.trollCorporation.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.trollCorporation.common.exceptions.AlreadyExistsUserException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.domain.exceptions.DbConnectionException;
import com.trollCorporation.domain.users.FriendEntity;
import com.trollCorporation.domain.users.FriendsDao;
import com.trollCorporation.domain.users.UserEntity;
import com.trollCorporation.domain.users.UsersDao;

public class UsersServicesImpl implements UsersServices {
	
	private static final Logger LOG = Logger.getLogger(UsersServicesImpl.class.getSimpleName());
	
	@Autowired
	private UsersDao usersDao;
	@Autowired
	private FriendsDao friendsDao;
	
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

	public List<User> getFriendsListByUsername(String username) {
		List<User> friends = new ArrayList<User>();
		List<FriendEntity> friendsEntities = new ArrayList<FriendEntity>();
		try {
			friendsEntities = friendsDao.getFriends(username);
		} catch (DbConnectionException e) {}
		for(FriendEntity friend : friendsEntities) {
			if (friend.isValid()) {
				User user;
				if (friend.getUser().getUsername().equals(username)) {
					user = new User(friend.getUserFriend().getUsername());
				} else {
					user = new User(friend.getUser().getUsername());
				}
				friends.add(user);
			}
		}
		return friends;
	}
	
	public List<User> getFriendsRequests(String username) {
		List<User> friendsRequest = new ArrayList<User>();
		List<FriendEntity> friendsEntities = new ArrayList<FriendEntity>();
		try {
			friendsEntities = friendsDao.getFriendsRequests(username);
		} catch (DbConnectionException e) {}
		for(FriendEntity friend : friendsEntities) {
			if (!friend.isValid()) {
				User user;
				if (friend.getUser().getUsername().equals(username)) {
					user = new User(friend.getUserFriend().getUsername());
				} else {
					user = new User(friend.getUser().getUsername());
				}
				friendsRequest.add(user);
			}
		}
		return friendsRequest;
	}
	
	public boolean addFriend(final String username, final String friendName) {
		try {
			UserEntity user = usersDao.findUserByUsername(username);
			UserEntity userFriend = usersDao.findUserByUsername(friendName);
			if (user != null && userFriend != null) {
				FriendEntity friend = friendsDao.findByNames(username, friendName);
				if (friend == null) {
					friend = new FriendEntity();
					friend.setUser(user);
					friend.setUserFriend(userFriend);
					friendsDao.register(friend);
				} else {
					if (!friend.isValid()) {
						friend.setValid(true);
						friendsDao.update(friend);
					}
				}
				return true;
			}
		} catch (DbConnectionException d) {
			LOG.error("Problem while trying to add friend into DB", d);
		}
		return false;
	}
	
	public boolean removeFriend(final String username, final String friendName) {
		try {
			FriendEntity friend = friendsDao.findByNames(username, friendName);
			if (friend != null) {
				friendsDao.delete(friend);
			} 
			return true;
		} catch (DbConnectionException d) {
			LOG.error("Problem while trying to add friend into DB", d);
		}
		return false;
	}
}
