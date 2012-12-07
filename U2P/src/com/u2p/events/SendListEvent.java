package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class SendListEvent extends EventObject{
	private String group;
	private InetAddress address;
	public SendListEvent(Object source,String group,InetAddress address) {
		super(source);
		this.group=group;
		this.address=address;
		// TODO Auto-generated constructor stub
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public InetAddress getAddress(){
		return this.address;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
