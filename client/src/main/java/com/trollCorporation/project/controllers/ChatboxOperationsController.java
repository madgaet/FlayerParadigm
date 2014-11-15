package com.trollCorporation.project.controllers;

import java.util.List;

import com.trollCorporation.common.model.ActiveUsers;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.utils.Observable;

public interface ChatboxOperationsController extends Observable {
	
	boolean sendMessage(final String message);
	boolean isLastMessageChanged();
	void setMessage(Message message);
	String getMessage();
	boolean isActiveUsersChanged();
	void setActiveUsers(ActiveUsers activeUsers);
	List<String> getActiveUsers();
	
}
