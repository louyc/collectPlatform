package com.neusoft.gbw.cp.schedule.service;

import com.neusoft.gbw.cp.schedule.channel.ChannelPool;

public class ScheduleService {

	private TaskDispatchorService mDispatchorService;

	public TaskDispatchorService getmDispatchorService() {
		return mDispatchorService;
	}

	private void init() {

	}

	public void start() {
		init();
		
		if (mDispatchorService == null) {
			mDispatchorService = new TaskDispatchorService();
			mDispatchorService.start();
		}
		ChannelPool.getInstance().open();

	}

	public void stop() {
		if (mDispatchorService != null) {
			mDispatchorService.stop();
		}
	}
}
