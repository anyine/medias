package cn.wizool.bank.service;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.FileDownload;

public interface FileDownloadService extends ICommonServiceSupport {

	public abstract void transSelectAll(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<FileDownload> callback);

	public abstract void transDeleteSelected(String uid, String[] ids);

	public abstract void transDeleteByDId(String uid, String id);

	public abstract void transUpdate(String uid, String id, String fid,
			long size);

	public abstract void transDeleteAll(String uid, NamedConditions cond);
}
