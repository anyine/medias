package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.ConfigDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.Config;

public class ConfigDaoImpl extends CommonDataAccessSupport<Config> implements
		ConfigDao {

	@Override
	protected String getModelName() {
		return Config.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getAll", "from <model> ir order by name");

		super.createHqlTemplates(tmp);
	}

}
