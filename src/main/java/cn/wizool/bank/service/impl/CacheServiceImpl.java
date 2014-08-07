package cn.wizool.bank.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.wizool.bank.cache.DocumentCache;
import cn.wizool.bank.cache.MachineCache;
import cn.wizool.bank.cache.TaskCache;
import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.common.PropertiesOutputStream;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.FileDownload;
import cn.wizool.bank.model.FileLog;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.MachineTasks;
import cn.wizool.bank.model.Media;
import cn.wizool.bank.model.Notice;
import cn.wizool.bank.model.Task;
import cn.wizool.bank.model.TaskDocuments;
import cn.wizool.bank.model.TaskLog;
import cn.wizool.bank.model.Train;
import cn.wizool.bank.service.CacheService;

public class CacheServiceImpl extends PlatFormServiceSupport implements
		CacheService {
	private static ExecutorService es = Executors.newFixedThreadPool(300);

	private Map<String, MachineCache> machines = new ConcurrentHashMap<String, MachineCache>();
	private Map<String, DocumentCache> documents = new ConcurrentHashMap<String, DocumentCache>();
	private Map<String, TaskCache> tasks = new ConcurrentHashMap<String, TaskCache>();

	private MachineCache loadMachine(String id) {
		synchronized (machines) {
			MachineCache cache = machines.get(id);
			if (cache == null) {
				Machine m = this.getMachineDao().select(id);
				if (m == null) {
					System.out.println(id);
				} else {
					cache = load(m);
					machines.put(id, cache);
				}
			}
			return cache;
		}
	}

	public void modifyTask(String tid) {
		synchronized (tasks) {
			tasks.remove(tid);
			loadTask(tid);
		}
	}

	private MachineCache load(Machine m) {
		MachineCache cache = new MachineCache(m.getId());
		cache.setIp(m.getIp());
		cache.setType(m.getType());

		if (StringUtil.notEmpty(m.getLinkman())) {
			String linkman = m.getLinkman().replaceAll(" |　", "");
			if (linkman.length() < 3) {
				linkman += "　";
			}
			if (StringUtil.notEmpty(m.getPhone())) {
				linkman += "　" + m.getPhone();
			} else {
				linkman += "　0000000";
			}
			if (StringUtil.notEmpty(m.getMobilephone())) {
				linkman += "　" + m.getMobilephone();
			}
			cache.setRelation(linkman);
		}

		String name = (m.getParent().getParent() == null ? "" : m.getParent()
				.getParent().getName())
				+ "　" + m.getParent().getName();
		cache.setName(name);

		return cache;
	}

	private DocumentCache loadDocument(String id) {
		synchronized (documents) {
			DocumentCache cache = documents.get(id);
			if (cache == null) {
				cache = new DocumentCache(id);
				Document doc = this.getDocumentDao().select(id);
				cache.setName(doc.getName());
				cache.setLength(doc.getLength());
				documents.put(id, cache);
			}
			return cache;
		}
	}

	public List<MachineCache> queryMachine(Properties cond) {
		List<MachineCache> list = new ArrayList<MachineCache>();
		for (String key : machines.keySet()) {
			MachineCache mc = machines.get(key);

			if (StringUtil.notEmpty(mc.getFileId())) {
				mc.setDocument(loadDocument(mc.getFileId()));
				if (mc.getDownSize() == mc.getDocument().getLength()) {
					final String id = mc.getId();
					final String fid = mc.getFileId();
					final Date start = mc.getStartTime();
					final Date end = mc.getDownTime();
					es.submit(new Runnable() {
						@Override
						public void run() {
							transFileDownload(id, fid, start, end);
						}
					});
					mc.setFileId(null);
					mc.setDocument(null);
					mc.setDownSize(null);
				}
			}

			if (StringUtil.notEmpty(mc.getTaskId())) {
				mc.setTask(loadTask(mc.getTaskId()));
			}

			if (condition(mc, cond)) {
				list.add(mc);
			}
		}
		return list;
	}

	private boolean condition(MachineCache mc, Properties cond) {

		// 机器类型
		if (StringUtil.notEmpty(cond.getProperty("type"))
				&& !cond.getProperty("type").equals("全部")) {
			if (!mc.getType().equals(cond.getProperty("type"))) {
				return false;
			}
		}

		// 开关机
		if (StringUtil.notEmpty(cond.getProperty("status"))
				&& !cond.getProperty("status").equals("全部")) {
			if (!mc.getBootStatus(Long.parseLong(cond.getProperty("times")))
					.equals(cond.getProperty("status"))) {
				return false;
			}
		}

		// 联社名称
		if (StringUtil.notEmpty(cond.getProperty("dept"))
				&& !cond.getProperty("dept").equals("全部")) {
			if (!mc.getName().contains(cond.getProperty("dept"))) {
				return false;
			}
		}

		// 网点名称
		if (StringUtil.notEmpty(cond.getProperty("location"))) {
			if (!mc.getName().contains(cond.getProperty("location"))) {
				return false;
			}
		}

		// 机器IP
		if (StringUtil.notEmpty(cond.getProperty("ip"))) {
			if (!mc.getIp().contains(cond.getProperty("ip"))) {
				return false;
			}
		}

		// 机器版本号
		if (StringUtil.notEmpty(cond.getProperty("version"))) {
			String enabled = cond.getProperty("versionEnabled");
			if (StringUtil.notEmpty(enabled) && (Boolean.parseBoolean(enabled))) {
				// 不等于
				if (StringUtil.notEmpty(mc.getVersion())) {
					// 有版本号
					if (mc.getVersion().equals(cond.getProperty("version"))) {
						return false;
					}
				}
			} else {
				// 等于
				if (StringUtil.notEmpty(mc.getVersion())) {
					// 有版本号
					if (!mc.getVersion().equals(cond.getProperty("version"))) {
						return false;
					}
				} else {
					// 无版本号
					return false;
				}
			}
		}
		// 任务名称
		if (StringUtil.notEmpty(cond.getProperty("taskName"))) {
			String enabled = cond.getProperty("taskNameEnabled");
			if (StringUtil.notEmpty(enabled) && (Boolean.parseBoolean(enabled))) {
				// 不等于
				if (mc.getTask() != null) {
					// 有任务
					if (mc.getTask().getName()
							.equals(cond.getProperty("taskName"))) {
						return false;
					}
				}
			} else {
				// 等于
				if (mc.getTask() != null) {
					// 有任务
					if (!mc.getTask().getName()
							.equals(cond.getProperty("taskName"))) {
						return false;
					}
				} else {
					// 无任务
					return false;
				}
			}
		}

		// 任务状态
		if (StringUtil.notEmpty(cond.getProperty("taskStatus"))
				&& !cond.getProperty("taskStatus").equals("全部")) {
			if ("其他".equals(cond.getProperty("taskStatus"))) {
				if (mc.getTask() != null
						&& (StringUtil.notEmpty(mc.getTaskStatus()))) {
					return false;
				}
			} else {
				if (StringUtil.notEmpty(mc.getTaskStatus())) {
					// 有任务状态
					if (!mc.getTaskStatus().equals(
							cond.getProperty("taskStatus"))) {
						return false;
					}
				} else {
					// 无任务状态
					return false;
				}
			}
		}

		// 文件名称
		if (StringUtil.notEmpty(cond.getProperty("fileName"))) {
			String enabled = cond.getProperty("fileNameEnabled");
			if (StringUtil.notEmpty(enabled) && (Boolean.parseBoolean(enabled))) {
				// 不等于
				if (mc.getDocument() != null) {
					// 有文件
					if (mc.getDocument().getName()
							.equals(cond.getProperty("fileName"))) {
						return false;
					}
				}
			} else {
				// 等于
				if (mc.getDocument() != null) {
					// 有文件
					if (!mc.getDocument().getName()
							.equals(cond.getProperty("fileName"))) {
						return false;
					}
				} else {
					// 无文件
					return false;
				}
			}
		}

		return true;
	}

	private TaskCache loadTask(String id) {
		synchronized (tasks) {
			TaskCache cache = tasks.get(id);
			if (cache == null) {
				cache = new TaskCache(id);
				Task task = this.getTaskDao().select(id);
				cache.setName(task.getName());
				cache.setDescription(loadDescription(id));
				tasks.put(id, cache);
			}
			return cache;
		}
	}

	private ByteArrayOutputStream loadDescription(String tid) {
		ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream(4096);
		final List<String> fileNames = new ArrayList<String>();
		final List<String> names = new ArrayList<String>();
		PropertiesOutputStream prop = new PropertiesOutputStream(stream, "gbk");
		NamedConditions cond = new NamedConditions("getTDByTaskId");
		cond.putString("TaskId", tid);
		List<TaskDocuments> objs = getTaskDocumentsDao().select(0,
				Integer.MAX_VALUE, cond);
		for (TaskDocuments td : objs) {
			fileNames.add(td.getDocument().getId() + "."
					+ td.getDocument().getSurfix().toLowerCase() + ";");
			names.add(td.getDocument().getName() + ";");
		}
		Task task = getTaskDao().select(tid);

		try {
			if (task instanceof Media) {
				Media m = (Media) task;
				Long a = m.getAge();
				String age = Long.toString(a == null ? 0 : a);
				prop.setProperty("age", age);
			} else if (task instanceof Train) {
				Train t = (Train) task;
				Long a = t.getAge();
				String age = Long.toString(a == null ? 0 : a);
				prop.setProperty("age", age);
			} else if (task instanceof Notice) {
				prop.setProperty("age", Long.toString(Long.MAX_VALUE));
			}
			String taskName = task.getName();
			prop.setProperty("version", "1");
			prop.setProperty("taskName", taskName);
			String temp = "";
			for (String id : fileNames) {
				temp += id;
			}
			prop.setProperty("fileName", temp);
			temp = "";
			for (String name : names) {
				temp += name;
			}
			prop.setProperty("names", temp);
			prop.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public void boot(String id, String version) {
		loadMachine(id).setBoot(version, new Date());
	}

	@Override
	public void active(String id) {
		loadMachine(id).setActive(new Date());
	}

	@Override
	public void downfile(final String id, String fid, long size) {
		MachineCache mc = loadMachine(id);
		if (!fid.equals(mc.getFileId())) {
			final String oldFid = mc.getFileId();
			final Date start = mc.getStartTime();
			final Date end = new Date();
			es.submit(new Runnable() {
				@Override
				public void run() {
					transFileDownload(id, oldFid, start, end);
				}
			});
		}
		mc.setDown(fid, size);
	}

	public void transFileDownload(String id, String fid, Date start, Date end) {
		FileDownload fd = new FileDownload();
		fd.setId(UUID.randomUUID().toString());
		fd.setDepartment(getMachineDao().select(id));
		fd.setDocument(getDocumentDao().select(fid));
		fd.setStart(start);
		fd.setEnd(end);
		getFileDownloadDao().create(fd);
	}

	@Override
	public void removeFile(final String id, final String fid) {
		es.submit(new Runnable() {
			@Override
			public void run() {
				transRemoveFile(id, fid);
			}
		});
	}

	public void transRemoveFile(String id, String fid) {
		Machine d = getMachineDao().select(id);
		Document doc = getDocumentDao().select(fid);
		FileLog fl = new FileLog();
		fl.setId(UUID.randomUUID().toString());
		fl.setDepartment(d);
		fl.setDoc(doc);
		fl.setOperateType("删除文件");
		fl.setBirth(new Date());
		getFileLogDao().create(fl);
	}

	@Override
	public void removeTask(final String id, final String tid) {
		es.submit(new Runnable() {
			@Override
			public void run() {
				TaskLog tl = new TaskLog();
				Task t = getTaskDao().select(tid);
				tl.setId(UUID.randomUUID().toString());
				tl.setTask(t);
				tl.setDepartment(getMachineDao().select(id));
				tl.setOperateType("结束任务");
				tl.setBirth(new Date());
				getTaskLogDao().create(tl);
			}
		});
	}

	@Override
	public void writeDescription(String id, String tid, OutputStream out)
			throws IOException {
		ByteArrayOutputStream stream = loadTask(tid).getDescription();
		stream.writeTo(out);
		loadMachine(id).setTask(tid, "描述");
	}

	@Override
	public void loadAllMachines() {
		NamedConditions cond = new NamedConditions("getAllMachines");
		for (Machine m : this.getMachineDao()
				.select(0, Integer.MAX_VALUE, cond)) {
			this.machines.put(m.getId(), load(m));
		}
	}

	@Override
	public void taskStatus(String id, String tid, String status) {
		if (status.equals("run")) {
			loadMachine(id).setTask(tid, "启动");
			NamedConditions cond = new NamedConditions(
					"getByMachineIdAndTaskId");
			cond.putString("MachineId", id);
			cond.putString("TaskId", tid);
			MachineTasks mt = getMachineTasksDao().select(cond);
			if (mt != null) {
				mt.setCount(mt.getCount() + 1);
				getMachineTasksDao().update(mt);
			}
		} else if (status.equals("down")) {
			loadMachine(id).setTask(tid, "下载");
		} else if (status.equals("stop")) {
			loadMachine(id).endTask();
		}
	}
}
