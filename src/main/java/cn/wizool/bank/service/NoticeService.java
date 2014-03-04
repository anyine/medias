package cn.wizool.bank.service;

import java.util.List;
import java.util.Map;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Notice;

public interface NoticeService extends ICommonServiceSupport {

	public abstract void transSelectAll(String uid, int start, int limit,
			NamedConditions con, QueryListCallback<Notice> callback);

	public abstract void transCreate(String uid, Map<String, String> map,
			List<Document> list);

	public abstract void transDelete(String uid, String[] ids);

	public abstract void transSelectModelById(String uid, String tid,
			QueryObjectCallback<Notice> callback);
}
