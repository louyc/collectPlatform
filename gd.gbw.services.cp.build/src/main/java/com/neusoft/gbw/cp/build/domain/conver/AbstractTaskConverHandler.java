package com.neusoft.gbw.cp.build.domain.conver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskAttribute;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskFreq;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskMonitor;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskConfDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskFreqDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskMonitorDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskScheduleDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public abstract class AbstractTaskConverHandler {
	
	public List<Task> converTaskList(TaskDTO dto) {
		List<Task> taskList = new ArrayList<Task>();
		//转换任务信息 taskId TaskAttribute
		Map<String, TaskAttribute> measureTaskMap = buildTaskAttribute(dto);
		//转换设备信息 key:task_id, value:list 多个Monitor_id
		Map<String, List<TaskMonitor>> taskMonitorMap = buildTaskMonitor(dto);
		//转换频率信息
		Map<String, Map<String, TaskFreq>> taskFreqMap = buildTaskFreq(dto);
		//转换任务配置信息 key:task_id, value:Map
		Map<String, Map<String, String>> taskConfMap = buildTaskConf(dto); 
		//转换调度信息  key:taskfreq_id, value:List
		Map<String, List<TaskSchedule>> taskScheduleMap = buildTaskSchedules(dto);
		
		List<Task> taskTypeList = null;
		for(TaskAttribute measureTask : measureTaskMap.values()) {
			String taskID = measureTask.getTask_id() + "";
			if (!taskFreqMap.containsKey(taskID) || !taskConfMap.containsKey(taskID)) {
				Log.debug("[构建服务]手动任务TaskDTO对应的频率集合和配置信息中缺少指定的任务ID, taskID=" + taskID + ",TaskFreqMap=" + taskFreqMap + ",taskConfMap=" + taskConfMap);
				continue;
			}
			Map<String, TaskFreq> freqMap = taskFreqMap.get(taskID);
			Map<String, String> confMap = taskConfMap.get(taskID);
			List<TaskMonitor> monitorList = taskMonitorMap.get(taskID);
			List<TaskSchedule> scheduleList = null;
			List<TaskSchedule> newScheduleList = null;
			for(TaskFreq item : freqMap.values()) {
				String taskFreqID = item.getTaskfreq_id() + "";
				if (!taskScheduleMap.containsKey(taskFreqID)) {
					continue;
				}
				scheduleList = taskScheduleMap.get(taskFreqID);
				if (monitorList!=null && !monitorList.isEmpty()) {
					for(TaskMonitor monitor : monitorList) {
						//排除非本频率的schedule
						newScheduleList = new ArrayList<TaskSchedule>();
						for(TaskSchedule one :scheduleList) {
							if(taskFreqID.equals(one.getTaskfreq_id() + ""))
								newScheduleList.add(one);
						}
						//如果是采集点则直接封装成scheduleList
//						int monitorType = monitor.getType_id();
						//monitorType 1、采集点，2、遥控站
//						if(monitorType == 1 || 
//						  (monitorType == 2 && confMap.containsKey(BuildConstants.EffectTask.STREAM_TASK) && confMap.get(BuildConstants.EffectTask.STREAM_TASK).equals("1") )) {
						taskTypeList = buildSetTaskList(measureTask, item, confMap, newScheduleList, monitor);
						taskList.addAll(taskTypeList);
//						}else if(monitorType == 2) {
//							//目前手动任务不用处理遥控站
//							//确定站点类型，如果是遥控站则遍历schedule
////							for(TaskSchedule schedule : scheduleList) {
////								taskTypeList = buildMeasureTaskList(measureTask, item, confMap, schedule, monitor);
////								taskList.addAll(taskTypeList);
////							}
//						}
						
					}
				}
			}
		}
		return taskList;
	}
	
	List<Task> buildSetTaskList(TaskAttribute taskAttr, TaskFreq taskFreq,
			Map<String, String> confValueMap, List<TaskSchedule> scheduleList,
			TaskMonitor taskMonitor) {
		List<Task> taskList = new ArrayList<Task>();
		Task task = null;
		int taskTypeID = taskAttr.getTask_type_id(); //3:广播，4：实验，5：地方三满，6：频谱
		switch(taskTypeID) {
		case BuildConstants.INTEGRATE_TASK_TYPE:
			if (confValueMap.containsKey(BuildConstants.QualityTask.OFFSET_TASK)) {
				task = buildTask(taskAttr, taskFreq, confValueMap, scheduleList, taskMonitor, buildBuildType(taskAttr,BuildConstants.QualityTask.OFFSET_TASK,ProtocolType.OffsetTaskSet));
				taskList.add(task);
			}
			if (confValueMap.containsKey(BuildConstants.EffectTask.COLLECT_STREAM_TASK) && confValueMap.get(BuildConstants.EffectTask.COLLECT_STREAM_TASK).equals("1") ) {
				task = buildTask(taskAttr, taskFreq, confValueMap, scheduleList, taskMonitor, buildBuildType(taskAttr,BuildConstants.EffectTask.COLLECT_STREAM_TASK,ProtocolType.StreamTaskSet));
				taskList.add(task);
			}
			
			if (confValueMap.containsKey(BuildConstants.QualityTask.LEVEL_KPI) ||
				confValueMap.containsKey(BuildConstants.QualityTask.AM_MODULATION_KPI) ||
				confValueMap.containsKey(BuildConstants.QualityTask.FM_MODULATION_KPI) ||
				confValueMap.containsKey(BuildConstants.QualityTask.BANDWIDTH_KPI)) {
				task = buildTask(taskAttr, taskFreq, confValueMap, scheduleList, taskMonitor, buildBuildType(taskAttr,BuildConstants.QualityTask.LEVEL_KPI,ProtocolType.QualityTaskSet));
				taskList.add(task);
			}
				
			break;
		case BuildConstants.SPECTRUM_TASK_TYPE:
				task = buildTask(taskAttr, taskFreq, confValueMap, scheduleList, taskMonitor, buildBuildType(taskAttr,null, ProtocolType.SpectrumTaskSet));
				taskList.add(task);
				
			break;
		}
		return taskList;
	}

	abstract BuildType buildBuildType(TaskAttribute taskAttr,String taskType, ProtocolType oriProType);
	
	
	public Task buildTask(TaskAttribute taskAttr, TaskFreq taskFreq, Map<String, String> confValueMap, List<TaskSchedule> scheduleList, TaskMonitor monitor, BuildType type) {
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
	
	private Map<String, TaskAttribute> buildTaskAttribute(TaskDTO dto) {
		Map<String, TaskAttribute> measureTaskMap = new HashMap<String, TaskAttribute>();
		TaskAttribute taskAttribute = new TaskAttribute();
		taskAttribute.setTask_id(Long.parseLong(dto.getTaskId()));    
		taskAttribute.setTask_name(dto.getTaskName());
//		taskAttribute.setTask_origin_id(dto.getTaskOriginId()==null?-1:Integer.parseInt(dto.getTaskOriginId()));
		taskAttribute.setTask_type_id(Integer.parseInt(dto.getTaskTypeId())); //3:广播，4：实验，5：地方三满，6：频谱
		taskAttribute.setTask_priority(Integer.parseInt(dto.getTaskPriority())); 
		taskAttribute.setValidity_b_time(dto.getValidityBTime());
		taskAttribute.setValidity_e_time(dto.getValidityETime());
		taskAttribute.setRecover_b_time(dto.getRecoverBTime());
		taskAttribute.setRecover_e_time(dto.getRecoverETime());
		taskAttribute.setIs_timelapse(Integer.parseInt(dto.getIsTimelapse()));
		taskAttribute.setTime_lapse(dto.getTimeLapse());
		taskAttribute.setTask_build_mode(Integer.parseInt(dto.getTaskBuildMode()));
//		taskAttribute.setOver_status(Integer.parseInt(dto.getOverStatus()));
//		taskAttribute.setSync_status(Integer.parseInt(dto.getSyncStatus()));
		measureTaskMap.put(dto.getTaskId(), taskAttribute);
		return measureTaskMap;
	}
	
	private Map<String, List<TaskMonitor>> buildTaskMonitor(TaskDTO dto) {
		Map<String, List<TaskMonitor>> taskMonitorMap = new HashMap<String, List<TaskMonitor>>();
		List<TaskMonitor> monitorList = new ArrayList<TaskMonitor>();
		List<TaskMonitorDTO> monitorDTOs = dto.getMonitorList();
		if (monitorDTOs!=null && !monitorDTOs.isEmpty()) {
			for (TaskMonitorDTO taskMonitorDTO : monitorDTOs) {
					TaskMonitor taskMonitor = new TaskMonitor();
					long monitorId = Long.parseLong(taskMonitorDTO.getMonitorId());
					MonitorDevice info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
					if (isNotNull(info)) {
						taskMonitor.setDevice_ip(info.getDevice_ip());
						taskMonitor.setPort(info.getPort());
						taskMonitor.setMonitor_code(info.getMonitor_code());
						taskMonitor.setMonitor_id(monitorId);
						taskMonitor.setVersion_id(info.getVersion_id());
						taskMonitor.setType_id(info.getType_id());
						String time =info.getTaskRecordLength_listen();
						if(null !=time){
							taskMonitor.setRecord_length(Integer.parseInt(time));
						}else{
							taskMonitor.setRecord_length(30);
						}
						
					} else {
						Log.info("[数据转换]未找到设备信息 monitorId=" + monitorId);
						continue;
					}
					monitorList.add(taskMonitor);
			}
		} else {
			Log.debug("[数据转换]TaskDTO中未找到TaskMonitorDTOList数据,TaskId:" + dto.getTaskId());
			return taskMonitorMap;
		}
		taskMonitorMap.put(dto.getTaskId(), monitorList);
		return taskMonitorMap;
	}
	
	private Map<String, Map<String, TaskFreq>> buildTaskFreq(TaskDTO dto) {
		//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
		Map<String, Map<String, TaskFreq>> taskFreqMap = new HashMap<String, Map<String,TaskFreq>>();
		Map<String, TaskFreq> freqMap = new HashMap<String, TaskFreq>();
		List<TaskFreqDTO> freqDTOs = dto.getFreqList();
		if (freqDTOs != null && !freqDTOs.isEmpty()) {
			for (TaskFreqDTO taskFreqDTO : freqDTOs) {
				TaskFreq taskFreq = new TaskFreq();
				taskFreq.setTaskfreq_id(Integer.parseInt(taskFreqDTO.getTaskfreqId()));
				taskFreq.setTask_id(Long.parseLong(taskFreqDTO.getTaskId()));
				taskFreq.setFreq(taskFreqDTO.getFreq());
				if(taskFreqDTO.getFreqBand() != null)
					taskFreq.setBand(Integer.parseInt(taskFreqDTO.getFreqBand()));
				taskFreq.setReceiver_code(taskFreqDTO.getReceiverCode());
				if(taskFreqDTO.getCodeRate() != null)
					taskFreq.setCode_rate(Integer.parseInt(taskFreqDTO.getCodeRate()));
//				taskFreq.setSync_status(Integer.parseInt(taskFreqDTO.getSyncStatus()));
				freqMap.put(taskFreqDTO.getTaskfreqId(), taskFreq);
			}
		} else {
			Log.debug("[数据转换]TaskDTO中未找到TaskFreqDTOList数据,TaskId:" + dto.getTaskId());
			return taskFreqMap;
		}
		taskFreqMap.put(dto.getTaskId(), freqMap);
		return taskFreqMap;
	}
	
	private Map<String, Map<String, String>> buildTaskConf(TaskDTO dto) {
		Map<String, Map<String, String>> taskConfMap = new HashMap<String, Map<String,String>>();
		Map<String, String> taskConfAttr = new Hashtable<String, String>();
		List<TaskConfDTO> confDTOs = new ArrayList<TaskConfDTO>();
		confDTOs = dto.getConfList();
		if (confDTOs != null && !confDTOs.isEmpty()) {
			for (TaskConfDTO taskConfDTO : confDTOs) {
					if(BuildConstants.TASK_SAMPLE_CIRCLE.equals(taskConfDTO.getConfCode())) 
						taskConfAttr.put(BuildConstants.TASK_SAMPLE_CIRCLE, taskConfDTO.getConfValue());
					else if(BuildConstants.TASK_SAMPLE_NUMBER.equals(taskConfDTO.getConfCode()))
						taskConfAttr.put(BuildConstants.TASK_SAMPLE_NUMBER, taskConfDTO.getConfValue());
					else
						taskConfAttr.put(taskConfDTO.getConfCode(), taskConfDTO.getConfValue());
					
					taskConfAttr.put("isUse", taskConfDTO.getInUse());
//					taskConfAttr.put("pGrp", taskConfDTO.getpGrp());
//					taskConfAttr.put("collectProperty", taskConfDTO.getCollectProperty());
			}
		} else {
			Log.debug("[数据转换]TaskDTO中未找到TaskConfDTOList数据,TaskId:" + dto.getTaskId());
			return taskConfMap;
		}
		taskConfMap.put(dto.getTaskId(), taskConfAttr);
		return taskConfMap;
	}

	private Map<String, List<TaskSchedule>> buildTaskSchedules(TaskDTO dto) {
		//key:taskfreq_id, value:List
		Map<String, List<TaskSchedule>> taskScheduleMap = new HashMap<String, List<TaskSchedule>>();
		List<TaskSchedule> taskSchedules = new ArrayList<TaskSchedule>();
		List<TaskFreqDTO> freqDTOs = null;
		List<TaskScheduleDTO> scheduleDTOs = null;
		freqDTOs = dto.getFreqList();
		if (freqDTOs!=null && !freqDTOs.isEmpty()) {
			for (TaskFreqDTO taskFreqDTO : freqDTOs) {
				if (taskFreqDTO.getTaskId().equals(dto.getTaskId())) {
					scheduleDTOs = taskFreqDTO.getScheduleList();
					for (TaskScheduleDTO taskScheduleDTO : scheduleDTOs) {

						TaskSchedule taskSchedule = new TaskSchedule();
						taskSchedule.setTime_id(Integer.parseInt(taskScheduleDTO.getTimeId()));
						taskSchedule.setTaskfreq_id(Integer.parseInt(taskScheduleDTO.getTaskfreqId()));
						taskSchedule.setRunplan_id(taskScheduleDTO.getRunplanId()==null?"":taskScheduleDTO.getRunplanId());
						taskSchedule.setSchedule_type(Integer.parseInt(taskScheduleDTO.getScheduleType()));
						taskSchedule.setDayofweek(taskScheduleDTO.getDayofweek());
						taskSchedule.setReportmode(Integer.parseInt(taskScheduleDTO.getReportmode()));
						taskSchedule.setStarttime(taskScheduleDTO.getStarttime());
						taskSchedule.setEndtime(taskScheduleDTO.getEndtime());
						taskSchedule.setExpiredays(Integer.parseInt(taskScheduleDTO.getExpiredays()));
						taskSchedule.setIs_overhaul(Integer.parseInt(taskScheduleDTO.getIsOverhaul()==null?"0":taskScheduleDTO.getIsOverhaul()));
						taskSchedule.setValid_start_time(taskScheduleDTO.getValidStartTime());
						taskSchedule.setValid_end_time(taskScheduleDTO.getValidEndTime());
						taskSchedules.add(taskSchedule);
						taskScheduleMap.put(taskFreqDTO.getTaskfreqId(), taskSchedules);
					}
				}
			}
			
		} else {
			Log.debug("[数据转换]TaskDTO中未找到TaskScheduleDTOList数据,TaskId:" + dto.getTaskId());
			return taskScheduleMap;
		}
		
		return taskScheduleMap;
	}

	private boolean isNotNull(Object obj) {
		if (obj!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	private static int converterToTime(String time) {
		int i = Time.timeToint(time);
		return i;
	}
	
}
