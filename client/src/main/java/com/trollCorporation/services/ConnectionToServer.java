package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.TimeoutException;
import com.trollCorporation.common.model.enums.OperationType;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.utils.ConfigurationsUtils;
import com.trollCorporation.common.utils.MessageUtils;

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
	
	public void sendOperation(final Operation operation) throws ConnectionException {
		try {
			if (sock == null) {
				throw new ConnectionException("The connection has not been set up!");
			}
			MessageUtils.sendOperation(sock, operation);
		} catch (IOException e) {
			LOG.error("Operation " + operation.getOperationType().toString() + " has failed!");
			throw new ConnectionException(e.getMessage());
		}
	}
	
	public Operation sendUnconnectedOperation(final Operation operation) throws ConnectionException, TimeoutException {
		try {
			if (OperationType.isADisconnectedOperation(operation.getOperationType())) {
				createSocket();
				MessageUtils.sendOperation(sock, operation);
				int retry = 0;
				Operation response;
				sock.setSoTimeout(30000);
				do {
					response = MessageUtils.getOperation(sock);
					if (response.getOperationType().equals(operation.getOperationType())) {
						return response;
					}
					retry ++;
					LOG.warn("Received " + response.getOperationType().toString() + " instead of " +
					operation.getOperationType() + "! Retrying to get the operation." );
				} while (!response.getOperationType().equals(operation.getOperationType())
						|| retry <= 10);
			}
		} catch (SocketTimeoutException s) {
			LOG.error("Server didn't answer in time...");
			throw new TimeoutException(s.getMessage());
		} catch (IOException e) {
			LOG.error("Operation " + operation.getOperationType().toString() + " has failed!");
			throw new ConnectionException(e.getMessage());
		} finally {
			//remove timeout option
			try {sock.setSoTimeout(0);} catch (SocketException e) {}
		}
		return null;
	}
	
	public void close() {
		try {
			if (sock != null) {
				sock.close();
			}
		} catch (IOException e) {
			// shouldn't be checked
		}
		sock = null;
	}
	
	public Socket getSocket() {
		return sock;
	}
}

