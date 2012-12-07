package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;
import java.util.List;

import com.u2p.core.comm.Client;

public class NewClientEvent extends EventObject{
	private InetAddress address;
	private String user;
	private List<String> commons;
	public NewClientEvent(Object source,InetAddress address,String user,List<String> commons) {
		super(source);
		this.address=address;
		this.user=user;
		this.commons=commons;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<String> getCommons() {
		return commons;
	}

	public void setCommons(List<String> commons) {
		this.commons = commons;
	}
}
