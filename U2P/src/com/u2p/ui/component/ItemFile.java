package com.u2p.ui.component;

import java.io.Serializable;

public class ItemFile implements Serializable{

	private static final long serialVersionUID = 8858451646619645528L;
	protected long id;
	protected String rutaImagen;
	protected String name;
	protected String user;
	protected String size;
	protected String rating;
	
	public ItemFile(long id, String rutaImagen, String name, String user,
			String size, String rating) {
		this.id = id;
		this.rutaImagen = rutaImagen;
		this.name = name;
		this.user = user;
		this.size = size;
		this.rating = rating;
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
