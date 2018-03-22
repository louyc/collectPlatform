package com.neusoft.gbw.cp.process.measure.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.channel.ChannelPool;
import com.neusoft.gbw.cp.process.measure.channel.ChannelType;
import com.neusoft.gbw.cp.process.measure.channel.MeaUnitChannel;
import com.neusoft.gbw.cp.process.measure.channel.online.OnlineTaskChannel;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectTaskDispatcher {
	
//	private CollectTaskModel taskModel = null;
//	public CollectTaskDispatcher() {
//		taskModel = CollectTaskModel.getInstance();
//	}

	public void taskDispatch(CollectTask task) {
		BusinessTaskType type = task.getBusTask().getType();
		if (type.equals(BusinessTaskType.measure_manual_set)) {
//			taskModel.put(task);
		} else if (type.equals(BusinessTaskType.measure_manual_del)){
//			taskModel.remove(task);
		}
	}
	
	public void taskDispatch(List<CollectTask> taskList) {
		CollectTask task = taskList.get(0);
		BusinessTaskType type = task.getBusTask().getType();
		if (type.equals(BusinessTaskType.measure_auto)) {
			manualAndAutoDispatch(taskList);
		} else if (type.equals(BusinessTaskType.measure_manual_recover)) {
			//添加   本次接收到自动回收任务的数量为
			Log.debug("本次接收到自动回收任务的数量为=" + taskList.size());
//		    taskModel.addRecoverList(taskList);
		} else if (type.equals(BusinessTaskType.measure_online)) {
			manualOnlineDispatch(taskList);
		}
	}
	
	private void manualAndAutoDispatch(List<CollectTask> taskList) {
		List<CollectTask> manualTaskList = new ArrayList<CollectTask>();
		List<CollectTask> autoTaskList = new ArrayList<CollectTask>();
		Iterator<CollectTask> iter = taskList.iterator();
//		Set<String> status = new HashSet<String>();//记录收测单元初始化状态
		Set<Integer> status = new HashSet<Integer>();//记录收测单元实验和广播任务
		while(iter.hasNext()) {
			CollectTask element = iter.next();
			int mode = element.getBusTask().getTask_build_mode();
			
			if (mode == 1) {
				manualTaskList.add(element);
			} else if (mode == 0) {
				autoTaskList.add(element);
				int task_type = element.getBusTask().getTask_origin_id();
				status.add(task_type);
			}
			
//			//计算收测单元种类
//			int runplan_type = element.getBusTask().getTask_origin_id();
//			int unitTime = element.getBusTask().getMeasure_unit_time();
//			String beginTime = element.getBusTask().getUnit_begin();
//			String unitKey = mode + "_" + unitTime + "_" + beginTime + "_" + runplan_type;
//			status.add(unitKey);
		}
		MeaUnitChannel channel = null;
		
		if(!autoTaskList.isEmpty()) {
			Log.debug("本次接收到收测单元自动任务的数量为=" + taskList.size());
			channel = (MeaUnitChannel)ChannelPool.getInstance().getChannel(ChannelType.measure_unit_auto);
			channel.createMeasureUnit(taskList);
			sendUnitRemind(channel, status);
		}
		
		if(!manualTaskList.isEmpty()) {
			Log.debug("本次接收到收测单元手动任务的数量为=" + manualTaskList.size());
			channel = (MeaUnitChannel)ChannelPool.getInstance().getChannel(ChannelType.measure_unit_manual);
			channel.createMeasureUnit(manualTaskList);
//			cacheThisTimeUnit(manualTaskList.get(0),status);
		}
	}
	
	
	
	private void sendUnitRemind(MeaUnitChannel channel, Set<Integer> status) {
		for(int operType : status) 
			channel.sendWebMsg(0, operType);
	}
	
	private void manualOnlineDispatch(List<CollectTask> taskList) {
		OnlineTaskChannel channel = null;
		if(!taskList.isEmpty()) {
			Log.debug("本次接收到在线监听任务的数量为=" + taskList.size());
			channel = (OnlineTaskChannel)ChannelPool.getInstance().getChannel(ChannelType.measure_online_auto);
			channel.createOnlineUnit(taskList);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_TASK_TOPIC, taskList);
	}
}
