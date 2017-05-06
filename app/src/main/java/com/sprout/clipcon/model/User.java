package com.sprout.clipcon.model;

public class User {
	private String name;
	private Group group;
	
	public User(String name) {
		this.name = name;
		group = null;
	}
}
