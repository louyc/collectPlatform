package com.neusoft.np.arsf.core.schedule;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.schedule.infra.constants.ScheduleDeclare;
import com.neusoft.np.arsf.core.schedule.util.FileLoadException;
import com.neusoft.np.arsf.core.schedule.util.FileLoadUtil;

public class ScheduleTest implements Runnable {

	public static void test() {
		ScheduleTest test = new ScheduleTest();
		Thread thread = new Thread(test);
		thread.start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			//			sendAddPeriod();
			//			Thread.sleep(1000 * 2 * 60);
			//			sendDelPeriod();
			//			Thread.sleep(5000);
			//			sendAddPeriod();
			sendAddPlan();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileLoadException e) {
			e.printStackTrace();
		}
	}

	protected static void sendAddPeriod() throws FileLoadException {
		ARSFToolkit.sendEvent(ScheduleDeclare.SCHEDULE_INCREMENTS, FileLoadUtil.getDataFromXml("plan_3.xml"));
	}

	protected static void sendAddPlan() throws FileLoadException {
		ARSFToolkit.sendEvent(ScheduleDeclare.SCHEDULE_INCREMENTS, FileLoadUtil.getDataFromXml("plan_2.xml"));
	}

	protected static void sendDelPeriod() throws FileLoadException {
		ARSFToolkit.sendEvent(ScheduleDeclare.SCHEDULE_REMOVE, FileLoadUtil.getDataFromXml("plan_3.xml"));
	}

}
