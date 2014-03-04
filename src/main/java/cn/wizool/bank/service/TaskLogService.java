package cn.wizool.bank.service;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.TaskLog;

public interface TaskLogService extends ICommonServiceSupport {

	public abstract void transCreate(String uid, String tid, String id,
			String type);

	public abstract void transSelect(String uid, int start, int limit,
			NamedConditions c, QueryListCallback<TaskLog> callback);

	public abstract void transDeleteSelected(String uid, String[] ids);

	public abstract void transDeleteAll(String uid, NamedConditions cond);

	public abstract int selectCountByCond(NamedConditions c);
}
