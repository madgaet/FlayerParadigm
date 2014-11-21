package com.trollCorporation.services;

import java.util.ArrayList;
import java.util.List;

public class ClientHandler {

	private List<ClientThread> clients = new ArrayList<ClientThread>();
	
	public synchronized void add(ClientThread client) {
		clients.add(client);
	}
	
	public synchronized void remove(Client client) {
		for (ClientThread clientThread : clients) {
			if (clientThread.getClient().equals(client)) {
				clients.remove(clientThread);
				break;
			}
		}
	}
	
	public ClientThread existsClientName(String name) {
		for (ClientThread client : clients) {
			if (client.getClient().getName().equals(name)) {
				return client;
			}
		}
		return null;
	}
	
	public List<ClientThread> getClientsList() {
		return clients;
	}
}
