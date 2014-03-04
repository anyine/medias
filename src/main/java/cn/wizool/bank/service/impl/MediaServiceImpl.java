package cn.wizool.bank.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.quartz.SchedulerException;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.CommonTask;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Media;
import cn.wizool.bank.model.TaskDocuments;
import cn.wizool.bank.model.User;
import cn.wizool.bank.service.MediaService;
import cn.wizool.bank.servlet.InterfaceServlet;

public class MediaServiceImpl extends PlatFormServiceSupport implements
		MediaService {

	@Override
	public void transSelectAll(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Media> callback) {
		List<Media> list = getMediaDao().select(start, limit, cond);
		callback.callback(getMediaDao().count(cond), list);
	}

	@Override
	public void transCreateKillPower(String uid, String dept, String dispatch,
			String enabled, String kpId) {
		// String[] strs = dept.split(" ");
		// Media m = new Media();
		// if (getMediaDao().select(kpId) == null) {
		// m.setId(kpId);
		// m.setAge(0);
		// m.setDispatch(dispatch);
		// m.setName("关机调度任务");
		// m.setPubishDate(new Date());
		// // Department publisher = getDepartmentDao().select(uid);
		// m.setPublisher(getUserDao().select(uid));
		// if (enabled.equals("true")) {
		// m.setEnabled(true);
		// } else if (enabled.equals("false")) {
		// m.setEnabled(false);
		// }
		// for (String s : strs) {
		// // Department d = getDepartmentDao().select(s);
		// Machine d = getMachineDao().select(s);
		// m.getDepartments().add(d);
		// }
		// getMediaDao().create(m);
		// } else {
		// m = getMediaDao().select(kpId);
		// m.setDispatch(dispatch);
		// if (enabled.equals("true")) {
		// m.setEnabled(true);
		// } else if (enabled.equals("false")) {
		// m.setEnabled(false);
		// }
		// for (String s : strs) {
		// // Department d = getDepartmentDao().select(s);
		// Machine d = getMachineDao().select(s);
		// m.getDepartments().add(d);
		// }
		// getMediaDao().update(m);
		// }
	}

	@Override
	public void transCreateMedia(User u, Map<String, String> map,
			List<Document> list) {
		Integer h = Integer.parseInt(map.get("h"));
		Integer mi = Integer.parseInt(map.get("m"));
		Integer age = h * 3600 + mi * 60;

		String dispatch = getDispatch(map);
		Boolean enabled = true;
		if (StringUtil.notEmpty(map.get("enabled"))) {
			if (map.get("enabled").equals("关")) {
				enabled = false;
			}
		} else {
			if (!(StringUtil.notEmpty(dispatch))) {
				enabled = false;
			}
		}

		// 保存任务
		Media m = new Media();
		m.setId(map.get("id"));
		m.setName(map.get("name"));
		m.setPubishDate(new Date());
		m.setBeginDate(map.get("startDate"));
		m.setEndDate(map.get("endDate"));
		m.setHour(map.get("hour"));
		m.setMinute(map.get("minute"));
		m.setPublisher(u);
		m.setAge(age);
		m.setDispatch(dispatch);
		m.setEnabled(enabled);
		getMediaDao().create(m);

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
			td.setTask(m);
			td.setIndex(x++);
			getTaskDocumentsDao().create(td);
		}

		// 保存 MachineTasks (机器任务的第三表)
		String machines = map.get("machines");
		String[] machineArr = machines.substring(0, machines.length() - 1)
				.split(" ");
		for (String s : machineArr) {
			Machine machine = getMachineDao().select(s);
			if (machine == null) {
				System.out.println(s);
			} else {
				MachineTasks mt = new MachineTasks();
				mt.setId(UUID.randomUUID().toString());
				mt.setMachine(getMachineDao().select(s));
				mt.setTask(m);
				mt.setCount(0);
				mt.setDate(new Date());
				getMachineTasksDao().create(mt);
			}
		}
	}

	@Override
	public String getDispatch(Map<String, String> map) {
		String dispatch = "";
		String startDate = map.get("startDate");
		String endDate = map.get("endDate");
		String hour = map.get("hour");
		String minute = map.get("minute");
		if (StringUtil.notEmpty(startDate) && StringUtil.notEmpty(hour)
				&& StringUtil.notEmpty(minute)) {
			// 含有调度的任务
			String year1 = startDate.substring(0, 4);
			String month1 = Integer.parseInt(startDate.substring(5, 7)) + "";
			String day1 = Integer.parseInt(startDate.substring(8, 10)) + "";
			if (StringUtil.notEmpty(endDate)) {
				String year2 = endDate.substring(0, 4);
				String month2 = Integer.parseInt(endDate.substring(5, 7)) + "";
				String day2 = Integer.parseInt(endDate.substring(8, 10)) + "";
				if (year2.equals(year1) && month2.equals(month1)) {
					dispatch = "0 " + minute + " " + hour + " " + day1 + "-"
							+ day2 + " " + month1 + " ? " + year1;
				}
			} else {
				dispatch = "0 " + minute + " " + hour + " " + day1 + " "
						+ month1 + " ? " + year1;
			}
		}
		return dispatch;
	}

	@Override
	public void transDelete(String uid, String[] ids) {
		for (String id : ids) {
			Media m = getMediaDao().select(id);
			m.setDisplay(false);
			if (StringUtil.notEmpty(m.getDispatch())) {
				try {
					InterfaceServlet.getScheduler().unschedule("S:" + id);
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
			if (m.isEnabled()) {
				m.setEnabled(false);
			}
			m.setDispatch(null);
			getMediaDao().update(m);
		}
	}

	@Override
	public void transDelete(String uid, String id) {
		Media m = getMediaDao().select(id);
		m.setDisplay(false);
		if (StringUtil.notEmpty(m.getDispatch())) {
			try {
				InterfaceServlet.getScheduler().unschedule("S:" + id);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		if (m.isEnabled()) {
			m.setEnabled(false);
		}
		m.setDispatch(null);
		getMediaDao().update(m);
	}

	@Override
	public void transSetList(String docIds, HttpSession session, String type) {
		String strs = docIds.substring(0, docIds.length() - 1);
		String[] strArr = strs.split(",");
		List<Document> list = null;
		if (type.equals("media")) {
			list = (List<Document>) session.getAttribute("listMedia");
		} else if (type.equals("notice")) {
			list = (List<Document>) session.getAttribute("listNotice");
		} else if (type.equals("train")) {
			list = (List<Document>) session.getAttribute("listTrain");
		}
		if (list == null) {
			list = new ArrayList<Document>();
			for (String s : strArr) {
				Document d = getDocumentDao().select(s);
				list.add(d);
			}
			if (type.equals("media")) {
				session.setAttribute("listMedia", list);
			} else if (type.equals("notice")) {
				session.setAttribute("listNotice", list);
			} else if (type.equals("train")) {
				session.setAttribute("listTrain", list);
			}
		} else {
			for (String s : strArr) {
				Document d = getDocumentDao().select(s);
				list.add(d);
			}
		}
	}

	@Override
	public void transUpdate(String uid, Map<String, String> map) {
		Media m = getMediaDao().select(map.get("oldId"));
		if (StringUtil.notEmpty(map.get("dispatch"))) {
			m.setDispatch(map.get("dispatch"));
			if (map.get("enabled").equals("开")) {
				m.setEnabled(true);
			} else if (map.get("enabled").equals("关")) {
				m.setEnabled(false);
			}
		} else {
			m.setDispatch("");
			m.setEnabled(false);
		}
		Integer hour = Integer.parseInt(map.get("h"));
		Integer minute = Integer.parseInt(map.get("m"));
		Integer age = hour * 3600 + minute * 60;
		m.setAge(age);
		m.setBeginDate(map.get("startDate"));
		m.setEndDate(map.get("endDate"));
		m.setHour(map.get("hour"));
		m.setMinute(map.get("minute"));
		m.setName(map.get("name"));
		getMediaDao().update(m);
	}

	@Override
	public Document getDocById(String id) {
		return getDocumentDao().select(id);
	}

	@Override
	public void transSelectTDByCond(String uid, NamedConditions cond,
			QueryListCallback<TaskDocuments> callback) {
		List<TaskDocuments> list = getTaskDocumentsDao().select(0,
				Integer.MAX_VALUE, cond);
		callback.callback(getTaskDocumentsDao().count(cond), list);
	}

	@Override
	public CommonTask selectTaskById(String id) {
		return getCommonTaskDao().select(id);
	}

	@Override
	public void transSelectModelById(String uid, String tid,
			QueryObjectCallback<Media> callback) {
		callback.callback(getMediaDao().select(tid));
	}

	@Override
	public Media getMediaById(String mid) {
		return getMediaDao().select(mid);
	}

	@Override
	public Map<String, String> getById(String tid, String type) {
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

		if (!(StringUtil.notEmpty(type))) {
			Media m = getMediaDao().select(tid);
			String enabled = "";
			boolean e = m.isEnabled();
			if (e) {
				enabled = "开";
			} else {
				enabled = "关";
			}
			map.put("name", m.getName());
			map.put("age", m.getAge() + "");
			map.put("enabled", enabled);
			map.put("startDate", m.getBeginDate());
			map.put("endDate", m.getEndDate());
			map.put("hour", m.getHour());
			map.put("minute", m.getMinute());
			map.put("dispatch", m.getDispatch());
		}
		return map;
	}

	@Override
	public List<Media> getSelectByCond() {
		NamedConditions cond = new NamedConditions("getAllByDispatchAndEnabled");
		return getMediaDao().select(0, Integer.MAX_VALUE, cond);
	}
}
