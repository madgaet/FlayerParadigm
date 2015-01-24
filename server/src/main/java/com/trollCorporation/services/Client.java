package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import com.trollCorporation.common.exceptions.AlreadyExistsUserException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.common.exceptions.UnknownTargetException;
import com.trollCorporation.common.model.ListUsers;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.enums.ErrorType;
import com.trollCorporation.common.model.operations.ConnectionOperation;
import com.trollCorporation.common.model.operations.FriendsListUsersOperation;
import com.trollCorporation.common.model.operations.FriendsRequestOperation;
import com.trollCorporation.common.model.operations.MessageOperation;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.model.operations.RegisterOperation;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.domain.ejb.configurations.HibernateContext;
import com.trollCorporation.domain.services.UsersServices;
import com.trollCorporation.security.OperationValidator;

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
						if (OperationValidator.isValidConnectionOperation(operation)) {
							if (connect(operation)) {
								server.sendFriendsListToAllClients();
								server.sendActiveFriendsListToAllClients();
								server.sendFriendsRequests(this.getName());
							}
						}
						break;
					case REGISTRATION:
						if (OperationValidator.isValidRegisterOperation(operation)) {
							register(operation);
						}
						break;
					case CHATBOX_MAILING :
						if (OperationValidator.isValidMessageOperation(operation)) {
							mail(operation);
						}
						break;
					case USERS_LISTING :
						if (OperationValidator.isValidListUserOperation(operation)) {
							listUsers(operation);
						}
						break;
					case FRIENDS_REQUEST:
						if (OperationValidator.isValidFriendRequestOperation(operation)) {
							if (executeFriendRequest(operation)) {
								server.sendFriendsListToClient(this);
							}
						}
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
	
	private boolean connect(Operation operation) throws IOException {
		if (operation instanceof ConnectionOperation) {
			ConnectionOperation connectionOperation = (ConnectionOperation) operation;
			try {
				User user = userServices.getUserByName(connectionOperation.getUserName());
				connectionOperation.connect(user.getPassword());
			} catch (ConnectionException c) {
				connectionOperation.setErrorType(ErrorType.DB_ERROR);
			}
			if (connectionOperation.isConnected()) {
				if (server.isUserAlreadyConnected(connectionOperation.getUserName())) {
					connectionOperation.setErrorType(ErrorType.USER_ALREADY_CONNECTED);
					receiveOperation(connectionOperation);
					return false;
				} else {
					receiveOperation(connectionOperation);
					this.name = connectionOperation.getUserName();
					String ipAddress = socket.getRemoteSocketAddress().toString();
					LOGGER.info("Client " + name + " is connected with ip " + ipAddress + " !");
					return true;
				}
			} else {
				receiveOperation(connectionOperation);
				close();
				LOGGER.info("Failed connection attempt!");
			}
		}
		return false;
	}
	
	private void register(Operation operation) throws IOException {
		if (operation instanceof RegisterOperation) {
			RegisterOperation registerOperation = (RegisterOperation) operation;
			User user = registerOperation.getUser();
			registerOperation.setIsRegistered(false);
			try {
				userServices.register(user);
				registerOperation.setIsRegistered(true);
				LOGGER.info("Client " + user.getName() + " is registered!");
			} catch (AlreadyExistsUserException e) {
				registerOperation.setErrorType(ErrorType.USER_ALREADY_EXISTS);
				LOGGER.info("Registration Failed!", e);
			} catch (RegistrationException e) {
				registerOperation.setErrorType(ErrorType.DB_ERROR);
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
			String target = null;
			if (messageOperation.getMessage().getSender().equals(name)) {
				String message = messageOperation.getMessage().getMessageValue();
				List<String> targets = messageOperation.getTargets();
				for (int i = 0; i < targets.size(); i++) {
					target = targets.get(i);
					try {
						server.sendMessageToTarget(message, target, this);
					} catch (UnknownTargetException e) {
						receiveMessage("[INFO] : The user " + target + " does not exist or is not connected!", target);
					}
				}
			}
		}
	}
	
	private void listUsers(Operation operation) throws IOException {
		if (operation instanceof FriendsListUsersOperation) {
			if (((FriendsListUsersOperation)operation).isActiveFriendsList()) {
				server.sendActiveFriendsToClient(this);
			} else {
				server.sendFriendsListToClient(this);
			}
		}
	}
	
	private boolean executeFriendRequest(Operation operation) throws IOException {
		if (operation instanceof FriendsRequestOperation) {
			FriendsRequestOperation friendRequest = (FriendsRequestOperation) operation;
			if (friendRequest.isAddRequest()) {
				if (userServices.addFriend(this.getName(), friendRequest.getUsername())) {
					server.sendFriendsRequests(friendRequest.getUsername());
					return true;
				}
			} else {
				return userServices.removeFriend(this.getName(), friendRequest.getUsername());
			}
		}
		return false;
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
	
	public void receiveMessage(final String message, final String sender) throws IOException {
		Message messageObj = new Message(message, sender);
		MessageOperation messageOperation = new MessageOperation(messageObj);
		receiveOperation(messageOperation);
	}
	
	public void receiveFriendsList(final List<User> users, boolean active) throws IOException {
		ListUsers activeUsers = new ListUsers(users);
		FriendsListUsersOperation listUsersOperation = new FriendsListUsersOperation();
		listUsersOperation.setUsers(activeUsers);
		listUsersOperation.setActiveFriendsList(active);
		receiveOperation(listUsersOperation);
	}
	
	public void receiveFriendRequest(final String name) throws IOException {
		FriendsRequestOperation friendRequest = new FriendsRequestOperation(name, true);
		receiveOperation(friendRequest);
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
