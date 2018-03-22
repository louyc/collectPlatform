package com.neusoft.gbw.cp.build.domain.services;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.load.data.build.domain.model.LoadContext;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskBuildMgr {
	/**
	 * 平台启动执行
	 */
	public void start() {
		//平台首次启动  补采   20170327  lyc 自动补采 作用不大   
		//例如20分钟轮训采集的  超过20分后 之前的也不能补采  只能补采当前收测单元的
//		startLeakCollectTaskBuild();
		TaskBuildHandler handler = new TaskBuildHandler(null, BuildConstants.TaskBuild.START_PLATFORM_BUILD);
		Thread thread = new Thread(handler);
		thread.setName("平台启动构建任务线程");
		thread.start();
//		handler.setServiceName("平台启动构建任务线程");
//		servicePool.addService(handler);
	}
	/**
	 * 启动效果类任务线程
	 * @param msg
	 * @param buildType
	 */
	public void startTimeTaskBuild(TimeRemindMsg msg, int buildType) {
		TaskBuildHandler handler = new TaskBuildHandler(msg, buildType);
		Thread thread = new Thread(handler);
		thread.setName("定时构建周期任务线程#" + msg.getRemindtime() + "_" + msg.getTimeinterval());
		thread.start();
//		handler.setServiceName("定时构建任务线程#" + msg.getRemindtime() + "_" + msg.getTimeinterval());
//		servicePool.addService(handler);
	}
	/**
	 * 启动删除设置任务
	 */
	public void startDelSetTaskBuild(TimeRemindMsg msg) {
		TaskBuildHandler handler = new TaskBuildHandler(msg, BuildConstants.TaskBuild.START_PERIOD_DEL_TASK_BUILD);
		Thread thread = new Thread(handler);
		thread.setName("定时构建周期删除质量任务线程#" + msg.getRemindtime() + "_" + msg.getTimeinterval());
		thread.start();
	}
	/**
	 * 初始化补采机制
	 */
	public void startLeakCollectTaskBuild() {
		//删除漏采的收测单元
		Map<String, String> unUnitMapAll = BusinessUtils.getUnfinishedUnit();
		Map<String, String> unUnitMap = new HashMap<String,String>();
		if(null!=unUnitMapAll && unUnitMapAll.size()>0){
			for(String sr: unUnitMapAll.keySet()){
				if(BusinessUtils.judePartformIsDone(String.valueOf(sr.split("_")[1]))){
					unUnitMap.put(sr, unUnitMapAll.get(sr));
				}
			}
		}
		Log.debug("[构建服务]补采采集失败的收测单元（过滤后）总个数，size=" + unUnitMap.size());
		BusinessUtils.delUnFinishedUnitProcess(unUnitMap);
		//lyc  START_PERIOD_BUILD  START_LEAKAGE_BUILD
		startAutoTaskBuild(BuildConstants.TaskBuild.START_PERIOD_BUILD);
	}
	
	/**
	 * 启动突发频率任务线程
	 */
	public void startBurstTaskBuild() {
		TimeRemindMsg remindMsg = null;
		Map<Integer, Integer> map = LoadContext.getInstance().getIntervalMap();
		for(Integer time : map.keySet()){
			//构建当前启动就要生成的收测单元的拆分
			remindMsg = buildCurrentMsg(time);
			startBurstTaskBuild(remindMsg);
		}
	}
	
	private void startBurstTaskBuild(TimeRemindMsg msg) {
		TaskBuildHandler handler = new TaskBuildHandler(msg, BuildConstants.TaskBuild.START_BURST_BUILD);
		Thread thread = new Thread(handler);
		thread.setName("突发构建任务线程#" + msg.getRemindtime() + "_" + msg.getTimeinterval());
		thread.start();
//		handler.setServiceName("定时构建任务线程#" + msg.getRemindtime() + "_" + msg.getTimeinterval());
//		servicePool.addService(handler);
	}
	
	private void startAutoTaskBuild(int buildType) {
		TimeRemindMsg remindMsg = null;
		Map<Integer, Integer> map = LoadContext.getInstance().getIntervalMap();
		for(Integer time : map.keySet()){
			
			//构建当前启动就要生成的收测单元的拆分
			remindMsg = buildCurrentMsg(time);
			startTimeTaskBuild(remindMsg, buildType);
		}
	}
	
	private TimeRemindMsg buildCurrentMsg(Integer time) {
		TimeRemindMsg msg = new TimeRemindMsg();
		msg.setTimeinterval(time);
		String startTime = TimeUtils.getCurrentStartTime(time);
		msg.setRemindtime(startTime);
		String endTime = null;
		try {
			endTime = TimeUtils.getAfterTime(startTime, time);
			msg.setEndTime(endTime);
		} catch (Exception e) {
		}
		
		return msg;
	}
}
