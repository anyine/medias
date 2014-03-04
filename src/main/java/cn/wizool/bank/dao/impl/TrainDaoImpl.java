package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.TrainDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Train;

public class TrainDaoImpl extends CommonDataAccessSupport<Train> implements
		TrainDao {

	@Override
	protected String getModelName() {
		return Train.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate(
				"getAll",
				"from <model> t where ( t.name like :sName or :esName=false ) "
						+ " and ( t.pubishDate>=:tStartDate or :etStartDate=false ) "
						+ " and ( t.pubishDate<=:tEndDate or :etEndDate=false ) "
						+ " and ( t.publisher.parent.name like :sPublisherName or :esPublisherName=false ) "
						+ " and ( t.startDate>=:tBeginDate or :etBeginDate=false ) "
						+ " and ( t.startDate<=:tOverDate or :etOverDate=false ) "
						+ " and ( t.isDisplay=1 ) order by t.pubishDate desc");

		tmp.setTemplate("getAllByGreatStartDate",
				"from <model> t where t.startDate >= :tCurrentDate");

		tmp.setTemplate(
				"getTrainLog",
				"from <model> t where t.isDisplay=1 "
						+ " and t.id in "
						+ " ( select mt.task.id from <MachineTasks> mt where mt.machine.id=:sMachineId ) ");

		tmp.setParameter("MachineTasks", MachineTasks.class.getName());

		super.createHqlTemplates(tmp);
	}

}
