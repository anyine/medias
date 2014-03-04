package cn.wizool.bank.model;

public class FileDistribute {

	private String id;
	private Machine node;
	private Document document;
	private long from;
	private long length;

	public Machine getNode() {
		return node;
	}

	public void setNode(Machine node) {
		this.node = node;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
