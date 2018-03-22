package com.neusoft.gbw.cp.load.data.build.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.RunplanView;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskFreqTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskMonitorTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskScheduleTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskTable;
import com.neusoft.gbw.cp.load.data.build.domain.services.IDBDataConver;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.DBConverType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskAttribute;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskFreq;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskMonitor;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskRunplan;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.cp.load.data.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.load.data.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class TaskDBDataConver implements IDBDataConver{

	private TaskDataMgr taskDataMgr = null;

	public TaskDBDataConver(TaskDataMgr taskDataMgr) {
		this.taskDataMgr = taskDataMgr;
	}

	public List<Task> getTaskList(String taskId) {
		List<Task> list = new ArrayList<Task>();
		list.addAll(getTaskList(DBConverType.DEVICE_TASK_SET));

		Iterator<Task> iter = list.iterator();
		while(iter.hasNext()) {
			Task element = iter.next();
			String taskID = element.getMeasureTask().getTask_id()+"";
			if (!taskId.equals(taskID))
				iter.remove();
		}
		return list;
	}
	/**
	 * 查询质量设置任务
	 */
	public List<Task> getMoniTaskList(String monitorId) {
		List<Task> list = new ArrayList<Task>();
		list.addAll(getTaskList(DBConverType.DEVICE_TASK_SET));

		Iterator<Task> iter = list.iterator();
		while(iter.hasNext()) {
			Task element = iter.next();
			String id = element.getTaskMonitor().getMonitor_id() + "";
			if (!monitorId.equals(id))
				iter.remove();
		}
		return list;
	}
	/**
	 * 查询质量回收任务
	 */
	public List<Task> getMoniTaskRecoverList(String monitorId) {
		List<Task> list = new ArrayList<Task>();
		list.addAll(getTaskList(DBConverType.DEVICE_TASK_RECOVER));
		
		Iterator<Task> iter = list.iterator();
		while(iter.hasNext()) {
			Task element = iter.next();
			String id = element.getTaskMonitor().getMonitor_id() + "";
			if (!monitorId.equals(id))
				iter.remove();
		}
		return list;
	}
	/**
	 * 查询回收结果    20170504 lyc
	 * @param monitorId
	 * @return
	 */
	public Map<String,Object> getMoniRecoverTaskList(String monitorId) {
		return getRecoverResult(monitorId);
	}
	

	/*public List<Task> getTaskList(DBConverType type) {
		//key:task_id, value:MeasureTask
		Map<Integer, TaskTable> measureTaskMap = taskDataMgr.getMeasureTaskMap();
		//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
		Map<Integer, Map<Integer, TaskFreqTable>> taskFreqMap = taskDataMgr.getTaskFreqMap();

		Map<String, RunplanView> taskRunplanMap = taskDataMgr.getTaskRunplanMap();
		//key:task_id, value:Map
		Map<Integer, Map<String, String>> taskConfMap = taskDataMgr.getTaskConfMap(); 
		//key:taskfreq_id, value:List
		Map<Integer, List<TaskScheduleTable>> taskScheduleMap = taskDataMgr.getTaskScheduleMap();
		//key:task_id, value:list 多个Monitor_id
		Map<Integer, List<TaskMonitorTable>> taskMonitorMap = taskDataMgr.getTaskMonitorMap();

		List<Task> taskList = new ArrayList<Task>();
		List<Task> taskTypeList = null;

		for(TaskTable measureTask : measureTaskMap.values()) {
			int taskID = measureTask.getTask_id();
			int task_type_id = measureTask.getTask_type_id();
			if (!taskFreqMap.containsKey(taskID) || !taskConfMap.containsKey(taskID)) {
				Log.debug("收测任务中缺少指定的任务ID, taskID=" + taskID);
				continue;
			}
			Map<Integer, TaskFreqTable> freqMap = taskFreqMap.get(taskID);
			Map<String, String> confMap = taskConfMap.get(taskID);
			List<TaskMonitorTable> monitorList = taskMonitorMap.get(taskID);
			List<TaskScheduleTable> scheduleList = null;
			for(TaskFreqTable item : freqMap.values()) {
				int taskFreqID = item.getTaskfreq_id();
				if (!taskScheduleMap.containsKey(taskFreqID)) {
					continue;
				}
				scheduleList = taskScheduleMap.get(taskFreqID);
				for(TaskMonitorTable monitor : monitorList) {
//					for(TaskScheduleTable schedule : scheduleList) {
//						taskTypeList = buildTaskList(type, measureTask, item, confMap, schedule, monitor);
//						if(taskTypeList != null)
//							taskList.addAll(taskTypeList);
//					}
					//如果是效果类任务，还是按照一个小schedule去划分任务，但是如果是质量设置类任务则需要将任务scheduleList传入task对象 modify by jiahao 2015822
					//0:其他任务操作; 1:采集录音(设备端任务); 2:收测录音(平台录音)
					int buildType = 0;
//					int monitorType = monitor.getType_id();
					if (confMap.containsKey(BuildConstants.EffectTask.COLLECT_STREAM_TASK))
						buildType = 1;
					else if(confMap.containsKey(BuildConstants.EffectTask.RECIEVER_STREAM_TASK))
						buildType = 2;

					switch(type) {
					case DEVICE_TASK_RECOVER:
						if(buildType != 2) {
							taskTypeList = buildzhiTaskList(type, measureTask, item, confMap, scheduleList, monitor, buildType);
							if(taskTypeList != null)
								taskList.addAll(taskTypeList);
						}
						break;
					case DEVICE_TASK_SET:
						if(buildType != 2) {
							taskTypeList = buildzhiTaskList(type, measureTask, item, confMap, scheduleList, monitor, buildType);
							if(taskTypeList != null)
								taskList.addAll(taskTypeList);
						}
						break;
					case PLATFORM_STREAM:
//						if (buildType == 2 && monitorType == 2 && (task_type_id == 3 || task_type_id == 4)) {
						if (buildType == 2 && (task_type_id == 3 || task_type_id == 4)) {
							//如果是收测录音，则将生成收测录音任务
							for(TaskScheduleTable schedule : scheduleList) {
								String runplan_id = schedule.getRunplan_id();
								RunplanView view = taskRunplanMap.get(runplan_id);
								taskTypeList = buildXiaoTaskList(type, measureTask, item, confMap, schedule, monitor, view);
								if(taskTypeList != null)
									taskList.addAll(taskTypeList);
							}
						}
						break;
					}
				}
			}
		}
		return taskList;
	}*/

	/**
	 * 三满任务删除 20160808  lyc
	 * @param type
	 * @return
	 */
	public List<Task> getDelTaskList(DBConverType type) {
		//key:task_id, value:MeasureTask
		Map<Integer, TaskTable> delTaskMap = taskDataMgr.getDelTaskMap();   //20160808 lyc  三满任务集合
		//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
		Map<Integer, Map<Integer, TaskFreqTable>> taskFreqMap = taskDataMgr.getTaskFreqMap();
		//key:task_id, value:Map
		Map<Integer, Map<String, String>> taskConfMap = taskDataMgr.getTaskConfMap(); 
		//key:taskfreq_id, value:List
		Map<Integer, List<TaskScheduleTable>> taskScheduleMap = taskDataMgr.getTaskScheduleMap();
		//key:task_id, value:list 多个Monitor_id
		Map<Integer, List<TaskMonitorTable>> taskMonitorMap = taskDataMgr.getTaskMonitorMap();

		List<Task> taskList = new ArrayList<Task>();
		List<Task> taskTypeList = null;

		for(TaskTable delTask : delTaskMap.values()) {
			int taskID = delTask.getTask_id();
			if (!taskFreqMap.containsKey(taskID) || !taskConfMap.containsKey(taskID)) {
				Log.debug("质量任务中缺少指定的任务ID, taskID=" + taskID);
				continue;
			}
			Map<Integer, TaskFreqTable> freqMap = taskFreqMap.get(taskID);
			Map<String, String> confMap = taskConfMap.get(taskID);
			List<TaskMonitorTable> monitorList = taskMonitorMap.get(taskID);
			List<TaskScheduleTable> scheduleList = null;
			for(TaskFreqTable item : freqMap.values()) {
				int taskFreqID = item.getTaskfreq_id();
				if (!taskScheduleMap.containsKey(taskFreqID)) {
					continue;
				}
				scheduleList = taskScheduleMap.get(taskFreqID);
				//如果是效果类任务，还是按照一个小schedule去划分任务，但是如果是质量设置类任务则需要将任务scheduleList传入task对象 modify by jiahao 2015822
				//0:其他任务操作; 1:采集录音(设备端任务); 2:收测录音(平台录音)
				int buildType = 0;
				if (confMap.containsKey(BuildConstants.EffectTask.COLLECT_STREAM_TASK))
					buildType = 1;
				else if(confMap.containsKey(BuildConstants.EffectTask.RECIEVER_STREAM_TASK))
					buildType = 2;
				for(TaskMonitorTable monitor : monitorList) {
						if(buildType != 2) {
							taskTypeList = buildzhiTaskList(type, delTask, item, confMap, scheduleList, monitor, buildType);
							if(taskTypeList != null)
								taskList.addAll(taskTypeList);
						}
				}
			}
		}
		return taskList;
	}
	
	/**
	 * 所有类型任务修改 20160803
	 * @param type
	 * @return
	 */
	public List<Task> getTaskList(DBConverType type) {
		//key:task_id, value:MeasureTask
		Map<Integer, TaskTable> measureTaskMap = taskDataMgr.getMeasureTaskMap(); //任务列表
		//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
		Map<Integer, Map<Integer, TaskFreqTable>> taskFreqMap = taskDataMgr.getTaskFreqMap();  //任务频率表

		Map<String, RunplanView> taskRunplanMap = taskDataMgr.getTaskRunplanMap(); //运行图表   gbrd_runplan_infos_v
		//key:task_id, value:Map
		Map<Integer, Map<String, String>> taskConfMap = taskDataMgr.getTaskConfMap();  //任务属性表
		//key:taskfreq_id, value:List
		Map<Integer, List<TaskScheduleTable>> taskScheduleMap = taskDataMgr.getTaskScheduleMap(); //任务周期表
		//key:task_id, value:list 多个Monitor_id 
		Map<Integer, List<TaskMonitorTable>> taskMonitorMap = taskDataMgr.getTaskMonitorMap(); //任务站点表
		
		List<Task> taskList = new ArrayList<Task>();
		List<Task> taskTypeList = null;

		for(TaskTable measureTask : measureTaskMap.values()) {
			int taskID = measureTask.getTask_id();
			int task_type_id = measureTask.getTask_type_id();
			if (!taskFreqMap.containsKey(taskID) || !taskConfMap.containsKey(taskID)) {
				Log.debug("收测任务中缺少指定的任务ID, taskID=" + taskID);
				continue;
			}
			Map<Integer, TaskFreqTable> freqMap = taskFreqMap.get(taskID);
			Map<String, String> confMap = taskConfMap.get(taskID);
			List<TaskMonitorTable> monitorList = taskMonitorMap.get(taskID);
			List<TaskScheduleTable> scheduleList = null;
			for(TaskFreqTable item : freqMap.values()) {
				int taskFreqID = item.getTaskfreq_id();
				if (!taskScheduleMap.containsKey(taskFreqID)) {
					continue;
				}
				scheduleList = taskScheduleMap.get(taskFreqID);
				//如果是效果类任务，还是按照一个小schedule去划分任务，但是如果是质量设置类任务则需要将任务scheduleList传入task对象 modify by jiahao 2015822
				//0:其他任务操作; 1:采集录音(设备端任务); 2:收测录音(平台录音)
				int buildType = 0;
				if (confMap.containsKey(BuildConstants.EffectTask.COLLECT_STREAM_TASK))
					buildType = 1;
				else if(confMap.containsKey(BuildConstants.EffectTask.RECIEVER_STREAM_TASK))
					buildType = 2;
				if(type == DBConverType.DEVICE_TASK_RECOVER){
					for(TaskMonitorTable monitor : monitorList) {
						if(buildType != 2) {
							taskTypeList = buildzhiTaskList(type, measureTask, item, confMap, scheduleList, monitor, buildType);
							if(taskTypeList != null)
								taskList.addAll(taskTypeList);
						}
					}
				}else if(type == DBConverType.DEVICE_TASK_SET){
					for(TaskMonitorTable monitor : monitorList) {
						if(buildType != 2) {
							taskTypeList = buildzhiTaskList(type, measureTask, item, confMap, scheduleList, monitor, buildType);
							if(taskTypeList != null)
								taskList.addAll(taskTypeList);
						}
					}
				}else if(type == DBConverType.PLATFORM_STREAM){
					if (buildType == 2 && (task_type_id == 3 || task_type_id == 4)) {
						//如果是收测录音，则将生成收测录音任务
						for(TaskScheduleTable schedule : scheduleList) {
							String runplan_id = schedule.getRunplan_id();
							RunplanView view = taskRunplanMap.get(runplan_id);
							taskTypeList = buildXiaoTaskList(type, measureTask, item, confMap, schedule, monitorList, view);
							if(taskTypeList != null)
								taskList.addAll(taskTypeList);
						}
					}
				}
				/*switch(type) {
				case DEVICE_TASK_RECOVER:
					break;
				case DEVICE_TASK_SET:
					break;
				case PLATFORM_STREAM:
					break;
				}*/
			}
		}
		return taskList;
	}

	/**
	 * 巡检效果类任务   20170427
	 * @param type
	 * @return
	 */
	public List<RealTimeStreamDTO> getStreamList(DBConverType type) {
		//key:task_id, value:MeasureTask
		List<RealTimeStreamDTO> streamList = taskDataMgr.getStreamList(); //遥控站巡检任务列表
		
		return streamList;
	}
	
	public Map<String,Object> getRecoverResult(String monitorId){
		return taskDataMgr.queryRecoverTaskData(monitorId);
	}
	
/*	private List<Task> buildXiaoTaskList(DBConverType type, TaskTable mTask, TaskFreqTable taskFreqTable, Map<String, String> confValueMap, TaskScheduleTable schedule, TaskMonitorTable monitor, RunplanView view) {
		TaskAttribute taskAttr = newTaskAttribute(mTask, confValueMap);
		TaskFreq taskFreq = newTaskFreq(taskFreqTable);
		TaskMonitor taskMonitor = newTaskMonitor(monitor, confValueMap);
		Map<String, String> taskConfMap = newTaskConfMap(confValueMap);
		TaskRunplan info= newTaskRunplanInfo(view);

		List<TaskSchedule> taskScheduleList = new ArrayList<TaskSchedule>();
		TaskSchedule taskSchedule = newTaskSchedule(schedule);
		taskSchedule.setRunplan(info);
		taskScheduleList.add(taskSchedule);
		List<Task> taskList = new ArrayList<Task>();

		Task task = null;
		task = buildTask(taskAttr, taskFreq, taskConfMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto));
		taskList.add(task);
		return taskList;
	}*/
	/**
	 * 效果类任务 修改 lyc
	 * @param type
	 * @param mTask
	 * @param taskFreqTable
	 * @param confValueMap
	 * @param schedule
	 * @param monitor
	 * @param view
	 * @return
	 */
	private List<Task> buildXiaoTaskList(DBConverType type, TaskTable mTask, TaskFreqTable taskFreqTable, Map<String, String> confValueMap,
			TaskScheduleTable schedule, List<TaskMonitorTable> monitorList, RunplanView view) {
		TaskAttribute taskAttr = newTaskAttribute(mTask, confValueMap);
		List<TaskMonitor> listTaskMonitor = new ArrayList<TaskMonitor>();
		TaskFreq taskFreq = newTaskFreq(taskFreqTable);
		for(TaskMonitorTable monitor: monitorList){
			TaskMonitor taskMonitor = newTaskMonitor(monitor, confValueMap);
			listTaskMonitor.add(taskMonitor);
		}
		Map<String, String> taskConfMap = newTaskConfMap(confValueMap);
		TaskRunplan info= newTaskRunplanInfo(view);
		
		List<TaskSchedule> taskScheduleList = new ArrayList<TaskSchedule>();
		TaskSchedule taskSchedule = newTaskSchedule(schedule);
		taskSchedule.setRunplan(info);
		taskScheduleList.add(taskSchedule);
		List<Task> taskList = new ArrayList<Task>();
		
		Task task = null;
		task = buildTask(taskAttr, taskFreq, taskConfMap, taskScheduleList, listTaskMonitor, new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto));
		taskList.add(task);
		return taskList;
	}

	private List<Task> buildzhiTaskList(DBConverType type, TaskTable mTask, TaskFreqTable taskFreqTable, Map<String, String> confValueMap, List<TaskScheduleTable> scheduleList, TaskMonitorTable monitor, int buildType) {
		TaskAttribute taskAttr = newTaskAttribute(mTask, confValueMap);
		TaskFreq taskFreq = newTaskFreq(taskFreqTable);
		TaskMonitor taskMonitor = newTaskMonitor(monitor, confValueMap);
		Map<String, String> taskConfMap = newTaskConfMap(confValueMap);
		List<TaskSchedule> taskScheduleList = newTaskScheduleList(scheduleList);
		List<Task> taskList = new ArrayList<Task>();
		List<Task> tempList = null;
		tempList = buildCollectManualTaskList(type, taskAttr, taskFreq, taskConfMap, taskScheduleList, taskMonitor, buildType);
		taskList.addAll(tempList);
		return taskList;
	}

	private List<Task> buildCollectManualTaskList(DBConverType type, TaskAttribute taskAttr, TaskFreq taskFreq, Map<String, String> confValueMap, List<TaskSchedule> taskScheduleList, TaskMonitor taskMonitor, int buildType) {
		List<Task> taskList = new ArrayList<Task>();
		Task task = null;
		int taskTypeID = taskAttr.getTask_type_id();
		switch(taskTypeID) {
		case BuildConstants.INTEGRATE_TASK_TYPE:
			if (confValueMap.containsKey(BuildConstants.QualityTask.OFFSET_TASK)) {
				if(type.equals(DBConverType.DEVICE_TASK_SET))
					task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.OffsetTaskSet, BusinessTaskType.measure_manual_set, ProtocolType.OffsetTaskSet));
				else
					task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.OffsetHistoryQuery, BusinessTaskType.measure_manual_recover, ProtocolType.OffsetTaskSet));
				taskList.add(task);
			}
			if (buildType == 1 && confValueMap.containsKey(BuildConstants.EffectTask.COLLECT_STREAM_TASK)) {
				if(type.equals(DBConverType.DEVICE_TASK_SET))
					task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.StreamTaskSet, BusinessTaskType.measure_manual_set, ProtocolType.StreamTaskSet));
				else
					task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.StreamHistoryQuery, BusinessTaskType.measure_manual_recover, ProtocolType.StreamTaskSet));
				taskList.add(task);
			}

			if (confValueMap.containsKey(BuildConstants.QualityTask.LEVEL_KPI) ||
					confValueMap.containsKey(BuildConstants.QualityTask.AM_MODULATION_KPI) ||
					confValueMap.containsKey(BuildConstants.QualityTask.FM_MODULATION_KPI) ||
					confValueMap.containsKey(BuildConstants.QualityTask.BANDWIDTH_KPI)) {
				if(type.equals(DBConverType.DEVICE_TASK_SET))
					task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.QualityTaskSet, BusinessTaskType.measure_manual_set, ProtocolType.QualityTaskSet));
				else
					task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.QualityHistoryQuery, BusinessTaskType.measure_manual_recover, ProtocolType.QualityTaskSet));

				taskList.add(task);
			}

			break;
		case BuildConstants.SPECTRUM_TASK_TYPE:
			if(type.equals(DBConverType.DEVICE_TASK_SET))
				task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.SpectrumTaskSet, BusinessTaskType.measure_manual_set, ProtocolType.SpectrumTaskSet));
			else
				task = buildTask(taskAttr, taskFreq, confValueMap, taskScheduleList, taskMonitor, new BuildType(ProtocolType.SpectrumHistoryQuery, BusinessTaskType.measure_manual_recover, ProtocolType.SpectrumTaskSet));

			taskList.add(task);
			break;
		}
		return taskList;
	}
	/**
	 * 效果类 任务修改  lyc
	 * @param taskAttr
	 * @param taskFreq
	 * @param confValueMap
	 * @param scheduleList
	 * @param monitor
	 * @param type
	 * @return
	 */
	private Task buildTask(TaskAttribute taskAttr, TaskFreq taskFreq, Map<String, String> confValueMap,
			List<TaskSchedule> scheduleList, List<TaskMonitor> monitor, BuildType type) {
		Task task = new Task();
		task.setBuildType(type);
		task.setMeasureTask(taskAttr);
		task.setTaskConfAttr(confValueMap);
		task.setTaskFreq(taskFreq);
		task.setTaskMonitorList(monitor);
		//将schedule按照开始时间排序
		List<TaskSchedule> newList =  DataUtils.sort(scheduleList);
		task.setScheduleList(newList);
		return task;
	}
	
	private Task buildTask(TaskAttribute taskAttr, TaskFreq taskFreq, Map<String, String> confValueMap,
			List<TaskSchedule> scheduleList, TaskMonitor monitor, BuildType type) {
		Task task = new Task();
		task.setBuildType(type);
		task.setMeasureTask(taskAttr);
		task.setTaskConfAttr(confValueMap);
		task.setTaskFreq(taskFreq);
		task.setTaskMonitor(monitor);
		//将schedule按照开始时间排序
		List<TaskSchedule> newList =  DataUtils.sort(scheduleList);
		task.setScheduleList(newList);
		return task;
	}

	//	protected List<Task> getTaskList() {
	//		//key:task_id, value:MeasureTask
	//		Map<Integer, TaskTable> measureTaskMap = taskDataMgr.getMeasureTaskMap();
	//		//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
	//		Map<Integer, Map<Integer, TaskFreqTable>> taskFreqMap = taskDataMgr.getTaskFreqMap();
	//		//key:task_id, value:Map
	//		Map<Integer, Map<String, String>> taskConfMap = taskDataMgr.getTaskConfMap(); 
	//		//key:taskfreq_id, value:List
	//		Map<Integer, List<TaskScheduleTable>> taskScheduleMap = taskDataMgr.getTaskScheduleMap();
	//		//key:task_id, value:list 多个Monitor_id
	//		Map<Integer, List<TaskMonitorTable>> taskMonitorMap = taskDataMgr.getTaskMonitorMap();
	//		
	//		List<Task> taskList = new ArrayList<Task>();
	//		List<Task> taskTypeList = null;
	//		for(TaskTable measureTask : measureTaskMap.values()) {
	//			int taskID = measureTask.getTask_id();
	//			if (!taskFreqMap.containsKey(taskID) || !taskConfMap.containsKey(taskID)) {
	//				Log.debug("收测任务中缺少指定的任务ID, taskID=" + taskID);
	//				continue;
	//			}
	//			Map<Integer, TaskFreqTable> freqMap = taskFreqMap.get(taskID);
	//			Map<String, String> confMap = taskConfMap.get(taskID);
	//			List<TaskMonitorTable> monitorList = taskMonitorMap.get(taskID);
	//			List<TaskScheduleTable> scheduleList = null;
	//			for(TaskFreqTable item : freqMap.values()) {
	//				int taskFreqID = item.getTaskfreq_id();
	//				if (!taskScheduleMap.containsKey(taskFreqID)) {
	//					continue;
	//				}
	//				scheduleList = taskScheduleMap.get(taskFreqID);
	//				for(TaskMonitorTable monitor : monitorList) {
	//					for(TaskScheduleTable schedule : scheduleList) {
	//						taskTypeList = buildTaskList(measureTask, item, confMap, schedule, monitor);
	//						taskList.addAll(taskTypeList);
	//					}
	//				}
	//				
	//			}
	//		}
	//		return taskList;
	//	}
	//	
	//	private List<Task> buildTaskList(TaskTable mTask, TaskFreqTable taskFreqTable, Map<String, String> confValueMap, TaskScheduleTable schedule, TaskMonitorTable monitor) {
	//		TaskAttribute taskAttr = newTaskAttribute(mTask, confValueMap);
	//		TaskFreq taskFreq = newTaskFreq(taskFreqTable);
	//		TaskMonitor taskMonitor = newTaskMonitor(monitor);
	//		Map<String, String> taskConfMap = newTaskConfMap(confValueMap);
	//		TaskSchedule taskSchedule = newTaskSchedule(schedule);
	//		
	//		List<Task> taskList = new ArrayList<Task>();
	//		int taskOriginalID = mTask.getTask_origin_id();
	//		
	//		Task task = null;
	//		List<Task> tempList = null;
	//		if (taskOriginalID == BuildConstants.MANUANL) {
	//			tempList = buildManualTaskList(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, BusinessTaskType.measure_manual_set);
	//			taskList.addAll(tempList);
	//		}
	//		if (taskOriginalID == BuildConstants.MANUANL) {
	//			tempList = buildManualTaskList(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, BusinessTaskType.measure_manual_recover);
	//			taskList.addAll(tempList);
	//		}
	//		if (taskOriginalID == BuildConstants.BROADCAST) {
	//			task = buildTask(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto_centre));
	//			taskList.add(task);
	//		}
	//		if (taskOriginalID == BuildConstants.EXPRIMENT) {
	//			task = buildTask(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto_expr));
	//			taskList.add(task);
	//		}
	//		
	//		return taskList;
	//	}

	//	protected List<Task> getTaskList(List<BusinessTaskType> typeList) {
	//		//key:task_id, value:MeasureTask
	//		Map<Integer, TaskTable> measureTaskMap = taskDataMgr.getMeasureTaskMap();
	//		//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
	//		Map<Integer, Map<Integer, TaskFreqTable>> taskFreqMap = taskDataMgr.getTaskFreqMap();
	//		//key:task_id, value:Map
	//		Map<Integer, Map<String, String>> taskConfMap = taskDataMgr.getTaskConfMap(); 
	//		//key:taskfreq_id, value:List
	//		Map<Integer, List<TaskScheduleTable>> taskScheduleMap = taskDataMgr.getTaskScheduleMap();
	//		//key:task_id, value:list 多个Monitor_id
	//		Map<Integer, List<TaskMonitorTable>> taskMonitorMap = taskDataMgr.getTaskMonitorMap();
	//		
	//		List<Task> taskList = new ArrayList<Task>();
	//		List<Task> taskTypeList = null;
	//		for(TaskTable measureTask : measureTaskMap.values()) {
	//			int taskID = measureTask.getTask_id();
	//			if (!taskFreqMap.containsKey(taskID) || !taskConfMap.containsKey(taskID)) {
	//				Log.debug("收测任务中缺少指定的任务ID, taskID=" + taskID);
	//				continue;
	//			}
	//			Map<Integer, TaskFreqTable> freqMap = taskFreqMap.get(taskID);
	//			Map<String, String> confMap = taskConfMap.get(taskID);
	//			List<TaskMonitorTable> monitorList = taskMonitorMap.get(taskID);
	//			List<TaskScheduleTable> scheduleList = null;
	//			for(TaskFreqTable item : freqMap.values()) {
	//				int taskFreqID = item.getTaskfreq_id();
	//				if (!taskScheduleMap.containsKey(taskFreqID)) {
	//					continue;
	//				}
	//				scheduleList = taskScheduleMap.get(taskFreqID);
	//				for(TaskMonitorTable monitor : monitorList) {
	//					for(TaskScheduleTable schedule : scheduleList) {
	//						taskTypeList = buildTaskList(measureTask, item, confMap, schedule, monitor, typeList);
	//						taskList.addAll(taskTypeList);
	//					}
	//				}
	//				
	//			}
	//		}
	//		return taskList;
	//	}

	//	private List<Task> buildTaskList(TaskTable mTask, TaskFreqTable taskFreqTable, Map<String, String> confValueMap, TaskScheduleTable schedule, TaskMonitorTable monitor, List<BusinessTaskType> typeList) {
	//		TaskAttribute taskAttr = newTaskAttribute(mTask, confValueMap);
	//		TaskFreq taskFreq = newTaskFreq(taskFreqTable);
	//		TaskMonitor taskMonitor = newTaskMonitor(monitor);
	//		Map<String, String> taskConfMap = newTaskConfMap(confValueMap);
	//		TaskSchedule taskSchedule = newTaskSchedule(schedule);
	//		
	//		List<Task> taskList = new ArrayList<Task>();
	//		int taskOriginalID = mTask.getTask_origin_id();
	//		
	//		Task task = null;
	//		List<Task> tempList = null;
	//		for(BusinessTaskType type : typeList) {
	//			switch(type) {
	//			case measure_manual_set:
	//				if (taskOriginalID == BuildConstants.MANUANL) {
	//					tempList = buildManualTaskList(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, BusinessTaskType.measure_manual_set);
	//					taskList.addAll(tempList);
	//				}
	//				break;
	//			case measure_manual_recover:
	//				if (taskOriginalID == BuildConstants.MANUANL) {
	//					tempList = buildManualTaskList(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, BusinessTaskType.measure_manual_recover);
	//					taskList.addAll(tempList);
	//				}
	//				break;
	//			case measure_auto_centre:
	//				if (taskOriginalID == BuildConstants.BROADCAST) {
	//					task = buildTask(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto_centre));
	//					taskList.add(task);
	//				}
	//				break;
	//			case measure_auto_expr:
	//				if (taskOriginalID == BuildConstants.EXPRIMENT || taskOriginalID == BuildConstants.OVERSEAS ) {
	//					task = buildTask(taskAttr, taskFreq, taskConfMap, taskSchedule, taskMonitor, new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto_expr));
	//					taskList.add(task);
	//				}
	//				break;
	//			default:
	//				break;
	//			}
	//		}
	//		
	//		return taskList;
	//	}

	//	private List<Task> buildManualTaskList(TaskAttribute taskAttr, TaskFreq taskFreq, Map<String, String> confValueMap, TaskSchedule schedule, TaskMonitor taskMonitor, BusinessTaskType type) {
	//		List<Task> taskList = new ArrayList<Task>();
	//		Task task = null;
	//		int taskTypeID = taskAttr.getTask_type_id();
	//		switch(taskTypeID) {
	//		case BuildConstants.INTEGRATE_TASK_TYPE:
	//			if (confValueMap.containsKey(BuildConstants.QualityTask.OFFSET_TASK)) {
	//				if(type.equals(BusinessTaskType.measure_manual_set))
	//					task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.OffsetTaskSet, type));
	//				else
	//					task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.OffsetHistoryQuery, type));
	//				taskList.add(task);
	//			}
	//			if (confValueMap.containsKey(BuildConstants.EffectTask.STREAM_TASK)) {
	//				if(type.equals(BusinessTaskType.measure_manual_set))
	//					task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.StreamTaskSet, type));
	//				else
	//					task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.StreamHistoryQuery, type));
	//				taskList.add(task);
	//			}
	//			
	//			if (confValueMap.containsKey(BuildConstants.QualityTask.LEVEL_KPI)) {
	//				if(type.equals(BusinessTaskType.measure_manual_set))
	//					task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.QualityTaskSet, type));
	//				else
	//					task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.QualityHistoryQuery, type));
	//				
	//				taskList.add(task);
	//			}
	//				
	//			break;
	//		case BuildConstants.SPECTRUM_TASK_TYPE:
	//			if(type.equals(BusinessTaskType.measure_manual_set))
	//				task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.SpectrumTaskSet, type));
	//			else
	//				task = buildTask(taskAttr, taskFreq, confValueMap, schedule, taskMonitor, new BuildType(ProtocolType.SpectrumHistoryQuery, type));
	//			
	//			taskList.add(task);
	//			break;
	//		}
	//		return taskList;
	//	}

	private TaskAttribute newTaskAttribute(TaskTable mTask, Map<String, String> confValueMap) {
		TaskAttribute attribute = new TaskAttribute();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(mTask);
			//			dataMap.putAll(converMap(confValueMap));  //采样周期和个数 从conf中提取
			NMBeanUtils.fillObjectAttrsO(attribute, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}

		return attribute;
	}

	//	private Map<String, String> converMap(Map<String, String> confValueMap) {
	//		confValueMap.put(BuildConstants.SAMPLE_CIRCLE, confValueMap.get(BuildConstants.TASK_SAMPLE_CIRCLE));
	//		confValueMap.put(BuildConstants.SAMPLE_NUMBER, confValueMap.get(BuildConstants.TASK_SAMPLE_NUMBER));
	//		return confValueMap;
	//	}

	private TaskFreq newTaskFreq(TaskFreqTable taskFreq) {
		TaskFreq freq = new TaskFreq();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(taskFreq);
			NMBeanUtils.fillObjectAttrsO(freq, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}

		return freq;
	}

	private TaskRunplan newTaskRunplanInfo(RunplanView view) {
		if(view == null)
			return null;
		TaskRunplan info = new TaskRunplan();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(view);
			NMBeanUtils.fillObjectAttrsO(info, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}

		return info;
	}

	private TaskMonitor newTaskMonitor(TaskMonitorTable monitor, Map<String, String> confValueMap) {
		TaskMonitor taskMonitor = new TaskMonitor();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(monitor);
			NMBeanUtils.fillObjectAttrsO(taskMonitor, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}

		return taskMonitor;
	}

	private TaskSchedule newTaskSchedule(TaskScheduleTable schedule) {
		if(schedule == null)
			return null;
		TaskSchedule taskSchedule = new TaskSchedule();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(schedule);
			NMBeanUtils.fillObjectAttrsO(taskSchedule, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}

		return taskSchedule;
	}

	private List<TaskSchedule> newTaskScheduleList(List<TaskScheduleTable> scheduleList) {
		List<TaskSchedule> taskScheduleList = new ArrayList<TaskSchedule>();

		for(TaskScheduleTable table : scheduleList) {
			TaskSchedule taskSchedule = new TaskSchedule();
			try {
				Map<String, Object> dataMap = NMBeanUtils.getObjectField(table);
				NMBeanUtils.fillObjectAttrsO(taskSchedule, dataMap);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
				return null;
			}
			taskScheduleList.add(taskSchedule);
		}
		return taskScheduleList;
	}

	private Map<String, String> newTaskConfMap(Map<String, String> confValueMap) {
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(confValueMap);
		return map;
	}
}
