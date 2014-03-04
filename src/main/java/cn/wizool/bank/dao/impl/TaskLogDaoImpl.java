package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.TaskLogDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.TaskLog;

public class TaskLogDaoImpl extends CommonDataAccessSupport<TaskLog> implements
		TaskLogDao {

	@Override
	protected String getModelName() {
		return TaskLog.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate(
				"getNoticeLog",
				"from <model> tl where tl.operateType='下发任务' "
						+ " and ( tl.machine.id=:sDepartmentId or :esDepartmentId=false ) "
						+ " and ( tl.task.isDisplay=1 ) order by tl.birth desc");

		tmp.setTemplate(
				"getAll",
				"from <model> tl where tl.machine.type in ('培训','广告') "
						+ " and ( tl.machine.ip=:sIp or :esIp=false ) "
						+ " and ( tl.machine.type = :sType or :esType = false ) "
						+ " and ( tl.operateType = :sOperateType or :esOperateType = false ) "
						+ " and ( tl.machine.parent.parent.name like :sDept or :esDept=false ) "
						+ " and ( tl.machine.parent.name like :sLocation or :esLocation=false ) "
						+ " and ( tl.task.isDisplay=1 ) order by tl.birth desc");

		tmp.setTemplate(
				"getMsgNotice",
				"from <model> tl where tl.operateType='下发任务' "
						+ " and ( tl.machine.id=:sDepartmentId or :esDepartmentId=false ) "
						+ " and (tl.task.id not in "
						+ " ( select tl1.task.id from <model> tl1 where tl1.operateType='开始任务' "
						+ " and ( tl1.machine.id=:sDepartmentId or :esDepartmentId=false ) ) ) "
						+ " and ( tl.task.isDisplay=1 ) order by tl.birth desc");

		super.createHqlTemplates(tmp);
	}
	// + " and ( tl.task.name like :sCurrentTask or :esCurrentTask=false ) "
}
