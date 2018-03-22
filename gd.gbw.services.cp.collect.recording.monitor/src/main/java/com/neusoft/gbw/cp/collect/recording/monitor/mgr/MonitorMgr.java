package com.neusoft.gbw.cp.collect.recording.monitor.mgr;

public class MonitorMgr {

	private MonitorInit monitor = null;
//	private RecordMonitorProcess process = null;

	public void init() {
		new ConfigLoader().loadConfig();
		//获取jms录音配置
		new ConfigLoader().initRecordMonitorConfig();
		monitor = new MonitorInit();
		monitor.init();
	}
	
	public void start() {
		//增加定时提醒初始化功能
		new TimeMsgDisposeProcess().disposeTimeSetMsg();
//		//增加初始化监测录音平台状态，但不重启，等待下一分钟整点重启
//		process = new RecordMonitorProcess();
//		new Thread(process).start();
	}
	
	public void stop() {
		
	}
}
