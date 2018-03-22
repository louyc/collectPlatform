package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.core.business.BusinessRunplan;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.OperationType;
import com.neusoft.gbw.cp.core.collect.ScheduleFormatType;
import com.neusoft.gbw.cp.core.collect.ScheduleInfo;
import com.neusoft.gbw.cp.core.collect.ScheduleType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskAttribute;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskRunplan;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.cp.manual.uitl.ManualSpitProcessor;
import com.neusoft.gbw.cp.manual.vo.ManualSchedule;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class ManualTaskBuilder extends BasicTaskBuilder{
	
	public static final String END_TIME = "23:59:59";
	public static final String START_TIME = "00:00:00";
	
	
	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		Task task = (Task) info.getBuisness();
		
		//如果是泰顺，则需要确认默认接收机
		if(isOpenEncode() && isTaiShun(task)) {
			ProtocolType type = task.getBuildType().getProType();
			if(type.equals(ProtocolType.QualityTaskSet))
				task.getTaskFreq().setReceiver_code(ConfigVariable.TAISHUN_QUALITYTASKSET_ENCODE);
			else if(type.equals(ProtocolType.StreamTaskSet))
				task.getTaskFreq().setReceiver_code(ConfigVariable.TAISHUN_STREAMTASKSET_ENCODE);
			else task.getTaskFreq().setReceiver_code("");
		}
		
		BusinessTask bTask = new BusinessTask();
		bTask.setFreq(task.getTaskFreq().getFreq());
		bTask.setMonitor_id(task.getTaskMonitor().getMonitor_id());
		bTask.setMonitor_type(task.getTaskMonitor().getType_id());
		bTask.setMonitor_code(task.getTaskMonitor().getMonitor_code());
		bTask.setTask_name(task.getMeasureTask().getTask_name());//增加taskName
		bTask.setTaskfreq_id(task.getTaskFreq().getTaskfreq_id());
		bTask.setFreq(task.getTaskFreq().getFreq());
		bTask.setTask_origin_id(task.getMeasureTask().getTask_type_id());
//		bTask.setRunplan_id(task.getTaskSchedule().getRunplan_id());   
//		bTask.setRunplan_id(task.getScheduleList().get(0).getRunplan_id());   //新加  20160806  lyc
//		bTask.setRunplan(getBusinessRunplan(task)); //新加  20160806  lyc
		bTask.setTask_id(task.getTaskFreq().getTask_id());
//		bTask.setTime_id(task.getTaskSchedule().getTime_id());
		bTask.setType(info.getType().getBusTaskType());
		bTask.setUnit_begin(task.getMeasureTask().getValidity_b_time());
		bTask.setUnit_end(task.getMeasureTask().getValidity_e_time());
		bTask.setRecover_begin(task.getMeasureTask().getRecover_b_time());
		bTask.setRecover_end(task.getMeasureTask().getRecover_e_time());
		bTask.setMeasure_unit_time(getRecordLength(task));
		bTask.setIs_force(BuildConstants.NO_FORCE);
		return bTask;
	}
	
	private BusinessRunplan getBusinessRunplan(Task task) {
		TaskRunplan taskRunplan = task.getScheduleList().get(0).getRunplan();
		if(taskRunplan == null)
			return null;
		BusinessRunplan runplan = new BusinessRunplan();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(taskRunplan);
			NMBeanUtils.fillObjectAttrsO(runplan, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}
		return runplan;
	}
	private boolean isTaiShun(Task task) {
		boolean isFlag = false;
		long monitorId = task.getTaskMonitor().getMonitor_id();
		MonitorDevice info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
		if (info == null) 
			return false;
		
		int manufacturer_id = info.getManufacturer_id();
		if(2 == manufacturer_id)
			isFlag = true;
		return isFlag ;
	}
	
	private boolean isOpenEncode() {
		return ConfigVariable.TAISHUN_SET_TASK_ENCODE_USE_SWITCH == 1;
	}
	
	private static int getRecordLength(Task sTask) {
		String recordLength = null;
		Map<String, String> confMap = sTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.EffectTask.EFFECT_RECORDLENGTH))
			recordLength = confMap.get(BuildConstants.EffectTask.EFFECT_RECORDLENGTH);
		if(null !=recordLength){
			if(recordLength.contains(":"))
				return (Integer)TimeUtils.converterToTime(recordLength)/60;
			else
				return (Integer)Integer.parseInt(recordLength);
		}else{
			return 1;
		}
	}
	@Override
	ScheduleInfo buildScheduleInfo(BuildInfo info) {
		ScheduleInfo schedule = new ScheduleInfo();
		Task task = (Task) info.getBuisness();
		TaskAttribute taskAttr = task.getMeasureTask();
		BusinessTaskType type = info.getType().getBusTaskType();
		switch(type) {
		case measure_manual_recover:
			schedule.setType(ScheduleType.realtime);
			break;
		default:
			if(taskAttr.getIs_timelapse() == 1) {
				schedule.setType(ScheduleType.plan);
				schedule.setFormat(ScheduleFormatType.yMdHms);
				schedule.setTime(taskAttr.getTime_lapse());
				schedule.setOperType(buildOperationType(type));
			}else {
				schedule.setType(ScheduleType.realtime);
			}
			break;
		}

//		//此处以后增加检修处理
		if(null !=task.getScheduleList() && task.getScheduleList().size()>0 && null !=task.getScheduleList().get(0).getRunplan_id() && task.getScheduleList().get(0).getRunplan_id()!=""){
			ManualSpitProcessor process = new ManualSpitProcessor();
			process.setManualSchedule(converManualSchedule(task.getScheduleList()));
			List<ManualSchedule> newScheduleList = null;
			try {
				newScheduleList = process.getSplitResult();
			} catch (Exception e) {
				Log.error("", e);
			}
			((Task) info.getBuisness()).setScheduleList(converSchedule(newScheduleList));
		}
		//将最新生成的schedule放入info中
		return schedule;
	}
	
	private List<ManualSchedule> converManualSchedule(List<TaskSchedule> scheduleList) {
		List<ManualSchedule> newScheduleList = new ArrayList<ManualSchedule>();
		ManualSchedule mscheudle = null;
		for(TaskSchedule one : scheduleList) {
			mscheudle = new ManualSchedule();
			mscheudle.setDayofweek(one.getDayofweek());
			mscheudle.setEndtime(one.getEndtime());
			mscheudle.setExpiredays(one.getExpiredays());
			mscheudle.setIs_overhaul(one.getIs_overhaul());
			mscheudle.setReportmode(one.getReportmode());
			mscheudle.setRunplan_id(one.getRunplan_id());
			mscheudle.setSchedule_type(one.getSchedule_type());
			mscheudle.setStarttime(one.getStarttime());
			mscheudle.setSync_status(one.getSync_status());
			mscheudle.setTaskfreq_id(one.getTaskfreq_id());
			mscheudle.setTime_id(one.getTime_id());
			mscheudle.setValid_start_time(one.getValid_start_time());
			mscheudle.setValid_end_time(one.getValid_end_time());
			newScheduleList.add(mscheudle);
		}
		return newScheduleList;
	}
	
	private List<TaskSchedule> converSchedule(List<ManualSchedule> scheduleList) {
		List<TaskSchedule> newScheduleList = new ArrayList<TaskSchedule>();
		TaskSchedule scheudle = null;
		for(ManualSchedule one : scheduleList) {
			scheudle = new TaskSchedule();
			scheudle.setDayofweek(one.getDayofweek());
			scheudle.setEndtime(one.getEndtime());
			scheudle.setExpiredays(one.getExpiredays());
			scheudle.setIs_overhaul(one.getIs_overhaul());
			scheudle.setReportmode(one.getReportmode());
			scheudle.setRunplan_id(one.getRunplan_id());
			scheudle.setSchedule_type(one.getSchedule_type());
			scheudle.setStarttime(one.getStarttime());
			scheudle.setSync_status(one.getSync_status());
			scheudle.setTaskfreq_id(one.getTaskfreq_id());
			scheudle.setTime_id(one.getTime_id());
			scheudle.setValid_start_time(one.getValid_start_time());
			scheudle.setValid_end_time(one.getValid_end_time());
			newScheduleList.add(scheudle);
		}
		return newScheduleList;
	}
	
	protected OperationType buildOperationType(BusinessTaskType type) {
		OperationType operType = null;
		switch(type) {
		case measure_manual_set:
			operType = OperationType.add;
			break;
		case measure_manual_del:
			operType = OperationType.del;
			break;
		default:
			break;
		}
		return operType;
	}
	
	@Override
	Map<String, Object> buildExpandObj(BuildInfo info) {
		Task task = (Task) info.getBuisness();
		Map<String, Object> expandObj = new HashMap<String, Object>();
		expandObj.put(ExpandConstants.REPORT_CONTROL_KEY, task.getExpandObj());
		expandObj.put(ExpandConstants.TASK_UNIQUE_ID, DataUtils.getTaskID(task));//存放taskID
		return expandObj;
	}

	@Override
	public int getTransferTimeOut(BuildInfo info) {
		int time = 15;
		BusinessTaskType type = info.getType().getBusTaskType();
		switch(type) {
		case measure_manual_recover:
			time = 15;
			break;
		case measure_manual_del:
			time = 60;
		default:
		}
		return time;
	}
}
