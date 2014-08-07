package cn.wizool.bank.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import cn.wizool.bank.common.FileUtil;
import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.common.PropertiesOutputStream;
import cn.wizool.bank.common.TaskScheduler;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.DateUtil.DateFormatType;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Config;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.util.store.NoticeManager;

@MultipartConfig(maxFileSize = 1024 * 1024 * 10 * 500)
public class InterfaceServlet extends PlatFormHttpServlet {

	private static final long serialVersionUID = 1L;

	private static NoticeManager nm = NoticeManager.Default();
	private static TaskScheduler scheduler = new TaskScheduler();
	public static ConcurrentSkipListMap<String, String[]> files = new ConcurrentSkipListMap<String, String[]>();
	public static ConcurrentSkipListMap<String, String[]> tasks = new ConcurrentSkipListMap<String, String[]>();
	private static Semaphore semaphore = new Semaphore(50);

	private Map<String, HttpServletResponse> list = new ConcurrentHashMap<String, HttpServletResponse>();

	/**
	 * 修改 系统参数 version与reboot 文件的值
	 */
	public void updateSystemParameter() {
		String version = getRequest().getParameter("version");
		String reboot = getRequest().getParameter("reboot");
		String loc = getRequest().getServletContext().getRealPath(
				File.separator);
		File file1 = new File(loc + "client" + File.separator + "version.txt");
		File file2 = new File(loc + "reboot.txt");
		try {
			FileUtil.write(version, file1);
			FileUtil.write(reboot, file2);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "修改系统参数失败!");
			e.printStackTrace();
		}
	}

	/**
	 * 获取 系统参数 version与reboot 文件的值
	 */
	public void getSystemParameter() {
		String loc = getRequest().getServletContext().getRealPath(
				File.separator);
		File file1 = new File(loc + "client" + File.separator + "version.txt");
		File file2 = new File(loc + "reboot.txt");
		String version = FileUtil.read(file1);
		String reboot = FileUtil.read(file2);

		JSON json = new JSON(getResponse());
		json.beginObject();
		json.setAttribute("version", version);
		json.setAttribute("reboot", reboot);
		json.endObject();
		json.end();
	}

	public static void putOneTask(String id, String tid) {
		List<String> list = new ArrayList<String>();
		list.add(tid);
		putTasks(id, list);
	}

	public void putTasks() {
		String[] ids = getRequest().getParameterValues("list");
		for (String id : ids) {
			String[] ptid = id.split(";");
			List<String> list = new ArrayList<String>();
			for (int i = 1; i < ptid.length; i++) {
				list.add(ptid[i]);
			}
			putTasks(ptid[0], list);
		}
	}

	public static void putTasks(String id, List<String> tids) {
		nm.getStore(id, true).putOneProduct(tids);
	}

	@SuppressWarnings("unchecked")
	public void getTask() {
		String id = getRequest().getParameter("id");
		getResponse().setContentType("text/plain");
		getResponse().setCharacterEncoding("gbk");
		List<String> product = null;
		String last = null;
		ServletOutputStream out = null;
		try {
			out = getResponse().getOutputStream();
			int i = getResponse().getBufferSize();
			while (i-- != 0) {
				out.write(' ');
			}
		} catch (IOException e) {

		}
		while ((product = (List<String>) nm.getStore(id, true).getProduct(
				getRequest())) != null) {
			synchronized (product) {
				try {
					while (product.size() > 0) {
						last = product.remove(0);
						this.writeCommand(last, out);
					}
					out.flush();
					getResponse().flushBuffer();
					// this.getCacheService().active(id);
				} catch (IOException e) {
					if (last != null && !last.equals("empty")) {
						product.add(last);
						nm.getStore(id, true).putProduct(product);
					}
					break;
				}
			}
		}
	}

	public static TaskScheduler getScheduler() {
		return scheduler;
	}

	public static void setScheduler(TaskScheduler scheduler) {
		InterfaceServlet.scheduler = scheduler;
	}

	/**
	 * 设置机器活动时间
	 */
	public void active() {
		String id = getRequest().getParameter("id");
		this.getCacheService().active(id);
	}

	/**
	 * 删除机器 任务信息
	 * 
	 * @throws IOException
	 */
	public void removeTask() throws IOException {
		String id = getRequest().getParameter("id");
		String tid = getRequest().getParameter("tid");
		this.getCacheService().removeTask(id, tid);
	}

	/**
	 * 删除机器 文件信息
	 * 
	 * @throws IOException
	 */
	public void removeFile() throws IOException {
		String id = getRequest().getParameter("id");
		String fid = getRequest().getParameter("tid");
		this.getCacheService().removeFile(id, fid);
	}

	/**
	 * 修改任务状态
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void taskStatus() throws IOException, InterruptedException {
		String tid = getRequest().getParameter("tid");
		String id = getRequest().getParameter("id");
		String status = getRequest().getParameter("status");
		this.getCacheService().taskStatus(id, tid, status);
	}

	/**
	 * 添加版本号、上次关机时间、本次开机时间
	 * 
	 * 当前任务设置为空、删除下载进度未完成的日志
	 * 
	 * @throws IOException
	 */
	public void boot() throws IOException {
		String version = getRequest().getParameter("version");
		String ip = getRequest().getRemoteAddr();
		Map<String, String> map = getMachineService().getMachineByIp(ip);
		getResponse().getWriter().print(map.get("id") + ":" + map.get("type"));
		this.getCacheService().boot(map.get("id"), version);
	}

	/**
	 * 返回利率牌播放频率
	 * 
	 * @throws IOException
	 */
	public void getFrequency() throws IOException {
		HttpServletResponse response = getResponse();
		response.setContentType("application/binary");
		final Map<String, String> map = new HashMap<String, String>();
		NamedConditions c = new NamedConditions("getAll");
		getConfigService().transSelectAll(null, c,
				new QueryListCallback<Config>() {
					@Override
					public void callback(int total, List<Config> objs) {
						for (Config c : objs) {
							map.put(c.getName(), c.getValue());
						}
					}
				});
		PropertiesOutputStream po = new PropertiesOutputStream(
				response.getOutputStream(), "GBK");
		if (map.size() > 0) {
			for (String s : map.keySet()) {
				if (s.equals("frequency")) {
					po.setProperty(s, map.get(s));
				}
			}
		}
		po.close();
	}

	/**
	 * 返回滚动信息 利率与广告信息
	 * 
	 * @throws IOException
	 */
	public void getRoll() throws IOException {
		HttpServletResponse response = getResponse();
		String deptId = getRequest().getParameter("id");
		response.setContentType("application/binary");
		PropertiesOutputStream po = new PropertiesOutputStream(
				response.getOutputStream(), "GBK");

		String str = getConfigs();
		Config c = getConfigService().selectById("message");
		if (c != null) {
			if (StringUtil.notEmpty(c.getValue())) {
				str += "                    广告信息:" + c.getValue()
						+ "             ";
			}
		}
		Machine dept = getMachineService().getObject(deptId);
		if (dept != null) {
			if (dept.getType().equals("广告")) {
				po.setProperty("type", "media");
			} else if (dept.getType().equals("培训")) {
				po.setProperty("type", "train");
			}
		}
		po.setProperty("message", str);
		po.close();
	}

	private String getConfigs() {
		final Map<String, String> map = new HashMap<String, String>();
		NamedConditions c = new NamedConditions("getAll");
		getConfigService().transSelectAll(null, c,
				new QueryListCallback<Config>() {
					@Override
					public void callback(int total, List<Config> objs) {
						for (Config c : objs) {
							map.put(c.getName(), c.getValue());
						}
					}
				});
		return getStr(map);
	}

	private String getStr(Map<String, String> map) {
		String str = "";
		for (String s : map.keySet()) {
			if (s.equals("regularThreeMonth")) {
				str += "定期整存整取(三个月)：" + map.get(s) + "%    ";
			} else if (s.equals("regularSixMonth")) {
				str += "定期整存整取(六个月)：" + map.get(s) + "%    ";
			} else if (s.equals("regularOneYear")) {
				str += "定期整存整取(一年)：" + map.get(s) + "%    ";
			} else if (s.equals("regularTwoYear")) {
				str += "定期整存整取(两年)：" + map.get(s) + "%    ";
			} else if (s.equals("regularThreeYear")) {
				str += "定期整存整取(三年)：" + map.get(s) + "%    ";
			} else if (s.equals("regularFiveYear")) {
				str += "定期整存整取(五年)：" + map.get(s) + "%    ";
			} else if (s.equals("current")) {
				str += "活期：" + map.get(s) + "%    ";
			} else if (s.equals("agreementDeposit")) {
				str += "协定存款" + map.get(s) + "%    ";
			} else if (s.equals("accessOneYear")) {
				str += "零、整存取(一年)：" + map.get(s) + "%    ";
			} else if (s.equals("accessThreeYear")) {
				str += "零、整存取(三年)：" + map.get(s) + "%    ";
			} else if (s.equals("accessFiveYear")) {
				str += "零、整存取(五年)：" + map.get(s) + "%    ";
			} else if (s.equals("educationOneYear")) {
				str += "教育储蓄、免征税(一年)：" + map.get(s) + "%    ";
			} else if (s.equals("educationThreeYear")) {
				str += "教育储蓄、免征税(三年)：" + map.get(s) + "%    ";
			} else if (s.equals("educationSixYear")) {
				str += "教育储蓄、免征税(六年)：" + map.get(s) + "%    ";
			} else if (s.equals("noticeDepositOneDay")) {
				str += "通知存款(一天)：" + map.get(s) + "%    ";
			} else if (s.equals("noticeDepositSevenDay")) {
				str += "通知存款(七天)：" + map.get(s) + "%    ";
			} else if (s.equals("discount")) {
				str += "折扣：" + map.get(s) + "    ";
			} else if (s.equals("shortLoansThreeMonth")) {
				str += "短期贷款(六个月以内)：" + map.get(s) + "%    ";
			} else if (s.equals("shortLoansSixMonth")) {
				if (map.get(s).equals("无")) {
					str += "短期贷款(六个月至一年)：" + map.get(s) + "       ";
				} else {
					str += "短期贷款(六个月至一年)：" + map.get(s) + "%    ";
				}
			}
			// else if (s.equals("longLoansSixMonth")) {
			// str += "中长期贷款(六个月至一年)：" + map.get(s) + "%    ";
			// }
			else if (s.equals("longLoansOneToThree")) {
				str += "中长期贷款(一年至三年[含])：" + map.get(s) + "%    ";
			} else if (s.equals("longLoansThreeToFive")) {
				str += "中长期贷款(三年至五年[含])：" + map.get(s) + "%    ";
			} else if (s.equals("longLoansFiveMore")) {
				str += "中长期贷款(五年以上[含])：" + map.get(s) + "%    ";
			}
		}
		return str;
	}

	/**
	 * 返回除利率播放频率之外的利率信息
	 * 
	 * @throws IOException
	 */
	public void getRate() throws IOException {
		HttpServletResponse response = getResponse();
		String deptId = getRequest().getParameter("id");
		PropertiesOutputStream po = new PropertiesOutputStream(
				response.getOutputStream(), "GBK");
		response.setContentType("application/binary");
		final Map<String, String> map = new HashMap<String, String>();
		NamedConditions c = new NamedConditions("getAll");
		getConfigService().transSelectAll(null, c,
				new QueryListCallback<Config>() {
					@Override
					public void callback(int total, List<Config> objs) {
						for (Config c : objs) {
							map.put(c.getName(), c.getValue());
						}
					}
				});
		Machine dept = getMachineService().getObject(deptId);
		if (dept != null) {
			if (dept.getType().equals("广告")) {
				po.setProperty("type", "media");
			} else if (dept.getType().equals("培训")) {
				po.setProperty("type", "train");
			}
		}
		if (map.size() > 0) {
			for (String s : map.keySet()) {
				if (!(s.equals("frequency"))) {
					po.setProperty(s, map.get(s));
				}
			}
		}
		po.close();
	}

	/**
	 * 返回任务播放列表
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void getDesc() throws IOException, Exception {
		HttpServletResponse response = getResponse();
		response.setContentType("application/binary");
		String id = getRequest().getParameter("id");
		String tid = getRequest().getParameter("tid");
		this.getCacheService().writeDescription(id, tid,
				response.getOutputStream());
	}

	/**
	 * 获取机器任务列表 D:/ROOT
	 */
	public void taskList() {
		String id = getRequest().getParameter("id");
		String parameter = getRequest().getParameter("ids");
		if (!parameter.equals("")) {
			String taskIds[] = parameter.split(";");
			tasks.put(id, taskIds);
		} else {
			tasks.put(id, new String[0]);
		}
	}

	/**
	 * 获取机器文件列表 D:/TMP
	 */
	public void fileList() {
		String id = getRequest().getParameter("id");
		String parameter = getRequest().getParameter("ids");
		if (!parameter.equals("")) {
			String docIds[] = parameter.split(";");
			files.put(id, docIds);
		} else {
			files.put(id, new String[0]);
		}
	}

	public boolean writeCommand(String command, OutputStream response)
			throws IOException {
		if (command.indexOf('<') == -1 && command.indexOf('>') == -1) {
			response.write('<');
			response.write(command.getBytes("GBK"));
			response.write(':');
			response.write(MD5.Md5(command).getBytes("GBK"));
			response.write('>');
			return true;
		}
		return false;
	}

	public long toLong(byte[] b) {
		long l = 0;
		l = b[0];
		l |= ((long) b[1] << 8);
		l |= ((long) b[2] << 16);
		l |= ((long) b[3] << 24);
		l |= ((long) b[4] << 32);
		l |= ((long) b[5] << 40);
		l |= ((long) b[6] << 48);
		l |= ((long) b[7] << 56);
		return l;
	}

	public byte[] toByteArray(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Long(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	/**
	 * 下载文件 返回信息
	 * 
	 * @throws Exception
	 */
	public void download() throws Exception {
		String id = getRequest().getParameter("id");
		String fid = getRequest().getParameter("fid");
		String down = getRequest().getParameter("down");
		OutputStream os = getResponse().getOutputStream();
		if ("info".equals(down)) {
			Document doc = getDocumentService().getDocById(fid);
			PropertiesOutputStream prop = new PropertiesOutputStream(os, "gbk");
			if (doc == null) {
				prop.setProperty("exist", "false");
			} else {
				prop.setProperty("exist", "true");
				prop.setProperty("size", "" + doc.getLength());
				if (!(StringUtil.notEmpty(doc.getMd5()))) {
					String loc = getRequest().getServletContext()
							.getInitParameter("filepath")
							+ File.separator
							+ fid;
					String md5 = MD5.byteArrayToHex(FileUtil.md5(loc));
					doc.setMd5(md5);
					getDocumentService().transUpdate(null, doc);
				}
				prop.setProperty("md5", doc.getMd5());
			}
			prop.close();
		} else {
			if (down != null && down.equals("file")) {
				int start = Integer
						.parseInt(getRequest().getParameter("start"));
				int count = Integer
						.parseInt(getRequest().getParameter("count"));
				int bufln = Integer.parseInt(getRequest()
						.getParameter("buffer"));

				semaphore.acquire();
				InputStream in = null;
				try {
					MessageDigest sha = MessageDigest.getInstance("MD5");
					String loc = getRequest().getServletContext()
							.getInitParameter("filepath")
							+ File.separator
							+ fid;
					File file = new File(loc);
					in = new FileInputStream(file);
					in.skip(start * bufln);

					byte[] buffer = new byte[bufln];

					long rlen = bufln;

					int i = 0;
					while (i++ < count) {
						if ((start + 1) * bufln > file.length()) {
							rlen = file.length() - (start * bufln);
						}
						in.read(buffer, 0, (int) rlen);
						sha.update(buffer, 0, (int) rlen);
						os.write('S');
						os.write(toByteArray(rlen));
						os.write(buffer, 0, (int) rlen);
						os.write(sha.digest());
						os.flush();
						getCacheService().downfile(id, fid,
								start * bufln + rlen);
						start++;
						Thread.sleep(20);
					}
				} catch (SocketException e) {
				} catch (FileNotFoundException e) {
					os.write('A');
					os.flush();
				} catch (Exception e) {
				} finally {
					if (in != null) {
						in.close();
					}
					semaphore.release();
				}
			}
		}
	}

	public void downFile() {
		String[] ids = getRequest().getParameterValues("list");
		String id = ids[0];
		String[] ptid = id.split(";");
		List<String> list1 = new ArrayList<String>();
		for (int i = 1; i < ptid.length; i++) {
			list1.add(ptid[i]);
		}
		putTasks(ptid[0], list1);

		HttpServletResponse res = getResponse();
		synchronized (res) {
			list.put(ptid[0], res);
			try {
				res.wait(60 * 1000);
			} catch (InterruptedException e) {

			}
			list.remove(ptid[0]);
		}
	}

	/**
	 * 上传机器截屏图片，保存到服务器指定路径
	 */
	public void upload() {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		try {
			request.setCharacterEncoding("utf-8");
			Part p = request.getPart("file");
			String name = getName(getFileName(p, "UTF-8"));
			Machine m = getMachineService().getObject(id);
			if (m != null) {
				HttpServletResponse res = list.get(id);
				String fileName = DateUtil.format(new Date(),
						DateFormatType.FULL_TYPE)
						+ "-"
						+ (m.getParent().getParent() == null ? "" : m
								.getParent().getParent().getName()
								+ "-")
						+ m.getParent().getName()
						+ "-"
						+ m.getName() + "-" + name;
				if (res != null) {
					synchronized (res) {
						res.setContentType("application/octet");
						res.setHeader(
								"Content-Disposition",
								"attachment;filename="
										+ URLEncoder.encode(fileName, "UTF-8"));
						FileUtil.copy(p.getInputStream(), res.getOutputStream());
						res.notify();
					}
				} else {

					String loc = request.getServletContext().getInitParameter(
							"uploaddir");
					File file = new File(loc + File.separator + fileName);
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					p.write(file.getAbsolutePath());
				}
			}
		} catch (Exception e) {
		}
	}

	private String getName(String str) {
		if (str.lastIndexOf("\\") != -1) {
			str = str.substring(str.lastIndexOf("\\") + 1);
		}
		if (str.lastIndexOf("/") != -1) {
			str = str.substring(str.lastIndexOf("/") + 1);
		}

		return str;
	}

	/**
	 * 实时监控页面 获取服务器时间
	 */
	public void getDate() {
		JSON json = new JSON(getResponse());
		json.beginObject();
		json.setAttribute("date", DateUtil.format(new Date()));
		json.endObject();
		json.end();
	}

}
