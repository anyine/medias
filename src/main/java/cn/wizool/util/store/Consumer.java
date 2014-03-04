package cn.wizool.util.store;

public abstract class Consumer implements Runnable {

	private Store store;

	public Consumer(Store store) {
		this.store = store;
	}

	@Override
	public void run() {
		Object p;
		while ((p = store.getProduct(this.toString())) != null) {
			this.consume(p);
		}
	}
	
	public abstract void consume(Object product);

}
