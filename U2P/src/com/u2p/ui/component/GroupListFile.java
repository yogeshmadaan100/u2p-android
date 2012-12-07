package com.u2p.ui.component;

import java.util.HashMap;
import java.util.List;

public class GroupListFile {
	private HashMap<String,List<ItemFile>> listGroup;
	
	public GroupListFile(){
		listGroup=new HashMap<String,List<ItemFile>>();
	}
	
	public void addListFileToGroup(String group,List<ItemFile> files){
		if(!listGroup.containsKey(group)){
			listGroup.put(group, files);
		}else{
			List<ItemFile> aux=listGroup.get(group);
			aux.addAll(files);
		}
	}
	
	public List<ItemFile> getListFile(String group){
		return this.listGroup.get(group);
	}
}
