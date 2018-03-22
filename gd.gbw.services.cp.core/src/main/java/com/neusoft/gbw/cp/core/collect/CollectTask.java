package com.neusoft.gbw.cp.core.collect;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;


public class CollectTask {
	private String collectTaskID;  	  		//采集通信ID
	private TaskPriority taskPriority;		//采集任务优先级
	private CollectAttrInfo attrInfo;		//通信信息
	private ScheduleInfo schedule;			//调度信息
	private ProtocolData data;				//协议信息
	private BusinessTask busTask;			//业务任务属性信息（可扩展）
	private TaskType taskType;              //任务类型，（系统任务--采集平台自动触发任务，临时任务-页面点击任务，突发系统任务-突发频率任务）
	private Map<String, Object> expandMap;	//扩展对象Map
	private long orgMonitorId;       // 创建收测单元时的站点id
	
	
	public long getOrgMonitorId() {
		return orgMonitorId;
	}
	public void setOrgMonitorId(long orgMonitorId) {
		this.orgMonitorId = orgMonitorId;
	}
	public String getCollectTaskID() {
		return collectTaskID;
	}
	public void setCollectTaskID(String collectTaskID) {
		this.collectTaskID = collectTaskID;
	}
	public TaskPriority getTaskPriority() {
		return taskPriority;
	}
	public void setTaskPriority(TaskPriority taskPriority) {
		this.taskPriority = taskPriority;
	}
	public CollectAttrInfo getAttrInfo() {
		return attrInfo;
	}
	public void setAttrInfo(CollectAttrInfo attrInfo) {
		this.attrInfo = attrInfo;
	}
	public ScheduleInfo getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleInfo schedule) {
		this.schedule = schedule;
	}
	public ProtocolData getData() {
		return data;
	}
	public void setData(ProtocolData data) {
		this.data = data;
	}
	public BusinessTask getBusTask() {
		return busTask;
	}
	public void setBusTask(BusinessTask busTask) {
		this.busTask = busTask;
	}
	public Map<String, Object> getExpandMap() {
		return expandMap;
	}
	public void setExpandMap(Map<String, Object> expandMap) {
		this.expandMap = expandMap;
	}
	public Object getExpandObject(String key) {
		if (expandMap.containsKey(key))
			return expandMap.get(key);
		return null;
	}
	public void addExpandObj(String key, Object value) {
		if (this.expandMap == null)
			this.expandMap = new HashMap<String, Object>();
		expandMap.put(key, value);
	}
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
}
