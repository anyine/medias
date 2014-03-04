package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.MachineTasksDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.MachineTasks;

public class MachineTasksDaoImpl extends CommonDataAccessSupport<MachineTasks>
		implements MachineTasksDao {

	@Override
	protected String getModelName() {
		return MachineTasks.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getByMachineIdAndTaskId",
				"from <model> mt where mt.machine.id=:sMachineId "
						+ " and mt.task.id=:sTaskId");

		tmp.setTemplate("getByTaskId",
				"from <model> mt where mt.task.id=:sTaskId");

		super.createHqlTemplates(tmp);
	}

}
