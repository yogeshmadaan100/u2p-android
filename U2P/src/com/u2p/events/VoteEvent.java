package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class VoteEvent extends EventObject implements ActivityEvents{
	private InetAddress address;
	private String group,file;
	private int vote;
	
	public VoteEvent(Object source,InetAddress address) {
		super(source);
		this.address=address;
		this.vote=0;
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setGroupAndFile(String group,String file){
		this.group=group;
		this.file=file;
	}
	
	public String getGroup(){
		return this.group;
	}
	
	public String getFile(){
		return this.file;
	}
	
	public void vote(int v){
		this.vote=v;
	}
	
	public int getVote(){
		return this.vote;
	}
	
	public InetAddress getAddress() {
		// TODO Auto-generated method stub
		return this.address;
	}

}
