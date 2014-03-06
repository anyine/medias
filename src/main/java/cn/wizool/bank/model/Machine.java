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
public class Machine {

	@Id
	private String id;

	@Column(nullable = true)
	private String name;

	@Column(nullable = true)
	private String type;

	@Column(unique=true)
	private String ip;

	@Column
	private Long port;

	@Column
	private String linkman;

	@Column
	private String phone;

	@Column
	private String mobilephone;

	@Column
	private String version = "";

	@Column
	private Date bootTime;

	@Column
	private Date shutdownTime;

	@ManyToOne
	private Task currentTask;

	@Column
	private Date active;

	@ManyToOne()
	private Branch parent;

	@OneToMany(mappedBy = "machine")
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Branch getParent() {
		return parent;
	}

	public void setParent(Branch parent) {
		this.parent = parent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public Date getActive() {
		return active;
	}

	public void setActive(Date active) {
		this.active = active;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getBootTime() {
		return bootTime;
	}

	public void setBootTime(Date bootTime) {
		this.bootTime = bootTime;
	}

	public Date getShutdownTime() {
		return shutdownTime;
	}

	public void setShutdownTime(Date shutdownTime) {
		this.shutdownTime = shutdownTime;
	}

	public List<MachineTasks> getMts() {
		return mts;
	}

	public void setMts(List<MachineTasks> mts) {
		this.mts = mts;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

}
