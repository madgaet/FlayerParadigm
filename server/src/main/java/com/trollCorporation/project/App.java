package com.trollCorporation.project;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.trollCorporation.services.Server;
import com.trollCorporation.services.ServerStatus;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Logger logger = Logger.getLogger(App.class.getName());
    	int port = 4242;
    	try {
    		new Thread(new ServerStatus()).start();
    		InetAddress adresseLocale = InetAddress.getLocalHost();
    		System.out.println(adresseLocale);
    		logger.info("adresse server : " + adresseLocale);
    		Server server = new Server(port);
    		server.run();
    	} catch (IOException io) {
    		logger.error("Problem with connection server", io);
    		System.out.println("Internal error : shouldn't append");
    	}
        
    }
}
