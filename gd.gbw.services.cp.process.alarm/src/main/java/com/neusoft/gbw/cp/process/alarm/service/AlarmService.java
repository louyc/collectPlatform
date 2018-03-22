package com.neusoft.gbw.cp.process.alarm.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.alarm.dao.SqlSessionFactoryHolder;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class AlarmService {
	
	private BlockingQueue<EquAlarm> alarmData = null;
	private AlarmDisposeProcess process = null;
	private NMServiceCentre pool = new NMServiceCentre();
	
	public void init() {
		alarmData = ARSFToolkit.getBlockingQueue(EquAlarm.class, "equAlarm", 100);
		
	}
	
	public void put(EquAlarm alarm) {
		try {
			alarmData.put(alarm);
		} catch (InterruptedException e) {
			Log.error(this.getClass().getName()+"队列存储报错", e);
		}
	}
	
	public EquAlarm take() throws InterruptedException {
		return alarmData.take();
	}

	public void start() {
		init();
		for(int i = 0; i < 5; i++) {
			try{
				process = new AlarmDisposeProcess(this);
				process.setServiceName("#告警处理线程_" + i);
				pool.addService(process);
			}catch(Exception e){
				Log.debug(this.getClass().getName()+"线程启动报错",e);
			}
		}
		try {
			SqlSessionFactoryHolder.getInstance().init();
		} catch (Exception e) {
			Log.error("数据库初始化报错", e);
		}
	}
	
	public void stop() {
		
	}
}
