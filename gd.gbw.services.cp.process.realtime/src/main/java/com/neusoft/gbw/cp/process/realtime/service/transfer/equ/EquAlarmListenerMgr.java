package com.neusoft.gbw.cp.process.realtime.service.transfer.equ;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.process.realtime.constants.ConfigVariable;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class EquAlarmListenerMgr {
	
	private NMServiceCentre pool = new NMServiceCentre();
	private BlockingQueue<String> queue = null;
	private AlarmSendListener sendListener = null;
	private String sendThreadName = "设备告警发送线程";
	
	public void init() {
		queue = ARSFToolkit.getBlockingQueue("equAlarmQueue", 1000);
	}
	
	public void put(String alarm) throws InterruptedException {
		Log.debug(this.getClass().getName()+"  存储队列queue   长度： "+queue.size());
		queue.put(alarm);
	}
	
	public String take() throws InterruptedException {
		Log.debug(this.getClass().getName()+"  取队列queue  长度："+queue.size());
		return queue.take();
	}

	public void open() {
		//如果为0.则不进行告警发送，1进行告警发送
		if(ConfigVariable.IS_SEND_EQU_ALARM == 0)
			return;
		
		init();
		startRecieveListener();
	}
	
	private void startRecieveListener() {
		try{
			sendListener = new AlarmSendListener(this);
			sendListener.setServiceName(sendThreadName);
			pool.addService(sendListener);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"线程启动报错",e);
		}
	}
	
	public void close() {
		//如果为0.则不进行告警发送，1进行告警发送
		if(ConfigVariable.IS_SEND_EQU_ALARM == 0)
			return;
		pool.stopAllServicePool();
	}
}
