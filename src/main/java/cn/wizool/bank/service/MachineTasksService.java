package cn.wizool.bank.service;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.MachineTasks;

public interface MachineTasksService extends ICommonServiceSupport {

	public abstract void transSelectByCond(String uid, NamedConditions cond,
			QueryListCallback<MachineTasks> callback);

	public abstract String[] getMachinesByTaskId(String tid);

}
