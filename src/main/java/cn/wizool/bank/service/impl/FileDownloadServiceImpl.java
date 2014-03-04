package cn.wizool.bank.service.impl;

import java.util.List;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.FileDownload;
import cn.wizool.bank.service.FileDownloadService;

public class FileDownloadServiceImpl extends PlatFormServiceSupport implements
		FileDownloadService {
	@Override
	public void transSelectAll(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<FileDownload> callback) {
		callback.callback(getFileDownloadDao().count(cond),
				getFileDownloadDao().select(start, limit, cond));
	}

	@Override
	public void transDeleteSelected(String uid, String[] ids) {
		getFileDownloadDao().delete(ids);
	}

	@Override
	public void transDeleteByDId(String uid, String id) {
		List<FileDownload> list = getList(id);
		for (FileDownload fd : list) {
			getFileDownloadDao().delete(fd);
		}
	}

	@Override
	public void transUpdate(String uid, String id, String fid, long size) {
		// if (getMachineDao().select(id) != null) {
		// NamedConditions cond = new NamedConditions("getObjectByCond");
		// cond.putString("DeptId", id);
		// cond.putString("FileId", fid);
		// FileDownload fd = getFileDownloadDao().select(cond);
		// if (fd == null) {
		// fd = new FileDownload();
		// fd.setId(UUID.randomUUID().toString());
		// fd.setDownSize(size);
		// fd.setDepartment(getMachineDao().select(id));
		// fd.setDocument(getDocumentDao().select(fid));
		// fd.setStart(new Date());
		// if (size == getDocumentDao().select(fid).getLength()) {
		// fd.setEnd(new Date());
		// fd.setSchedule("已完成");
		// } else {
		// fd.setSchedule("未完成");
		// }
		// getFileDownloadDao().create(fd);
		// } else {
		// fd.setDownSize(size);
		// if (size == getDocumentDao().select(fid).getLength()) {
		// fd.setEnd(new Date());
		// fd.setSchedule("已完成");
		// } else {
		// fd.setSchedule("未完成");
		// }
		// getFileDownloadDao().update(fd);
		// }
		// }
	}

	private List<FileDownload> getList(String id) {
		NamedConditions cond = new NamedConditions("getListById");
		cond.putString("DeptId", id);
		return getFileDownloadDao().select(0, Integer.MAX_VALUE, cond);
	}

	@Override
	public void transDeleteAll(String uid, NamedConditions cond) {
		List<FileDownload> list = getFileDownloadDao().select(0, Integer.MAX_VALUE, cond);
		if (list.size() > 0) {
			for (FileDownload f : list) {
				if (f != null) {
					this.getFileDownloadDao().delete(f);
				}
			}
		}
	}

}
