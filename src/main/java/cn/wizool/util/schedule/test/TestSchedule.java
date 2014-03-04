package cn.wizool.util.schedule.test;

import java.util.UUID;

import org.quartz.SchedulerException;

import cn.wizool.util.schedule.ScheduleManager;

public class TestSchedule {

	/**
	 * @param args
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws SchedulerException,
			InterruptedException {
		ScheduleManager manager = ScheduleManager.create(UUID.randomUUID()
				.toString());
		manager.open();
		String id = manager.schedule(new Runnable() {

			@Override
			public void run() {
				System.out.println("ok");
			}

		}, "0/10 0-59 16 * * ? 2012");
		Thread.sleep(10 * 1000);
		manager.unschedule(id);
		Thread.sleep(30 * 1000);
		manager.close();
	}

}
