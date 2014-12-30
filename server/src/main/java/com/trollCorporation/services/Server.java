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

public class Server implements Runnable {
	
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private ServerSocket socketServer;
	private ClientHandler clientHandler;
	private volatile boolean interrupted;
	
	public Server(final int port) throws IOException {
		socketServer = new ServerSocket(port);
		clientHandler = new ClientHandler();
		interrupted = false;
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
		sendUsersListToAllClients();
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
			if (target.equals(sender.getName())) {
				sender.receiveMessage(formatedMsg);
			} else {
				ClientThread targetClient;
				if ((targetClient = clientHandler.existsClientName(target)) != null) {
					targetClient.getClient().receiveMessage(formatedMsg);
				}
			}
		} else {
			for (ClientThread targetClient : clientHandler.getClientsList()) {
				sendMessageToTarget(message, targetClient.getClient().getName(), sender);
			}
		}
	}
	
	public void sendUsersListToAllClients() {
		try {
			for (ClientThread clientThread : this.clientHandler.getClientsList()) {
				sendUsersListToClient(clientThread.getClient());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendUsersListToClient(final Client client) throws IOException {
		List<User> users = new ArrayList<User>();
		for (ClientThread clientThread : this.clientHandler.getClientsList()) {
			if (!client.getName().equals(clientThread.getClient().getName())) {
				users.add(new User(clientThread.getClient().getName()));
			}
		}
		client.receiveUserList(users);
	}
	
}
