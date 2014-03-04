package cn.wizool.bank.service;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Config;

public interface ConfigService  extends ICommonServiceSupport {

	public abstract void transCreate(String uid, Config cf);

	public abstract int getListCount();

	public abstract void transSelectAll(String uid, NamedConditions c,
			QueryListCallback<Config> callback);

	public abstract Config selectById(String id);

	public abstract void transUpdate(String uid, Config c);

}
