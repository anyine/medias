package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.TaskDocumentsDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.TaskDocuments;

public class TaskDocumentsDaoImpl extends
		CommonDataAccessSupport<TaskDocuments> implements TaskDocumentsDao {

	@Override
	protected String getModelName() {
		return TaskDocuments.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getTDByTaskId",
				"from <model> td where td.task.id = :sTaskId order by td.index");
		super.createHqlTemplates(tmp);
	}

}
