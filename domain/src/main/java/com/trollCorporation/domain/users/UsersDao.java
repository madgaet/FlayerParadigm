package com.trollCorporation.domain.users;

import java.util.List;

import com.trollCorporation.common.exceptions.RegistrationException;

public interface UsersDao {
	
	//finder methods
	List<UserEntity> getUsers();
	
	UserEntity findUserById(int id);
	UserEntity findUserByUsername(String username);
	
	//persister methods
	void register(UserEntity user) throws RegistrationException;
}
