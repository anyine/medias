package cn.wizool.bank.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class CommonTask {

	@Id
	private String id;

	@Column(name = "CLASS_TYPE")
	private String classType;

	@Column
	private String name;

	@ManyToOne()
	private User publisher;

	@Column
	private Date pubishDate;

	@Column
	private String dispatch;

	@Column
	private Long age;

	@Column
	private Boolean enabled;

	@Column()
	private String important;

	@Column()
	private Date startDate;

	@Column
	private boolean isDisplay = true;

	@OneToMany(mappedBy = "task")
	private List<TaskDocuments> tds = new ArrayList<TaskDocuments>();

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

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
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

}
