package cn.wizool.bank.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Task;
import cn.wizool.bank.service.TaskService;

public class TaskServiceImpl extends PlatFormServiceSupport implements
		TaskService {

	@Override
	public void transCreate(String uid, Map<String, String> map) {
		// Task t = null;
		// String id = map.get("id");
		// List<Document> docList = getDocList(id);
		// Department doc = getDepartmentDao().select(map.get("depId"));
		// List<Task> tList = getTaskList(map.get("depId"));
		//
		// for (Task task : tList) {
		// if (task.getId().equals(id)) {
		// t = task;
		// break;
		// }
		// }
		// if (t == null) {
		// t = new Task();
		// t.setId(id);
		// t.setName(map.get("name"));
		// t.setType(map.get("type"));
		// t.setDocType(map.get("docType"));
		// t.setImportant(map.get("important"));
		// t.setAge(Integer.parseInt(map.get("age")));
		// t.setPerson(doc);
		// t.setDocuments(docList);
		// getTaskDao().create(t);
		// } else {
		// t.setName(map.get("name"));
		// t.setType(map.get("type"));
		// t.setDocType(map.get("docType"));
		// t.setImportant(map.get("important"));
		// t.setAge(Integer.parseInt(map.get("age")));
		// t.setPerson(doc);
		// t.setDocuments(docList);
		// getTaskDao().update(t);
		// }
	}

	// private List<Task> getTaskList(String departmentId) {
	// NamedConditions conditions = new NamedConditions("getTaskByDepId");
	// conditions.putString("DepartmentId", departmentId);
	// return getTaskDao().select(0, 1000000, conditions);
	// }
	//
	// private List<Document> getDocList(String id) {
	// NamedConditions conditions = new NamedConditions("getDocByTaskId");
	// conditions.putString("TaskId", id);
	// return getDocumentDao().select(0, 1000, conditions);
	// }

	@Override
	public void transDelete(String uid, String[] ids, String url) {
		for (String id : ids) {
			File f = new File(url + File.separator + id);
			if (f.exists()) {
				deleteFile(f);
			}
			getTaskDao().delete(id);
		}
	}

	public void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
			f.delete();
		} else {
			f.delete();
		}
	}

	@Override
	public void transUpdate(String uid, Task t) {
		getTaskDao().update(t);
	}

	@Override
	public void transSelectAll(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Task> callback) {
		int total = getTaskDao().count(cond);
		List<Task> list = getTaskDao().select(start, limit, cond);
		callback.callback(total, list);
	}

	@Override
	public void transUpdate(String uid, String id, String webUrl)
			throws IOException {
		//
		// Task t = getTaskDao().select(id);
		// System.out.println("taskId = " + id);
		// List<Document> list = t.getDocuments();
		// System.out.println("t Name  = " + t.getName() + "  , size = "
		// + list.size());
		// Integer num = list.size();
		// String filenames = "";
		// for (Document d : list) {
		// String arr[] = d.getUrl().trim().split("\\\\");
		// filenames += arr[arr.length - 1] + ";";
		// }
		// String type = t.getDocType();
		// String endType = null;
		// if (type.equals("图片")) {
		// endType = "picture";
		// } else if (type.equals("视频")) {
		// endType = "video";
		// } else if (type.equals("Word文档")) {
		// endType = "word";
		// } else if (type.equals("Excel表格")) {
		// endType = "excel";
		// } else if (type.equals("PDF文档")) {
		// endType = "pdf";
		// } else if (type.equals("幻灯片")) {
		// endType = "ppt";
		// } else if (type.equals("GD文档")) {
		// endType = "gd";
		// }
		//
		// Properties p = new Properties();
		// p.setProperty("type", endType);
		// p.setProperty("filename", filenames);
		// p.setProperty("sum", num.toString());
		// IniUtil ini = new IniUtil(p);
		// ini.write(webUrl + File.separator + id + File.separator +
		// "desc.ini");
		//
		// FileToZip.compressionFileAndDelete(webUrl + File.separator + id
		// + File.separator + "task", webUrl + File.separator + id
		// + File.separator + "task.zip");
	}

	@Override
	public void transSelect(String uid, String id,
			QueryObjectCallback<Task> callback) {
		callback.callback(getTaskDao().select(id));
	}

	@Override
	public Task transSelectById(String id) {
		return getTaskDao().select(id);
	}

	@Override
	public void getAll() {
		NamedConditions cond = new NamedConditions("getAllTask");
		List<Task> list = getTaskDao().select(0, Integer.MAX_VALUE, cond);
		for (Task t : list) {
			MachineTasks m = new MachineTasks();
			m.setId(UUID.randomUUID().toString());
			m.setCount(0);
			m.setDate(new Date());
			m.setMachine(getMachineDao().select(
					"067c43da-8b28-44c8-beda-be623b1120b9"));
			m.setTask(t);
			getMachineTasksDao().create(m);
		}
	}
}
