package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.CommonTaskDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.model.CommonTask;

public class CommonTaskDaoImpl extends CommonDataAccessSupport<CommonTask>
		implements CommonTaskDao {

	@Override
	protected String getModelName() {
		return CommonTask.class.getName();
	}

}
