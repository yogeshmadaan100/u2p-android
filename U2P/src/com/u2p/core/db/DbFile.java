package com.u2p.db;

public class DbFile {
	private long id;
	private String group;
	private String name;
	private String uri;
	private int positive,negative;
	
	public DbFile(String name, String uri,int positive,int negative) {
		super();
		this.name = name;
		this.uri = uri;
		this.positive=positive;
		this.negative=negative;
	}

	public int getPositive() {
		return positive;
	}

	public void setPositive(int positive) {
		this.positive = positive;
	}

	public int getNegative() {
		return negative;
	}

	public void setNegative(int negative) {
		this.negative = negative;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "BdFile [id=" + id + ", name=" + name+ ", uri=" + uri + "]";
	}

}
