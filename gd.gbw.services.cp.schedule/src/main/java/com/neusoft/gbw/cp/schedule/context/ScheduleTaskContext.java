package com.neusoft.gbw.cp.schedule.context;

import java.util.Hashtable;
import java.util.Map;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class ScheduleTaskContext {
	
	private Map<String, CollectTask> map = null;
	
	private static class DelayTaskBuilderHolder {
		private static final ScheduleTaskContext INSTANCE = new ScheduleTaskContext();
	}
	
	
	public static ScheduleTaskContext getInstance() {
		return DelayTaskBuilderHolder.INSTANCE;
	}
	
	private ScheduleTaskContext() {
		map = new Hashtable<String, CollectTask>();
	}
	
	public void syncTask(CollectTask task){
		String id = getId(task);
		putMap(id, task);
	}
	
	public void removeTask(CollectTask task) {
		String id = getId(task);
		map.remove(id);
	}
	
	//如何获取唯一ID
	public String getId(CollectTask task) {
//		String id = task.getBusTask().getTask_id() + "_" + task.getBusTask().getTaskfreq_id() + "_" + task.getBusTask().getTime_id();
		String id = task.getBusTask().getTask_id() + "_" + task.getBusTask().getFreq() + "_" + task.getBusTask().getMonitor_id()
				+"_"+task.getBusTask().getRunplan_id();
		return id;
	}
	
	private void putMap(String id,CollectTask task){
		this.map.put(id, task);
	}
	
	public Map<String, CollectTask> getMap() {
		return map;
	}
	public CollectTask getCollectTask(String id){
			
			return map.get(id);
		
	}
	
	
	public void setMap(Map<String, CollectTask> map) {
		this.map = map;
	}
	
}
