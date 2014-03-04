package cn.wizool.bank.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.DateUtil.DateFormatType;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.TaskDocuments;
import cn.wizool.bank.model.Train;
import cn.wizool.bank.service.TrainService;

public class TrainServiceImpl extends PlatFormServiceSupport implements
		TrainService {

	@Override
	public void transSelectAll(String uid, int start, int limit,
			NamedConditions con, QueryListCallback<Train> callback) {
		List<Train> list = getTrainDao().select(start, limit, con);
		callback.callback(getTrainDao().count(con), list);
	}

	@Override
	public void transCreate(String uid, Map<String, String> map,
			List<Document> list) {
		String startDateStr = map.get("startDate");
		Integer h = Integer.parseInt(map.get("h"));
		Integer m = Integer.parseInt(map.get("m"));
		Integer age = h * 3600 + m * 60;

		Train t = new Train();
		t.setId(map.get("id"));
		t.setName(map.get("name"));
		t.setPubishDate(new Date());
		t.setAge(age);
		t.setDateStr(startDateStr);
		t.setStartDate(getStartDate(startDateStr));
		t.setPublisher(getUserDao().select(uid));
		getTrainDao().create(t);

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
			td.setTask(t);
			td.setIndex(x++);
			getTaskDocumentsDao().create(td);
		}

		// 保存 MachineTasks (机器任务的第三表)
		String[] strs = map.get("machines").split(" ");
		for (String s : strs) {
			MachineTasks mt = new MachineTasks();
			mt.setId(UUID.randomUUID().toString());
			mt.setMachine(getMachineDao().select(s));
			mt.setTask(t);
			mt.setCount(0);
			mt.setDate(new Date());
			getMachineTasksDao().create(mt);
		}
	}

	@Override
	public Date getStartDate(String startDateStr) {
		return DateUtil.parse(DateUtil.getStrDate(startDateStr),
				DateFormatType.DEFAULT_TYPE);
	}

	@Override
	public void transDelete(String uid, String[] ids) {
		for (String id : ids) {
			Train t = getTrainDao().select(id);
			t.setDisplay(false);
			getTrainDao().update(t);
		}
	}

	@Override
	public void transDelete(String uid, String id) {
		Train t = getTrainDao().select(id);
		t.setDisplay(false);
		getTrainDao().update(t);
	}

	@Override
	public void transUpdate(String uid, Map<String, String> map) {
		Train t = getTrainDao().select(map.get("oldId"));
		t.setDateStr(map.get("startDate"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			t.setStartDate(sdf.parse(DateUtil.getStrDate(map.get("startDate"))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Integer hour = Integer.parseInt(map.get("h"));
		Integer minute = Integer.parseInt(map.get("m"));
		Integer age = hour * 3600 + minute * 60;
		t.setAge(age);
		t.setName(map.get("name"));
		getTrainDao().update(t);
	}

	@Override
	public void transSelectModelById(String uid, String tid,
			QueryObjectCallback<Train> callback) {
		callback.callback(getTrainDao().select(tid));
	}

	@Override
	public Map<String, String> getIds(String tid) {
		Map<String, String> map = new HashMap<String, String>();
		String docIds = "";
		String machineIds = "";

		NamedConditions cond1 = new NamedConditions("getTDByTaskId");
		cond1.putString("TaskId", tid);
		List<TaskDocuments> tds = getTaskDocumentsDao().select(0,
				Integer.MAX_VALUE, cond1);
		NamedConditions cond2 = new NamedConditions("getByTaskId");
		cond2.putString("TaskId", tid);
		List<MachineTasks> mts = getMachineTasksDao().select(0,
				Integer.MAX_VALUE, cond2);
		for (TaskDocuments td : tds) {
			docIds += td.getDocument().getId() + ",";
		}
		for (MachineTasks mt : mts) {
			machineIds += mt.getMachine().getId() + ",";
		}
		map.put("docIds", docIds);
		map.put("machineIds", machineIds);
		Train t = getTrainDao().select(tid);
		map.put("name", t.getName());
		map.put("age", t.getAge() + "");
		map.put("startDate", DateUtil.format(t.getStartDate()));
		return map;
	}

	@Override
	public List<Train> getSelectByCond() {
		NamedConditions cond = new NamedConditions("getAllByGreatStartDate");
		cond.putDate("CurrentDate", new Date());
		return getTrainDao().select(0, Integer.MAX_VALUE, cond);
	}

}
