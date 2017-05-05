package com.sprout.clipcon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private String name;
	private Group group;
	
	public User(String name) {
		this.name = name;
		group = null;
	}
}
