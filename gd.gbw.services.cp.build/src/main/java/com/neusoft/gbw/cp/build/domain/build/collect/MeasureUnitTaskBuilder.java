package com.neusoft.gbw.cp.build.domain.build.collect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.core.business.BusinessRunplan;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.collect.OperationType;
import com.neusoft.gbw.cp.core.collect.ScheduleFormatType;
import com.neusoft.gbw.cp.core.collect.ScheduleInfo;
import com.neusoft.gbw.cp.core.collect.ScheduleType;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskMonitor;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskRunplan;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class MeasureUnitTaskBuilder extends BasicTaskBuilder {

	private static final String MMSS = ":40:00";

	/*	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		Task task = (Task) info.getBuisness();
		updateFirstEnCode(info);
		BusinessTask busTask = new BusinessTask();
		busTask.setTask_id(task.getMeasureTask().getTask_id());
		busTask.setTask_name(task.getMeasureTask().getTask_name());
		int runplan_type = task.getMeasureTask().getTask_type_id();
		busTask.setTask_origin_id(runplan_type);
		busTask.setTask_build_mode(task.getMeasureTask().getTask_build_mode());
		long monitorId = task.getTaskMonitor().getMonitor_id();
		busTask.setMonitor_id(monitorId);
		busTask.setMonitor_code(task.getTaskMonitor().getMonitor_code());
		busTask.setRunplan_id(task.getScheduleList().get(0).getRunplan_id());
		busTask.setFreq(task.getTaskFreq().getFreq());
		busTask.setManufacturer_id(info.getDevice().getManufacturer_id());   // 设备号 不确定
		busTask.setType(info.getType().getBusTaskType());
		busTask.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		busTask.setRecordLength(getRecordLength(task));
		//判断运行图类型为广播还是实验，提取不同的收测单元间隔
		int unitTime = task.getMeasureTask().getTask_type_id() == 3 ? info.getDevice().getBroadMeasureUnitTime()   //??   对应哪个站点的收测时间
				: info.getDevice().getExperMeasureUnitTime();
		busTask.setMeasure_unit_time(unitTime);
		TimeRemindMsg msg = (TimeRemindMsg)task.getExpandObj();
		busTask.setUnit_begin(msg.getRemindtime());
		busTask.setUnit_end(msg.getEndTime());
		busTask.setMeasure_status(1); //1:第一次采集，2：补采
		busTask.setUnit_marking_type(getUnit_marking_type(monitorId, runplan_type));
		busTask.setRunplan(getBusinessRunplan(task));
		return busTask;
	}*/
	/**
	 * 效果类 修改  lyc
	 */
	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		Task task = (Task) info.getBuisness();
		updateFirstEnCode(info);
		MonitorDevice firstDevice = info.getDeviceList().get(0);
		((Task) info.getBuisness()).getTaskFreq().setReceiver_code(firstDevice.getFirstEnCode());//修改接收机
		BusinessTask busTask = new BusinessTask();
		busTask.setTask_id(task.getMeasureTask().getTask_id());
		busTask.setTask_name(task.getMeasureTask().getTask_name());
		int runplan_type = task.getMeasureTask().getTask_type_id();
		busTask.setTask_origin_id(runplan_type);
		busTask.setTask_build_mode(task.getMeasureTask().getTask_build_mode());
		long monitorId = task.getTaskMonitorList().get(0).getMonitor_id();
		busTask.setMonitor_id(monitorId);
		busTask.setOrgMonitorId(monitorId);
		List<Long> id = new ArrayList<Long>();
		for(TaskMonitor c: task.getTaskMonitorList()){
			id.add(c.getMonitor_id());
		}
		busTask.setMonitor_id_list(id);  
		busTask.setMonitor_code(task.getTaskMonitorList().get(0).getMonitor_code());
		busTask.setRunplan_id(task.getScheduleList().get(0).getRunplan_id());
		busTask.setFreq(task.getTaskFreq().getFreq());
		busTask.setManufacturer_id(firstDevice.getManufacturer_id());  
		busTask.setType(info.getType().getBusTaskType());
		busTask.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		busTask.setRecordLength(getRecordLength(task));
		//判断运行图类型为广播还是实验，提取不同的收测单元间隔
		int unitTime = task.getMeasureTask().getTask_type_id() == 3 ? firstDevice.getBroadMeasureUnitTime()   //??   对应哪个站点的收测时间
				: firstDevice.getExperMeasureUnitTime();
		busTask.setTask_type_id(task.getMeasureTask().getTask_type_id());   //效果任务类型
		busTask.setMeasure_unit_time(unitTime);
		TimeRemindMsg msg = (TimeRemindMsg)task.getExpandObj();
		busTask.setUnit_begin(timePlan(msg.getRemindtime(),msg.getTimeinterval()));
		busTask.setUnit_end(timePlan(msg.getEndTime(),msg.getTimeinterval()));
		busTask.setMeasure_status(1); //1:第一次采集，2：补采
		busTask.setUnit_marking_type(getUnit_marking_type(monitorId, runplan_type));
		busTask.setRunplan(getBusinessRunplan(task));
		busTask.setAlias_code(firstDevice.getAlias_code());
		busTask.setAlias_name(firstDevice.getAlias_name());
		return busTask;
	}

	public static String timePlan(String time,int timeinterval){
		if(null==time || time.split(" ").length!=2){
			return time;
		}
		String day=time.split(" ")[0];
		String hour =time.split(" ")[1].split(":")[0]; 
		String second =time.split(" ")[1].split(":")[2]; 
		String minute = time.split(" ")[1].split(":")[1];
		long b = Long.valueOf(minute);
		if(timeinterval==20){
			if(b>=0 && b<20){
				return day+" "+hour+":"+"00"+":"+second;
			}else if(b<40 && b>=20){
				return day+" "+hour+":"+"20"+":"+second;
			}else{
				return day+" "+hour+":"+"40"+":"+second;
			}
		}else if(timeinterval==40){
			return day+" "+hour+":"+minute.replace(minute.substring(1), "0")+":"+second;
		}else{
			return day+" "+hour+":"+"00"+":"+second;
		}
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

	private int getUnit_marking_type(long monitorId, int runplan_type) {
		String key =  monitorId  + "_" + runplan_type;
		return DataMgrCentreModel.getInstance().getUnitStatus(key);
	}


	ScheduleInfo buildScheduleInfo(BuildInfo info) {

		Task task = (Task)info.getBuisness();
		int typeId = task.getMeasureTask().getTask_type_id();
		TimeRemindMsg msg = (TimeRemindMsg)task.getExpandObj();
		//目前schedule在收测任务中只有一条
		TaskSchedule taskSchedule = task.getScheduleList().get(0);
		int scheduleType = taskSchedule.getSchedule_type();
		//时间格式：yyyy-MM-dd HH:mm:ss
		String start = null, end = null;
		start = taskSchedule.getStarttime();
		end = taskSchedule.getEndtime();
		switch(scheduleType) {
		case BuildConstants.SINGLE_TYPE:
			break;
		case BuildConstants.CYCLE_TYPE:
			//判断是否跨天
			if (TimeUtils.isCrossDay(start, end)){
				start = TimeUtils.getYMdTime() + " " + start;
				end = TimeUtils.getDifferOneDay() + " " + end;
			} else {
				start = TimeUtils.getYMdTime() + " " + start;
				end = TimeUtils.getYMdTime() + " " + end;
			}
			break;
		}
		long time=0;
		if(typeId ==3){
			time = Long.valueOf(info.getDeviceList().get(0).getGuangboDelayTime())*60;
		}else{
			time = Long.valueOf(info.getDeviceList().get(0).getShiyanDelayTime())*60;
		}
		ScheduleInfo schedule = new ScheduleInfo();
		//针对突发频率任务进行操作，如果是突发频率则需要在本小时的40分钟进行下发，如果是正常收测任务，创建对应schedule信息，直接下发
		TaskType type = info.getTaskType();
		if(type.equals(TaskType.burst)) {
			schedule.setType(ScheduleType.plan);
			schedule.setFormat(ScheduleFormatType.yMdHms);
			schedule.setTime(getTime(start));//把当前时间分钟改成40分
			//			schedule.setTime(start);
			schedule.setOperType(OperationType.add);
			return schedule;
		}

		try {
			Date stDate =null;
			long startTime = TimeUtils.getTimeToMilliSecond(start);
			long startRemind = TimeUtils.getTimeToMilliSecond(msg.getRemindtime());
			long endRemind = TimeUtils.getTimeToMilliSecond(msg.getEndTime());
			if (startRemind < startTime && startTime < endRemind) { //播放开始时间 在采集时间间隔内  播放开发时间延时
				try {
					stDate= new Date(TimeUtils.getTimeToMilliSecond(start)+time*1000);
				} catch (Exception e) {
					Log.debug(e.getMessage()+"播放开始时间转换报错");
				}
				start=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stDate);
			} else if(startRemind > startTime){  //播放开始时间 小于采集时间间隔  采集时间间隔开始时间延时
				try {
					stDate= new Date(TimeUtils.getTimeToMilliSecond(msg.getRemindtime())+time*1000);
				} catch (Exception e) {
					Log.debug(e.getMessage()+"采集开始时间转换报错");
				}
				start=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stDate);
			}else{
				start=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			}
			startTime = TimeUtils.getTimeToMilliSecond(start);
			String currentTime = TimeUtils.getCurrentTime();
			long current = TimeUtils.getTimeToMilliSecond(currentTime);
			//判断周期的时间与时间当前时间的关系
			if (startTime > current) {
				//实际启动时间小于周期开始时间，正常设置计划任务开始
				schedule.setType(ScheduleType.plan);
				schedule.setFormat(ScheduleFormatType.yMdHms);
				schedule.setTime(start);
				schedule.setOperType(OperationType.add);
			} else {
				//实际启动时间等于或大于开始时间，设置为实时任务
				schedule.setType(ScheduleType.realtime);
			}
		} catch (Exception e) {
			Log.error("设置计划任务时间出错", e);
		}

		return schedule;
	}
	private String getTime(String startTime) {
		return startTime.substring(0,startTime.indexOf(":", 2)) + MMSS;
	}

	private void updateFirstEnCode(BuildInfo info) {
		((Task) info.getBuisness()).getTaskFreq().setReceiver_code(info.getDevice().getFirstEnCode());//修改接收机
	}

	public int getTransferTimeOut() {
		return 10;
	}

	private int getRecordLength(Task task) {
		Map<String, String> taskConfAttr = task.getTaskConfAttr();
		//不提取收测单元录音，在配置文件中提取
		return Integer.parseInt(taskConfAttr.get(BuildConstants.TASK_RECORDLENGTH_LISTEN)== null ? ConfigVariable.MEASURE_RECORD_TIME : taskConfAttr.get(BuildConstants.TASK_RECORDLENGTH_LISTEN));
	}

	//	private String[] getBeginAndEndTime(Task task) throws TimeException {
	//		TaskSchedule schedule = task.getTaskSchedule();
	//		int scheduleType = schedule.getSchedule_type();
	//		TimeRemindMsg msg = (TimeRemindMsg)task.getExpandObj();
	//		int interval = msg.getTimeinterval();
	//		String time = msg.getRemindtime();
	//		//时间格式：yyyy-MM-dd HH:mm:ss
	//		String start = null, end = null;
	//		switch(scheduleType) {
	//		case BuildConstants.SINGLE_TYPE:
	//			start = schedule.getStarttime();
	//			end = schedule.getEndtime();
	//			break;
	//		case BuildConstants.CYCLE_TYPE:
	//			//当前格式 00:00:00
	//			start = schedule.getStarttime();
	//			end = schedule.getEndtime();
	//			//判断是否跨天
	//			if (TimeUtils.isCrossDay(start, end)){
	//				start = TimeUtils.getYMdTime() + " " + start;
	//				end = TimeUtils.getDifferOneDay() + " " + end;
	//			} else {
	//				start = TimeUtils.getYMdTime() + " " + start;
	//				end = TimeUtils.getYMdTime() + " " + end;
	//			}
	//			break;
	//		}
	//		
	//		try {
	//			return TimeUtils.getBeginAndEndTime(start, end, time, interval);
	//		} catch (Exception e) {
	//			throw new TimeException("时间间隔切分失败， start=" + start + ", end=" + end + ", time=" + time + ", interval=" + interval);
	//		}
	//	}

}
