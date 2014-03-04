package cn.wizool.util.store;

import java.util.ArrayList;
import java.util.List;

public class NoticeManager extends StoreManager implements Runnable {

	@Override
	public void run() {
		List<String> empty = new ArrayList<String>();
		empty.add("empty");
		while (true) {
			try {
				Thread.sleep(18000);
				synchronized (this) {
					for (String id : stores.keySet()) {
						this.getStore(id).putIfEmpty(empty);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static NoticeManager manager = null;

	public static synchronized NoticeManager Default() {
		if (manager == null) {
			manager = new NoticeManager();
			new Thread(manager).start();
		}
		return manager;
	}
}
