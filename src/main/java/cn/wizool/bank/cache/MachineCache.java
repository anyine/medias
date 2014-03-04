package cn.wizool.bank.cache;

import java.util.Date;

public class MachineCache {
	/**
	 * ID
	 */
	private String id;

	/**
	 * (查询)网点名称
	 */
	private String name;

	/**
	 * (查询)机器IP地址
	 */
	private String ip;

	/**
	 * (查询)联系人信息等
	 */
	private String relation;

	/**
	 * (查询)机器类型
	 */
	private String type;

	/**
	 * (传递)活动时间
	 */
	private Date active;

	/**
	 * (传递)开机时间
	 */
	private Date bootTime;

	/**
	 * (传递)程序的版本
	 */
	private String version;

	/**
	 * (传递)当前任务ID
	 */
	private String taskId;

	/**
	 * (传递)当前任务状态
	 */
	private String taskStatus;

	/**
	 * (传递)下载文件ID
	 */
	private String fileId;

	/**
	 * (传递)已经下载的大小
	 */
	private Long downSize;

	private TaskCache task;

	private DocumentCache document;

	private Date startTime;

	private Date downTime;

	public MachineCache(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBootTime() {
		return bootTime;
	}

	public void setBootTime(Date bootTime) {
		this.bootTime = bootTime;
		this.active = bootTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getDownSize() {
		return downSize;
	}

	public void setDownSize(Long downSize) {
		this.downSize = downSize;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getActive() {
		return active;
	}

	public void setActive(Date active) {
		this.active = active;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public void setBoot(String version, Date bootTime) {
		this.setTask(null);
		this.setTaskId("");
		this.setTaskStatus("");
		this.setDocument(null);
		this.setFileId("");
		this.setDownSize(null);
		this.setVersion(version);
		this.setBootTime(bootTime);
		this.setActive(bootTime);
	}

	public void setTask(String tid, String status) {
		this.setTaskId(tid);
		this.setTaskStatus(status);
	}

	public String setDown(String newFid, long size) {
		String oldFid = this.getFileId();
		this.setFileId(newFid);
		this.setDownSize(size);

		if (!newFid.equals(oldFid)) {
			return oldFid;
		}
		return null;
	}

	public TaskCache getTask() {
		return task;
	}

	public void setTask(TaskCache task) {
		this.task = task;
	}

	public DocumentCache getDocument() {
		return document;
	}

	public void setDocument(DocumentCache document) {
		this.document = document;
	}

	public Date getDownTime() {
		return downTime;
	}

	public void setDownTime(Date downTime) {
		this.downTime = downTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getBootStatus(long times) {
		Date date = getActive();
		String status = "关机";
		if (date != null && (new Date().getTime() - date.getTime()) < times) {
			status = "开机";
		}
		return status;
	}

	public void endTask() {
		this.setTask(null);
		this.setTaskId("");
		this.setTaskStatus("");
	}
}
