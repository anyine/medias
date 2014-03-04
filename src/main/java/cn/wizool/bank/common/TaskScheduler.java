package cn.wizool.bank.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class TaskScheduler {
	private static final String APP_GROUP_ID = "bank";
	Scheduler scheduler = null;

	public void open() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
	}

	public void schedule(String tid, String[] ids, String schedule)
			throws SchedulerException {
		List<String> list = new ArrayList<String>();
		for (String id : ids) {
			list.add(id);
		}
		this.schedule(tid, list, schedule);
	}

	public void unschedule(String tid) throws SchedulerException {
		TriggerKey key = TriggerKey.triggerKey(tid, APP_GROUP_ID);
		if (scheduler.checkExists(key)) {
			scheduler.unscheduleJob(key);
		}
	}

	public void schedule(String tid, List<String> ids, String schedule)
			throws SchedulerException {
		if (this.scheduler == null) {
			this.open();
		}

		this.unschedule(tid);

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(tid, APP_GROUP_ID)
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule(schedule)
								.withMisfireHandlingInstructionFireAndProceed())
				.build();

		String idlist = "";
		for (String id : ids) {
			idlist += id + ";";
		}
		JobDetail job = JobBuilder.newJob(TaskJob.class)
				.withIdentity(UUID.randomUUID().toString(), APP_GROUP_ID)
				.usingJobData("ids", idlist).usingJobData("tid", tid).build();
		scheduler.scheduleJob(job, trigger);
	}

	public void close() throws SchedulerException {
		scheduler.shutdown();
	}
}
