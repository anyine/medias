package cn.wizool.util.store.test;

import cn.wizool.util.store.Consumer;
import cn.wizool.util.store.Store;

public class DataConsumer extends Consumer {

	private String name;

	public DataConsumer(String name, Store store) {
		super(store);
		this.name = name;
	}

	@Override
	public void consume(Object product) {
		DataProduct p = (DataProduct) product;
		System.out.println(name + ":" + p.getData());

		try {
			Thread.sleep((long) (Math.random() * 5000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
