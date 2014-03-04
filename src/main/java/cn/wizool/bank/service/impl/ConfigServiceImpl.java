package cn.wizool.bank.service.impl;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Config;
import cn.wizool.bank.service.ConfigService;

public class ConfigServiceImpl extends PlatFormServiceSupport implements
		ConfigService {

	@Override
	public void transCreate(String uid, Config cf) {
		getConfigDao().create(cf);
	}

	@Override
	public int getListCount() {
		return getConfigDao().count();
	}

	@Override
	public void transSelectAll(String uid, NamedConditions c,
			QueryListCallback<Config> callback) {
		int total = getConfigDao().count(c);
		callback.callback(total, getConfigDao().select(0, Integer.MAX_VALUE,c));
	}

	@Override
	public Config selectById(String id) {
		return getConfigDao().select(id);
	}

	@Override
	public void transUpdate(String uid, Config c) {
		getConfigDao().update(c);
	}

}
