package com.sprout.clipcon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contents {
	public static String TYPE_STRING = "STRING";
	public static String TYPE_IMAGE = "IMAGE";
	public static String TYPE_FILE = "FILE";

	private String contentsType;
	private long contentsSize;
	public String contentsPKName;
	private String uploadUserName;
	private String uploadTime;
	private String contentsValue;

	public Contents(String contentsType, long contentsSize, String contentsPKName, String uploadUserName, String uploadTime, String contentsValue) {
		this.contentsType = contentsType;
		this.contentsSize = contentsSize;
		this.contentsPKName = contentsPKName;
		this.uploadUserName = uploadUserName;
		this.uploadTime = uploadTime;
		this.contentsValue = contentsValue;
	}
}