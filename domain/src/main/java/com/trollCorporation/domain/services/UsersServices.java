package com.trollCorporation.domain.services;

import org.springframework.stereotype.Service;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;

@Service
public interface UsersServices {
	
	User getUserByName(String username) throws ConnectionException;
	
	void register(User user) throws RegistrationException;
}
