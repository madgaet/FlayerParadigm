package com.trollCorporation.services;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.operations.ListUsersOperation;
import com.trollCorporation.common.model.operations.MessageOperation;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;

public class OperationProcessor extends Thread {

	private static Logger LOG = Logger.getLogger(OperationProcessor.class.getName());
	
	private static OperationProcessor singleton;
	private boolean interrupted;
	
	//injections
	private ConnectionToServer getConnection() throws ConnectionException {
		return ConnectionToServer.getInstance();
	}
	
	private ChatboxOperationsController getChatboxController() throws ConnectionException {
		return ChatboxOperationsControllerImpl.getInstance();
	}
	
	public synchronized static OperationProcessor getInstance() throws ConnectionException {
		if (singleton == null) {
			synchronized (ConnectionToServer.class) {
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
				case CHATBOX_USERS_LISTING:
					if (operation instanceof ListUsersOperation) {
						getChatboxController().setActiveUsers(((ListUsersOperation) operation).getUsers());
					}
					break;
				default:
					// unknow case
				}
			} catch (ConnectionException e) {
				interrupted = true;
			}
		}
	}
}
