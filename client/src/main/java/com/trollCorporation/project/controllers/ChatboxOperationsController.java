package com.trollCorporation.project.controllers;

import java.util.List;

import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.utils.Observable;

public interface ChatboxOperationsController extends Observable {
	
	boolean sendMessage(String sender, String message);
	boolean sendMessage(String sender, String message, List<String> receivers);
	boolean isLastMessageChanged();
	void setMessage(Message message);
	Message getMessage();
	
}
