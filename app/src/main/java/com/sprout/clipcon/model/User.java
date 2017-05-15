package com.sprout.clipcon.model;

import android.util.Log;

public class User {
	private String name;
	private Group group;
	public User(String name, Group group) {
		Log.d("delf", "[SYSTEM] user is create.(name: "+ name + ", group key: " + group.getPrimaryKey());
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
