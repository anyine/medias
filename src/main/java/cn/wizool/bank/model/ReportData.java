package cn.wizool.bank.model;

import java.util.Date;
import java.util.Map;

import org.springframework.aop.framework.Advised;

import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.ServiceFactory;
import cn.wizool.bank.iwebutil.newlay.ServiceManager;
import cn.wizool.bank.service.FileDownloadService;
import cn.wizool.bank.service.FileLogService;
import cn.wizool.bank.service.MachineService;
import cn.wizool.bank.service.TaskLogService;

public class ReportData implements Runnable {
	private static final String PLATFORM = "platform";
	private static final String TASKLOG_SERVICE = "TaskLogService";
	private static final String FILELOG_SERVICE = "FileLogService";
	private static final String FILEDOWNLOAD_SERVICE = "FileDownloadService";
	private static final String MACHINE_SERVICE = "MachineService";

	private String mid;
	private String tid;
	private String type;
	private ServiceFactory factory;

	public ReportData(String type, String mid, String tid,
			ServiceFactory factory) {
		super();
		this.type = type;
		this.mid = mid;
		this.tid = tid;
		this.factory = factory;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ServiceFactory getFactory() {
		return factory;
	}

	public void setFactory(ServiceFactory factory) {
		this.factory = factory;
	}

	protected Object getService(String mng, String svc) {
		ServiceManager serviceManager = factory.getServiceManager(mng);
		Map<String, Advised> serviceProxyBeans = serviceManager
				.getServiceProxyBeans();
		return serviceProxyBeans.get(svc);
	}

	public TaskLogService getTaskLogService() {
		return (TaskLogService) this.getService(PLATFORM, TASKLOG_SERVICE);
	}

	public MachineService getMachineService() {
		return (MachineService) this.getService(PLATFORM, MACHINE_SERVICE);
	}

	public FileDownloadService getFileDownloadService() {
		return (FileDownloadService) this.getService(PLATFORM,
				FILEDOWNLOAD_SERVICE);
	}

	public FileLogService getFileLogService() {
		return (FileLogService) this.getService(PLATFORM, FILELOG_SERVICE);
	}

	@Override
	public void run() {
		if (this.type.equals("startTask")) {
			if (!(tid.equals("f6f2a76c-bf34-4cc7-adba-8d2ab99d0fbe"))) {
				getTaskLogService().transCreate(null, tid, mid, "STARTTASK");
				getMachineService().transSetCurrentTask(null, mid, tid,
						"STARTTASK");
			}
		} else if (this.type.equals("endTask")) {
			if (!(tid.equals("f6f2a76c-bf34-4cc7-adba-8d2ab99d0fbe"))) {
				getTaskLogService().transCreate(null, tid, mid, "ENDTASK");
				getMachineService().transSetCurrentTask(null, mid, tid,
						"ENDTASK");
			}
		} else if (this.type.equals("removeTask")) {
			getTaskLogService().transCreate(null, tid, mid, "REMOVETASK");
		} else if (this.type.equals("removeFile")) {
			getFileLogService().transCreate(null, tid, mid, "REMOVEFILE");
		} else if (this.type.equals("boot")) {
			getMachineService().transSelectById(null, mid,
					new QueryObjectCallback<Machine>() {
						@Override
						public void callback(Machine m) {
							if (m != null) {
								m.setVersion(tid);
								m.setBootTime(new Date());
								m.setShutdownTime(m.getActive());
								m.setCurrentTask(null);
							}
						}
					});
			getFileDownloadService().transDeleteByDId(null, mid);
		} else if (this.type.equals("active")) {
			getMachineService().transSelectById(null, mid,
					new QueryObjectCallback<Machine>() {
						@Override
						public void callback(Machine objs) {
							if (objs != null) {
								objs.setActive(new Date());
							}
						}
					});
		}
	}

}
