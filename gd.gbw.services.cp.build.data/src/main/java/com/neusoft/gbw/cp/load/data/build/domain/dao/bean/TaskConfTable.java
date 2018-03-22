package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class TaskConfTable {

	private int id;					//配置ID
	private int task_id;			//收测任务ID
	private String conf_code;		//属性项
	private String conf_value;		//属性值
	private int is_use;				//是否使用
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public String getConf_code() {
		return conf_code;
	}
	public void setConf_code(String conf_code) {
		this.conf_code = conf_code;
	}
	public String getConf_value() {
		return conf_value;
	}
	public void setConf_value(String conf_value) {
		this.conf_value = conf_value;
	}
	public int getIs_use() {
		return is_use;
	}
	public void setIs_use(int is_use) {
		this.is_use = is_use;
	}
}
