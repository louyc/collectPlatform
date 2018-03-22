package com.neusoft.gbw.cp.load.data.build.domain.vo;

import java.util.List;

import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;

public class BuildInfo {

	private BuildType type;
	private MonitorDevice device;
	private TaskType taskType;	//任务类型
	private Object buisness;
	private List<MonitorDevice> deviceList;
	private int occupKey;
	private boolean useFtp;  //true:连接ftp自己下载    false:走协议
	private boolean inspectSteam;  //巡检遥控站   true ：巡检    false:不巡检
	
	public boolean isInspectSteam() {
		return inspectSteam;
	}
	public void setInspectSteam(boolean inspectSteam) {
		this.inspectSteam = inspectSteam;
	}
	public boolean isUseFtp() {
		return useFtp;
	}
	public void setUseFtp(boolean useFtp) {
		this.useFtp = useFtp;
	}
	public List<MonitorDevice> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(List<MonitorDevice> deviceList) {
		this.deviceList = deviceList;
	}
	public BuildType getType() {
		return type;
	}
	public void setType(BuildType type) {
		this.type = type;
	}
	public MonitorDevice getDevice() {
		return device;
	}
	public void setDevice(MonitorDevice device) {
		this.device = device;
	}
	public Object getBuisness() {
		return buisness;
	}
	public void setBuisness(Object buisness) {
		this.buisness = buisness;
	}
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public int getOccupKey() {
		return occupKey;
	}
	public void setOccupKey(int occupKey) {
		this.occupKey = occupKey;
	}
	@Override
	public String toString() {
		return "BuildInfo [type=" + type + ", device=" + device + "]";
	}
}
