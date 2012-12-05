package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class AuthenticationEvent extends EventObject implements ActivityEvents{
	private InetAddress address;
	public AuthenticationEvent(Object source,InetAddress address) {
		super(source);
		this.address=address;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InetAddress getAddress() {
		// TODO Auto-generated method stub
		return address;
	}
}
