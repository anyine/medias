package cn.wizool.util.store.test;

import java.util.UUID;

import cn.wizool.util.store.Consumer;
import cn.wizool.util.store.Store;
import cn.wizool.util.store.StoreManager;

public class Main1 {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		StoreManager sm = new StoreManager();
		Store ss[] = new Store[10];
		int i = -1;
		while (i++ < 9) {
			ss[i] = sm.createStore(UUID.randomUUID().toString());
			Consumer c = new DataConsumer("n" + i, ss[i]);
			new Thread(c).start();
		}

		// Thread.sleep(5000);

		i = 0;
		while (i++ < 100) {
			DataProduct dp = new DataProduct();
			dp.setData(UUID.randomUUID().toString() + ">>" + i);
			int index = new Double(Math.random() * 10.0).intValue();
			if (index < 10) {
				ss[index].putProduct(dp);
				// Thread.sleep((long) (Math.random() * 5000));
			}
		}

		Thread.sleep(5000);
	}

}
