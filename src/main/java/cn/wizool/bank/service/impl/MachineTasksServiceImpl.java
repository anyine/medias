package cn.wizool.bank.service.impl;

import java.util.List;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.service.MachineTasksService;

public class MachineTasksServiceImpl extends PlatFormServiceSupport implements
		MachineTasksService {

	@Override
	public void transSelectByCond(String uid, NamedConditions cond,
			QueryListCallback<MachineTasks> callback) {
		callback.callback(getMachineTasksDao().count(cond),
				getMachineTasksDao().select(0, Integer.MAX_VALUE, cond));
	}

	@Override
	public String[] getMachinesByTaskId(String tid) {
		NamedConditions cond = new NamedConditions("getByTaskId");
		cond.putString("TaskId", tid);
		List<MachineTasks> list = getMachineTasksDao().select(0,
				Integer.MAX_VALUE, cond);
		String[] machines = new String[list.size()];
		int i = 0;
		for (MachineTasks mt : list) {
			if (mt == null) {
				System.out.println(mt);
			} else if (mt.getMachine() == null) {
				System.out.println(mt.getId());
			} else {
				machines[i++] = mt.getMachine().getId();
			}
		}
		return machines;
	}

}
