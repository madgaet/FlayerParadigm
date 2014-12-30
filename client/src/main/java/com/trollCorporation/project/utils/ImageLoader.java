package com.trollCorporation.project.utils;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public final class ImageLoader {
	
	private static final Logger LOG = Logger.getLogger(ImageLoader.class.getName());
	
	public static Image prepareImage(String url) {
		Image img= null;
		try {
		img = ImageIO.read(ImageLoader.class.getClassLoader().getResource(url));
		} catch (IOException e) {
			System.out.println("the image " + url + " couldn't be read");
			LOG.error("Error loading img " + url, e);
		}
		return img;
	}
	
}
