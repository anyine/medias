package cn.wizool.bank.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.quartz.SchedulerException;

import cn.wizool.bank.common.FileUtil;
import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Media;
import cn.wizool.bank.model.TaskDocuments;

@MultipartConfig(maxFileSize = 1024 * 1024 * 10 * 500)
public class MediaServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -322348877125571780L;
	private static final String kpId = "43e87216-76ff-40af-9c10-07367dc8e65c";

	/**
	 * 获取除关机调度之外的广告媒体任务列表
	 */
	public void get() {
		final HttpServletResponse response = getResponse();
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		String name = getRequest().getParameter("name");
		String startStrDate = getRequest().getParameter("startDate");
		String endStrDate = getRequest().getParameter("endDate");
		String strEnabled = getRequest().getParameter("enabled");
		String publisherName = getRequest().getParameter("publisherName");
		NamedConditions cond = new NamedConditions("getAll");
		if (StringUtil.notEmpty(name)) {
			cond.putString("Name", "%" + name + "%");
		}
		if (StringUtil.notEmpty(publisherName)) {
			cond.putString("PublisherName", "%" + publisherName + "%");
		}
		Boolean enabled = null;
		if (StringUtil.notEmpty(strEnabled)) {
			if (!(strEnabled.equals("2"))) {
				if (strEnabled.equals("1")) {
					enabled = true;
				} else if (strEnabled.equals("0")) {
					enabled = false;
				}
				cond.putBoolean("Enabled", enabled);
			}
		}
		if (!(StringUtil.notEmpty(startStrDate))) {
			startStrDate = DateUtil.getStrDate("197001010101");
		}
		if (!(StringUtil.notEmpty(endStrDate))) {
			endStrDate = DateUtil.getStrDate("301201010101");
		}
		String getStartStrDate = DateUtil.getStrDate(startStrDate);
		String getEndStrDate = DateUtil.getStrDate(endStrDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			cond.putDate("StartDate",
					sdf.parse(DateUtil.getStrDate(getStartStrDate)));
			cond.putDate("EndDate",
					sdf.parse(DateUtil.getStrDate(getEndStrDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		getMediaService().transSelectAll(null, start, limit, cond,
				new QueryListCallback<Media>() {
					@Override
					public void callback(int total, List<Media> objs) {
						JSONArray json = new JSONArray(response);
						json.setSuccess(true);
						json.setTotal(total);
						createMediaList(json, objs);
						json.flush();
					}
				});
	}

	protected void createMediaList(JSONArray json, List<Media> objs) {
		if (objs.size() > 0) {
			for (Media m : objs) {
				if (!(m.getName().equals("关机调度任务"))) {
					json.setAttribute("id", m.getId());
					json.setAttribute("name", m.getName());
					json.setAttribute("startDate", m.getBeginDate());
					json.setAttribute("endDate", m.getEndDate());
					json.setAttribute("hour", m.getHour());
					json.setAttribute("minute", m.getMinute());
					json.setAttribute("age", StringUtil.getAge(m.getAge()));
					json.setAttribute("date",
							DateUtil.format(m.getPubishDate()));
					json.setAttribute("dispatch", m.getDispatch());
					json.setAttribute("publisherName", m.getPublisher()
							.getName());
					json.setAttribute("dept", m.getPublisher().getParent()
							.getName());
					String enabled = "";
					boolean e = m.isEnabled();
					if (e) {
						enabled = "开";
					} else {
						enabled = "关";
					}
					json.setAttribute("enabled", enabled);
				}
			}
		}

	}

	/**
	 * 获取关机调度
	 */
	public void getKillPower() {
		// final Map<String, String> map = new HashMap<String, String>();
		// getMediaService().transSelectModelById(null, kpId,
		// new QueryObjectCallback<Media>() {
		// @Override
		// public void callback(Media m) {
		// if (m != null) {
		// map.put("dispatch", m.getDispatch());
		// String deptStr = "";
		// for (Machine d : m.getDepartments()) {
		// deptStr += d.getId() + " ";
		// }
		// map.put("depts", deptStr);
		// String enabledStr = "";
		// if (m.isEnabled()) {
		// enabledStr = "true";
		// } else {
		// enabledStr = "false";
		// }
		// map.put("enabled", enabledStr);
		// }
		// }
		// });
		// JSON json = new JSON(getResponse());
		// json.beginObject();
		// json.setAttribute("enabled", map.get("enabled"));
		// json.setAttribute("depts", map.get("depts"));
		// json.setAttribute("dispatch", map.get("dispatch"));
		// json.endObject();
		// json.end();
	}

	/**
	 * 设置关机调度
	 */
	public void killPower() {
		// String dept = getRequest().getParameter("dept");
		// String dispatch = getRequest().getParameter("dispatch");
		// String enabled = getRequest().getParameter("enabled");
		// if (dept == null || dept.equals("")) {
		// NamedConditions cond = new NamedConditions("getPc");
		// List<Machine> list = getMachineService().transSelectByCond(cond);
		// for (Machine d : list) {
		// dept += d.getId() + " ";
		// }
		// }
		// try {
		// getMediaService().transCreateKillPower(getCurrentUser().getId(),
		// dept, dispatch, enabled, kpId);
		//
		// // 执行任务
		// String[] depts = dept.split(" ");
		// InterfaceServlet.getScheduler().schedule(
		// "S:" + "f6f2a76c-bf34-4cc7-adba-8d2ab99d0fb8", depts,
		// dispatch);
		//
		// JSON.sendSuccess(getResponse());
		// } catch (Exception e) {
		// JSON.sendSuccess(getResponse(), "添加关机调度失败!");
		// e.printStackTrace();
		// }
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
	 * 编辑任务
	 */
	public void edit() {
		HttpSession session = getRequest().getSession();
		String oldId = getRequest().getParameter("oldId");
		String name = getRequest().getParameter("name");
		String machines = getRequest().getParameter("deptIds");
		String docIds = getRequest().getParameter("docIds");
		String enabled = getRequest().getParameter("enabled");
		String checked = getRequest().getParameter("checked");
		String h = getRequest().getParameter("h");
		String m = getRequest().getParameter("m");
		String startDate = getRequest().getParameter("startDate");
		String endDate = getRequest().getParameter("endDate");
		String hour = getRequest().getParameter("hour");
		String minute = getRequest().getParameter("minute");

		// 如果不选择机器，则默认所有广告机
		if (!(StringUtil.notEmpty(machines))) {
			NamedConditions cond = new NamedConditions("getPc");
			cond.putString("Type", "广告");
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
		map.put("h", h);
		map.put("m", m);
		map.put("docIds", docIds);
		map.put("checked", checked);
		map.put("enabled", enabled);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("hour", hour);
		map.put("minute", minute);
		List<Document> docList = (List<Document>) session
				.getAttribute("listMedia");
		try {
			String[] docArr = docIds.substring(0, docIds.length() - 1).split(
					",");
			String[] machineArr = machines.split(" ");
			// 如选择了 "任务所包含文件是否立刻下发?"，则给所选择的机器下发该任务所包含的所有文件
			if (Boolean.parseBoolean(checked)) {
				for (String deptId : machineArr) {
					List<String> l = new ArrayList<String>();
					for (String docId : docArr) {
						l.add("D:" + docId);
					}
					InterfaceServlet.putTasks(deptId, l);
				}
			}

			if (StringUtil.notEmpty(oldId)) {
				// 修改任务
				// 则删除之前任务,新建任务
				getMediaService().transDelete(null, oldId);
				getMediaService().transCreateMedia(getCurrentUser(), map,
						docList);
			} else {
				// 新建任务
				getMediaService().transCreateMedia(getCurrentUser(), map,
						docList);
			}

			// 执行任务,下发指令
			String dispatch = getMediaService().getDispatch(map);
			String taskId = "S:" + id;
			if (StringUtil.notEmpty(dispatch)) {
				InterfaceServlet.getScheduler().schedule(taskId, machineArr,
						dispatch);
			} else {
				for (String s : machineArr) {
					InterfaceServlet.putOneTask(s, taskId);
				}
			}
			JSON.sendSuccess(getResponse());
		} catch (SchedulerException e) {
			JSON.sendSuccess(getResponse(), "编辑广告媒体成功，但该调度不会执行！");
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "编辑广告媒体失败！");
			e.printStackTrace();
		}
		session.removeAttribute("listMedia");
	}

	/**
	 * 删除任务 — 逻辑删除
	 */
	public void delete() {
		String[] ids = getRequest().getParameterValues("ids");
		try {
			getMediaService().transDelete(null, ids);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "删除广告媒体失败！");
			e.printStackTrace();
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
			Map<String, String> map = getMediaService().getById(taskId, "");
			getMediaService().transSetList(map.get("docIds"), session, type);

			Integer age = Integer.parseInt(map.get("age"));
			Integer hour = age / 3600;
			Integer minute = age % 3600 / 60;

			JSON json = new JSON(getResponse());
			json.beginObject();
			json.setAttribute("oldId", taskId);
			json.setAttribute("h", hour);
			json.setAttribute("m", minute);
			json.setAttribute("name", map.get("name"));
			json.setAttribute("docIds", map.get("docIds"));
			json.setAttribute("deptIds", map.get("machineIds"));
			json.setAttribute("enabled", map.get("enabled"));
			json.setAttribute("startDate", map.get("startDate"));
			json.setAttribute("endDate", map.get("endDate"));
			json.setAttribute("hour", map.get("hour"));
			json.setAttribute("minute", map.get("minute"));
			json.setAttribute("dispatch", map.get("dispatch"));
			json.endObject();
			json.end();

			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "获取任务信息失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 选择文件后，放到session中
	 */
	public void setList() {
		HttpSession session = getRequest().getSession();
		String docIds = getRequest().getParameter("docIds");
		String type = getRequest().getParameter("type");
		try {
			getMediaService().transSetList(docIds, session, type);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "获取文件列表失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 关闭编辑任务时，删除session中的信息
	 */
	public void removeSession() {
		HttpSession session = getRequest().getSession();
		String type = getRequest().getParameter("type");
		if (type.equals("media")) {
			session.removeAttribute("listMedia");
		} else if (type.equals("notice")) {
			session.removeAttribute("listNotice");
		} else if (type.equals("train")) {
			session.removeAttribute("listTrain");
		}
		JSON.sendSuccess(getResponse());
	}

	/**
	 * 保存排序后的列表
	 */
	public void saveOrderDatas() {
		String id = getRequest().getParameter("id");
		String type = getRequest().getParameter("type");
		String moveType = getRequest().getParameter("moveType");
		HttpSession session = getRequest().getSession();
		List<Document> list = null;
		if (type.equals("media")) {
			list = (List<Document>) session.getAttribute("listMedia");
		} else if (type.equals("notice")) {
			list = (List<Document>) session.getAttribute("listNotice");
		} else if (type.equals("train")) {
			list = (List<Document>) session.getAttribute("listTrain");
		}
		if (list != null) {
			int index = getIndex(list, id);
			Document selectDoc = getMediaService().getDocById(id);
			orderList(moveType, list, index, selectDoc);
		}
	}

	private void orderList(String moveType, List<Document> list, int index,
			Document selectDoc) {
		if (moveType.equals("upFirist")) {// 置顶
			if (index != 0) {
				list.remove(index);
				list.add(0, selectDoc);
				JSON.sendSuccess(getResponse());
			} else {
				JSON.sendSuccess(getResponse(), "此行为第一行");
			}
		} else if (moveType.equals("upOne")) {// 向上移动一个
			if (index != 0) {
				Document temp = list.get(index - 1);
				list.add(index, temp);
				list.add(index - 1, selectDoc);
				list.remove(index + 1);
				list.remove(index + 1);
				JSON.sendSuccess(getResponse());
			} else {
				JSON.sendSuccess(getResponse(), "此行为第一行");
			}
		} else if (moveType.equals("downOne")) {// 向下移动一个
			if (index != (list.size() - 1)) {
				Document temp = list.get(index + 1);
				list.add(index, temp);
				list.add(index + 1, selectDoc);
				list.remove(index + 2);
				list.remove(index + 2);
				JSON.sendSuccess(getResponse());
			} else {
				JSON.sendSuccess(getResponse(), "此行为最后一行");
			}
		} else if (moveType.equals("downLast")) {// 置底
			if (index != (list.size() - 1)) {
				list.remove(index);
				list.add(list.size(), selectDoc);
				JSON.sendSuccess(getResponse());
			} else {
				JSON.sendSuccess(getResponse(), "此行为最后一行");
			}
		} else if (moveType.equals("deleteOne")) {// 删除
			list.remove(index);
			JSON.sendSuccess(getResponse());
		}
	}

	private int getIndex(List<Document> list, String id) {
		int index = 0;
		for (Document doc : list) {
			if (doc.getId().equals(id)) {
				index = list.indexOf(doc);
			}
		}
		return index;
	}

	/**
	 * 获取session中的列表信息
	 */
	public void getDocmentList() {
		String type = getRequest().getParameter("type");
		HttpSession session = getRequest().getSession();
		JSON json = new JSON(getResponse());
		List<Document> list = null;
		if (type.equals("media")) {
			list = (List<Document>) session.getAttribute("listMedia");
		} else if (type.equals("notice")) {
			list = (List<Document>) session.getAttribute("listNotice");
		} else if (type.equals("train")) {
			list = (List<Document>) session.getAttribute("listTrain");
		}
		json.beginArray();
		if (list != null) {
			for (Document doc : list) {
				if (StringUtil.notEmpty(doc.getId())) {
					json.beginObject();
					json.setAttribute("id", doc.getId());
					json.setAttribute("name", doc.getName());
					// json.setAttribute("type", doc.getType());
					json.setAttribute("surfix", doc.getSurfix());
					json.setAttribute("length",
							StringUtil.returnLength((int) doc.getLength()));
					json.setAttribute("date",
							DateUtil.format(doc.getUploadDate()));
					json.endObject();
				}
			}
		}
		json.endArray();
		json.end();
	}

	/**
	 * 上传文件，保存数据库
	 */
	public void saveDoc() {
		HttpSession session = getRequest().getSession();
		try {
			getRequest().setCharacterEncoding("UTF-8");
			getResponse().setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String names = getFilePartValue(getRequest(), "strs");
		String type = getFilePartValue(getRequest(), "type");
		String strs = names.substring(0, names.length() - 1);
		String[] strArr = strs.split(",");
		int excepNum = 0;
		int n = 0;
		String ret = null;
		Collection<Part> x;
		try {
			x = getRequest().getParts();
			Map<String, Part> maps = new HashMap<String, Part>();
			for (Part p : x) {
				if (p.getName().equals("documents")) {
					n++;
					maps.put(getFileName(p, "UTF-8"), p);
				}
			}
			List<Document> list = null;
			if (type.equals("media")) {
				list = (List<Document>) session.getAttribute("listMedia");
			} else if (type.equals("notice")) {
				list = (List<Document>) session.getAttribute("listNotice");
			} else if (type.equals("train")) {
				list = (List<Document>) session.getAttribute("listTrain");
			}
			for (String s : strArr) {
				String[] ss = uploadFile(maps.get(s));
				Document d = getDocumentService().getDocById(ss[3]);
				if ("文件重复".equals(ss[0])) {
					excepNum++;
				} else if (ss[0] == null) {
					if (d != null) {
						d.setDisplay(true);
						getDocumentService().transUpdate(null, d);
					} else {
						d = new Document();
						d.setId(ss[3]);
						d.setMd5(ss[3]);
						d.setName(ss[1]);
						d.setIndex(getDocumentService().selectAllCount() + 1);
						d.setSurfix(getFileEndName(ss[1]));
						d.setLength(maps.get(s).getSize());
						d.setUploadDate(new Date());
						d.setUpload_publisher(getCurrentUser());
						d.setType(StringUtil.getDocType(getFileEndName(ss[1])));
						getDocumentService().transCreate(null, d);
					}
				} else {
					ret = ss[0];
					break;
				}

				if (list == null) {
					list = new ArrayList<Document>();
					list.add(d);
					if (type.equals("media")) {
						session.setAttribute("listMedia", list);
					} else if (type.equals("notice")) {
						session.setAttribute("listNotice", list);
					} else if (type.equals("train")) {
						session.setAttribute("listTrain", list);
					}
				} else {
					list.add(d);
				}
			}
			getResponse().setContentType(
					"application/octet-stream; charset=utf-8");
			if (ret != null) {
				JSON.sendErrorMessage(getResponse(), ret, true);
			} else if (excepNum == n - 1) {
				JSON.sendErrorMessage(getResponse(), "文件已存在！", true);
			} else {
				JSON.sendSuccess(getResponse(), true);
			}
			JSON.sendSuccess(getResponse(), true);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param p
	 * @return String[4] 0:错误信息;成功则为null;文件重复:("文件重复") 1:文件全名 2:UUID 3:MD5
	 */
	private String[] uploadFile(Part p) {
		String[] strs = new String[4]; // 0:成功或错误信息 1:文件全名 2:UUID 3:MD5
		String uuid = UUID.randomUUID().toString();
		strs[2] = uuid;
		String succee = null;
		try {
			getRequest().setCharacterEncoding("utf-8");
			String fileName = getFileName(p, "UTF-8");
			strs[1] = fileName;
			String extension = getFileEndName(fileName);// 后缀
			if (extension.length() <= 0) { // 文件没有后缀名，不是有效文件
				succee = "请重新选择文件";
			} else {
				// 本地路径 ： d:\TMP
				String loc = getRequest().getServletContext().getInitParameter(
						"filepath");
				File dir = new File(loc);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				p.write(loc + File.separator + uuid);

				File judgeFile = new File(loc + File.separator + uuid);// 已保存到本地的文件
				if (!judgeFile.exists()) {
					succee = "上传失败，请重试！";
				} else {
					// 获取文件的MD5
					String md5 = MD5.byteArrayToHex(FileUtil.md5(loc
							+ File.separator + uuid));
					strs[3] = md5;
					// 判断MD5是否存在
					if (isMD5(md5)) {
						// 删除本地文件
						judgeFile.delete();
						succee = "文件重复";
					} else {
						// 换名
						judgeFile
								.renameTo(new File(loc + File.separator + md5));
					}
				}
			}
			strs[0] = succee;

		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "上传文件失败！");
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 任务详细信息——机器信息
	 */
	public void getSelectDeptsInfo() {
		String taskId = getRequest().getParameter("taskId");
		final long times = Long.parseLong(getRequest().getServletContext()
				.getInitParameter("times")) * 1000;

		NamedConditions cond = new NamedConditions("getByTaskId");
		cond.putString("TaskId", taskId);
		getMachineTasksService().transSelectByCond(null, cond,
				new QueryListCallback<MachineTasks>() {
					@Override
					public void callback(int total, List<MachineTasks> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setTotal(total);
						for (MachineTasks mt : objs) {
							json.setAttribute("id", mt.getMachine().getId());
							json.setAttribute("name", mt.getMachine().getName());
							json.setAttribute("ip", mt.getMachine().getIp());
							json.setAttribute("type", mt.getMachine().getType());
							json.setAttribute("location", mt.getMachine()
									.getParent().getName());
							json.setAttribute("dept", mt.getMachine()
									.getParent().getParent().getName());
							Date date = mt.getMachine().getActive();
							if (date == null) {
								json.setAttribute("status", "关机");
							} else {
								Date cdate = new Date(System
										.currentTimeMillis());
								Long times1 = cdate.getTime() - date.getTime();
								if (times1 > times) {
									json.setAttribute("status", "关机");
								} else {
									json.setAttribute("status", "开机");
								}
							}
						}
						json.flush();
					}
				});
	}

	/**
	 * 任务详细信息——文件信息
	 */
	public void getSelectDocsInfo() {
		String taskId = getRequest().getParameter("taskId");
		NamedConditions cond = new NamedConditions("getTDByTaskId");
		cond.putString("TaskId", taskId);
		getMediaService().transSelectTDByCond(null, cond,
				new QueryListCallback<TaskDocuments>() {
					@Override
					public void callback(int total, List<TaskDocuments> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setMessage("");
						json.setTotal(total);
						for (TaskDocuments td : objs) {
							Document d = td.getDocument();
							json.setAttribute("id", d.getId());
							json.setAttribute("name", d.getName());
							json.setAttribute("length", StringUtil
									.returnLength((int) d.getLength()));
							json.setAttribute("uploadDate",
									DateUtil.format(d.getUploadDate()));
							json.setAttribute("dept", d.getUpload_publisher()
									.getParent().getName());
						}
						json.flush();
					}
				});
	}

	/**
	 * 判断document表中是否含有MD5
	 * 
	 * @param md5
	 * @return 有true 无false
	 */
	private boolean isMD5(String md5) {
		boolean flag = false;
		List<Document> list = this.getDocumentService().getListAlls();
		for (Document d : list) {
			if (md5.equals(d.getMd5()))
				return true;
		}
		return flag;
	}

	public void insertMachineTasks() {
		getTaskService().getAll();
	}

}
