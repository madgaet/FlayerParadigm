package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import com.trollCorporation.common.model.ActiveUsers;
import com.trollCorporation.common.model.ConnectionOperation;
import com.trollCorporation.common.model.ListUsersOperation;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.exceptions.UnknownTargetException;

public class Client implements Runnable {
	
	private final Logger LOGGER = Logger.getLogger(Client.class.getName());
	private static int count = 1;
	private Socket socket;
	private Server server;
	private int clientNumber;
	private String name;
	
	public Client(final Socket socket,final Server server) {
		this.socket = socket;
		this.server = server;
		this.clientNumber = count++;
		this.name = "" + clientNumber;
	}
	
	public void run() {
		LOGGER.info("Processing client " + clientNumber + " ...");
		try {
			while (!socket.isClosed()) {
				Operation operation = MessageUtils.getOperation(socket);
				if (operation != null) {
					switch (operation.getOperationType()) {
					case CONNECTION :
						if (operation instanceof ConnectionOperation) {
							ConnectionOperation connectionOperation = (ConnectionOperation) operation;
							if (receiveConnectionAck(connectionOperation)) {
								LOGGER.info("Client " + name + " is connected!");
							} else {
								LOGGER.info("Failed connection attempt!");
							}
						}
						break;
					case CHATBOX_MAILING :
						if (operation instanceof MessageOperation) {
							MessageOperation messageOperation = (MessageOperation) operation;
							try {
								String message = messageOperation.getMessage().getMessageValue();
								//String target = messageOperation.getTarget();
								String target = null;
								server.sendMessageToTarget(message, target, this);
							} catch (UnknownTargetException e) {
								receiveMessage("[INFO] : This user does not exist or is not connected!");
							}
						}
						break;
					case CHATBOX_USERS_LISTING :
						server.sendUsersListToAllClients();
						break;
					}
				}
			}
		} catch (IOException e) {
			LOGGER.info("Client " + name + " disconnected for unknow reason!");
			System.out.println("Client " + name + " disconnected for unknow reason!");
			server.disconnect(this);
		}
		LOGGER.info("Ending client " + clientNumber + " process!");
	}
	
	public void close() {
		if (socket != null) {
			LOGGER.info("Closing socket of client " + clientNumber);
			try {
				socket.close();
			} catch (IOException ignoredException) {
			}
		}
	}
	
	public boolean receiveConnectionAck(ConnectionOperation connectionOperation) throws IOException {
		connectionOperation.connect(connectionOperation.getEncryptedPassword());
		MessageUtils.sendOperation(socket, connectionOperation);
		if (connectionOperation.isConnected()) {
			this.name = connectionOperation.getUserName();
			return true;
		} else {
			return false;
		}
	}
	
	public void receiveMessage(final String message) throws IOException {
		Message messageObj = new Message(message);
		MessageOperation messageOperation = new MessageOperation(messageObj);
		MessageUtils.sendOperation(socket, messageOperation);
	}
	
	public void receiveUserList(final List<User> users) throws IOException {
		ActiveUsers activeUsers = new ActiveUsers(users);
		ListUsersOperation listUsersOperation = new ListUsersOperation(activeUsers);
		MessageUtils.sendOperation(socket, listUsersOperation);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(Client client) {
		if (client.getName().equals(this.getName())) {
			if (client.clientNumber == this.clientNumber) {
				return true;
			}
		}
		return false;
	}

}
