package com.neusoft.gbw.cp.process.measure.vo.online;

public class ManualOnlineUnitStore {

	private long id;
	private int listener_task_id;
	private long monitor_id;
	private String radio_url;
	private String eval_url;
	private String collect_time;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getListener_task_id() {
		return listener_task_id;
	}
	public void setListener_task_id(int listener_task_id) {
		this.listener_task_id = listener_task_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}

	public String getRadio_url() {
		return radio_url;
	}
	public void setRadio_url(String radio_url) {
		this.radio_url = radio_url;
	}

	public String getEval_url() {
		return eval_url;
	}
	public void setEval_url(String eval_url) {
		this.eval_url = eval_url;
	}
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
}
