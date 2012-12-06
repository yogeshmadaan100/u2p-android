package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class ListEvent extends EventObject implements ActivityEvents{
	private InetAddress address;
	private String group;
	
	public ListEvent(Object source,InetAddress address) {
		super(source);
		this.address=address;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setGroup(String group){
		this.group=group;
	}
	
	public String getGroup(){
		return this.group;
	}
	public InetAddress getAddress() {
		// TODO Auto-generated method stub
		return this.address;
	}

}
