package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;
import java.util.List;

public class ListCommons extends EventObject{
	private List<String> commons;
	private InetAddress address;
	
	public ListCommons(Object source,InetAddress address,List<String> commons) {
		super(source);
		this.address=address;
		this.commons=commons;
		// TODO Auto-generated constructor stub
	}

	public List<String> getCommons() {
		return commons;
	}

	public void setCommons(List<String> commons) {
		this.commons = commons;
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
