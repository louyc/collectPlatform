package com.neusoft.gbw.cp.build.domain.build.collect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.collect.CollectAttrInfo;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ScheduleInfo;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public abstract class AbstractTaskBuilder {

	public CollectTask buildTask(BuildInfo info) throws BuildException {
		CollectTask task = new CollectTask();
		//获取任务计算优先级
		TaskPriority taskPriority = buildCollectTaskPriority(info);
		
		task.setTaskPriority(taskPriority);
		task.setSchedule(buildScheduleInfo(info));
		
		CollectAttrInfo cAttrInfo = buildCollectAttrInfo(info);
		if (cAttrInfo == null) {
			throw new BuildException("[数据校验]任务采集通信息构建失败, buildType=" + info.getType().toString() + ",monitorId=" + info.getDevice().getMonitor_id());
		}
		task.setAttrInfo(cAttrInfo);
		task.setBusTask(buildBusinessTask(info));
		
		
		ProtocolData data = buildQuery(info, taskPriority);
		if (data.getQuery() == null) {
			throw new BuildException("[数据校验]任务通信协议构建失败,protocolType=" + data.getType() +",version=" + data.getProVersion()+ ",monitorId=" + info.getDevice().getMonitor_id());
		}else {
			if(validateQuery(data.getQuery(),info,task)) 
				task.setData(data);
		}
		
		task.setExpandMap(buildExpandObj(info));
		task.setCollectTaskID(DataUtils.getCollectTaskID(task));
		task.setTaskType(buildTaskType(info));
		return task;
	}
	
	/**
	 * 针对效果类 多站点任务
	 * @param info
	 * @return
	 * @throws BuildException
	 */
	public CollectTask buildTaskMore(BuildInfo info) throws BuildException {
		CollectTask task = new CollectTask();
		//获取任务计算优先级
		TaskPriority taskPriority = buildCollectTaskPriority(info);
		
		task.setSchedule(buildScheduleInfo(info));
		
		CollectAttrInfo cAttrInfo = buildCollectAttrInfoMore(info);
		if (cAttrInfo == null) {
			throw new BuildException("[数据校验]任务采集通信息构建失败, buildType=" + info.getType().toString() + ",monitorId=" + info.getDevice().getMonitor_id());
		}
		task.setAttrInfo(cAttrInfo);
		task.setBusTask(buildBusinessTask(info));
		//20161103   针对播出结束时间（12:00--14:10） 比当前时间间隔（14:00--14:20）差20分内的做优先级重算：间隔越小 优先级越高 lyc
		String unit_begin = task.getBusTask().getUnit_begin();
		String endTime = ((Task)info.getBuisness()).getScheduleList().get(0).getEndtime();
		long timeDiff=0;
		long startT =0;
		long endT =0;
		try {
			if(((Task)info.getBuisness()).getMeasureTask().getTask_type_id()==3){
				startT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(unit_begin).getTime();
				endT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime).getTime();
			}else{
				startT = new SimpleDateFormat("hh:mm:ss").parse(unit_begin.substring(11, unit_begin.length())).getTime();
				endT = new SimpleDateFormat("hh:mm:ss").parse(endTime).getTime();
			}
			timeDiff = (endT-startT)/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(timeDiff<=1200 && timeDiff>0){
			long measureP = taskPriority.getMeasurePriority();
			taskPriority.setMeasurePriority(measureP+1200-timeDiff);
		}
		
		task.setTaskPriority(taskPriority);
		ProtocolData data = buildQuery(info, taskPriority);
		if (data.getQuery() == null) {
			throw new BuildException("[数据校验]任务通信协议构建失败,protocolType=" + data.getType() +",version=" + data.getProVersion()+ ",monitorId=" + info.getDevice().getMonitor_id());
		}else {
			if(validateQuery(data.getQuery(),info,task)) 
				task.setData(data);
		}
		
		task.setExpandMap(buildExpandObj(info));
		task.setCollectTaskID(DataUtils.getCollectTaskID(task));
		task.setTaskType(buildTaskType(info));
		return task;
	}
	/**
	 * 构建优先级
	 * @param info
	 * @return
	 */
	abstract TaskPriority buildCollectTaskPriority(BuildInfo info);
	/**
	 * 构建任务类型
	 * @param info
	 * @return
	 */
	abstract TaskType buildTaskType(BuildInfo info);
	
	abstract CollectAttrInfo buildCollectAttrInfoMore(BuildInfo info);
	/**
	 * 构建采集配置类型
	 * @param info
	 * @return
	 */
	abstract CollectAttrInfo buildCollectAttrInfo(BuildInfo info);
	/**
	 * 任务分发调度信息
	 * @param info
	 * @return
	 */
	abstract ScheduleInfo buildScheduleInfo(BuildInfo info);
	/**
	 * 构建协议对象
	 * @param info
	 * @param taskPriority
	 * @return
	 */
	abstract ProtocolData buildQuery(BuildInfo info, TaskPriority taskPriority);
	/**
	 * 校验协议
	 * @param query
	 * @param info
	 * @param task
	 * @return
	 * @throws BuildException
	 */
	abstract boolean validateQuery(Object query,BuildInfo info,CollectTask task) throws BuildException;
	/**
	 * 构建业务类型
	 * @param info
	 * @return
	 */
	abstract BusinessTask buildBusinessTask(BuildInfo info);
	/**
	 * 构建扩展属性
	 * @param info
	 * @return
	 */
	abstract Map<String, Object> buildExpandObj(BuildInfo info);
}
