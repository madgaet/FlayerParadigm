package com.trollCorporation.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.trollCorporation.common.model.operations.Operation;

public class MessageUtils {
	
	public static void sendOperation(Socket socket, Operation operation)
		throws IOException {
		OutputStream stream = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(stream);
		oos.writeObject(operation);
		oos.flush();
	}
	
	public static Operation getOperation(Socket socket) 
			throws IOException {
		InputStream stream = socket.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(stream);
		try {
			return (Operation) ois.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
