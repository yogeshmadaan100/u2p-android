package com.u2p.messages;

import java.io.Serializable;

public class Hello implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public Hello(String message){
		this.message=message;
	}

	@Override
	public String toString() {
		return "Hello [message=" + message + "]";
	}

}
