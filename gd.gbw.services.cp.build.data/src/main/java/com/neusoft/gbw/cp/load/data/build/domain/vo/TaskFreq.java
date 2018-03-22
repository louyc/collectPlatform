package com.neusoft.gbw.cp.load.data.build.domain.vo;
public class TaskFreq {

	private int taskfreq_id;				//任务项ID
	private long task_id;					//收测任务ID
	private String freq;					//频率
	private int band;						//波段  短波、中波、调频
	private String receiver_code;			//接收机
	private int code_rate;					//码率
	private int sync_status;				//下发同步状态
	
	public int getTaskfreq_id() {
		return taskfreq_id;
	}
	public void setTaskfreq_id(int taskfreq_id) {
		this.taskfreq_id = taskfreq_id;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public int getBand() {
		return band;
	}
	public void setBand(int band) {
		this.band = band;
	}
	public String getReceiver_code() {
		return receiver_code;
	}
	public void setReceiver_code(String receiver_code) {
		this.receiver_code = receiver_code;
	}
	public int getCode_rate() {
		return code_rate;
	}
	public void setCode_rate(int code_rate) {
		this.code_rate = code_rate;
	}
	public int getSync_status() {
		return sync_status;
	}
	public void setSync_status(int sync_status) {
		this.sync_status = sync_status;
	}
	@Override
	public String toString() {
		return "TaskFreq [taskfreq_id=" + taskfreq_id + ", task_id=" + task_id
				+ ", freq=" + freq + ", receiver_code=" + receiver_code
				+ ", code_rate=" + code_rate + ", sync_status=" + sync_status
				+ "]";
	}
}
