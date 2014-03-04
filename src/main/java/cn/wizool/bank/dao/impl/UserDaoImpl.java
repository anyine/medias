package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.UserDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.User;

public class UserDaoImpl extends CommonDataAccessSupport<User> implements
		UserDao {

	@Override
	protected String getModelName() {
		return User.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("login",
				"from <model> u where u.name=:sName and  u.password=:sPassword");

		tmp.setTemplate("getUsersByParentId",
				"from <model> u where u.parent.id in (:saBranchIds)");

		tmp.setTemplate("getUserByName", "from <model> u where u.name = :sName");
		super.createHqlTemplates(tmp);
	}

}
