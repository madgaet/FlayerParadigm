package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.utils.ConfigurationsUtils;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.project.exceptions.ConnectionException;

public class ConnectionToServer {

	private static Logger LOG = Logger.getLogger(ConnectionToServer.class.getName());
	private static String serverAddress;
	private static int serverPort;
	static {
		serverAddress = ConfigurationsUtils.getProperty("address", "localhost");
		serverPort = Integer.valueOf(ConfigurationsUtils.getProperty("port", "4242"));
	}
	
	public static boolean isConnectionToServerPossible() {
		try {
			new ConnectionToServer(42424);
			return true;
		} catch (ConnectionException e) {
			return false;
		}
	}

	private Socket sock;
	
	public ConnectionToServer() throws ConnectionException {
		this(serverAddress, serverPort);
	}
	
	public ConnectionToServer(int port) throws ConnectionException{
		this(serverAddress, port);
	}
	
	public ConnectionToServer(String address, int port) throws ConnectionException{
		try {
			sock = new Socket(address, port);
			System.out.println(sock.getRemoteSocketAddress());
		} catch (IOException e) {
			System.out.println("Unable to connect to " + address +" on port " + port + ".");
			LOG.error("Unable to connect to " + address + ":" + port);
			throw new ConnectionException("Unable to connect to server");
		}
	}
	
	public Operation getOperation() throws ConnectionException {
		try {
			return MessageUtils.getOperation(sock);
		} catch (IOException e) {
			LOG.error("getting message isn't working");
			throw new ConnectionException(e.getMessage());
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
}

