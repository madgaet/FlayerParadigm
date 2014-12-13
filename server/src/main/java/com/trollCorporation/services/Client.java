package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import com.trollCorporation.common.exceptions.UnknownTargetException;
import com.trollCorporation.common.model.ActiveUsers;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.operations.ConnectionOperation;
import com.trollCorporation.common.model.operations.ListUsersOperation;
import com.trollCorporation.common.model.operations.MessageOperation;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.model.operations.RegisterOperation;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.domain.ejb.configurations.HibernateContext;
import com.trollCorporation.domain.services.UsersServices;

public class Client implements Runnable {
	
	private final Logger LOGGER = Logger.getLogger(Client.class.getName());
	private static int count = 1;
	private Socket socket;
	private Server server;
	private int clientNumber;
	private String name;
	private UsersServices userServices;
	
	public Client(final Socket socket,final Server server) {
		this.socket = socket;
		this.server = server;
		this.clientNumber = count++;
		this.name = "" + clientNumber;
		this.userServices =	(UsersServices) HibernateContext.getMappedImpl("UsersServices");
	}
	
	public void run() {
		LOGGER.info("Processing client " + clientNumber + " ...");
		try {
			while (!socket.isClosed()) {
				Operation operation = MessageUtils.getOperation(socket);
				if (operation != null) {
					switch (operation.getOperationType()) {
					case CONNECTION :
						connect(operation);
						break;
					case REGISTRATION:
						register(operation);
						break;
					case CHATBOX_MAILING :
						mail(operation);
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
		} finally {
			server.disconnect(this);
		}
		LOGGER.info("Ending client " + clientNumber + " process!");
	}
	
	private void connect(Operation operation) throws IOException {
		if (operation instanceof ConnectionOperation) {
			ConnectionOperation connectionOperation = (ConnectionOperation) operation;
			User user = userServices.getUserByName(connectionOperation.getUserName());
			connectionOperation.connect(user.getPassword());
			receiveOperation(connectionOperation);
			if (connectionOperation.isConnected()) {
				this.name = connectionOperation.getUserName();
				LOGGER.info("Client " + name + " is connected!");
			} else {
				close();
				LOGGER.info("Failed connection attempt!");
			}
		}
	}
	
	private void register(Operation operation) throws IOException {
		if (operation instanceof RegisterOperation) {
			RegisterOperation registerOperation = (RegisterOperation) operation;
			User user = registerOperation.getUser();
			try {
				userServices.register(user);
				registerOperation.setIsRegistered(true);
				LOGGER.info("Client " + user.getName() + " is registered!");
			} catch (Exception e) {
				registerOperation.setIsRegistered(false);
				LOGGER.info("Registration Failed!", e);
			} finally {
				receiveOperation(registerOperation);
				close();
			}
		}
	}
	
	private void mail(Operation operation) throws IOException {
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
	
	public void receiveOperation(Operation operation) throws IOException {
		MessageUtils.sendOperation(socket, operation);
	}
	
	public void receiveMessage(final String message) throws IOException {
		Message messageObj = new Message(message);
		MessageOperation messageOperation = new MessageOperation(messageObj);
		receiveOperation(messageOperation);
	}
	
	public void receiveUserList(final List<User> users) throws IOException {
		ActiveUsers activeUsers = new ActiveUsers(users);
		ListUsersOperation listUsersOperation = new ListUsersOperation(activeUsers);
		receiveOperation(listUsersOperation);
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Client other = (Client) obj;
		if (clientNumber != other.clientNumber) {
			return false;
		}
		return true;
	}

}
