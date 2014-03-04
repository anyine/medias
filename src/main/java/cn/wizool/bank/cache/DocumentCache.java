package cn.wizool.bank.cache;

public class DocumentCache {

	private String id;

	/**
	 * 文件名称
	 */
	private String name;

	/**
	 * 文件大小
	 */
	private long length;

	public DocumentCache(String id) {
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

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

}
