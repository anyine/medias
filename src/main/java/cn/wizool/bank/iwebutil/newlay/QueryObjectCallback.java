package cn.wizool.bank.iwebutil.newlay;

/**
 * 查询时提供的回调对象，当查询完成后，查询应当调用该回调对象的callback返回数据。
 * <br/> 使用这种方法查询，主要解决Hibernate延迟加载问题。
 * @author zhangbo
 *
 * @param <TYPE> 查询的对象
 */
public interface QueryObjectCallback<TYPE> {
	/**
	 * 当查询到结果后回调该方法，并把查询到的结果作为参数。
	 * @param objs 查询结果，如果没有查询到则为NULL
	 */
	public void callback(TYPE objs);
}
