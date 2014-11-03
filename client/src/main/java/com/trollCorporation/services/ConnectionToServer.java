package com.trollCorporation.services;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.MessageOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.utils.MessageUtils;
import com.trollCorporation.common.utils.PropertiesLoader;
import com.trollCorporation.project.exceptions.ConnectionException;

public class ConnectionToServer {

	private static Logger LOG = Logger.getLogger(ConnectionToServer.class.getName());
	private static String serverAddress;
	private static int serverPort;
	static {
		try {
			PropertiesLoader props = new PropertiesLoader("config.properties");
			serverAddress = props.getProperty("address", "localhost");
			serverPort = Integer.valueOf(props.getProperty("port", "4242"));
		} catch (ConfigurationException e) {
			System.out.println("Wrong configuration");
			LOG.error("Wrong configuration");
		}
	}
	
	public static boolean isConnectionToServerPossible() {
		try {
			URL url = new URL("http://" + serverAddress);
            return (url.openConnection()!=null);
		} catch (IOException e) {
			return false;
		}
	}

	private Socket sock;
	
	public ConnectionToServer() {
		this(serverAddress, serverPort);
	}
	
	public ConnectionToServer(int port){
		this(serverAddress, port);
	}
	
	public ConnectionToServer(String address, int port){
		try {
			sock = new Socket(address, port);
			System.out.println("new sock ok");
			System.out.println(sock.getRemoteSocketAddress());
		} catch (IOException e) {
			System.out.println("Unable to connect to " + address +" on port " + port + ".");
			LOG.error("Unable to connect to " + address + ":" + port);
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

