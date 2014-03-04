package cn.wizool.bank.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.common.TimerScheduler;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.Train;

public class TrainServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -3215250244402516345L;

	/**
	 * 显示所有培训资料信息
	 */
	public void get() {
		final HttpServletResponse response = getResponse();
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		String name = getRequest().getParameter("name");
		String startDateStr = getRequest().getParameter("startDate");
		String endDateStr = getRequest().getParameter("endDate");
		String beginDateStr = getRequest().getParameter("beginDate");
		String overDateStr = getRequest().getParameter("overDate");
		String publisherName = getRequest().getParameter("publisherName");
		NamedConditions cond = new NamedConditions("getAll");

		if (StringUtil.notEmpty(name)) {
			cond.putString("Name", "%" + name + "%");
		}
		if (StringUtil.notEmpty(publisherName)) {
			cond.putString("PublisherName", "%" + publisherName + "%");
		}
		if (!(StringUtil.notEmpty(startDateStr))) {
			startDateStr = DateUtil.getStrDate("197001010101");
		}
		if (!(StringUtil.notEmpty(endDateStr))) {
			endDateStr = DateUtil.getStrDate("301201010101");
		}
		if (!(StringUtil.notEmpty(beginDateStr))) {
			beginDateStr = DateUtil.getStrDate("197001010101");
		}
		if (!(StringUtil.notEmpty(overDateStr))) {
			overDateStr = DateUtil.getStrDate("301201010101");
		}
		String startDate = DateUtil.getStrDate(startDateStr);
		String endDate = DateUtil.getStrDate(endDateStr);
		String beginDate = DateUtil.getStrDate(beginDateStr);
		String overDate = DateUtil.getStrDate(overDateStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			cond.putDate("StartDate", sdf.parse(DateUtil.getStrDate(startDate)));
			cond.putDate("EndDate", sdf.parse(DateUtil.getStrDate(endDate)));
			cond.putDate("BeginDate", sdf.parse(DateUtil.getStrDate(beginDate)));
			cond.putDate("OverDate", sdf.parse(DateUtil.getStrDate(overDate)));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		getTrainService().transSelectAll(null, start, limit, cond,
				new QueryListCallback<Train>() {
					@Override
					public void callback(int total, List<Train> objs) {
						JSONArray json = new JSONArray(response);
						json.setSuccess(true);
						json.setTotal(total);
						for (Train t : objs) {
							json.setAttribute("id", t.getId());
							json.setAttribute("name", t.getName());
							json.setAttribute("date",
									DateUtil.format(t.getPubishDate()));
							json.setAttribute("age",
									StringUtil.getAge(t.getAge()));
							json.setAttribute("startDate",
									DateUtil.format(t.getStartDate()));
							json.setAttribute("dateStr", t.getDateStr());
							json.setAttribute("publisherName", t.getPublisher()
									.getName());
							json.setAttribute("dept", t.getPublisher()
									.getParent().getName());
						}
						json.flush();
					}
				});
	}

	/**
	 * 给该任务包含的机器下发所包含的文件
	 */
	public void sendDocs() {
		String taskId = getRequest().getParameter("taskId");
		final Map<String, String> map = getMediaService().getById(taskId,
				"getIdArrs");
		String docIds = map.get("docIds");
		String machineIds = map.get("machineIds");
		String[] docArr = docIds.substring(0, docIds.length() - 1).split(",");
		String[] machineArr = machineIds.substring(0, machineIds.length() - 1)
				.split(",");
		for (String deptId : machineArr) {
			List<String> list = new ArrayList<String>();
			for (String docId : docArr) {
				list.add("D:" + docId);
			}
			InterfaceServlet.putTasks(deptId, list);
		}
	}

	/**
	 * 点击修改任务按钮，弹出的编辑任务窗口中，记录所选任务包含的所有文件和返回所选任务包含的所有机器Id
	 */
	public void clickModify() {
		String taskId = getRequest().getParameter("taskId");
		String type = getRequest().getParameter("type");
		HttpSession session = getRequest().getSession();
		try {
			Map<String, String> map = getTrainService().getIds(taskId);
			getMediaService().transSetList(map.get("docIds"), session, type);
			Integer age = Integer.parseInt(map.get("age"));
			Integer hour = age / 3600;
			Integer minute = age % 3600 / 60;

			JSON json = new JSON(getResponse());
			json.beginObject();
			json.setAttribute("oldId", taskId);
			json.setAttribute("hour", hour);
			json.setAttribute("minute", minute);
			json.setAttribute("name", map.get("name"));
			json.setAttribute("startDate", map.get("startDate"));
			json.setAttribute("docIds", map.get("docIds"));
			json.setAttribute("deptIds", map.get("machineIds"));
			json.endObject();
			json.end();

			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "获取文件列表失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 编辑任务
	 */
	public void edit() {
		HttpSession session = getRequest().getSession();
		String oldId = getRequest().getParameter("oldId");
		String name = getRequest().getParameter("name");
		String startDate = getRequest().getParameter("startDate");
		String machines = getRequest().getParameter("deptIds");
		String docIds = getRequest().getParameter("docIds");
		String checked = getRequest().getParameter("checked");

		String h = getRequest().getParameter("h");
		String m = getRequest().getParameter("m");

		if (machines == null || machines.equals("")) {
			NamedConditions cond = new NamedConditions("getPc");
			cond.putString("Type", "培训");
			List<Machine> list = getMachineService().transSelectByCond(cond);
			for (Machine d : list) {
				machines += d.getId() + " ";
			}
		}
		String id = UUID.randomUUID().toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		map.put("machines", machines);
		map.put("docIds", docIds);
		map.put("h", h);
		map.put("m", m);
		map.put("startDate", startDate);
		try {
			String[] docArr = docIds.substring(0, docIds.length() - 1).split(
					",");
			String[] machineArr = machines.split(" ");
			// 如选择了 "任务所包含文件是否立刻下发?",则给所选择的机器下发该任务所包含的所有文件
			if (Boolean.parseBoolean(checked)) {
				for (String deptId : machineArr) {
					List<String> l = new ArrayList<String>();
					for (String docId : docArr) {
						l.add("D:" + docId);
					}
					InterfaceServlet.putTasks(deptId, l);
				}
			}

			List<Document> list = (List<Document>) session
					.getAttribute("listTrain");
			if (StringUtil.notEmpty(oldId)) {
				// 修改任务
				// 删除之前的任务，新建任务
				getTrainService().transDelete(null, oldId);
				getTrainService().transCreate(getCurrentUser().getId(), map,
						list);
			} else {
				// 新建任务
				getTrainService().transCreate(getCurrentUser().getId(), map,
						list);
			}

			// 执行任务,下发指令
			TimerScheduler.getDefault().schedule("S:" + id, machineArr,
					getTrainService().getStartDate(startDate));

			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "编辑培训资料失败！");
			e.printStackTrace();
		}
		session.removeAttribute("listTrain");
	}

	/**
	 * 删除任务 — 逻辑删除
	 */
	public void delete() {
		String[] ids = getRequest().getParameterValues("ids");
		try {
			getTrainService().transDelete(null, ids);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "删除培训资料失败！");
			e.printStackTrace();
		}
	}

}
