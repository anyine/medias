package cn.wizool.bank.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Document {

	@Id
	private String id;

	@Column(nullable = true)
	private String name;

	@Column(nullable = true)
	private long length;

	@Column()
	private String md5;

	@Column(name = "IND", nullable = true)
	private int index = 0;

	@Column(nullable = true)
	private String surfix;

	@Column
	private String type;

	@Column
	private Date uploadDate;

	@Column
	private Integer state = 0;

	@Column
	private boolean isDisplay = true;

	@ManyToOne
	private User upload_publisher;

	@OneToMany(mappedBy = "document")
	private List<TaskDocuments> tds = new ArrayList<TaskDocuments>();

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getSurfix() {
		return surfix;
	}

	public void setSurfix(String surfix) {
		this.surfix = surfix;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TaskDocuments> getTds() {
		return tds;
	}

	public void setTds(List<TaskDocuments> tds) {
		this.tds = tds;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public User getUpload_publisher() {
		return upload_publisher;
	}

	public void setUpload_publisher(User upload_publisher) {
		this.upload_publisher = upload_publisher;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
