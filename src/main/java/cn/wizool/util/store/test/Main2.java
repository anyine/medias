package cn.wizool.util.store.test;

import java.util.UUID;

import cn.wizool.util.store.Consumer;
import cn.wizool.util.store.Store;
import cn.wizool.util.store.StoreManager;

public class Main2 {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		StoreManager sm = new StoreManager();
		Store ss = sm.createStore(UUID.randomUUID().toString());

		Consumer c1 = new DataConsumer("c1", ss);
		new Thread(c1).start();

		DataProduct dp1 = new DataProduct();
		dp1.setData(UUID.randomUUID().toString());
		ss.putProduct(dp1);
		
		Thread.sleep(100);

		int i = 0;
		while (i++ < 100) {
			DataProduct dp = new DataProduct();
			dp.setData(UUID.randomUUID().toString() + ">>" + i);
			ss.putProduct(dp);
			// Thread.sleep((long) (Math.random() * 5000));
		}
		
		Consumer c2 = new DataConsumer("c2", ss);
		new Thread(c2).start();

		Thread.sleep(5000);
	}

}
