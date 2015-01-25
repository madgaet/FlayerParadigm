package com.trollCorporation.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.trollCorporation.common.exceptions.UnknownTargetException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.utils.MessageFormatter;
import com.trollCorporation.domain.ejb.configurations.HibernateContext;
import com.trollCorporation.domain.services.UsersServices;

public class Server implements Runnable {
	
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private ServerSocket socketServer;
	private ClientHandler clientHandler;
	private volatile boolean interrupted;
	private UsersServices userServices;
	
	public Server(final int port) throws IOException {
		socketServer = new ServerSocket(port);
		clientHandler = new ClientHandler();
		interrupted = false;
		this.userServices =	(UsersServices) HibernateContext.getMappedImpl("UsersServices");
	}
	
	public void run() {
		LOGGER.info("Server starting !\n");
		System.out.println("Server starting !");
		try {
			while(!interrupted) {
				LOGGER.info("Waiting for connections...");
				System.out.println("Waiting for connections...");
				Socket socket = socketServer.accept();
				startingClient(socket);
			}
		} catch (IOException e) {
			handleException(e);
		}
	}
	
	private synchronized void startingClient(Socket socket) {
		if (socket == null) {
			return;
		} else {
			LOGGER.info("Creating client");
			System.out.println("Creating Client");
			Client client = new Client(socket, this);
			ClientThread clientConnection = new ClientThread(client);
			clientHandler.add(clientConnection);
			clientConnection.start();
		}
	}
	
	public boolean isUserAlreadyConnected(String name) {
		return clientHandler.existsClientName(name) != null ? true : false;
	}
	
	public synchronized void disconnect(Client client) {
		clientHandler.remove(client);
		sendFriendsListToAllClients();
	}
	
	private void handleException(final Exception e) {
		if (!(e instanceof SocketException)) {
			LOGGER.error("Error with socket", e);
			System.out.println("Error with socket");
		} else {
			LOGGER.error("Unknown error.", e);
		}
		e.printStackTrace();
	}
	
	public void stop() {
		this.interrupted = true;
		closeServer();
	}
	
	private void closeServer() {
		if (socketServer != null) {
			try {
				socketServer.close();
			} catch (IOException ignoredException) {
			}
		}
	}
	
	public void sendMessageToTarget(final String message, final String target, final Client sender) 
			throws UnknownTargetException, IOException {
		if (target != null) {
			String formatedMsg = MessageFormatter.formatSentMessage(message, sender.getName());
			ClientThread targetClient;
			if ((targetClient = clientHandler.existsClientName(target)) != null) {
				targetClient.getClient().receiveMessage(formatedMsg, sender.getName());
			}
		} 
	}
	
	public void sendMessageToTargets(final String message, final List<String> targets, final Client sender) 
			throws UnknownTargetException, IOException {
		if (targets != null && !targets.isEmpty()) {
			String formatedMsg = MessageFormatter.formatSentMessage(message, sender.getName());
			for (String target : targets) {
				ClientThread targetClient;
				if ((targetClient = clientHandler.existsClientName(target)) != null) {
					targetClient.getClient().receiveMessage(formatedMsg, sender.getName());
				}
			}
		} 
	}
	
	public void sendFriendsListToAllClients() {
		try {
			for (ClientThread clientThread : this.clientHandler.getClientsList()) {
				sendFriendsListToClient(clientThread.getClient());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendActiveFriendsListToAllClients() {
		try {
			for (ClientThread clientThread : this.clientHandler.getClientsList()) {
				sendActiveFriendsToClient(clientThread.getClient());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void sendFriendsListToClient(final Client client) throws IOException {
		List<User> friends = userServices.getFriendsListByUsername(client.getName());
		client.receiveFriendsList(friends, false);
	}
	
	void sendActiveFriendsToClient(final Client client) throws IOException {
		List<User> friends = new ArrayList<User>();
		for (User friend : userServices.getFriendsListByUsername(client.getName())) {
			if (clientHandler.existsClientName(friend.getName()) != null) {
				friends.add(friend);
			}
		}
		client.receiveFriendsList(friends, true);
	}
	
	public void sendFriendsRequests(final String clientName) throws IOException {
		ClientThread thread = clientHandler.existsClientName(clientName);
		Client client =  (thread == null) ? null : thread.getClient();
		if (client != null) {
			List<User> friendsRequest = userServices.getFriendsRequests(client.getName());
			if (!friendsRequest.isEmpty()) {
				for (User user : friendsRequest) {
					client.receiveFriendRequest(user.getName());
				}
			} else {
				sendActiveFriendsToClient(client);
			}
		}
	}
}
