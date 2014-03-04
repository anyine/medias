package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.BranchDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.Branch;

public class BranchDaoImpl extends CommonDataAccessSupport<Branch> implements
		BranchDao {

	@Override
	protected String getModelName() {
		return Branch.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getBranchByName", "from <model> b where b.name=:sName");
		tmp.setTemplate("getChildIdArrByBranchId",
				"from <model> b where b.parent.id=:sParentId");

		tmp.setTemplate("byParent",
				"from <model> b where b.name = :sName and b.parent.id=:sParentId");
		tmp.setTemplate("getChilds", "from <model> d");
		super.createHqlTemplates(tmp);
	}

}
