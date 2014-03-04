package cn.wizool.util.schedule;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ScheduleJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String mid = dataMap.getString("name");
		String uuid = dataMap.getString("id");
		ScheduleManager manager = ScheduleManager.getManager(mid);
		manager.execute(uuid);
	}

}