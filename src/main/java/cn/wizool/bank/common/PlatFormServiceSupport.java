package cn.wizool.bank.common;

import cn.wizool.bank.dao.BranchDao;
import cn.wizool.bank.dao.CommonTaskDao;
import cn.wizool.bank.dao.ConfigDao;
import cn.wizool.bank.dao.DocumentDao;
import cn.wizool.bank.dao.FileDownloadDao;
import cn.wizool.bank.dao.FileLogDao;
import cn.wizool.bank.dao.MachineDao;
import cn.wizool.bank.dao.MachineTasksDao;
import cn.wizool.bank.dao.MediaDao;
import cn.wizool.bank.dao.NoticeDao;
import cn.wizool.bank.dao.TaskDao;
import cn.wizool.bank.dao.TaskDocumentsDao;
import cn.wizool.bank.dao.TaskLogDao;
import cn.wizool.bank.dao.TrainDao;
import cn.wizool.bank.dao.UserDao;
import cn.wizool.bank.iwebutil.newlay.ServiceSupport;

public class PlatFormServiceSupport extends ServiceSupport {

	private static final String PLATFORM = "platform";
	private static final String DOCUMENT_DAO = "DocumentDao";
	private static final String TASKLOG_DAO = "TaskLogDao";
	private static final String FILELOG_DAO = "FileLogDao";
	private static final String TASK_DAO = "TaskDao";
	private static final String TRAIN_DAO = "TrainDao";
	private static final String NOTICE_DAO = "NoticeDao";
	private static final String MEIDA_DAO = "MediaDao";
	private static final String COMMONTASK_DAO = "CommonTaskDao";
	private static final String TASKDOCUMENTS_DAO = "TaskDocumentsDao";
	private static final String CONFIG_DAO = "ConfigDao";
	private static final String FILEDOWNLOAD_DAO = "FileDownloadDao";
	private static final String USER_DAO = "UserDao";
	private static final String BRANCH_DAO = "BranchDao";
	private static final String MACHINE_DAO = "MachineDao";
	private static final String MACHINETASKS_DAO = "MachineTasksDao";

	public UserDao getUserDao() {
		return (UserDao) this.getDataAccessObject(PLATFORM, USER_DAO);
	}

	public MachineDao getMachineDao() {
		return (MachineDao) this.getDataAccessObject(PLATFORM, MACHINE_DAO);
	}

	public MachineTasksDao getMachineTasksDao() {
		return (MachineTasksDao) this.getDataAccessObject(PLATFORM,
				MACHINETASKS_DAO);
	}

	public BranchDao getBranchDao() {
		return (BranchDao) this.getDataAccessObject(PLATFORM, BRANCH_DAO);
	}

	public FileDownloadDao getFileDownloadDao() {
		return (FileDownloadDao) this.getDataAccessObject(PLATFORM,
				FILEDOWNLOAD_DAO);
	}

	public ConfigDao getConfigDao() {
		return (ConfigDao) this.getDataAccessObject(PLATFORM, CONFIG_DAO);
	}

	public TaskDocumentsDao getTaskDocumentsDao() {
		return (TaskDocumentsDao) this.getDataAccessObject(PLATFORM,
				TASKDOCUMENTS_DAO);
	}

	public TaskDao getTaskDao() {
		return (TaskDao) this.getDataAccessObject(PLATFORM, TASK_DAO);
	}

	public TrainDao getTrainDao() {
		return (TrainDao) this.getDataAccessObject(PLATFORM, TRAIN_DAO);
	}

	public NoticeDao getNoticeDao() {
		return (NoticeDao) this.getDataAccessObject(PLATFORM, NOTICE_DAO);
	}

	public MediaDao getMediaDao() {
		return (MediaDao) this.getDataAccessObject(PLATFORM, MEIDA_DAO);
	}

	public CommonTaskDao getCommonTaskDao() {
		return (CommonTaskDao) this.getDataAccessObject(PLATFORM,
				COMMONTASK_DAO);
	}

	public DocumentDao getDocumentDao() {
		return (DocumentDao) this.getDataAccessObject(PLATFORM, DOCUMENT_DAO);
	}

	public TaskLogDao getTaskLogDao() {
		return (TaskLogDao) this.getDataAccessObject(PLATFORM, TASKLOG_DAO);
	}

	public FileLogDao getFileLogDao() {
		return (FileLogDao) this.getDataAccessObject(PLATFORM, FILELOG_DAO);
	}

}
