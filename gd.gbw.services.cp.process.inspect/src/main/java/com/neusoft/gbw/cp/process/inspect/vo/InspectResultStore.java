package com.neusoft.gbw.cp.process.inspect.vo;

public class InspectResultStore {

	private int result_id;
	
	private long monitor_id;
	
	private String inspec_code;
	
	private String inspec_message;
	
	private String time_stamp;
	
	private int inspec_result;
	
	private String inspec_finish_time;
	
	private int inspec_finish_status; //巡检完成状态,0：异常、1：正常，2：超时
	
	private int inspec_finish_score;

	public int getResult_id() {
		return result_id;
	}

	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}

	public long getMonitor_id() {
		return monitor_id;
	}

	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}

	public String getInspec_code() {
		return inspec_code;
	}

	public void setInspec_code(String inspec_code) {
		this.inspec_code = inspec_code;
	}

	public String getInspec_message() {
		return inspec_message;
	}

	public void setInspec_message(String inspec_message) {
		this.inspec_message = inspec_message;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	public int getInspec_result() {
		return inspec_result;
	}

	public void setInspec_result(int inspec_result) {
		this.inspec_result = inspec_result;
	}

	public String getInspec_finish_time() {
		return inspec_finish_time;
	}

	public void setInspec_finish_time(String inspec_finish_time) {
		this.inspec_finish_time = inspec_finish_time;
	}

	public int getInspec_finish_status() {
		return inspec_finish_status;
	}

	public void setInspec_finish_status(int inspec_finish_status) {
		this.inspec_finish_status = inspec_finish_status;
	}

	public int getInspec_finish_score() {
		return inspec_finish_score;
	}

	public void setInspec_finish_score(int inspec_finish_score) {
		this.inspec_finish_score = inspec_finish_score;
	}
}
