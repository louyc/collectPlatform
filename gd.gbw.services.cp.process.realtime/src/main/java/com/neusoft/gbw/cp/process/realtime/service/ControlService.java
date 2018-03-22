package com.neusoft.gbw.cp.process.realtime.service;

import com.neusoft.gbw.cp.process.realtime.channel.ChannelPool;
import com.neusoft.gbw.cp.process.realtime.context.TaskProcessContext;
import com.neusoft.gbw.cp.process.realtime.service.transfer.equ.EquAlarmListenerMgr;
import com.neusoft.gbw.cp.process.realtime.service.transfer.quality.QualityTransferDispose;

public class ControlService {
	
	private DataDispatcher dataDispatcher;
	private EquAlarmListenerMgr alarmMgr;
	
	public DataDispatcher getDataDispatcher() {
		return dataDispatcher;
	}
	
	public EquAlarmListenerMgr getAlarmMgr() {
		return alarmMgr;
	}

	public void start() {
		new ConfigLoader().loadConfig();
		if(dataDispatcher == null) {
			dataDispatcher = new DataDispatcher();
			dataDispatcher.start();
		}

		ChannelPool.getInstance().init();
		ChannelPool.getInstance().open();
		TaskProcessContext.getInstance().loadRealtimeProcess();
		//实时指标通信初始化
		new QualityTransferDispose().init();
		
		if(alarmMgr == null) {
			alarmMgr = new EquAlarmListenerMgr();
			alarmMgr.open();
		}
	}

	public void stop() {
		if(dataDispatcher != null) {
			dataDispatcher.stop();
		}
		ChannelPool.getInstance().close();
		
		if(alarmMgr != null) {
			alarmMgr.close();
		}
	}
}
