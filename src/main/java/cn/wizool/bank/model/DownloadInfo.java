package cn.wizool.bank.model;

import java.util.Date;

public class DownloadInfo {

	private String id;
	private Machine node1;
	private Machine node2;
	private Document document;
	private Long from;
	private Long length;
	private Integer status;// 0:正在下载,1:下载完成
	private Date startTime;
	private Date endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Machine getNode1() {
		return node1;
	}

	public void setNode1(Machine node1) {
		this.node1 = node1;
	}

	public Machine getNode2() {
		return node2;
	}

	public void setNode2(Machine node2) {
		this.node2 = node2;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
