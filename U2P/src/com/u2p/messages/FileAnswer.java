package com.u2p.messages;

import java.io.File;
import java.io.Serializable;

public class FileAnswer implements Serializable{

	private static final long serialVersionUID = 1L;

	private String requestedFileURI;
	private File file;
	private int fileSize;
	
	public FileAnswer(String requestedFileURI, File file, int fileSize){
		setRequestedFileURI(requestedFileURI);
		setFile(file);
		setFileSize(fileSize);
	}

	public String getRequestedFileURI() {
		return requestedFileURI;
	}

	public void setRequestedFileURI(String requestedFileURI) {
		this.requestedFileURI = requestedFileURI;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
}
