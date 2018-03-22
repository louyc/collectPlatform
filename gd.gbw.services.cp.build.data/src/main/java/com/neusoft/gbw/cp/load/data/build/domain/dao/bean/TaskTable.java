package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class TaskTable {

	private int task_id;				//收测任务ID
	private String task_name;			//收测任务名称
	private int task_build_mode;		//任务创建方式   0:自动任务 1:手动任务
	private int task_type_id;           //任务类型ID  3:广播，4：实验，5：地方三满，6：频谱
	private int task_priority;          //任务优先级
	private String task_create_time;    //任务创建时间
	private String validity_b_time;     //有效开始时间
	private String validity_e_time;     //有效结束时间
//	private int taskSamplecCircle;		//采样周期
//	private int taskSampleNumber;		//采样个数
	private int is_timelapse;			//是否延时下发
	private String time_lapse;			//延时时间
	private int task_status_id;			//任务下发状态：下发成功、下发失败、未下发、下发中
	private String task_status_desc;	//任务状态描述
	private int recycle_status_id;		//任务回收状态
	private String recycle_time;        //任务回收时间
	private int over_status;			//任务完成状态， 1：完成  0：未完成
	private int sync_status;			//下发同步状态
	private int data_status;			//数据状态  0 当前有效、1 预设、2 过期、3 删除
	private int in_use;					//任务启用状态
	
	public String getRecycle_time() {
		return recycle_time;
	}
	public void setRecycle_time(String recycle_time) {
		this.recycle_time = recycle_time;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public int getTask_build_mode() {
		return task_build_mode;
	}
	public void setTask_build_mode(int task_build_mode) {
		this.task_build_mode = task_build_mode;
	}
	public int getTask_type_id() {
		return task_type_id;
	}
	public void setTask_type_id(int task_type_id) {
		this.task_type_id = task_type_id;
	}
	public int getTask_priority() {
		return task_priority;
	}
	public void setTask_priority(int task_priority) {
		this.task_priority = task_priority;
	}
	public String getTask_create_time() {
		return task_create_time;
	}
	public void setTask_create_time(String task_create_time) {
		this.task_create_time = task_create_time;
	}
	public String getValidity_b_time() {
		return validity_b_time;
	}
	public void setValidity_b_time(String validity_b_time) {
		this.validity_b_time = validity_b_time;
	}
	public String getValidity_e_time() {
		return validity_e_time;
	}
	public void setValidity_e_time(String validity_e_time) {
		this.validity_e_time = validity_e_time;
	}
	public int getIs_timelapse() {
		return is_timelapse;
	}
	public void setIs_timelapse(int is_timelapse) {
		this.is_timelapse = is_timelapse;
	}
	public String getTime_lapse() {
		return time_lapse;
	}
	public void setTime_lapse(String time_lapse) {
		this.time_lapse = time_lapse;
	}
	public int getOver_status() {
		return over_status;
	}
	public void setOver_status(int over_status) {
		this.over_status = over_status;
	}
	public int getSync_status() {
		return sync_status;
	}
	public void setSync_status(int sync_status) {
		this.sync_status = sync_status;
	}
	public int getData_status() {
		return data_status;
	}
	public void setData_status(int data_status) {
		this.data_status = data_status;
	}
	public int getIn_use() {
		return in_use;
	}
	public void setIn_use(int in_use) {
		this.in_use = in_use;
	}
	public int getTask_status_id() {
		return task_status_id;
	}
	public void setTask_status_id(int task_status_id) {
		this.task_status_id = task_status_id;
	}
	public int getRecycle_status_id() {
		return recycle_status_id;
	}
	public void setRecycle_status_id(int recycle_status_id) {
		this.recycle_status_id = recycle_status_id;
	}
	public String getTask_status_desc() {
		return task_status_desc;
	}
	public void setTask_status_desc(String task_status_desc) {
		this.task_status_desc = task_status_desc;
	}
//	public int getTaskSamplecCircle() {
//		return taskSamplecCircle;
//	}
//	public void setTaskSamplecCircle(int taskSamplecCircle) {
//		this.taskSamplecCircle = taskSamplecCircle;
//	}
//	public int getTaskSampleNumber() {
//		return taskSampleNumber;
//	}
//	public void setTaskSampleNumber(int taskSampleNumber) {
//		this.taskSampleNumber = taskSampleNumber;
//	}
	
}
