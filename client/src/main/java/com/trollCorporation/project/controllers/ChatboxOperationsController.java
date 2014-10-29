package com.trollCorporation.project.controllers;

import java.util.List;

import com.trollCorporation.common.utils.Observable;

public interface ChatboxOperationsController extends Observable {
	
	boolean sendMessage(final String message);
	boolean isLastMessageChanged();
	String getMessage();
	boolean isActiveUsersChanged();
	List<String> getActiveUsers();
	
}
