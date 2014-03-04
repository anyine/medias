package cn.wizool.bank.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Task;

public interface TaskService extends ICommonServiceSupport {

	public abstract void transCreate(String uid, Map<String, String> map);

	public abstract void transDelete(String uid, String[] ids, String url);

	public abstract void transUpdate(String uid, Task t);

	public abstract void transUpdate(String uid, String id, String webUrl)
			throws IOException;

	public abstract void transSelectAll(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Task> callback);

	public abstract void transSelect(String uid, String id,
			QueryObjectCallback<Task> callback);

	public abstract Task transSelectById(String id);

	public abstract void getAll();
}
