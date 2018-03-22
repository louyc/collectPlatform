package com.neusoft.gbw.cp.load.data.build.domain.vo;

public class TaskPriorityInfo {

	private TaskType type;
	private	String role;
	private String taskProType;
	private int basePriority;
	
	private long language;
	private long station;
	
	public TaskType getType() {
		return type;
	}
	public void setType(TaskType type) {
		this.type = type;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getTaskProType() {
		return taskProType;
	}
	public void setTaskProType(String taskProType) {
		this.taskProType = taskProType;
	}
	public long getStation() {
		return station;
	}
	public long getLanguage() {
		return language;
	}
	public void setLanguage(long language) {
		this.language = language;
	}
	public void setStation(long station) {
		this.station = station;
	}
	public int getBasePriority() {
		return basePriority;
	}
	public void setBasePriority(int basePriority) {
		this.basePriority = basePriority;
	}
}