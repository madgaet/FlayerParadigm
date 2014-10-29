package com.trollCorporation.project;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.trollCorporation.services.Server;

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
    		InetAddress adresseLocale = InetAddress.getLocalHost();
    		System.out.println(adresseLocale);
    		System.out.println("adresse server : " + InetAddress.getByName("Madgaet-PC"));
    		Server server = new Server(port);
    		server.run();
    		//server.stop();
    	} catch (IOException io) {
    		logger.error("Problem with connection server", io);
    		System.out.println("Internal error : shouldn't append");
    	}
        
    }
}
