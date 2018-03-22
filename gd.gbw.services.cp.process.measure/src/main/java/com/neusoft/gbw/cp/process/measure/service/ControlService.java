package com.neusoft.gbw.cp.process.measure.service;

import com.neusoft.gbw.cp.process.measure.channel.ChannelPool;
import com.neusoft.gbw.cp.process.measure.context.TaskProcessContext;

public class ControlService {
	
	private DataDispatchMgr dataDispatchor;
	
	public DataDispatchMgr getDataDispatchor() {
		return dataDispatchor;
	}
	
	public void start() {
		if(dataDispatchor == null) {
			dataDispatchor = new DataDispatchMgr();
			dataDispatchor.start();
		}
		new ConfigLoader().loadConfig();
		ChannelPool.getInstance().init();
		ChannelPool.getInstance().open();
		TaskProcessContext.getInstance().loadRealtimeProcess();
		new JmsConfigLoader().initJMSSendConfig(); //创建jms发送配置
		new JmsConfigLoader().initSocketSendConfig(); //创建socket发送配置
		new JmsConfigLoader().initRecordMonitorConfig(); //创建与监控录音程序通信配置
		new JmsConfigLoader().initEvaluationRecieveConfig();
		new JmsConfigLoader().initEvaluationSendConfig();
	}

	public void stop() {
		if(dataDispatchor != null) {
			dataDispatchor.stop();
		}
		ChannelPool.getInstance().close();
	
	}
}
