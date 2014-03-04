package cn.wizool.bank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FileDownload {

	@Id
	private String id;

	@ManyToOne
	private Machine department;

	@ManyToOne
	private Document document;

	@Column
	private Date start;

	@Column
	private Date end;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Machine getDepartment() {
		return department;
	}

	public void setDepartment(Machine department) {
		this.department = department;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
