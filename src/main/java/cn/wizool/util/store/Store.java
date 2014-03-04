package cn.wizool.util.store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Store {
	List<Object> products = new ArrayList<Object>();
	Set<Object> oldConsumers = new HashSet<Object>();
	Object curConsumer = null;

	Store() {

	}

	public List<Object> getProducts(Object consumer) {
		synchronized (products) {
			if (consumer == null || oldConsumers.contains(consumer)) {
				oldConsumers.remove(consumer);
				return null;
			}

			if (curConsumer == null) {
				curConsumer = consumer;
			} else if (!curConsumer.equals(consumer)) {
				this.oldConsumers.add(this.curConsumer);
				this.curConsumer = consumer;
			}

			products.notify();
			try {
				if (products.size() == 0) {
					products.wait();
				}

				if (products.size() == 0) {
					return null;
				}

				List<Object> ps = new ArrayList<Object>();

				while (products.size() > 0) {
					ps.add(products.remove(0));
				}

				return ps;
			} catch (InterruptedException e) {
				return null;
			}
		}
	}

	public Object getProduct(Object consumer) {
		synchronized (products) {
			if (consumer == null || oldConsumers.contains(consumer)) {
				oldConsumers.remove(consumer);
				return null;
			}

			if (curConsumer == null) {
				curConsumer = consumer;
			} else if (!curConsumer.equals(consumer)) {
				this.oldConsumers.add(this.curConsumer);
				this.curConsumer = consumer;
			}

			products.notify();
			try {
				if (products.size() == 0) {
					products.wait();
				}

				if (products.size() > 0) {
					return products.remove(0);
				}
				return null;
			} catch (InterruptedException e) {
				return null;
			}
		}
	}

	public void putProduct(Object product) {
		synchronized (products) {
			products.add(product);
			products.notify();
		}
	}

	public void putOneProduct(Object product) {
		synchronized (products) {
			products.clear();
			products.add(product);
			products.notify();
		}
	}

	public void putIfEmpty(Object product) {
		synchronized (products) {
			if (products.size() == 0) {
				products.add(product);
				products.notify();
			}
		}
	}

}
