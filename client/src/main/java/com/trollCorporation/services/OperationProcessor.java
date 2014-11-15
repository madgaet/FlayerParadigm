package com.trollCorporation.services;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.trollCorporation.common.model.ListUsersOperation;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;
import com.trollCorporation.project.exceptions.ConnectionException;

public class OperationProcessor extends Thread {

	private static Logger LOG = Logger.getLogger(OperationProcessor.class.getName());
	
	private static OperationProcessor singleton;
	private boolean interrupted;
	private ConnectionToServer connection;
	
	//injections
	private ChatboxOperationsController chatboxController = ChatboxOperationsControllerImpl.getInstance();
	
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
	
	private OperationProcessor() throws ConnectionException {
		this.connection = ConnectionToServer.getInstance();
	}

	public Operation getOperation() throws ConnectionException {
		try {
			return MessageUtils.getOperation(connection.getSocket());
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
						chatboxController.setMessage(((MessageOperation) operation).getMessage());
					}
					break;
				case CHATBOX_USERS_LISTING:
					if (operation instanceof ListUsersOperation) {
						chatboxController.setActiveUsers(((ListUsersOperation) operation).getUsers());
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
