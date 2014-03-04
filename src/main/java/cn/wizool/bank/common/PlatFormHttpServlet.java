package cn.wizool.bank.common;

import cn.wizool.bank.iwebutil.newlay.AbstractHttpServlet;
import cn.wizool.bank.model.User;
import cn.wizool.bank.service.BranchService;
import cn.wizool.bank.service.CacheService;
import cn.wizool.bank.service.ConfigService;
import cn.wizool.bank.service.DocumentService;
import cn.wizool.bank.service.FileDownloadService;
import cn.wizool.bank.service.FileLogService;
import cn.wizool.bank.service.MachineService;
import cn.wizool.bank.service.MachineTasksService;
import cn.wizool.bank.service.MediaService;
import cn.wizool.bank.service.NoticeService;
import cn.wizool.bank.service.TaskLogService;
import cn.wizool.bank.service.TaskService;
import cn.wizool.bank.service.TrainService;
import cn.wizool.bank.service.UserService;

public abstract class PlatFormHttpServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = -3295271408899300159L;

	private static final String PLATFORM = "platform";
	private static final String DOCUMENT_SERVICE = "DocumentService";
	private static final String TASKLOG_SERVICE = "TaskLogService";
	private static final String FILELOG_SERVICE = "FileLogService";
	private static final String TASK_SERVICE = "TaskService";
	private static final String TRAIN_SERVICE = "TrainService";
	private static final String NOTICE_SERVICE = "NoticeService";
	private static final String MEDIA_SERVICE = "MediaService";
	private static final String CONFIG_SERVICE = "ConfigService";
	private static final String FILEDOWNLOAD_SERVICE = "FileDownloadService";
	private static final String USER_SERVICE = "UserService";
	private static final String BRANCH_SERVICE = "BranchService";
	private static final String MACHINE_SERVICE = "MachineService";
	private static final String MACHINETASKS_SERVICE = "MachineTasksService";
	private static final String CACHE_SERVICE = "CacheService";

	public UserService getUserService() {
		return (UserService) this.getService(PLATFORM, USER_SERVICE);
	}

	public BranchService getBranchService() {
		return (BranchService) this.getService(PLATFORM, BRANCH_SERVICE);
	}

	public MachineService getMachineService() {
		return (MachineService) this.getService(PLATFORM, MACHINE_SERVICE);
	}

	public MachineTasksService getMachineTasksService() {
		return (MachineTasksService) this.getService(PLATFORM,
				MACHINETASKS_SERVICE);
	}

	public FileDownloadService getFileDownloadService() {
		return (FileDownloadService) this.getService(PLATFORM,
				FILEDOWNLOAD_SERVICE);
	}

	public ConfigService getConfigService() {
		return (ConfigService) this.getService(PLATFORM, CONFIG_SERVICE);
	}

	public TaskService getTaskService() {
		return (TaskService) this.getService(PLATFORM, TASK_SERVICE);
	}

	public TrainService getTrainService() {
		return (TrainService) this.getService(PLATFORM, TRAIN_SERVICE);
	}

	public NoticeService getNoticeService() {
		return (NoticeService) this.getService(PLATFORM, NOTICE_SERVICE);
	}

	public MediaService getMediaService() {
		return (MediaService) this.getService(PLATFORM, MEDIA_SERVICE);
	}

	public DocumentService getDocumentService() {
		return (DocumentService) this.getService(PLATFORM, DOCUMENT_SERVICE);
	}

	public TaskLogService getTaskLogService() {
		return (TaskLogService) this.getService(PLATFORM, TASKLOG_SERVICE);
	}

	public FileLogService getFileLogService() {
		return (FileLogService) this.getService(PLATFORM, FILELOG_SERVICE);
	}

	public CacheService getCacheService() {
		return (CacheService) this.getService(PLATFORM, CACHE_SERVICE);
	}

	public User getCurrentUser() {
		return (User) getRequest().getSession().getAttribute("CurrentUser");
	}

	public void setCurrentUser(User user) {
		getRequest().getSession().removeAttribute("CurrentUser");
		getRequest().getSession().setAttribute("CurrentUser", user);
	}

}
