package cn.wizool.bank.servlet;

import java.util.List;

import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.FileDownload;

public class FileDownloadServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = 1899684402826066726L;

	/**
	 * 下载进度列表
	 */
	public void getAll() {
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		getFileDownloadService().transSelectAll(null, start, limit, getCond(),
				new QueryListCallback<FileDownload>() {
					@Override
					public void callback(int total, List<FileDownload> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setTotal(total);
						json.setSuccess(true);
						for (FileDownload fd : objs) {
							if (fd.getDepartment() != null) {
								json.setAttribute("id", fd.getId());
								json.setAttribute("name",
										fd.getDepartment() == null ? "" : fd
												.getDepartment().getName());
								json.setAttribute(
										"location",
										fd.getDepartment().getParent() == null ? ""
												: fd.getDepartment()
														.getParent().getName());
								json.setAttribute("type",
										fd.getDepartment() == null ? "" : fd
												.getDepartment().getType());
								json.setAttribute("dept", fd.getDepartment()
										.getParent().getParent() == null ? ""
										: fd.getDepartment().getParent()
												.getParent().getName());
								json.setAttribute(
										"length",
										fd.getDocument() == null ? ""
												: StringUtil
														.returnLength((int) fd
																.getDocument()
																.getLength()));
								json.setAttribute("docName",
										fd.getDocument() == null ? "" : fd
												.getDocument().getName());
								json.setAttribute(
										"start",
										fd.getStart() == null ? "" : DateUtil
												.format(fd.getStart()));
								if (fd.getEnd() != null) {
									json.setAttribute(
											"end",
											fd.getEnd() == null ? "" : DateUtil
													.format(fd.getEnd()));
								}
							}
						}
						json.flush();
					}
				});
	}

	/**
	 * 删除下载进度
	 * 
	 * 过滤条件
	 */
	public void deleteFileDownload() {
		String style = getRequest().getParameter("style");
		String[] ids = getRequest().getParameterValues("ids");
		try {
			if (style.equals("deleteAll")) {
				getFileDownloadService().transDeleteAll(null, getCond());
			} else if (style.equals("deleteSelected")) {
				getFileDownloadService().transDeleteSelected(null, ids);
			}

			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "删除失败！");
			e.printStackTrace();
		}
	}

	private NamedConditions getCond() {
		String type = getRequest().getParameter("type");
		String dept = getRequest().getParameter("dept");
		String location = getRequest().getParameter("location");
		String docName = getRequest().getParameter("docName");
		NamedConditions cond = new NamedConditions("getAll");

		if (StringUtil.notEmpty(type) && !("全部".equals(type))) {
			cond.putString("Type", type);
		}
		if (StringUtil.notEmpty(location)) {
			cond.putString("Location", "%" + location + "%");
		}
		if (StringUtil.notEmpty(dept)) {
			cond.putString("Dept", "%" + dept + "%");
		}
		if (StringUtil.notEmpty(docName)) {
			cond.putString("DocName", "%" + docName + "%");
		}
		return cond;
	}

}
