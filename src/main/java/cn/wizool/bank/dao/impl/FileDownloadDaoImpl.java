package cn.wizool.bank.dao.impl;

import cn.wizool.bank.dao.FileDownloadDao;
import cn.wizool.bank.iwebutil.newlay.CommonDataAccessSupport;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.model.FileDownload;

public class FileDownloadDaoImpl extends CommonDataAccessSupport<FileDownload>
		implements FileDownloadDao {

	@Override
	protected String getModelName() {
		return FileDownload.class.getName();
	}

	@Override
	protected void createHqlTemplates(ParameterString tmp) {
		tmp.setTemplate(
				"getAll",
				"from <model> fd where  fd.department.type in ('培训','广告') "
						+ " and ( fd.department.type = :sType or :esType = false ) "
						+ " and ( fd.department.parent.name like :sLocation or :esLocation=false ) "
						+ " and ( fd.department.parent.parent.name like :sDept or :esDept=false ) "
						+ " and ( fd.document.name like :sDocName or :esDocName=false ) "
						+ " order by fd.start desc");

		tmp.setTemplate("getObjectByCond",
				"from <model> fd where ( fd.department.id=:sDeptId or :esDeptId=false ) "
						+ " and ( fd.document.id=:sFileId or :esFileId=false )");

		tmp.setTemplate("getListById",
				"from <model> fd where fd.department.id=:sDeptId and fd.end is null");
		super.createHqlTemplates(tmp);
	}

}
