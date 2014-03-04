package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.MediaDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.Media;

public class MediaDaoImpl extends CommonDataAccessSupport<Media> implements
		MediaDao {

	@Override
	protected String getModelName() {
		return Media.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate(
				"getAll",
				"from <model> m where ( m.name like :sName or :esName=false ) "
						+ " and ( m.pubishDate>=:tStartDate or :etStartDate=false ) "
						+ " and ( m.pubishDate<=:tEndDate or :etEndDate=false ) "
						+ " and ( m.enabled = :bEnabled or :ebEnabled=false ) "
						+ " and ( m.publisher.parent.name like :sPublisherName or :esPublisherName=false ) "
						+ " and ( m.isDisplay=1 ) order by m.pubishDate desc");
		tmp.setTemplate("getAllByDispatchAndEnabled",
				"from <model> m where ( m.dispatch is not null and m.dispatch != '' ) "
						+ " and m.enabled = 1");

		super.createHqlTemplates(tmp);
	}

}
