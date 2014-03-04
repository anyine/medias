package cn.wizool.bank.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.FileLog;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.service.FileLogService;

public class FileLogServiceImpl extends PlatFormServiceSupport implements
		FileLogService {

	@Override
	public void transCreate(String uid, String did, String id, String type) {
		String str = "";
		// Department d = getDepartmentDao().select(id);
		Machine d = getMachineDao().select(id);
		Document doc = getDocumentDao().select(did);
		FileLog fl = new FileLog();
		fl.setId(UUID.randomUUID().toString());
		fl.setDepartment(d);
		fl.setDoc(doc);
		if (type.equals("STARTFILE")) {
			str = "下载开始";
		} else if (type.equals("ENDFILE")) {
			str = "下载结束";
		} else if (type.equals("REMOVEFILE")) {
			str = "删除文件";
		}
		fl.setOperateType(str);
		fl.setBirth(new Date());
		getFileLogDao().create(fl);
	}

	@Override
	public void transDeleteSelected(String uid, String[] ids) {
		getFileLogDao().delete(ids);
	}

	@Override
	public void transDeleteAll(String uid, NamedConditions cond) {
		List<FileLog> list = getFileLogDao().select(0, Integer.MAX_VALUE, cond);
		for (FileLog f : list) {
			if (f != null) {
				getFileLogDao().delete(f);
			}
		}
	}

	@Override
	public void transSelectByCond(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<FileLog> callback) {
		int total = getFileLogDao().count(cond);
		callback.callback(total, getFileLogDao().select(start, limit, cond));
	}

}
