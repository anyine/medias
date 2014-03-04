package cn.wizool.bank.servlet;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.servlet.http.HttpServletRequest;

import cn.wizool.bank.cache.MachineCache;
import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.Task;
import cn.wizool.bank.model.User;

public class DepartmentServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -8467812352824521708L;

	/**
	 * 获取机器文件列表
	 */
	public void getSelectDocsInfo() {
		final String id = getRequest().getParameter("id");
		ConcurrentSkipListMap<String, String[]> ht = InterfaceServlet.files;
		final JSON json = new JSON(getResponse());
		int total = 0;
		json.beginObject();
		json.beginAttribute("root");
		json.beginArray();
		if (ht != null) {
			if (ht.get(id) != null && (ht.get(id).length > 0)) {
				String[] docIds = ht.get(id);
				total = docIds.length;
				final List<String> list = new ArrayList<String>();
				for (String str : docIds) {
					final String did = str.split("::")[0];
					final String size = str.split("::")[1];
					final String date = str.split("::")[2];
					getDocumentService().transSelectModel(null, did,
							new QueryObjectCallback<Document>() {
								@Override
								public void callback(Document d) {
									if (d == null) {
										list.add("R:" + did);
									} else {
										if (d.isDisplay()) {
											json.beginObject();
											json.setAttribute("id", d.getId());
											json.setAttribute("name",
													d.getName());
											json.setAttribute(
													"length",
													StringUtil
															.returnLength((int) d
																	.getLength()));
											json.setAttribute("uploadDate",
													DateUtil.format(d
															.getUploadDate()));
											json.setAttribute("dept", d
													.getUpload_publisher()
													.getParent().getName());
											json.setAttribute(
													"realitySize",
													StringUtil
															.returnLength(Integer
																	.parseInt(size)));
											json.setAttribute("updateDate",
													date);
											json.endObject();
										} else {
											list.add("R:" + did);
										}
									}
								}
							});
				}
				InterfaceServlet.putTasks(id, list);
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
	 * 获取机器任务列表
	 */
	public void getSelectTasksInfo() {
		final String id = getRequest().getParameter("id");
		ConcurrentSkipListMap<String, String[]> ht = InterfaceServlet.tasks;
		final JSON json = new JSON(getResponse());
		int total = 0;
		json.beginObject();
		json.beginAttribute("root");
		json.beginArray();
		if (ht != null) {
			if (ht.get(id) != null) {
				String[] taskIds = ht.get(id);
				total = taskIds.length;

				final List<String> list = new ArrayList<String>();
				for (final String taskId : taskIds) {
					getTaskService().transSelect(null, taskId,
							new QueryObjectCallback<Task>() {
								@Override
								public void callback(Task t) {
									if (t == null) {
										list.add("R:" + taskId);
									} else {
										if (t.isDisplay()) {
											json.beginObject();
											json.setAttribute("id", t.getId());
											json.setAttribute("name",
													t.getName());
											json.setAttribute("publisherName",
													t.getPublisher().getName());
											json.setAttribute("publishDate",
													DateUtil.format(t
															.getPubishDate()));
											json.endObject();
										} else {
											list.add("R:" + taskId);
										}
									}
								}
							});
				}
				InterfaceServlet.putTasks(id, list);
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
	 * 获取机构信息列表 以树结构显示
	 */
	public void get() {
		String id = "";
		User u = getCurrentUser();
		if (u == null) {
			id = "root";
		} else {
			id = getCurrentUser().getParent().getId();
		}
		// if (!(id.equals("root"))) {
		// id = getCurrentUser().getParent().getParent().getId();
		// }
		final String type = getRequest().getParameter("type");
		final String style = getRequest().getParameter("style");
		final String deptIds = getRequest().getParameter("deptIds");

		final JSON json = new JSON(getResponse());
		getBranchService().transGetBranchById(null, id,
				new QueryObjectCallback<Branch>() {
					@Override
					public void callback(Branch objs) {
						json.beginArray();
						json.beginObject();
						json.setAttribute("id", objs.getId());
						json.setAttribute("text", objs.getName());
						json.setAttribute("expanded", true);
						json.beginAttribute("children");
						json.beginArray();
						createDepartments(json, objs, type, deptIds);
						json.endArray();
						json.endObject();
					}

					private int createDepartments(JSON json, Branch objs,
							String type, String deptIds) {
						List<Branch> dept = objs.getChild();
						int num = 0;
						for (Integer j = 0; j < dept.size(); j++) {
							num++;
							json.beginObject();
							String id = dept.get(j).getId();
							if (dept.get(j).getParent().getId().equals("root")) {
								json.setAttribute("expanded", true);
							}
							if (StringUtil.notEmpty(deptIds)) {
								String strs = deptIds.substring(0,
										deptIds.length() - 1);
								String[] strArr = strs.split(",");
								for (String s : strArr) {
									Machine m = getMachineService()
											.getObject(s);
									if (m.getParent().getId()
											.equals(dept.get(j).getId())) {
										json.setAttribute("checked", true);
									}
								}
								json.setAttribute("expanded", true);
							}
							json.setAttribute("id", id);
							String name = dept.get(j).getName();
							json.setAttribute("text", name);
							json.beginAttribute("children");
							json.beginArray();
							int sub = createDepartments(json, dept.get(j),
									type, deptIds);

							if (style.equals("branchlist")) {
								json.endArray();
								json.endAttribute();
								if (sub == 0) {
									json.setAttribute("leaf", true);
								}
								json.endObject();
							}
							if (style.equals("machinelist")) {
								if (sub == 0) {
									NamedConditions cond = new NamedConditions(
											"getMachineByPId");
									cond.putString("ParentId", dept.get(j)
											.getId());
									cond.putString("Type", type);
									List<Machine> list = getMachineService()
											.transSelectByCond(cond);
									for (Machine m : list) {
										json.beginObject();
										if (StringUtil.notEmpty(deptIds)) {
											String strs = deptIds.substring(0,
													deptIds.length() - 1);
											String[] strArr = strs.split(",");
											for (String s : strArr) {
												if (m.getId().equals(s)) {
													json.setAttribute(
															"checked", true);
												}
											}
										}
										json.setAttribute("id", m.getId());
										json.setAttribute("text", m.getName());
										json.setAttribute("leaf", true);
										json.endObject();
									}
								}
								json.endArray();
								json.endAttribute();
								json.endObject();
							}
						}
						return num;
					}
				});
		json.endArray();
		json.end();

	}

	/**
	 * 获取所有机构信息 以树结构显示
	 * 
	 * 机器管理中左侧显示的机构树
	 */
	public void getDepts() {
		String id = getCurrentUser().getParent().getId();

		final JSON json = new JSON(getResponse());
		getBranchService().transGetBranchById(null, id,
				new QueryObjectCallback<Branch>() {
					@Override
					public void callback(Branch objs) {
						json.beginArray();
						json.beginObject();
						json.setAttribute("id", objs.getId());
						json.setAttribute("text", objs.getName());
						json.setAttribute("expanded", true);
						json.beginAttribute("children");
						json.beginArray();
						createDepartments(json, objs);
						json.endArray();
						json.endObject();
					}

					private int createDepartments(JSON json, Branch objs) {
						List<Branch> dept = objs.getChild();
						int num = 0;
						for (Integer j = 0; j < dept.size(); j++) {
							num++;
							json.beginObject();
							String id = dept.get(j).getId();
							json.setAttribute("expanded", true);
							json.setAttribute("id", id);
							String name = dept.get(j).getName();
							json.setAttribute("text", name);
							json.beginAttribute("children");
							json.beginArray();
							int sub = createDepartments(json, dept.get(j));

							json.endArray();
							json.endAttribute();
							if (sub == 0) {
								json.setAttribute("leaf", true);
							}
							json.endObject();
						}
						return num;
					}
				});
		json.endArray();
		json.end();

	}

	/**
	 * 获取机器信息列表
	 */
	public void getMachines() {
		String type = getRequest().getParameter("type");
		String branchId = getRequest().getParameter("branchId");
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		NamedConditions cond = new NamedConditions("getMachineList");
		if (StringUtil.notEmpty(type) && !("全部".equals(type))) {
			cond.putString("Type", type);
		}
		if (StringUtil.notEmpty(branchId)) {
			String[] branchIdArr = getBranchService().getIdsByCurrentId(
					branchId);
			cond.putStringArray("BranchIds", branchIdArr);
		}

		getMachineService().transSelect(null, start, limit, cond,
				new QueryListCallback<Machine>() {
					@Override
					public void callback(int total, List<Machine> list) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setTotal(total);
						for (Machine m : list) {
							json.setAttribute("id", m.getId());
							json.setAttribute("name", m.getName());
							json.setAttribute("ip", m.getIp());
							json.setAttribute("type", m.getType());
							json.setAttribute("linkman", m.getLinkman());
							json.setAttribute("phone", m.getPhone());
							json.setAttribute("mobilephone", m.getMobilephone());
							json.setAttribute("parentName", m.getParent()
									.getName());
						}
						json.flush();
					}
				});
	}

	/**
	 * 获取用户列表
	 */
	public void getUsers() {
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		String id = getCurrentUser().getParent().getId();
		String[] branchIdArr = getBranchService().getIdsByCurrentId(id);
		NamedConditions cond = new NamedConditions("getUsersByParentId");
		cond.putStringArray("BranchIds", branchIdArr);
		cond.putString("ParentId", id);
		getUserService().transSelectByCond(null, start, limit, cond,
				new QueryListCallback<User>() {
					@Override
					public void callback(int total, List<User> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setTotal(total);
						for (User u : objs) {
							json.setAttribute("id", u.getId());
							json.setAttribute("name", u.getName());
							json.setAttribute("parentName", u.getParent()
									.getName());
						}
						json.flush();
					}
				});
	}

	/**
	 * 对机构 处理
	 */
	public void createBranch() {
		String id = getRequest().getParameter("id");
		String type = getRequest().getParameter("type");
		final String name = getRequest().getParameter("name");
		try {
			if (type.equals("createX")) {// 新增下级
				getBranchService().transGetBranchById(null, id,
						new QueryObjectCallback<Branch>() {
							@Override
							public void callback(Branch objs) {
								Branch b = new Branch();
								b.setId(UUID.randomUUID().toString());
								b.setName(name);
								b.setParent(objs);
								getBranchService().transCreateBranch(null, b);
								objs.getChild().add(b);
							}
						});
			} else if (type.equals("createRoot")) {// 新增顶层节点
				User u = getCurrentUser();
				String pid = "";
				if (u == null) {
					pid = "root";
				} else {
					pid = u.getParent().getId();
				}
				getBranchService().transGetBranchById(null, pid,
						new QueryObjectCallback<Branch>() {
							@Override
							public void callback(Branch objs) {
								Branch b = new Branch();
								b.setId(UUID.randomUUID().toString());
								b.setName(name);
								b.setParent(objs);
								getBranchService().transCreateBranch(null, b);
								objs.getChild().add(b);
							}
						});
			}
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "机构添加失败！");
			e.printStackTrace();
		}
	}

	public void modifyBranch() {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		final String name = request.getParameter("name");
		try {
			getBranchService().transUpdateBranch(null, id, name);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "机构修改失败！");
			e.printStackTrace();
		}
	}

	public void deleteBranch() {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		try {
			getBranchService().transDeleteBranch(null, id);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "该机构与其他对象有关联，则删除失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 对机器 处理
	 * 
	 * 网点注册
	 */
	public void editMachine() {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String ip = request.getParameter("ip");
		if (!(StringUtil.notEmpty(ip))) {
			ip = getRequest().getRemoteAddr();
		}
		String linkman = request.getParameter("linkman");
		String phone = request.getParameter("phone");
		String mobilephone = request.getParameter("mobilephone");

		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("pid", pid);
		map.put("name", name);
		map.put("type", type);
		map.put("ip", ip);
		map.put("linkman", linkman);
		map.put("phone", phone);
		map.put("mobilephone", mobilephone);

		try {
			getMachineService().transEditMachine(null, map);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "编辑机器失败！");
			e.printStackTrace();
		}
	}

	public void deleteMachine() {
		HttpServletRequest request = getRequest();
		String[] ids = request.getParameterValues("ids");
		try {
			getMachineService().transDeleteMachine(null, ids);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "该机器与其他对象有关联，则删除失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 对用户 处理
	 */
	public void createUser() {
		HttpServletRequest request = getRequest();
		String pid = request.getParameter("pid");
		String name = request.getParameter("name");
		try {
			User u = new User();
			u.setId(UUID.randomUUID().toString());
			u.setName(name);
			u.setPassword(MD5.Sha1("123456"));
			u.setParent(getBranchService().getObjectById(pid));
			getUserService().transCreate(null, u);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "用户添加失败！");
			e.printStackTrace();
		}
	}

	public void deleteUser() {
		HttpServletRequest request = getRequest();
		String[] ids = request.getParameterValues("ids");
		try {
			getUserService().transDelete(null, ids);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "该用户与其他对象有关联，则删除失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 实时监控 列表
	 */
	public void getPc() {
		HttpServletRequest request = getRequest();
		String ip = request.getParameter("ip");
		String status = request.getParameter("status");
		String type = request.getParameter("type");
		String dept = request.getParameter("dept");
		String location = request.getParameter("location");
		String version = request.getParameter("version");
		String versionEnabled = request.getParameter("versionEnabled");
		String taskName = request.getParameter("taskName");
		String taskNameEnabled = getRequest().getParameter("taskNameEnabled");
		String taskStatus = getRequest().getParameter("taskStatus");
		String fileName = request.getParameter("fileName");
		String fileNameEnabled = getRequest().getParameter("fileNameEnabled");

		long times = Long.parseLong(request.getServletContext()
				.getInitParameter("times")) * 1000;

		JSON json = new JSON(getResponse());
		json.beginObject();
		json.beginAttribute("root");
		json.beginArray();

		Properties cond = new Properties();
		cond.setProperty("ip", ip);
		cond.setProperty("status", status);
		cond.setProperty("type", type);
		cond.setProperty("dept", dept);
		cond.setProperty("location", location);
		cond.setProperty("version", version);
		cond.setProperty("versionEnabled", versionEnabled);
		cond.setProperty("taskName", taskName);
		cond.setProperty("taskNameEnabled", taskNameEnabled);
		cond.setProperty("taskStatus", taskStatus);
		cond.setProperty("fileName", fileName);
		cond.setProperty("fileNameEnabled", fileNameEnabled);
		cond.setProperty("times", "" + times);
		List<MachineCache> list = getCacheService().queryMachine(cond);
		for (MachineCache mc : list) {
			pcJsonList(json, mc, times);
		}

		json.endArray();
		json.endAttribute();
		json.setAttribute("success", true);
		json.setAttribute("total", list.size());
		json.endObject();
		json.end();
	}

	private void pcJsonList(JSON json, MachineCache mc, long times) {
		json.beginObject();
		json.setAttribute("id", mc.getId());// 机器ID
		json.setAttribute("machineName", mc.getName());// 网点名称

		json.setAttribute(
				"machineStatus",
				mc.getType() + "　" + mc.getBootStatus(times) + "　"
						+ (mc.getVersion() == null ? "" : mc.getVersion()));// 机器状态——>'类型、开关机、版本号'

		json.setAttribute("currentTaskStatus", mc.getTask() == null ? "" : ("["
				+ mc.getTaskStatus() + "]" + mc.getTask().getName()));// 当前任务状态——>'任务名称、任务状态'
		json.setAttribute("taskId",
				StringUtil.notEmpty(mc.getTaskId()) ? mc.getTaskId() : "");// 当前任务的ID

		if (mc.getDocument() != null) {
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMinimumFractionDigits(2);
			String persent = nf.format(((double) mc.getDownSize())
					/ ((double) mc.getDocument().getLength()));
			String length = StringUtil.returnLength(
					mc.getDownSize().intValue(), false)
					+ "/"
					+ StringUtil.returnLength((int) mc.getDocument()
							.getLength(), false);
			json.setAttribute("downloadStatus", "["
					+ (persent.length() == 5 ? (" " + persent) : persent) + "]"
					+ length + "　" + mc.getDocument().getName());// 当前下载状态——>'文件名称、下载进度百分比'
		} else {
			json.setAttribute("downloadStatus", "");
		}

		json.setAttribute(
				"bootTime",
				mc.getBootTime() == null ? "" : DateUtil.format(mc
						.getBootTime()));// 本次开机时间
		json.setAttribute("ip", mc.getIp());// 机器IP
		json.setAttribute("relation", mc.getRelation());// 联系人信息
		json.endObject();
	}

	public boolean isOn(Date time, String status) {
		Date cdate = new Date(System.currentTimeMillis());
		final long times = Long.parseLong(getRequest().getServletContext()
				.getInitParameter("times")) * 1000;

		if ("0".endsWith(status)) {
			if (time == null) {
				return true;
			}
			if (cdate.getTime() - time.getTime() > times) {
				return true;
			} else {
				return false;
			}
		} else if ("1".endsWith(status)) {
			if (time == null) {
				return false;
			}
			if (cdate.getTime() - time.getTime() > times) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	public void isRes() {
		String uuid = getRequest().getParameter("UUID");
		Machine m = getMachineService().getObject(uuid);
		try {
			if (m == null) {
				getRequest().getSession().setAttribute("id", uuid);
				getRequest().getSession().setAttribute("ip",
						getRequest().getRemoteAddr());
				getResponse().sendRedirect("/bank/Register.html");
			} else {
				if (m.getType().equals("广告")) {
					getResponse().sendRedirect("/bank/Media1.html");
				} else if (m.getType().equals("培训")) {
					getRequest().getSession().setAttribute("id", uuid);
					getResponse().sendRedirect("/bank/Train1.html");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected String getIpAddr() {
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
