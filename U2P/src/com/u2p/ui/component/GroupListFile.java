package com.u2p.ui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupListFile {
	private HashMap<String,ArrayList<ItemFile>> listGroup;
	
	public GroupListFile(){
		listGroup=new HashMap<String,ArrayList<ItemFile>>();
	}
	
	public void addListFileToGroup(String group,ArrayList<ItemFile> files){
		if(!listGroup.containsKey(group)){
			listGroup.put(group, files);
		}else{
			List<ItemFile> aux=listGroup.get(group);
			aux.addAll(files);
		}
	}
	
	public ArrayList<ItemFile> getListFile(String group){
		return this.listGroup.get(group);
	}
	
	public ItemFile getFile(String group,String name){
		if(listGroup.containsKey(group)){
			List<ItemFile> aux=listGroup.get(group);
			for(ItemFile file:aux){
				if(file.getName().equals(name))
					return file;
			}
		}
		return null;
	}
	
	public void addFileToGroup(ItemFile file){
		if(listGroup.containsKey(file.getGroup())){
			List<ItemFile> aux=listGroup.get(file.getGroup());
			for(ItemFile f:aux){
				if(f.getName().equals(file.getName())){
					aux.remove(f);
					aux.add(file);
				}
			}
		}
	}
}
