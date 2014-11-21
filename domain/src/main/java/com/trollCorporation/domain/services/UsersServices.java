package com.trollCorporation.domain.services;

import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.model.User;

public interface UsersServices {
	
	User getUserByName(String username);
	
	void register(User user) throws RegistrationException;
}
