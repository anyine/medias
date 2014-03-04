package cn.wizool.bank.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "task")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CLASS_TYPE")
public abstract class Task {

	@Id
	private String id;

	@Column
	private String name;

	@ManyToOne()
	private User publisher;

	@Column
	private Date pubishDate;

	@Column
	private boolean isDisplay = true;

	@OneToMany(mappedBy = "task")
	private List<TaskDocuments> tds = new ArrayList<TaskDocuments>();

	@OneToMany(mappedBy = "task")
	private List<MachineTasks> mts = new ArrayList<MachineTasks>();

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

	public User getPublisher() {
		return publisher;
	}

	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}

	public Date getPubishDate() {
		return pubishDate;
	}

	public void setPubishDate(Date pubishDate) {
		this.pubishDate = pubishDate;
	}

	public List<TaskDocuments> getTds() {
		return tds;
	}

	public void setTds(List<TaskDocuments> tds) {
		this.tds = tds;
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public List<MachineTasks> getMts() {
		return mts;
	}

	public void setMts(List<MachineTasks> mts) {
		this.mts = mts;
	}

}
