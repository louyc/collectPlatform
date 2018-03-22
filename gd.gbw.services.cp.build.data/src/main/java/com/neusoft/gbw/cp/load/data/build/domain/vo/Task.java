package com.neusoft.gbw.cp.load.data.build.domain.vo;

import java.util.List;
import java.util.Map;

public class Task {

	private TaskAttribute measureTask;				//收测任务
	private TaskMonitor taskMonitor;				//任务监测设备
	private BuildType buildType;					//构建类型（协议类型和业务类型组成）
	private TaskFreq taskFreq;						//任务项信息
	private Map<String, String> taskConfAttr;		//任务配置项信息
	private List<TaskSchedule> scheduleList;		//频率下的周期配置集合
//	private TaskSchedule taskSchedule;				//周期时间配置
	private Object expandObj;						//扩展对象
	private List<TaskMonitor> taskMonitorList;      //效果任务    多站点集合  lyc
	
	public List<TaskMonitor> getTaskMonitorList() {
		return taskMonitorList;
	}
	public void setTaskMonitorList(List<TaskMonitor> taskMonitorList) {
		this.taskMonitorList = taskMonitorList;
	}
	public TaskAttribute getMeasureTask() {
		return measureTask;
	}
	public void setMeasureTask(TaskAttribute measureTask) {
		this.measureTask = measureTask;
	}
	public TaskMonitor getTaskMonitor() {
		return taskMonitor;
	}
	public void setTaskMonitor(TaskMonitor taskMonitor) {
		this.taskMonitor = taskMonitor;
	}
	public BuildType getBuildType() {
		return buildType;
	}
	public void setBuildType(BuildType buildType) {
		this.buildType = buildType;
	}
	public TaskFreq getTaskFreq() {
		return taskFreq;
	}
	public void setTaskFreq(TaskFreq taskFreq) {
		this.taskFreq = taskFreq;
	}
	public Map<String, String> getTaskConfAttr() {
		return taskConfAttr;
	}
	public void setTaskConfAttr(Map<String, String> taskConfAttr) {
		this.taskConfAttr = taskConfAttr;
	}
//	public TaskSchedule getTaskSchedule() {
//		return taskSchedule;
//	}
//	public void setTaskSchedule(TaskSchedule taskSchedule) {
//		this.taskSchedule = taskSchedule;
//	}
	public Object getExpandObj() {
		return expandObj;
	}
	public void setExpandObj(Object expandObj) {
		this.expandObj = expandObj;
	}
	public List<TaskSchedule> getScheduleList() {
		return scheduleList;
	}
	public void setScheduleList(List<TaskSchedule> scheduleList) {
		this.scheduleList = scheduleList;
	}
	
}
