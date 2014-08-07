package cn.wizool.bank.servlet;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.FileLog;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.Notice;
import cn.wizool.bank.model.TaskLog;
import cn.wizool.bank.model.Train;

public class TaskLogServlet extends PlatFormHttpServlet {

	private static final long serialVersionUID = -5734640759579889375L;

	/**
	 * 获取文件日志列表
	 */
	public void getFileLogAll() {
		final String docName = getRequest().getParameter("docName");
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));

		getFileLogService().transSelectByCond(null, start, limit,
				getCondFilelog(), new QueryListCallback<FileLog>() {
					@Override
					public void callback(int total, List<FileLog> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setMessage("");
						json.setTotal(total);
						for (FileLog fl : objs) {
							if ((!StringUtil.notEmpty(docName))
									|| ((fl.getDoc() != null) && (fl.getDoc()
											.getName().indexOf(docName) != -1))) {
								json.setAttribute("id", fl.getId());
								json.setAttribute("operateType",
										fl.getOperateType());
								json.setAttribute("name", fl.getDepartment()
										.getName());
								json.setAttribute("location", fl
										.getDepartment().getParent().getName());
								json.setAttribute("dept", fl.getDepartment()
										.getParent().getParent().getName());
								json.setAttribute("ip", fl.getDepartment()
										.getIp());
								json.setAttribute("type", fl.getDepartment()
										.getType());
								json.setAttribute("docName",
										fl.getDoc() == null ? "" : fl.getDoc()
												.getName());
								json.setAttribute("birth",
										DateUtil.format(fl.getBirth()));
							}
						}
						json.flush();
					}
				});
	}

	/**
	 * 删除文件日志信息
	 * 
	 * 过滤条件
	 */
	public void deleteFileLog() {
		String style = getRequest().getParameter("style");
		String[] ids = getRequest().getParameterValues("ids");
		try {
			if (style.equals("deleteAll")) {
				getFileLogService().transDeleteAll(null, getCondFilelog());
			} else if (style.equals("deleteSelected")) {
				getFileLogService().transDeleteSelected(null, ids);
			}
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "删除文件日志失败！");
			e.printStackTrace();
		}
	}

	private NamedConditions getCondFilelog() {
		final String ip = getRequest().getParameter("ip");
		String type = getRequest().getParameter("type");
		final String operateType = getRequest().getParameter("operateType");
		final String dept = getRequest().getParameter("dept");
		final String location = getRequest().getParameter("location");
		NamedConditions c = new NamedConditions("getAll");
		if (StringUtil.notEmpty(type) && !("全部".equals(type))) {
			c.putString("Type", type);
		}
		if (StringUtil.notEmpty(operateType) && !("全部".equals(operateType))) {
			c.putString("OperateType", operateType);
		}
		if (StringUtil.notEmpty(location)) {
			c.putString("Location", "%" + location + "%");
		}
		if (StringUtil.notEmpty(dept)) {
			c.putString("Dept", "%" + dept + "%");
		}
		if (StringUtil.notEmpty(ip)) {
			c.putString("Ip", ip);
		}
		return c;
	}

	/**
	 * 获取任务日志列表
	 */
	public void getTaskLogAll() {
		final String taskName = getRequest().getParameter("taskName");
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));

		getTaskLogService().transSelect(null, start, limit, getCondTasklog(),
				new QueryListCallback<TaskLog>() {
					@Override
					public void callback(int total, List<TaskLog> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setMessage("");
						json.setTotal(total);
						for (TaskLog tl : objs) {
							if ((!StringUtil.notEmpty(taskName))
									|| ((tl.getTask() != null) && (tl.getTask()
											.getName().indexOf(taskName) != -1))) {
								json.setAttribute("id", tl.getId());
								json.setAttribute("name", tl.getDepartment()
										.getName());
								json.setAttribute("location", tl
										.getDepartment().getParent().getName());
								json.setAttribute("dept", tl.getDepartment()
										.getParent().getParent().getName());
								json.setAttribute("ip", tl.getDepartment()
										.getIp());
								json.setAttribute("type", tl.getDepartment()
										.getType());
								json.setAttribute("taskName",
										tl.getTask() == null ? "" : tl
												.getTask().getName());
								json.setAttribute("operateType",
										tl.getOperateType());
								json.setAttribute(
										"birth",
										tl.getBirth() == null ? "" : DateUtil
												.format(tl.getBirth()));
							}
						}
						json.flush();
					}
				});
	}

	/**
	 * 删除任务日志信息
	 * 
	 * 过滤条件
	 */
	public void deleteTaskLog() {
		String style = getRequest().getParameter("style");
		String[] ids = getRequest().getParameterValues("ids");
		try {
			if (style.equals("deleteAll")) {
				getTaskLogService().transDeleteAll(null, getCondTasklog());
			} else if (style.equals("deleteSelected")) {
				getTaskLogService().transDeleteSelected(null, ids);
			}
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "删除任务日志失败！");
			e.printStackTrace();
		}
	}

	private NamedConditions getCondTasklog() {
		String ip = getRequest().getParameter("ip");
		String type = getRequest().getParameter("type");
		String operateType = getRequest().getParameter("operateType");
		String dept = getRequest().getParameter("dept");
		String location = getRequest().getParameter("location");
		NamedConditions c = new NamedConditions("getAll");
		if (StringUtil.notEmpty(type) && !("全部".equals(type))) {
			c.putString("Type", type);
		}
		if (StringUtil.notEmpty(operateType) && !("全部".equals(operateType))) {
			c.putString("OperateType", operateType);
		}
		if (StringUtil.notEmpty(location)) {
			c.putString("Location", "%" + location + "%");
		}
		if (StringUtil.notEmpty(dept)) {
			c.putString("Dept", "%" + dept + "%");
		}
		if (StringUtil.notEmpty(ip)) {
			c.putString("Ip", ip);
		}
		return c;
	}

	/**
	 * 培训机 通知公告 新通知信息
	 */
	public void getMsgNotices() {
		String ip = getRequest().getRemoteAddr();
		NamedConditions cond = new NamedConditions("byIp");
		cond.putString("Ip", ip);
		Machine d = getMachineService().getMachine(cond);
		if (d != null) {
			NamedConditions c = new NamedConditions("getMsgNotice");
			c.putString("MachineId", d.getId());
			getNoticeService().transSelectAll(null, 0, Integer.MAX_VALUE, c,
					new QueryListCallback<Notice>() {
						@Override
						public void callback(int total, List<Notice> objs) {
							JSONArray json = new JSONArray(getResponse());
							json.setSuccess(true);
							json.setMessage("");
							json.setTotal(total);
							for (Notice n : objs) {
								json.setAttribute("taskId", n.getId());
								json.setAttribute("taskName", n.getName());
								json.setAttribute("birth",
										DateUtil.format(n.getPubishDate()));
								json.setAttribute("person", n.getPublisher()
										.getParent().getName());
								if (StringUtil.notEmpty(n.getImportant())) {
									json.setAttribute("important",
											n.getImportant());
								}
							}
							json.flush();
						}
					});
		} else {
			System.out.println("ip =   " + ip);
		}
	}

	/**
	 * 培训机 通知公告 通知公告历史 任务列表
	 */
	public void getNoticeLogs() {
		String ip = getRequest().getRemoteAddr();
		NamedConditions cond = new NamedConditions("byIp");
		cond.putString("Ip", ip);
		Machine d = getMachineService().getMachine(cond);
		if (d != null) {
			NamedConditions c = new NamedConditions("getNoticeLog");
			c.putString("MachineId", d.getId());
			getNoticeService().transSelectAll(null, 0, Integer.MAX_VALUE, c,
					new QueryListCallback<Notice>() {
						@Override
						public void callback(int total, List<Notice> objs) {
							JSONArray json = new JSONArray(getResponse());
							json.setSuccess(true);
							json.setMessage("");
							json.setTotal(total);
							for (Notice n : objs) {
								json.setAttribute("taskId", n.getId());
								json.setAttribute("taskName", n.getName());
								json.setAttribute("birth",
										DateUtil.format(n.getPubishDate()));
								json.setAttribute("person", n.getPublisher()
										.getParent().getName());
							}
							json.flush();
						}
					});
		} else {
			System.out.println("ip =   " + ip);
		}
	}

	/**
	 * 培训机 公文管理 遍历出本地D:/TMP文件下的所有文件列表
	 */
	public void getServerFiles() {
		ConcurrentSkipListMap<String, String[]> ht = InterfaceServlet.files;
		final JSON json = new JSON(getResponse());
		int total = 0;
		json.beginObject();
		json.beginAttribute("root");
		json.beginArray();
		if (ht != null) {
			for (String s : ht.keySet()) {
				String[] docIds = ht.get(s);
				total = docIds.length;
				for (String str : docIds) {
					String did = str.split("::")[0];
					getDocumentService().transSelectModel(null, did,
							new QueryObjectCallback<Document>() {
								@Override
								public void callback(Document d) {
									if (d.isDisplay()) {
										json.beginObject();
										json.setAttribute("id", d.getId());
										json.setAttribute("name", d.getName());
										json.setAttribute("uploaddate",
												DateUtil.format(d
														.getUploadDate()));
										json.setAttribute("surfix",
												d.getSurfix());
										json.setAttribute("len", d.getLength());
										json.setAttribute("length", StringUtil
												.returnLength((int) d
														.getLength()));
										json.setAttribute("type", d.getType());
										if (d.getUpload_publisher() != null) {
											json.setAttribute("user", d
													.getUpload_publisher()
													.getName());
											json.setAttribute("dept", d
													.getUpload_publisher()
													.getParent().getName());
										}
										json.setAttribute("state", d.getState());
										json.endObject();
									}
								}
							});
				}
			}
		}
		json.endArray();
		json.endAttribute();
		json.setAttribute("success", true);
		json.setAttribute("message", "出现错误！");
		json.setAttribute("total", total);
		json.endObject();
		json.end();
	}

	/**
	 * 培训机 培训资料 培训资料历史 任务列表
	 */
	public void getTrainLogs() {
		String ip = getRequest().getRemoteAddr();
		NamedConditions cond = new NamedConditions("byIp");
		cond.putString("Ip", ip);
		Machine d = getMachineService().getMachine(cond);
		if (d != null) {
			NamedConditions c = new NamedConditions("getTrainLog");
			c.putString("MachineId", d.getId());
			getTrainService().transSelectAll(null, 0, Integer.MAX_VALUE, c,
					new QueryListCallback<Train>() {
						@Override
						public void callback(int total, List<Train> objs) {
							JSONArray json = new JSONArray(getResponse());
							json.setSuccess(true);
							json.setMessage("");
							json.setTotal(total);
							for (Train t : objs) {
								json.setAttribute("id", t.getId());
								json.setAttribute("name", t.getName());
								json.setAttribute("start",
										DateUtil.format(t.getStartDate()));
								json.setAttribute("deptName", t.getPublisher()
										.getParent().getName());
							}
							json.flush();
						}
					});
		} else {
			System.out.println("ip =   " + ip);
		}
	}

}

class MyComp implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 instanceof Train && o2 instanceof Train) {
			Train t1 = (Train) o1;
			Train t2 = (Train) o2;
			if (t2.getStartDate().getTime() - t1.getStartDate().getTime() > 0) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

}