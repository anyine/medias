package cn.wizool.bank.iwebutil.newlay;

import java.util.List;

import cn.wizool.bank.iwebutil.newlay.query.QueryConditions;

public interface ICommonDataAccessSupport<MODEL> {

	public abstract void create(MODEL m);

	public abstract void create(List<MODEL> ms);

	public abstract int delete(String id);

	public abstract int delete(String[] ids);

	public abstract int delete(List<String> ids);

	public abstract int delete(MODEL m);

	public abstract int deletes(List<MODEL> models);

	public abstract int delete(QueryConditions conditions);

	public abstract int update(MODEL m);

	public abstract int update(List<MODEL> ms);

	public abstract int update(QueryConditions conditions);

	public abstract int count();

	public abstract int count(QueryConditions conditions);

	public abstract List<MODEL> select(int start, int count);

	public abstract List<MODEL> select(int start, int count,
			QueryConditions conditions);

	public abstract MODEL select(QueryConditions conditions);

	public abstract MODEL select(String id);

	public abstract DataAccessFactory getDataAccessFactory();

}