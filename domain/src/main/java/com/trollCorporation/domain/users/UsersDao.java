package com.trollCorporation.domain.users;

import java.util.List;

import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.domain.exceptions.DbConnectionException;

public interface UsersDao {
	
	//finder methods
	List<UserEntity> getUsers();
	
	UserEntity findUserById(int id);
	UserEntity findUserByUsername(String username) throws DbConnectionException;
	
	//persister methods
	void register(UserEntity user) throws RegistrationException;
}
