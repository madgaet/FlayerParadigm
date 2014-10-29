package com.trollCorporation.services;

public class ClientThread extends Thread {
	
	private Client client;
	
	public ClientThread(Client client) {
		super(client);
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
}
