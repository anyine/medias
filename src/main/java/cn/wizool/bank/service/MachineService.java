package cn.wizool.bank.service;

import java.util.List;
import java.util.Map;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Machine;

public interface MachineService extends ICommonServiceSupport {

	public abstract void transCreateMachine(String uid, Machine m);

	public abstract void transEditMachine(String uid, Map<String, String> map);

	public abstract void transDeleteMachine(String uid, String[] ids);

	public abstract Machine getMachine(NamedConditions cond);

	public abstract Machine getObject(String id);

	public abstract List<Machine> transSelectByCond(NamedConditions cond);

	public abstract List<Machine> getAll();

	public abstract void transSelect(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Machine> callback);

	public abstract void transSelectById(String uid, String id,
			QueryObjectCallback<Machine> callback);

	public abstract int getCountByC(NamedConditions c);

	public abstract void transSetCurrentTask(String uid, String id, String tid,
			String type);

	public abstract Map<String, String> getMachineByIp(String ip);

}
