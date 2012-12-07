package com.u2p.ui.component;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;

import com.u2p.core.db.DbFile;

public class ItemFile implements Serializable{

	private static final long serialVersionUID = 8858451646619645528L;
	protected long id;
	protected String rutaImagen;
	protected String name;
	protected String user;
	protected String size;
	protected String rating;
	private String group;
	private InetAddress address;
	
	public ItemFile(long id, String rutaImagen, String name, String user,
			String size, String rating) {
		this.id = id;
		this.rutaImagen = rutaImagen;
		this.name = name;
		this.user = user;
		this.size = size;
		this.rating = rating;
	}
	
	public ItemFile(long id, String rutaImagen, String name, String user,
			String size, String rating,String group) {
		this.id = id;
		this.rutaImagen = rutaImagen;
		this.name = name;
		this.user = user;
		this.size = size;
		this.rating = rating;
		this.group=group;
	}


	public ItemFile(DbFile file, String user, String rutaImagen){
		this.id = file.getId();
		this.rutaImagen = rutaImagen;
		this.name = file.getName();
		this.user = user;
		File f = new File(file.getUri());
		this.size = Integer.toString((int)f.length());
		int total = file.getPositive() + file.getNegative();
		int rat = file.getPositive() - file.getNegative();
		this.rating = Integer.toString(rat)+"/"+Integer.toString(total);
		this.group=file.getGroup();
	}
	
	public ItemFile(){
		this.rutaImagen = "";
		this.name="";
		this.size="";
		this.rating="";
	}
	
	public ItemFile(long id, String rutaImagen, String name, String size, String rating) {
		this.id = id;
		this.rutaImagen = rutaImagen;
		this.name = name;
		this.size = size;
		this.rating = rating;
	}

	public void setAddress(InetAddress address){
		this.address=address;
	}
	
	public InetAddress getAddress(){
		return this.address;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
