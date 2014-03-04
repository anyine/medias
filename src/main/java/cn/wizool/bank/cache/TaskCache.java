package cn.wizool.bank.cache;

import java.io.ByteArrayOutputStream;

public class TaskCache {

	private String id;

	private String name;

	private ByteArrayOutputStream description;

	public TaskCache(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ByteArrayOutputStream getDescription() {
		return description;
	}

	public void setDescription(ByteArrayOutputStream description) {
		this.description = description;
	}

}
