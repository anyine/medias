package cn.wizool.bank.service;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.User;

public interface UserService extends ICommonServiceSupport {

	public abstract void transCreate(String uid, User u);

	public abstract void transDelete(String uid, String[] ids);

	public abstract void transSelectByCond(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<User> callback);

	public abstract void transSelectById(String uid, String id,
			QueryObjectCallback<User> callback);

	public abstract User getUserById(String id);

	public abstract User getUserByCond(NamedConditions cond);

}
