package cn.wizool.bank.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.Task;
import cn.wizool.bank.service.MachineService;

public class MachineServiceImpl extends PlatFormServiceSupport implements
		MachineService {

	@Override
	public void transCreateMachine(String uid, Machine m) {
		getMachineDao().create(m);
	}

	@Override
	public void transEditMachine(String uid, Map<String, String> map) {
		String id = map.get("id");
		if (StringUtil.notEmpty(id)) {
			Machine m = getMachineDao().select(id);
			if (StringUtil.notEmpty(map.get("pid"))) {
				Branch b = getBranchDao().select(map.get("pid"));
				m.setParent(b);
			}
			m.setIp(map.get("ip"));
			m.setName(map.get("name"));
			m.setType(map.get("type"));
			m.setLinkman(map.get("linkman"));
			m.setPhone(map.get("phone"));
			m.setMobilephone(map.get("mobilephone"));
			getMachineDao().update(m);
		} else {
			Machine m = new Machine();
			m.setId(UUID.randomUUID().toString());
			m.setName(map.get("name"));
			m.setType(map.get("type"));
			m.setIp(map.get("ip"));
			m.setLinkman(map.get("linkman"));
			m.setPhone(map.get("phone"));
			m.setMobilephone(map.get("mobilephone"));
			m.setParent(getBranchDao().select(map.get("pid")));
			getMachineDao().create(m);
		}
	}

	@Override
	public void transDeleteMachine(String uid, String[] ids) {
		getMachineDao().delete(ids);
	}

	@Override
	public Machine getMachine(NamedConditions cond) {
		return getMachineDao().select(cond);
	}

	@Override
	public Machine getObject(String id) {
		return getMachineDao().select(id);
	}

	@Override
	public List<Machine> transSelectByCond(NamedConditions cond) {
		return getMachineDao().select(0, Integer.MAX_VALUE, cond);
	}

	@Override
	public List<Machine> getAll() {
		return getMachineDao().select(0, Integer.MAX_VALUE);
	}

	@Override
	public void transSelect(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Machine> callback) {
		callback.callback(getMachineDao().count(cond),
				getMachineDao().select(start, limit, cond));
	}

	@Override
	public void transSelectById(String uid, String id,
			QueryObjectCallback<Machine> callback) {
		callback.callback(getMachineDao().select(id));
	}

	@Override
	public int getCountByC(NamedConditions c) {
		return getMachineDao().count(c);
	}

	@Override
	public void transSetCurrentTask(String uid, String id, String tid,
			String type) {
		Machine dep = getMachineDao().select(id);
		if (type.equals("STARTTASK")) {
			Task t = getTaskDao().select(tid);
			dep.setCurrentTask(t);
			getMachineDao().update(dep);
		} else if (type.equals("ENDTASK")) {
			dep.setCurrentTask(null);
			getMachineDao().update(dep);
		}
	}

	@Override
	public Map<String, String> getMachineByIp(String ip) {
		Map<String, String> map = new HashMap<String, String>();
		NamedConditions cond = new NamedConditions("byIp");
		cond.putString("Ip", ip);
		Machine m = getMachineDao().select(cond);
		if (m == null) {
			map.put("id", UUID.randomUUID().toString());
			map.put("type", "");
		} else {
			map.put("id", m.getId());
			map.put("type", m.getType().equals("广告") ? "media" : "train");
		}
		return map;
	}

}
