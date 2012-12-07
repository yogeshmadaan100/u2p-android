package com.u2p.messages;

import java.io.Serializable;
import java.util.ArrayList;

import com.u2p.ui.component.ItemFile;

public class ListAnswer implements Serializable{

	private static final long serialVersionUID = 1L;

	private ArrayList<ItemFile> itemsList;

	public ListAnswer(ArrayList<ItemFile> itemsList){
		setItemsList(itemsList);
	}
	
	public ArrayList<ItemFile> getItemsList() {
		return itemsList;
	}

	public void setItemsList(ArrayList<ItemFile> itemsList) {
		this.itemsList = itemsList;
	}
	
	
}
