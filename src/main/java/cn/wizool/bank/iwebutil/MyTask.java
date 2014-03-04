package cn.wizool.bank.iwebutil;

import java.util.TimerTask;

import cn.wizool.bank.servlet.InterfaceServlet;

public class MyTask extends TimerTask {

	String id;
	String tid;
	String type;
	String method;

	public MyTask(String id, String tid, String type, String method) {
		this.id = id;
		this.tid = tid;
		this.type = type;
		this.method = method;
	}

	@Override
	public void run() {
		if ("putTask".equals(this.method)) {
			putTask();
		}
	}

	public void putTask() {
		InterfaceServlet.putOneTask(id, type + ":" + tid);
	}
}
