package cn.wizool.util.schedule;

import java.util.HashMap;
import java.util.Map;
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

public class ScheduleManager {

	private Scheduler scheduler = null;
	private String name;
	private Map<String, Runnable> jobs = new HashMap<String, Runnable>();

	private static Map<String, ScheduleManager> map = new HashMap<String, ScheduleManager>();

	private ScheduleManager() {
	}

	private ScheduleManager(String name) {
		this.name = name;
	}

	public static ScheduleManager create(String name) {
		if (map.get(name) != null) {
			return null;
		}

		map.put(name, new ScheduleManager(name));

		return map.get(name);
	}

	public void open() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
	}

	public void unschedule(String id) throws SchedulerException {
		TriggerKey key = TriggerKey.triggerKey(id, name);
		if (scheduler.checkExists(key)) {
			scheduler.unscheduleJob(key);
		}
	}

	public String schedule(Runnable obj, String schedule)
			throws SchedulerException {
		String uuid = UUID.randomUUID().toString();

		if (this.scheduler == null) {
			this.open();
		}

		this.jobs.put(uuid, obj);

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(uuid, name)
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule(schedule)
								.withMisfireHandlingInstructionFireAndProceed())
				.build();

		JobDetail job = JobBuilder.newJob(ScheduleJob.class)
				.withIdentity(UUID.randomUUID().toString(), name)
				.usingJobData("id", uuid).usingJobData("name", this.name)
				.build();

		scheduler.scheduleJob(job, trigger);

		return uuid;
	}

	public void close() throws SchedulerException {
		scheduler.shutdown();
	}

	static ScheduleManager getManager(String mid) {
		return map.get(mid);
	}

	void execute(String uuid) {
		this.jobs.get(uuid).run();
	}

}