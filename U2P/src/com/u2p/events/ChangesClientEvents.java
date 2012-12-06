package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;
import java.util.HashMap;

public class ChangesClientEvents extends EventObject{
	private HashMap<InetAddress,Thread> activeClients;

	public ChangesClientEvents(Object source,HashMap<InetAddress,Thread> clients) {
		super(source);
		this.activeClients=clients;
		// TODO Auto-generated constructor stub
	}
	
	public HashMap<InetAddress,Thread> getClients(){
		return this.activeClients;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
