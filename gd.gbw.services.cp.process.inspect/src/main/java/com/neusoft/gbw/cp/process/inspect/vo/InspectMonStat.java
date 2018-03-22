package com.neusoft.gbw.cp.process.inspect.vo;

public class InspectMonStat {

	private long monitor_id;
	
	private int online_state;//状态：在线 1、软件未开启 2、掉线 3、软件已开启4

	public long getMonitor_id() {
		return monitor_id;
	}

	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}

	public int getOnline_state() {
		return online_state;
	}

	public void setOnline_state(int online_state) {
		this.online_state = online_state;
	}
}
