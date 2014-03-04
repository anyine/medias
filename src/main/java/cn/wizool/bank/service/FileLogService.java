package cn.wizool.bank.service;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.FileLog;

public interface FileLogService extends ICommonServiceSupport {
	public abstract void transCreate(String uid, String did, String id,
			String type);

	public abstract void transDeleteSelected(String uid, String[] ids);

	public abstract void transDeleteAll(String uid, NamedConditions cond);

	public abstract void transSelectByCond(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<FileLog> callback);
}
