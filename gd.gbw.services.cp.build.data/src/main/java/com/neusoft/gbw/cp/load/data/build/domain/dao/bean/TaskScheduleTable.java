package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class TaskScheduleTable {

	private int time_id;			//时间ID
	private int taskfreq_id;		//任务项ID
	private String runplan_id;		//运行图ID
	private int schedule_type;		//周期类型
	private String dayofweek;		//周期时间
	private int reportmode;			//上报类型
	private String starttime;		//起始时间
	private String endtime;			//终止时间
	private int expiredays;			//有效天数
	private int is_overhaul;		//是否检修
	private int sync_status;		//下发同步状态
	private String valid_start_time;//运行图的有效开始时间
	private String valid_end_time;	//运行图的有效结束时间
	
	public int getTime_id() {
		return time_id;
	}
	public void setTime_id(int time_id) {
		this.time_id = time_id;
	}
	public int getTaskfreq_id() {
		return taskfreq_id;
	}
	public void setTaskfreq_id(int taskfreq_id) {
		this.taskfreq_id = taskfreq_id;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}
	public int getSchedule_type() {
		return schedule_type;
	}
	public void setSchedule_type(int schedule_type) {
		this.schedule_type = schedule_type;
	}
	public String getDayofweek() {
		return dayofweek;
	}
	public void setDayofweek(String dayofweek) {
		this.dayofweek = dayofweek;
	}
	public int getReportmode() {
		return reportmode;
	}
	public void setReportmode(int reportmode) {
		this.reportmode = reportmode;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getExpiredays() {
		return expiredays;
	}
	public void setExpiredays(int expiredays) {
		this.expiredays = expiredays;
	}
	public int getIs_overhaul() {
		return is_overhaul;
	}
	public void setIs_overhaul(int is_overhaul) {
		this.is_overhaul = is_overhaul;
	}
	public int getSync_status() {
		return sync_status;
	}
	public void setSync_status(int sync_status) {
		this.sync_status = sync_status;
	}
	public String getValid_start_time() {
		return valid_start_time;
	}
	public void setValid_start_time(String valid_start_time) {
		this.valid_start_time = valid_start_time;
	}
	public String getValid_end_time() {
		return valid_end_time;
	}
	public void setValid_end_time(String valid_end_time) {
		this.valid_end_time = valid_end_time;
	}
}
