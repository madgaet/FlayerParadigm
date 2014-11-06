package com.trollCorporation.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.trollCorporation.common.utils.ConfigurationsUtils;

public class ServerStatus implements Runnable {
		
		private final Logger LOGGER = Logger.getLogger(ServerStatus.class.getName());
		
		private ServerSocket socketServer;
		
		public ServerStatus() throws IOException {
			int port = Integer.valueOf(ConfigurationsUtils.getProperty("statusPort", "42424"));
			socketServer = new ServerSocket(port);
		}
		
		public void run() {
			try {
				while(true) {
					socketServer.accept();
				}
			} catch (IOException e) {
				handleException(e);
			}
		}
		
		private void handleException(final Exception e) {
			if (!(e instanceof SocketException)) {
				LOGGER.error("Error with socket status server connection", e);
			} else {
				LOGGER.error("Unknown error.", e);
			}
			e.printStackTrace();
		}
}
