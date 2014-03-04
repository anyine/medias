package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.DocumentDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.Document;

public class DocumentDaoImpl extends CommonDataAccessSupport<Document>
		implements DocumentDao {

	@Override
	protected String getModelName() {
		return Document.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate("getFileShare",
				"from <model> doc where  doc.isDisplay=1 "
						+ " and doc.type not in('背景图片','选中作为背景') "
						+ " and ( doc.name like :sName or :esName = false ) "
						+ " and ( doc.type like :sType or :esType = false ) "
						+ " order by doc.uploadDate desc ");

		tmp.setTemplate("getAllDoc", "from <model> doc where  doc.isDisplay=1 ");

		tmp.setTemplate("getNotInTask",
				"from <model> doc where doc.id not in (:saDocsId)"
						+ " and ( doc.name like :sName or :esName = false ) "
						+ " and ( doc.type like :sType or :esType = false ) "
						+ " order by doc.uploadDate desc ");

		tmp.setTemplate("getInTask",
				"from <model> doc where doc.id in (:saDocsId)"
						+ " and ( doc.name like :sName or :esName = false ) "
						+ " and ( doc.type like :sType or :esType = false ) "
						+ " order by doc.uploadDate desc ");

		tmp.setTemplate("getBackGroundFile",
				"from <model> doc where doc.type in (:saTypes)"
						+ "and doc.isDisplay=1"
						+ " order by doc.uploadDate desc ");

		super.createHqlTemplates(tmp);
	}
}
