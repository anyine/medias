package cn.wizool.bank.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.wizool.bank.servlet.InterfaceServlet;

public class TimerScheduler {
	private static TimerScheduler timerScheduler = null;

	public static TimerScheduler getDefault() {
		if (timerScheduler == null) {
			timerScheduler = new TimerScheduler();
		}
		return timerScheduler;
	}

	private Map<String, Timer> list = new HashMap<String, Timer>();

	public void unschedule(String id) {
		Timer timer = list.get(id);
		if (timer != null) {
			timer.cancel();
		}
	}

	public void schedule(String tid, String[] ids, Date schedule) {
		this.unschedule(tid);
		Timer timer = new Timer();
		timer.schedule(new Task(tid, ids), schedule);
		list.put(tid, timer);
	}

	private class Task extends TimerTask {
		private String tid;
		private String[] ids;

		public Task(String tid, String[] ids) {
			this.tid = tid;
			this.ids = ids;
		}

		@Override
		public void run() {
			for (String id : ids) {
				if (id.trim().length() > 0) {
					InterfaceServlet.putOneTask(id, tid);
				}
			}
		}
	}
}
