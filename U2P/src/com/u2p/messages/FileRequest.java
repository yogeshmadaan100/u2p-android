package com.u2p.messages;

import java.io.Serializable;

public class FileRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String group;
	private String fileURI;
	
	public FileRequest(String name, String group, String fileURI){
		setName(name);
		setGroup(group);
		setFileURI(fileURI);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getFileURI() {
		return fileURI;
	}

	public void setFileURI(String fileURI) {
		this.fileURI = fileURI;
	}

}
