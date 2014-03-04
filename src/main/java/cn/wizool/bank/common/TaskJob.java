package cn.wizool.bank.common;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.wizool.bank.servlet.InterfaceServlet;

public class TaskJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String tid = dataMap.getString("tid");
		String[] ids = dataMap.getString("ids").split(";");
		for (String id : ids) {
			if (id.trim().length() > 0) {
				InterfaceServlet.putOneTask(id, tid);
			}
		}
	}

}
