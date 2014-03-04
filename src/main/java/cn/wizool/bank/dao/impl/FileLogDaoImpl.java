package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.FileLogDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.FileLog;

public class FileLogDaoImpl extends CommonDataAccessSupport<FileLog> implements
		FileLogDao {

	@Override
	protected String getModelName() {
		return FileLog.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate(
				"getAll",
				"from <model> fl where fl.machine.type  in ('培训','广告') "
						+ " and ( fl.machine.ip=:sIp or :esIp=false ) "
						+ " and ( fl.machine.type=:sType or :esType=false ) "
						+ " and ( fl.operateType=:sOperateType or :esOperateType=false ) "
						+ " and ( fl.machine.parent.parent.name like :sDept or :esDept=false ) "
						+ " and ( fl.machine.parent.name like :sLocation or :esLocation=false ) "
						+ " order by fl.birth desc");

		super.createHqlTemplates(tmp);
	}

}
