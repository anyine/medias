package cn.wizool.bank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TaskLog {

	@Id
	private String id;

	@ManyToOne()
	private Machine machine;

	@ManyToOne()
	private Task task;

	@Column
	private String operateType;

	@Column
	private Date birth;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Machine getDepartment() {
		return machine;
	}

	public void setDepartment(Machine department) {
		this.machine = department;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

}
