package com.trollCorporation.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;

@Service
public interface UsersServices {
	
	boolean addFriend(String username, String friendName);
	boolean removeFriend(String username, String friendName);
	User getUserByName(String username) throws ConnectionException;
	List<User> getFriendsListByUsername(String username);
	List<User> getFriendsRequests(String username);
	void register(User user) throws RegistrationException;
}
