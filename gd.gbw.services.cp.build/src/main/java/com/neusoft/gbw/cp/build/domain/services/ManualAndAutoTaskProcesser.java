package com.neusoft.gbw.cp.build.domain.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.collect.AbstractTaskBuilder;
import com.neusoft.gbw.cp.build.domain.conver.ITaskConver;
import com.neusoft.gbw.cp.build.domain.conver.ManualAndAutoTaskConver;
import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildPrepareInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class ManualAndAutoTaskProcesser implements ITaskProcess{
	
	private ITaskConver taskConver = null;
	
	public ManualAndAutoTaskProcesser() {
		taskConver = new ManualAndAutoTaskConver();//add by jiahao 20150311
	}

	@Override
	public void taskProcess(Object obj) {
		BuildPrepareInfo bInfo = (BuildPrepareInfo) obj;
		try {
			switch(bInfo.getPlatBuildType()) {
			case first_start_build:    //已改
				setProcess(bInfo);
				break;
			case period_del_task_build:   //周期删除
				autoDelTaskProcess(bInfo);
				break;
			case period_build:    //周期采集
				autoProcess(bInfo);
				break;
			case burst_task_build: // 突发频率  已改
				burstTaskProcess(bInfo);
				break;
			case leakage_task_build: //补采   已改
				leakageTaskProcess(bInfo);
				break;
			case upload_stream:    //下载录音    后边代码没有实时
				streamProcess(bInfo);
				break;
			case auto_recover_data:  //自动回收     lyc  2017006  203
				recoverTaskData(bInfo);
				break;
			case auto_monitor_inspect:  //自动巡检 
				inspectMonitor(bInfo);
				break;
			default:
				break;
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
		} catch(ConverException e) {
			Log.error("[构建服务]解析自动手动任务异常", e);
		} catch (BuildException e) {
			Log.error("[构建服务]构建自动手动任务异常", e);
		}
	}
	
	private void streamProcess(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		if(null!=infoList && infoList.size()>0){
	 		for(BuildInfo info : infoList) {
				if (!taskBuildMap.containsKey(info.getType().getKey())) {
					Log.warn("");
					continue;
				}
				
				build = taskBuildMap.get(info.getType().getKey());
				colTask = build.buildTask(info);
				sleep(3000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
			}
			infoList.clear();
		}
		infoList = null;
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	private void recoverTaskData(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
 		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			sleep(1500); //任务下发过快，站点无法处理
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	} 
	
	/**
	 *平台首次启动  下发未下发的手动任务
	 * @param bInfo
	 * @throws ConverException
	 * @throws BuildException
	 */
	private void setProcess(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			//获取任务数据，系统自动执行的任务设置都进行局部删除
			String taskId = DataUtils.getTaskID(colTask);
			//将任务ID与回收表中的数据进行比较，如果包含该条任务，并且任务的状态是异常的，则生成该任务的删除和新增任务
			
			
			sleep(4000); //任务下发过快，站点无法处理
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}
	
	private void autoDelTaskProcess(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		List<CollectTask> allList = new ArrayList<CollectTask>();
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			allList.add(colTask);
//			ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, autoTaskList);
//			sleep(1000); //任务下发过快，站点无法处理
//			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		List<CollectTask> delList = allList;
		Map<Long,String> map = new HashMap<>();
		for(CollectTask al : allList){
			long id = al.getBusTask().getTask_id();
			if(null !=map.get(id)){
				continue;
			}else{
				map.put(id, String.valueOf(al.getBusTask().getTask_id()));
				List<CollectTask> collectList = new ArrayList<CollectTask>();
				for(CollectTask del : delList){
					if(del.getBusTask().getTask_id() == id){
						collectList.add(del);
					}
				}
				ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, collectList);
				for(CollectTask task : collectList) {
					sleep(1500);
					ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
				}
			}
		}
		/*if(!allList.isEmpty()) {
			Log.debug("发送主题通知+MANUAL_SET_TASK_PROCESS_TOPIC********"+allList.size());
			ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, allList);
			sleep(3000);
			for(CollectTask task : allList) {
				sleep(1000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
 		}*/
		infoList.clear();
		infoList = null;
	} 
	/**
	 * 收测   周期任务
	 * @param bInfo
	 * @throws ConverException
	 * @throws BuildException
	 */
	private void autoProcess(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Log.debug("[构建服务]本批次生成收测任务过滤后任务个数：  infoListSize=" + infoList.size());
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		CollectTask inspectTask = null;
		boolean b = false;
		List<CollectTask> controlAutoTaskList = new ArrayList<CollectTask>();
		List<CollectTask> controlAutoInspectTaskList = new ArrayList<CollectTask>();
		List<CollectTask> controlManualTaskList = new ArrayList<CollectTask>();
 		for(BuildInfo info : infoList) {
 			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("未找到构建build对应的   key");
				continue;
			}
 			if(info.getBuisness() instanceof RealTimeStreamDTO){
 				build = taskBuildMap.get("StreamRealtimeQuery_measure_realtime");
 				inspectTask = build.buildTask(info);
 				inspectTask.getBusTask().setTask_build_mode(2);;
 				b= info.isInspectSteam();
 				controlAutoInspectTaskList.add(inspectTask);
 			}else{
 				build = taskBuildMap.get(info.getType().getKey());
 				colTask = build.buildTaskMore(info);
 				if (colTask.getBusTask().getTask_build_mode() == 0) {
 					controlAutoTaskList.add(colTask);
 				} else if (colTask.getBusTask().getTask_build_mode() == 1) {
 					controlManualTaskList.add(colTask);
 				}
 			}
		}
 		int size = controlAutoTaskList.size() + controlManualTaskList.size()+controlManualTaskList.size();
 		Log.debug("[构建服务]本批次生成收测单元(检修校验后)总数：taskSize=" + size
 				+"自动任务 size="+controlAutoTaskList.size()+"  手动任务 size="+controlManualTaskList.size()+" 遥控站巡检 size"+
 				controlAutoInspectTaskList.size());
 		if(controlAutoInspectTaskList.size()>0){
 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_TASK_TOPIC, controlAutoInspectTaskList);
 		}
 		if(controlAutoTaskList.size()>0){
 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, controlAutoTaskList);
 		}
 		if(controlManualTaskList.size()>0){
 	 		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, controlManualTaskList);
 		}
		infoList.clear();
		infoList = null;
	}
	/**
	 * 补采  收测任务
	 * @param bInfo
	 * @throws ConverException
	 * @throws BuildException
	 */
	private void leakageTaskProcess(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Log.debug("[构建服务]本批次补采频率任务过滤后任务个数：infoListSize=" + infoList.size());
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		List<CollectTask> controlAutoTaskList = new ArrayList<CollectTask>();
		List<CollectTask> controlManualTaskList = new ArrayList<CollectTask>();
 		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("未找到构建build对应的key");
				continue;
			}
			
			build = taskBuildMap.get(info.getType().getKey());
 			colTask = build.buildTaskMore(info);
			if (colTask.getBusTask().getTask_build_mode() == 0) {
				controlAutoTaskList.add(colTask);
			} else if (colTask.getBusTask().getTask_build_mode() == 1) {
				controlManualTaskList.add(colTask);
			}
		}
 		int size = controlAutoTaskList.size() + controlManualTaskList.size();
 		Log.debug("[构建服务]本批次生成补采频率收测单元(检修校验后)总数：leakageTaskSize=" + size
 				+"自动任务 size="+controlAutoTaskList.size()+"  手动任务 size="+controlManualTaskList.size());
 		if(controlAutoTaskList.size()>0)
 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, controlAutoTaskList);
 		if(controlManualTaskList.size()>0)
 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, controlManualTaskList);
		infoList.clear();
		infoList = null;
	}
	/**
	 * 突发频率任务
	 * @param bInfo
	 * @throws ConverException
	 * @throws BuildException
	 */
	private void burstTaskProcess(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Log.debug("[构建服务]本批次突发频率任务过滤后任务个数：infoListSize=" + infoList.size());
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		List<CollectTask> controlAutoTaskList = new ArrayList<CollectTask>();
		List<CollectTask> controlManualTaskList = new ArrayList<CollectTask>();
 		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("未找到构建build对应的key");
				continue;
			}
			
			build = taskBuildMap.get(info.getType().getKey());
 			colTask = build.buildTaskMore(info);
			if (colTask.getBusTask().getTask_build_mode() == 0) {
				controlAutoTaskList.add(colTask);
			} else if (colTask.getBusTask().getTask_build_mode() == 1) {
				controlManualTaskList.add(colTask);
			}
//			Log.debug("[构建服务]出现突发频率，构建突发频率任务,taskId=" + colTask.getBusTask().getTask_id() + 
//					",freq=" + colTask.getBusTask().getFreq()); 
//			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
 		int size = controlAutoTaskList.size() + controlManualTaskList.size();
 		Log.debug("[构建服务]本批次生成突发频率收测单元(检修校验后)总数：burstTaskSize=" + size
 				+"自动任务 size="+controlAutoTaskList.size()+"  手动任务 size="+controlManualTaskList.size());
 		if(controlAutoTaskList.size()>0)
 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, controlAutoTaskList);
 		if(controlManualTaskList.size()>0)
 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, controlManualTaskList);
		infoList.clear();
		infoList = null;
	}
	
	private void inspectMonitor(BuildPrepareInfo bInfo) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(bInfo);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			//存放标志信息，因为在处理过程中需要
			colTask.getExpandMap().put(ExpandConstants.INSPECT_MONITOR_KEY, BuildConstants.MONITOR_SOFT_ONLINE_STATUS);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	} 
	
}
