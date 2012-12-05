package com.u2p.messages;

import java.io.Serializable;

public class ACK implements Serializable{
	private int type;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ACK(int type){
		this.type=type;
	}
	
	public int getACKType(){
		return type;
	}
	
}
