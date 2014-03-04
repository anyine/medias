package cn.wizool.bank.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.Notice;

public class NoticeServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = 7518035979228510090L;

	/**
	 * 显示所有通知公告信息
	 */
	public void get() {
		final HttpServletResponse response = getResponse();
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		String name = getRequest().getParameter("name");
		String startStrDate = getRequest().getParameter("startDate");
		String endStrDate = getRequest().getParameter("endDate");
		String important = getRequest().getParameter("important");
		String publisherName = getRequest().getParameter("publisherName");
		NamedConditions cond = new NamedConditions("getAll");

		if (StringUtil.notEmpty(name)) {
			cond.putString("Name", "%" + name + "%");
		}
		if (StringUtil.notEmpty(publisherName)) {
			cond.putString("PublisherName", "%" + publisherName + "%");
		}
		if (StringUtil.notEmpty(important) && !("全部".equals(important))) {
			cond.putString("Important", important);
		}
		if (!(StringUtil.notEmpty(startStrDate))) {
			startStrDate = DateUtil.getStrDate("197001010101");
		}
		if (!(StringUtil.notEmpty(endStrDate))) {
			endStrDate = DateUtil.getStrDate("301201010101");
		}
		String startDate = DateUtil.getStrDate(startStrDate);
		String endDate = DateUtil.getStrDate(endStrDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			cond.putDate("StartDate", sdf.parse(DateUtil.getStrDate(startDate)));
			cond.putDate("EndDate", sdf.parse(DateUtil.getStrDate(endDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		getNoticeService().transSelectAll(null, start, limit, cond,
				new QueryListCallback<Notice>() {
					@Override
					public void callback(int total, List<Notice> objs) {
						JSONArray json = new JSONArray(response);
						json.setSuccess(true);
						json.setTotal(total);
						for (Notice n : objs) {
							json.setAttribute("id", n.getId());
							json.setAttribute("name", n.getName());
							json.setAttribute("date",
									DateUtil.format(n.getPubishDate()));
							json.setAttribute("important", n.getImportant());
							json.setAttribute("publisherName", n.getPublisher()
									.getName());
							json.setAttribute("dept", n.getPublisher()
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
	 * 创建任务
	 */
	public void create() {
		HttpSession session = getRequest().getSession();
		String name = getRequest().getParameter("name");
		String machines = getRequest().getParameter("deptIds");
		String docIds = getRequest().getParameter("docIds");
		String important = getRequest().getParameter("important");
		String checked = getRequest().getParameter("checked");

		if (!(StringUtil.notEmpty(machines))) {
			NamedConditions cond = new NamedConditions("getPc");
			cond.putString("Type", "培训");
			List<Machine> list = getMachineService().transSelectByCond(cond);
			for (Machine d : list) {
				machines += d.getId() + " ";
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("machines", machines);
		map.put("docIds", docIds);
		map.put("important", important);
		try {
			String[] docArr = docIds.substring(0, docIds.length() - 1).split(
					",");
			String[] machineArr = machines.split(" ");
			// 如选择了 “任务所包含文件是否立刻下发？”，则给所选择的机器下发该任务所包含的所有文件
			if (Boolean.parseBoolean(checked)) {
				for (String machine : machineArr) {
					List<String> l = new ArrayList<String>();
					for (String docId : docArr) {
						l.add("D:" + docId);
					}
					InterfaceServlet.putTasks(machine, l);
				}
			}

			List<Document> list = (List<Document>) session
					.getAttribute("listNotice");
			getNoticeService().transCreate(getCurrentUser().getId(), map, list);
			session.removeAttribute("listNotice");

			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "添加任务失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 删除任务 — 逻辑删除
	 */
	public void delete() {
		String[] ids = getRequest().getParameterValues("ids");
		try {
			getNoticeService().transDelete(null, ids);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "删除任务失败！");
			e.printStackTrace();
		}
	}

}
