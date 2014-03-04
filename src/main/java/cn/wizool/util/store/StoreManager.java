package cn.wizool.util.store;

import java.util.HashMap;
import java.util.Map;

public class StoreManager {
	protected Map<String, Store> stores = new HashMap<String, Store>();

	public synchronized Store getStore(String key) {
		return stores.get(key);
	}

	public synchronized Store getStore(String key, boolean create) {
		Store ret = stores.get(key);
		if (ret == null && create) {
			ret = createStore(key);
		}
		return ret;
	}

	public synchronized Store createStore(String key) {
		Store s = new Store();
		stores.put(key, s);
		return s;
	}
}
