package com.neusoft.gbw.cp.process.realtime.vo;

public class QualityDataStore {

	private long task_id;
	private long monitor_id;
	private String receiver_code;
	private int band;
	private String frequency;
	private String collect_time;
	private String quality_code;
	private String quality_value;
	private String store_time;
	private String min_value;
	private String max_value;

	//以下为录音数据属性
	private String record_stime;
	private String record_etime;
	private int record_size;
	private int record_id;
	private String file_name;
	private String url;
	private int recover_status;
	private String recover_url;

	private int task_unique_id;//任务查询唯一ID
	
	public String getRecord_stime() {
		return record_stime;
	}
	public void setRecord_stime(String record_stime) {
		this.record_stime = record_stime;
	}
	public String getRecord_etime() {
		return record_etime;
	}
	public void setRecord_etime(String record_etime) {
		this.record_etime = record_etime;
	}
	public int getRecord_size() {
		return record_size;
	}
	public void setRecord_size(int record_size) {
		this.record_size = record_size;
	}
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getRecover_status() {
		return recover_status;
	}
	public void setRecover_status(int recover_status) {
		this.recover_status = recover_status;
	}
	public String getRecover_url() {
		return recover_url;
	}
	public void setRecover_url(String recover_url) {
		this.recover_url = recover_url;
	}
	public int getTask_unique_id() {
		return task_unique_id;
	}
	public void setTask_unique_id(int task_unique_id) {
		this.task_unique_id = task_unique_id;
	}
	public String getMin_value() {
		return min_value;
	}
	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}
	public String getMax_value() {
		return max_value;
	}
	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public String getReceiver_code() {
		return receiver_code;
	}
	public void setReceiver_code(String receiver_code) {
		this.receiver_code = receiver_code;
	}
	public int getBand() {
		return band;
	}
	public void setBand(int band) {
		this.band = band;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public String getQuality_code() {
		return quality_code;
	}
	public void setQuality_code(String quality_code) {
		this.quality_code = quality_code;
	}
	public String getQuality_value() {
		return quality_value;
	}
	public void setQuality_value(String quality_value) {
		this.quality_value = quality_value;
	}
	public String getStore_time() {
		return store_time;
	}
	public void setStore_time(String store_time) {
		this.store_time = store_time;
	}
}
