package com.sprout.clipcon.model;

public class User {
	private String name;
	private Group group;


	public User(String name, Group group) {
		this.name = name;
		this.group = group;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGroupKey(Group groupKey) {
		this.group = groupKey;
	}

	public String getName() {
		return name;
	}

	public Group getGroup() {
		return group;
	}
}
