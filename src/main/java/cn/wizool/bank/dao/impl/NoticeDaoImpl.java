package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.NoticeDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Notice;

public class NoticeDaoImpl extends CommonDataAccessSupport<Notice> implements
		NoticeDao {

	@Override
	protected String getModelName() {
		return Notice.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate(
				"getAll",
				"from <model> n where ( n.name like :sName or :esName=false ) "
						+ " and ( n.pubishDate>=:tStartDate or :etStartDate=false ) "
						+ " and ( n.pubishDate<=:tEndDate or :etEndDate=false ) "
						+ " and ( n.publisher.parent.name like :sPublisherName or :esPublisherName=false ) "
						+ " and ( n.important=:sImportant or :esImportant=false ) "
						+ " and ( n.isDisplay=1 ) order by n.pubishDate desc");

		tmp.setTemplate(
				"getMsgNotice",
				"from <model> n where n.isDisplay=1 "
						+ " and n.id in "
						+ " ( select mt.task.id from <MachineTasks> mt where mt.machine.id=:sMachineId "
						+ " and mt.count=0 ) ");

		tmp.setTemplate(
				"getNoticeLog",
				"from <model> n where n.isDisplay=1 "
						+ " and n.id in "
						+ " ( select mt.task.id from <MachineTasks> mt where mt.machine.id=:sMachineId "
						+ " and mt.count>0 ) ");

		tmp.setParameter("MachineTasks", MachineTasks.class.getName());
		super.createHqlTemplates(tmp);
	}
}
