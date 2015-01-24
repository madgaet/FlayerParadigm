package com.trollCorporation.services;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.operations.FriendsListUsersOperation;
import com.trollCorporation.common.model.operations.FriendsRequestOperation;
import com.trollCorporation.common.model.operations.MessageOperation;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;
import com.trollCorporation.project.controllers.FriendsController;
import com.trollCorporation.project.controllers.FriendsControllerImpl;

public class OperationProcessor extends Thread {

	private static Logger LOG = Logger.getLogger(OperationProcessor.class.getName());
	
	private static OperationProcessor singleton;
	private boolean interrupted = false;
	
	//injections
	private ConnectionToServer getConnection() throws ConnectionException {
		return ConnectionToServer.getInstance();
	}
	
	private ChatboxOperationsController getChatboxController() throws ConnectionException {
		return ChatboxOperationsControllerImpl.getInstance();
	}
	
	private FriendsController getFriendsController() throws ConnectionException {
		return FriendsControllerImpl.getInstance();
	}
	
	public synchronized static OperationProcessor getInstance() throws ConnectionException {
		if (singleton == null) {
			synchronized (OperationProcessor.class) {
				if (singleton == null) {
					singleton = new OperationProcessor();
				}
			}
		}
		return singleton;
	}
	
	private OperationProcessor() {
	}

	public Operation getOperation() throws ConnectionException {
		try {
			return MessageUtils.getOperation(getConnection().getSocket());
		} catch (IOException e) {
			LOG.error("retrieving operation isn't working");
			throw new ConnectionException(e.getMessage());
		}
	}
	
	public void run() {
		while (!interrupted) {
			try {
				Operation operation = getOperation();
				switch (operation.getOperationType()) {
				case CHATBOX_MAILING:
					if (operation instanceof MessageOperation) {
						getChatboxController().setMessage(((MessageOperation) operation).getMessage());
					}
					break;
				case USERS_LISTING:
					if (operation instanceof FriendsListUsersOperation) {
						FriendsListUsersOperation listUsers = (FriendsListUsersOperation) operation;
						if (listUsers.isActiveFriendsList()) {
							getFriendsController().setActiveFriends(listUsers.getUsers());
						} else {
							getFriendsController().setFriendsList(listUsers.getUsers());
						}
					}
					break;
				case FRIENDS_REQUEST:
					if (operation instanceof FriendsRequestOperation) {
						FriendsRequestOperation friendRequest = (FriendsRequestOperation) operation;
						if (friendRequest.isAddRequest()) {
							getFriendsController().notifyFriendRequest(friendRequest);
						}
					}
				default:
					// unknow case
				}
			} catch (ConnectionException e) {
				interrupted = true;
			}
		}
		singleton = null;
	}
	
}
