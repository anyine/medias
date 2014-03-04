package cn.wizool.bank.service.impl;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.User;
import cn.wizool.bank.service.UserService;

public class UserServiceImpl extends PlatFormServiceSupport implements
		UserService {

	@Override
	public void transCreate(String uid, User u) {
		getUserDao().create(u);
	}

	@Override
	public void transDelete(String uid, String[] ids) {
		getUserDao().delete(ids);
	}

	@Override
	public void transSelectByCond(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<User> callback) {
		callback.callback(getUserDao().count(cond),
				getUserDao().select(start, limit, cond));
	}

	@Override
	public void transSelectById(String uid, String id,
			QueryObjectCallback<User> callback) {
		callback.callback(getUserDao().select(id));
	}

	@Override
	public User getUserById(String id) {
		return getUserDao().select(id);
	}

	@Override
	public User getUserByCond(NamedConditions cond) {
		return getUserDao().select(cond);
	}

}
