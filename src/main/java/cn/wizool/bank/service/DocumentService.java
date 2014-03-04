package cn.wizool.bank.service;

import java.util.List;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;

public interface DocumentService extends ICommonServiceSupport {

	public abstract void transCreate(String uid, Document d);

	public abstract void transDelete(String uid, String[] ids, String path);

	public abstract void transUpdate(String uid, Document d);

	public abstract void transSelectAll(String uid, int start, int limit,
			NamedConditions conditions, QueryListCallback<Document> callback);

	public abstract void transSelectModel(String uid, String id,
			QueryObjectCallback<Document> callback);

	public abstract Integer selectAllCount();

	public abstract String getCountByTaskId(String taskId);

	public abstract String getCount(String taskId);

	public abstract List<Document> getListAll();

	public abstract List<Document> getListAllByCond(NamedConditions cond);

	public abstract Document getDocById(String id);

	/**
	 * 不在当前任务中的文件
	 * 
	 * @param start
	 * @param limit
	 * @param cond
	 * @param callback
	 */
	public void transNotInTaskDocSelect(int start, int limit,
			NamedConditions cond, QueryListCallback<Document> callback);

	/**
	 * 当前任务中的文件
	 * 
	 * @param start
	 * @param limit
	 * @param cond
	 * @param callback
	 */
	public void transInTaskDocSelect(int start, int limit,
			NamedConditions cond, QueryListCallback<Document> callback);

	public List<Document> getListAlls();

	/**
	 * 获得全部背景图片
	 * 
	 * @param start
	 * @param limit
	 * @param cond
	 * @param callback
	 */
	public void getBackGroundFile(int start, int limit, NamedConditions cond,
			QueryListCallback<Document> callback);

	/**
	 * 选中作为背景图片
	 * 
	 * @param id
	 */
	public void checkForBack(String id);

	/**
	 * 通过条件获得doc
	 * 
	 * @param cd
	 * @return
	 */
	public Document getDocByCondition(NamedConditions cd);

	/**
	 * 删除背景图片
	 */
	public void transBackDel(String uid, String[] ids, String path);
}
