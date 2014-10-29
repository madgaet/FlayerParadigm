package com.trollCorporation.common.model;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 3804073669997133589L;
	private String name;
	
	public User(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
