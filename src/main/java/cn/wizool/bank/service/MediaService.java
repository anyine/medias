package cn.wizool.bank.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.CommonTask;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Media;
import cn.wizool.bank.model.TaskDocuments;
import cn.wizool.bank.model.User;

public interface MediaService extends ICommonServiceSupport {

	public abstract void transSelectAll(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Media> callback);

	public abstract void transCreateKillPower(String uid, String dept,
			String dispatch, String enabled, String kpId);

	public abstract void transSetList(String docIds, HttpSession session,
			String type);

	public abstract void transUpdate(String uid, Map<String, String> map);

	public abstract void transDelete(String uid, String[] ids);

	public abstract void transDelete(String uid, String id);

	public abstract Document getDocById(String id);

	public abstract void transSelectTDByCond(String uid, NamedConditions cond,
			QueryListCallback<TaskDocuments> callback);

	public abstract CommonTask selectTaskById(String id);

	public abstract void transSelectModelById(String uid, String tid,
			QueryObjectCallback<Media> callback);

	public abstract Media getMediaById(String mid);

	public abstract Map<String, String> getById(String tid, String type);

	public abstract List<Media> getSelectByCond();

	public abstract void transCreateMedia(User u, Map<String, String> map,
			List<Document> list);

	public abstract String getDispatch(Map<String, String> map);

}
