package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.MachineDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.Machine;

public class MachineDaoImpl extends CommonDataAccessSupport<Machine> implements
		MachineDao {

	@Override
	protected String getModelName() {
		return Machine.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getAllMachines",
				"from <model> m order by m.parent.parent.name,m.parent.name ");

		tmp.setTemplate("getMachine",
				"from <model> m where m.parent.id=:sBranchId "
						+ " and ( m.name=:sName ) " + " and ( m.ip=:sIp ) ");

		tmp.setTemplate("getMachineList",
				"from <model> m where (m.parent.id in (:saBranchIds) or :esaBranchIds=false)"
						+ " and ( m.type=:sType or :esType=false ) "
						+ " order by m.parent.name");

		tmp.setTemplate("getMachineByPId",
				"from <model> m where m.parent.id = :sParentId"
						+ " and ( m.type = :sType or :esType = false) ");

		tmp.setTemplate("getMachinesByBranchId",
				"from <model> m where m.type in ('培训','广告') "
						+ " and  m.parent.id in (:saBranchIds) "
						+ " and ( m.type = :sType or :esType = false) ");

		tmp.setTemplate("byIp", "from <model> d where d.ip = :sIp");
		tmp.setTemplate(
				"getPc",
				"from <model> m where m.type in ('培训','广告') "
						+ " and ( m.ip=:sIp or :esIp=false ) "
						+ " and ( m.type = :sType or :esType = false ) "
						+ " and ( m.version = :sVersion or :esVersion=false ) "
						+ " and ( m.parent.id in (:saBranchIds) or :esaBranchIds=false ) "
						+ " and ( m.parent.name like :sLocation or :esLocation=false ) "
						+ " order by m.parent.parent.name,m.parent.name ");
		tmp.setTemplate(
				"getPcNotLikeVersion",
				"from <model> m where m.type in ('培训','广告') "
						+ " and ( m.ip=:sIp or :esIp=false ) "
						+ " and ( m.type = :sType or :esType = false ) "
						+ " and ( m.version != :sVersion or :esVersion=false ) "
						+ " and ( m.parent.id in (:saBranchIds) or :esaBranchIds=false ) "
						+ " and ( m.parent.name like :sLocation or :esLocation=false ) "
						+ " order by m.parent.parent.name,m.parent.name ");

		tmp.setTemplate("getListByType",
				"from <model> m where m.type in('培训','广告')");
		tmp.setTemplate(
				"getPcTask",
				"from <model> m where m.type in ('培训','广告') "
						+ " and ( m.id not in (:saMachines) or :esaMachines = false ) "
						+ " and ( m.ip=:sIp or :esIp=false ) "
						+ " and ( m.type = :sType or :esType = false ) "
						+ " and ( m.parent.parent.name like :sDept or :esDept=false ) "
						+ " and ( m.parent.name like :sLocation or :esLocation=false ) ");

		tmp.setTemplate(
				"getPcTasks",
				"from <model> m where m.type in ('培训','广告') "
						+ " and ( m.id in (:saMachines) or :esaMachines = false ) "
						+ " and ( m.ip=:sIp or :esIp=false ) "
						+ " and ( m.type = :sType or :esType = false ) "
						+ " and ( m.parent.parent.name like :sDept or :esDept=false ) "
						+ " and ( m.parent.name like :sLocation or :esLocation=false ) ");
		super.createHqlTemplates(tmp);
	}

}
