package com.trollCorporation.project.client;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * Hello world!
 *
 */
public class App 
{	
    public static void main( String[] args ) throws ClassNotFoundException, InstantiationException,
    		IllegalAccessException, UnsupportedLookAndFeelException {    	
    	for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}	
    	Game.getInstance();
    }
}
