package cn.wizool.bank.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Notice;
import cn.wizool.bank.model.TaskDocuments;
import cn.wizool.bank.service.NoticeService;

public class NoticeServiceImpl extends PlatFormServiceSupport implements
		NoticeService {

	@Override
	public void transSelectAll(String uid, int start, int limit,
			NamedConditions con, QueryListCallback<Notice> callback) {
		List<Notice> list = getNoticeDao().select(start, limit, con);
		callback.callback(getNoticeDao().count(con), list);
	}

	@Override
	public void transCreate(String uid, Map<String, String> map,
			List<Document> list) {
		Notice n = new Notice();
		n.setId(UUID.randomUUID().toString());
		n.setName(map.get("name"));
		n.setImportant(map.get("important"));
		n.setPubishDate(new Date());
		n.setPublisher(getUserDao().select(uid));
		getNoticeDao().create(n);

		// 传过来的docIds与session中的文件列表相对比，然后保存 TaskDocuments (任务文件的第三表)
		String docIds = map.get("docIds");
		String[] strArr = docIds.substring(0, docIds.length() - 1).split(",");
		Map<String, Document> maps = new HashMap<String, Document>();
		for (Document doc : list) {
			maps.put(doc.getId(), doc);
		}
		int x = 1;
		for (String s : strArr) {
			TaskDocuments td = new TaskDocuments();
			td.setId(UUID.randomUUID().toString());
			td.setDocument(maps.get(s));
			td.setTask(n);
			td.setIndex(x++);
			getTaskDocumentsDao().create(td);
		}

		// 保存 MachineTasks (机器任务的第三表)
		for (String s : map.get("machines").split(" ")) {
			MachineTasks mt = new MachineTasks();
			mt.setId(UUID.randomUUID().toString());
			mt.setMachine(getMachineDao().select(s));
			mt.setTask(n);
			mt.setCount(0);
			mt.setDate(new Date());
			getMachineTasksDao().create(mt);
		}
	}

	@Override
	public void transDelete(String uid, String[] ids) {
		for (String id : ids) {
			Notice n = getNoticeDao().select(id);
			n.setDisplay(false);
			getNoticeDao().update(n);
		}
	}

	@Override
	public void transSelectModelById(String uid, String tid,
			QueryObjectCallback<Notice> callback) {
		callback.callback(getNoticeDao().select(tid));
	}

}
