package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.TaskDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.Task;

public class TaskDaoImpl extends CommonDataAccessSupport<Task> implements
		TaskDao {

	@Override
	protected String getModelName() {
		return Task.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getTaskByDepId",
				"from <model> t where t.person.id=:sDepartmentId and t.type=:sType");

		tmp.setTemplate("getAllTask", "from <model> t");

		super.createHqlTemplates(tmp);
	}

}
