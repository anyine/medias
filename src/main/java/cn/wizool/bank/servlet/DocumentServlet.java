package cn.wizool.bank.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import jxl.Sheet;
import jxl.Workbook;
import cn.wizool.bank.common.FileUtil;
import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.FileCopy;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.JSONArray;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;

@MultipartConfig(maxFileSize = 1024 * 1024 * 10 * 500)
@WebServlet("/upload")
public class DocumentServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = 8208940387142781485L;

	/**
	 * 获取除类型为“背景图片”和“选中背景图片”外的所有文件列表
	 */
	public void get() {
		final HttpServletResponse response = getResponse();
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		String fileName = getRequest().getParameter("fileName");
		String type = getRequest().getParameter("type");
		NamedConditions cond = new NamedConditions("getFileShare");
		if (StringUtil.notEmpty(fileName)) {
			cond.putString("Name", "%" + fileName + "%");
		}
		if (StringUtil.notEmpty(type) && !("全部".equals(type))) {
			cond.putString("Type", type);
		}
		getDocumentService().transSelectAll(null, start, limit, cond,
				new QueryListCallback<Document>() {
					@Override
					public void callback(int total, List<Document> objs) {
						JSONArray json = new JSONArray(response);
						json.setSuccess(true);
						json.setTotal(total);
						for (Document doc : objs) {
							json.setAttribute("id", doc.getId());
							json.setAttribute("name", doc.getName());
							json.setAttribute("uploaddate",
									DateUtil.format(doc.getUploadDate()));
							json.setAttribute("length", StringUtil
									.returnLength((int) doc.getLength()));
							json.setAttribute("type", doc.getType());
							if (doc.getUpload_publisher() != null) {
								json.setAttribute("user", doc.getUpload_publisher().getName());
								json.setAttribute("dept", doc.getUpload_publisher()
										.getParent().getName());
							}
							json.setAttribute("state", doc.getState());
						}
						json.flush();
					}
				});
	}

	/**
	 * 根据地址获取地址下的文件列表
	 */
	public void getByPath() {
		String path = getRequest().getParameter("path");
		File f = new File(path);
		File files[] = f.listFiles();
		if (files == null) {
			files = new File[0];
		}
		JSONArray json = new JSONArray(getResponse());
		json.setSuccess(true);
		json.setMessage("");
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				json.setAttribute("name", files[i].getName());
				json.setAttribute("length",
						StringUtil.returnLength((int) files[i].length()));
				long times = files[i].lastModified();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				java.util.Date dt = new Date(times);
				String sDateTime = sdf.format(dt);
				json.setAttribute("last", sDateTime.toString());
			}
		}
		json.flush();
	}

	/**
	 * 将选中的服务器文件拷贝到服务器D:/TMP路径下
	 */
	public void copy() {
		try {
			String path = getRequest().getParameter("path");
			String names = getRequest().getParameter("names");
			String local = getRequest().getServletContext().getInitParameter(
					"filepath");
			File f = new File(path);
			File files[] = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				long l = 0;
				String id = MD5.byteArrayToHex(FileUtil.md5(path
						+ File.separator + names));

				String[] str = files[i].getName().split("\\.");
				if (names.indexOf(files[i].getName()) != -1
						&& (!new File(path + File.separator + names).exists())) {
					l = FileCopy.copyFile2(files[i], new File(local), id);
				}
				if (l > 0) {
					Document d = getDocumentService().getDocById(id);
					if (d == null) {
						d = new Document();
						d.setId(id);
						d.setName(files[i].getName());
						d.setLength(files[i].length());
						d.setUploadDate(new Date());
						d.setIndex(getDocumentService().selectAllCount() + 1);
						d.setSurfix(str[str.length - 1]);
						d.setMd5(id);
						d.setUpload_publisher(getCurrentUser());
						d.setType(StringUtil.getDocType(str[str.length - 1]));
						getDocumentService().transCreate(
								getCurrentUser().getId(), d);

						JSON.sendSuccess(getResponse(), true);
					}
				}
			}
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "拷贝失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 下载单个文件
	 */
	public void download() {
		String fid = getRequest().getParameter("id");
		final String path = getRequest().getServletContext().getInitParameter(
				"filepath");
		getDocumentService().transSelectModel(null, fid,
				new QueryObjectCallback<Document>() {
					@Override
					public void callback(Document objs) {
						if (objs != null) {
							String src = path + File.separator + objs.getId();
							// + "." + objs.getSurfix();
							HttpServletResponse response = getResponse();
							response.setCharacterEncoding("UTF-8");
							try {
								response.setHeader(
										"Content-Disposition",
										"attachment;filename="
												+ URLEncoder.encode(
														objs.getName(), "UTF-8"));
							} catch (UnsupportedEncodingException e1) {
								e1.printStackTrace();
							}
							response.setContentType("application/octet-stream");
							try {
								FileUtil.copy(src, getResponse()
										.getOutputStream());
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	/**
	 * 修改文件的状态
	 */
	public void modifyState() {
		String id = getRequest().getParameter("id");
		String state = getRequest().getParameter("state");
		try {
			Document doc = getDocumentService().getDocById(id);
			if (doc != null) {
				if (state.equals("正常")) {
					doc.setState(0);
				} else if (state.equals("暂停")) {
					doc.setState(1);
				} else if (state.equals("取消")) {
					doc.setState(2);
				}
			}
			getDocumentService().transUpdate(null, doc);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "修改状态有误！");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件 逻辑删除，但删除D:/TMP下的实际文件和所有机器的该文件
	 */
	public void delete() {
		String[] ids = getRequest().getParameterMap().get("ids");
		final String path = getRequest().getServletContext().getInitParameter(
				"filepath");
		try {
			getDocumentService().transDelete(null, ids, path);

			// 删除机器上的该文件
			List<Machine> machines = getMachineService().getAll();

			for (Machine m : machines) {
				List<String> list = new ArrayList<String>();
				for (String s : ids) {
					list.add("R:" + s);
				}
				InterfaceServlet.putTasks(m.getId(), list);
			}

			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "您选中的该文件与任务有关联，无法删除该文件！");
			e.printStackTrace();
		}
	}

	/**
	 * 上传文件 以MD5 为 ID
	 */
	public void upload() {
		HttpServletResponse response = getResponse();
		Collection<Part> x;
		int excepNum = 0;
		String ret = null;
		try {
			x = getRequest().getParts();
			int n = x.size();
			Iterator<Part> it = x.iterator();
			for (int i = 1; i < n; i++) {
				Part p = it.next();
				String[] strs = uploadFile(p);
				if ("文件重复".equals(strs[0])) {
					excepNum++;
				} else if (strs[0] == null) { // 返回错误信息为空表示成功
					Integer count = getDocumentService().selectAllCount();
					Document d = getDocumentService().getDocById(strs[3]);
					if (d != null) {
						d.setDisplay(true);
						getDocumentService().transUpdate(null, d);
					} else {
						d = new Document();
						d.setId(strs[3]);
						d.setMd5(strs[3]);
						d.setName(strs[1]);
						d.setIndex(count + 1);
						d.setSurfix(getFileEndName(strs[1]));
						d.setLength(p.getSize());
						d.setUploadDate(new Date());
						d.setUpload_publisher(getCurrentUser());
						d.setType(StringUtil
								.getDocType(getFileEndName(strs[1])));
						getDocumentService().transCreate(null, d);
					}
				} else {
					ret = strs[0];
					break;
				}
			}
			response.setContentType("application/octet-stream; charset=utf-8");
			if (ret != null) {
				JSON.sendErrorMessage(getResponse(), ret, true);
			} else if (excepNum == n - 1) {
				JSON.sendErrorMessage(getResponse(), "文件已存在！", true);
			} else {
				JSON.sendSuccess(getResponse(), true);
			}
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "上传文件失败！");
			e.printStackTrace();
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
			String fileName = getFileName(p);
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
	 * 删除背景图片
	 */
	public void deleteBack() {
		String[] ids = getRequest().getParameterMap().get("ids");
		final String path = getRequest().getServletContext().getInitParameter(
				"filepath");
		try {
			getDocumentService().transBackDel(null, ids, path);
			// 删除机器上的该文件
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "操作失败");
			e.printStackTrace();
		}
	}

	/**
	 * 上传背景图片
	 */
	public void uploadBackGround() {
		HttpServletResponse response = getResponse();
		Collection<Part> x;
		int excepNum = 0;
		String ret = null;
		try {
			x = getRequest().getParts();
			int n = x.size();
			Iterator<Part> it = x.iterator();
			for (int i = 1; i < n; i++) {

				Part p = it.next();
				String[] strs = uploadFile(p);

				if ("文件重复".equals(strs[0])) {
					excepNum++;
				} else if (strs[0] == null) { // 返回错误信息为空表示成功
					Integer count = getDocumentService().selectAllCount();
					Document d = getDocumentService().getDocById(strs[3]);
					if (d != null) {
						d.setDisplay(true);
						getDocumentService().transUpdate(null, d);
					} else {
						d = new Document();
						d.setId(strs[3]);
						d.setMd5(strs[3]);
						d.setName(strs[1]);
						d.setIndex(count + 1);
						d.setSurfix(getFileEndName(strs[1]));
						d.setLength(p.getSize());
						d.setUploadDate(new Date());
						d.setUpload_publisher(getCurrentUser());
						d.setType("背景图片");
						getDocumentService().transCreate(null, d);
					}
				} else {
					ret = strs[0];
					break;
				}
			}
			response.setContentType("application/octet-stream; charset=utf-8");
			if (ret != null) {
				JSON.sendErrorMessage(getResponse(), ret, true);
			} else if (excepNum == n - 1) {
				JSON.sendErrorMessage(getResponse(), "文件已存在！", true);
			} else {
				JSON.sendSuccess(getResponse(), true);
			}
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "上传文件失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 获得全部背景图片
	 */
	public void getBackGroundFile() {
		String[] backCd = { "背景图片", "选中作为背景" };
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		NamedConditions cd = new NamedConditions();
		cd.setType("getBackGroundFile");
		cd.putStringArray("Types", backCd);
		getDocumentService().getBackGroundFile(start, limit, cd,
				new QueryListCallback<Document>() {

					@Override
					public void callback(int total, List<Document> objs) {
						JSONArray json = new JSONArray(getResponse());
						json.setSuccess(true);
						json.setTotal(total);
						for (Document doc : objs) {
							json.setAttribute("id", doc.getId());
							json.setAttribute("name", doc.getName());
							json.setAttribute("uploaddate",
									DateUtil.format(doc.getUploadDate()));
							json.setAttribute("length", StringUtil
									.returnLength((int) doc.getLength()));
							json.setAttribute("type", doc.getType());
							json.setAttribute("user", doc.getUpload_publisher()
									.getName());
							json.setAttribute("dept", doc.getUpload_publisher()
									.getParent().getName());
						}
						json.flush();
					}

				});
	}

	/**
	 * 选中作为背景图片
	 */
	public void chooseForBack() {
		final String path = getRequest().getServletContext().getInitParameter(
				"filepath");
		try {
			String id = getRequest().getParameter("id");
			Document doc = getDocumentService().getDocById(id);
			String loc = getRequest().getServletContext().getInitParameter(
					"backgroundImgPath");
			File outputDir = new File(loc);
			// File outputDir = new File("D:/BackGroundImg");
			if (!outputDir.exists()) {
				outputDir.mkdir();
			} else {
				deleteFile(outputDir);
				String src = path + File.separator + doc.getId();
				String file = outputDir.getAbsolutePath() + File.separator
						+ doc.getId() + "." + doc.getSurfix();
				FileUtil.copy(src, file);
				getDocumentService().checkForBack(id);
			}
			JSON.sendSuccess(getResponse());

		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "操作失败");
			e.printStackTrace();
		}
	}

	/**
	 * 获得选中背景图片
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void getCheckBack() throws FileNotFoundException, IOException {
		NamedConditions cd = new NamedConditions();
		cd.setType("getBackGroundFile");
		cd.putStringArray("Types", new String[] { "选中作为背景" });
		Document doc = this.getDocumentService().getDocByCondition(cd);

		String imgfile = null;

		if (doc != null) {
			imgfile = getRequest().getServletContext().getInitParameter(
					"filepath")
					+ File.separator + doc.getId();
			if (!new File(imgfile).exists()) {
				imgfile = null;
			}
		}

		if (imgfile == null) {
			imgfile = this.getRequest().getServletContext().getRealPath("/")
					+ "app/resource/image/login.jpg";
		}

		this.getResponse().setContentType("image/jpg");
		try {
			FileUtil.copy(imgfile, getResponse().getOutputStream());
		} catch (SocketException e) {
		}
	}

	/**
	 * 更新系统 和 导入文件Excel
	 */
	public void programUpload() {
		String succee = null;
		try {
			getRequest().setCharacterEncoding("utf-8");
			Part p = getRequest().getPart("url");
			String extension = getFileEndName(getFileName(p));
			if (extension.length() <= 0) {
				succee = "请重新选择文件";
			} else if (extension.equals("xls")) {
				String loc = getRequest().getServletContext().getRealPath(
						File.separator + "files.xls");
				File f = new File(loc);
				p.write(loc);
				if (!f.exists()) {
					succee = "上传失败，请重试！";
				} else {

					// 导入Excel
					try {
						InputStream is = new FileInputStream(loc);
						Workbook wbook = Workbook.getWorkbook(is);
						Sheet st = wbook.getSheet(0);
						int row = st.getRows();
						for (int i = 1; i < row; i++) {
							if (!(st.getCell(0, i).getContents().equals(""))) {
								Document doc = new Document();
								doc.setId(st.getCell(0, i).getContents());
								doc.setMd5(st.getCell(1, i).getContents());
								doc.setIndex(Integer.parseInt(st.getCell(2, i)
										.getContents()));
								doc.setLength(Long.parseLong(st.getCell(3, i)
										.getContents()));
								doc.setName(st.getCell(4, i).getContents());
								doc.setSurfix(st.getCell(5, i).getContents());
								doc.setType(StringUtil.getDocType(st.getCell(5,
										i).getContents()));

								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								doc.setUploadDate(sdf.parse(st.getCell(7, i)
										.getContents()));

								NamedConditions cond = new NamedConditions(
										"getUserByName");
								cond.putString("Name", "system");
								doc.setUpload_publisher(getUserService()
										.getUserByCond(cond));

								getDocumentService().transCreate(null, doc);
							}
						}

						wbook.close();
						is.close();
					} catch (Exception e) {
						JSON.sendErrorMessage(getResponse(), succee);
						e.printStackTrace();
					}
				}
			} else {
				String loc = getRequest().getServletContext().getRealPath(
						File.separator + "client");
				File dir = new File(loc);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				p.write(loc + File.separator + "client.zip");
				File judgeFile = new File(loc + File.separator + "client.zip");
				if (!judgeFile.exists()) {
					succee = "上传失败，请重试！";
					// } else {
					// File txt = new File(loc + File.separator +
					// "version.txt");
					// String content = MD5.byteArrayToHex(FileUtil.md5(loc
					// + File.separator + "client.zip"));
					// // String uid = UUID.randomUUID().toString();
					// FileUtil.write(content, txt);
				}
			}
			JSON.sendSuccess(getResponse(), true);
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "程序上传失败！");
			e.printStackTrace();
			succee = e.toString();
		}
	}

	/**
	 * 判断document表中是否含有MD5
	 * 
	 * @param md5
	 * @return 有true 无false
	 */
	private boolean isMD5(String md5) {
		boolean flag = false;
		List<Document> list = getDocumentService().getListAlls();
		for (Document d : list) {
			if (md5.equals(d.getMd5()))
				return true;
		}
		return flag;
	}

	private void deleteFile(File file) {
		File[] files = file.listFiles();
		for (File deleteFile : files) {
			if (deleteFile.isDirectory()) {
				// 如果是文件夹，则递归删除下面的文件后再删除该文件夹
				deleteFile(deleteFile);
			} else {
				deleteFile.delete();
			}
		}
	}

}
