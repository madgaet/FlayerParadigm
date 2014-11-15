package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.trollCorporation.common.model.ConnectionOperation;
import com.trollCorporation.common.model.ListUsersOperation;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.model.OperationType;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.utils.ConfigurationsUtils;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.project.exceptions.ConnectionException;

public class ConnectionToServer {

	private static Logger LOG = Logger.getLogger(ConnectionToServer.class.getName());
	private static ConnectionToServer singleton;
	private static String serverAddress;
	private static int serverPort;
	static {
		serverAddress = ConfigurationsUtils.getProperty("address", "localhost");
		serverPort = Integer.valueOf(ConfigurationsUtils.getProperty("port", "4242"));
	}

	private Socket sock;
	
	public synchronized static ConnectionToServer getInstance() 
		throws ConnectionException {
		if (singleton == null) {
			synchronized (ConnectionToServer.class) {
				if (singleton == null) {
					singleton = new ConnectionToServer(serverAddress, serverPort);
				}
			}
		}
		return singleton;
	}
	
	private ConnectionToServer(int port) throws ConnectionException{
		this(serverAddress, port);
	}
	
	private ConnectionToServer(String address, int port) throws ConnectionException{
		serverAddress = address;
		serverPort = port;
	}
	
	private void createSocket() throws ConnectionException {
		try {
			sock = new Socket(serverAddress, serverPort);
			System.out.println(sock.getRemoteSocketAddress());
		} catch (IOException e) {
			System.out.println("Unable to connect to " + serverAddress +" on port " + serverPort + ".");
			LOG.error("Unable to connect to " + serverAddress + ":" + serverPort);
			throw new ConnectionException("Unable to connect to server");
		}
	}
	
	public void sendMessage(final String message) throws ConnectionException {
		try {
			Message messageObj = new Message(message);
			MessageOperation messageOperation = new MessageOperation(messageObj);
			MessageUtils.sendOperation(sock, messageOperation);
		} catch (IOException e) {
			LOG.error("sending message isn't working");
			throw new ConnectionException(e.getMessage());
		}
	}
	
	public void requestFriendsList() throws ConnectionException {
		try {
			ListUsersOperation listUsersOperation = new ListUsersOperation();
			MessageUtils.sendOperation(sock, listUsersOperation);
		} catch (IOException e) {
			LOG.error("sending friends list request isn't working");
			throw new ConnectionException(e.getMessage());
		}
	}
	
	public boolean connectUserToServer(final User user) throws ConnectionException {
		try {
			createSocket();
			ConnectionOperation connOperation = new ConnectionOperation(user);
			MessageUtils.sendOperation(sock, connOperation);
			int retry = 0;
			Operation operation;
			do {
				operation = MessageUtils.getOperation(sock);
				if (operation.getOperationType().equals(OperationType.CONNECTION)) {
					ConnectionOperation connOpe = (ConnectionOperation) operation;
					return connOpe.isConnected();
				}
				retry ++;
			} while (!operation.getOperationType().equals(OperationType.CONNECTION)
					|| retry >= 10);
		} catch (IOException e) {
			LOG.error("sending try connection isn't working");
			throw new ConnectionException(e.getMessage());
		}
		close();
		return false;
	}
	
	public void close() {
		try {
			sock.close();
		} catch (IOException e) {
			// shouldn't be checked
		}
		sock = null;
	}
	
	public Socket getSocket() {
		return sock;
	}
}

