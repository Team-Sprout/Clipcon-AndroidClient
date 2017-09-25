package com.sprout.clipcon.model;

public class Contents {
	public final static String TYPE_STRING = "STRING";
	public final static String TYPE_IMAGE = "IMAGE";
	public final static String TYPE_FILE = "FILE";
	public final static String TYPE_MULTIPLE_FILE = "MULTIPLE_FILE";

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


	public String getContentsType() {
		return contentsType;
	}

	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}

	public long getContentsSize() {
		return contentsSize;
	}

	public void setContentsSize(long contentsSize) {
		this.contentsSize = contentsSize;
	}

	public String getContentsPKName() {
		return contentsPKName;
	}

	public void setContentsPKName(String contentsPKName) {
		this.contentsPKName = contentsPKName;
	}

	public String getUploadUserName() {
		return uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getContentsValue() {
		return contentsValue;
	}

	public void setContentsValue(String contentsValue) {
		this.contentsValue = contentsValue;
	}
}