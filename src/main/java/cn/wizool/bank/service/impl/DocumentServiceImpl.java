package cn.wizool.bank.service.impl;

import java.io.File;
import java.util.List;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Task;
import cn.wizool.bank.service.DocumentService;

public class DocumentServiceImpl extends PlatFormServiceSupport implements
		DocumentService {

	@Override
	public void transCreate(String uid, Document d) {
		getDocumentDao().create(d);
	}

	@Override
	public void transDelete(String uid, String[] ids, String path) {
		for (String id : ids) {
			Document doc = getDocumentDao().select(id);
			doc.setDisplay(false);
			File f = new File(path + File.separator + doc.getId());
			f.delete();
			getDocumentDao().update(doc);
		}
	}

	@Override
	public void transUpdate(String uid, Document d) {
		getDocumentDao().update(d);
	}

	@Override
	public void transSelectAll(String uid, int start, int limit,
			NamedConditions conditions, QueryListCallback<Document> callback) {
		int total = getDocumentDao().count(conditions);
		List<Document> list = getDocumentDao().select(start, limit, conditions);
		callback.callback(total, list);
	}

	@Override
	public void transSelectModel(String uid, String id,
			QueryObjectCallback<Document> callback) {
		callback.callback(getDocumentDao().select(id));
	}

	@Override
	public String getCountByTaskId(String taskId) {
		NamedConditions cond = new NamedConditions("getDocByTaskId");
		cond.putString("TaskId", taskId);
		int count = getDocumentDao().count(cond);
		return String.format("%1$03d", count + 1);
	}

	@Override
	public Integer selectAllCount() {
		NamedConditions cond = new NamedConditions("getAllDoc");
		getDocumentDao().count(cond);
		return getDocumentDao().count(cond);
	}

	@Override
	public String getCount(String taskId) {
		Task t = getTaskDao().select(taskId);
		int count = 0;
		if (t != null) {
			// count = t.getDocuments().size();
		}
		return String.format("%1$03d", count + 1);
	}

	@Override
	public List<Document> getListAll() {
		return getDocumentDao().select(0, Integer.MAX_VALUE);
	}

	@Override
	public List<Document> getListAllByCond(NamedConditions cond) {
		return getDocumentDao().select(0, Integer.MAX_VALUE, cond);
	}

	@Override
	public Document getDocById(String id) {
		return getDocumentDao().select(id);
	}

	/**
	 * 不在任务中的文件
	 */
	@Override
	public void transNotInTaskDocSelect(int start, int limit,
			NamedConditions cond, QueryListCallback<Document> callback) {
		List<Document> docList = this.getDocumentDao().select(start, limit,
				cond);
		callback.callback(this.getDocumentDao().count(cond), docList);
	}

	/**
	 * 当前任务中的文件
	 */
	@Override
	public void transInTaskDocSelect(int start, int limit,
			NamedConditions cond, QueryListCallback<Document> callback) {
		List<Document> docList = this.getDocumentDao().select(start, limit,
				cond);
		callback.callback(this.getDocumentDao().count(cond), docList);
	}

	@Override
	public List<Document> getListAlls() {
		NamedConditions cond = new NamedConditions("getAllDoc");
		return getDocumentDao().select(0, Integer.MAX_VALUE, cond);
	}

	@Override
	public void getBackGroundFile(int start, int limit, NamedConditions cond,
			QueryListCallback<Document> callback) {
		List<Document> docList = this.getDocumentDao().select(start, limit,
				cond);
		callback.callback(this.getDocumentDao().count(cond), docList);
	}

	@Override
	public void checkForBack(String id) {
		String[] backCd = { "选中作为背景" };
		NamedConditions cd = new NamedConditions();
		cd.setType("getBackGroundFile");
		cd.putStringArray("Types", backCd);
		Document checkedDoc = this.getDocumentDao().select(cd);
		if (checkedDoc != null) {
			checkedDoc.setType("背景图片");
			this.getDocumentDao().update(checkedDoc);
		}
		Document document = this.getDocumentDao().select(id);
		document.setType("选中作为背景");
		this.getDocumentDao().update(document);

	}

	@Override
	public Document getDocByCondition(NamedConditions cd) {
		return this.getDocumentDao().select(cd);
	}

	@Override
	public void transBackDel(String uid, String[] ids, String path) {
		for (String id : ids) {
			Document doc = getDocumentDao().select(id);
			doc.setDisplay(false);
			File f = new File(path + File.separator + doc.getId());
			f.delete();
			getDocumentDao().delete(doc);
		}
	}

}
