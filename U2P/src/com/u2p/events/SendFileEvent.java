package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class SendFileEvent extends EventObject{
	private String group,filename;
	private InetAddress address;
	
	public SendFileEvent(Object source,String group,String filename,InetAddress address) {
		super(source);
		// TODO Auto-generated constructor stub
		this.address=address;
		this.filename=filename;
		this.group=group;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
