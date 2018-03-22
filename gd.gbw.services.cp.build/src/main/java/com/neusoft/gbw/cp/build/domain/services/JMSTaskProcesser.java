package com.neusoft.gbw.cp.build.domain.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.collect.AbstractTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.runplan.TaskStreamBuilder;
import com.neusoft.gbw.cp.build.domain.conver.ITaskConver;
import com.neusoft.gbw.cp.build.domain.conver.JMSTaskConver;
import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskAppDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskMonitorDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class JMSTaskProcesser implements ITaskProcess{
	
	private ITaskConver taskConver = null;
	
	public JMSTaskProcesser() {
		taskConver = new JMSTaskConver();
	}

	@Override
	public void taskProcess(Object obj) {
		JMSDTO dto = (JMSDTO) obj;
		try {
			Log.debug("前台发过来的消息类型 id是******************：：：："+dto.getTypeId());
			int type = dto.getTypeId();
			switch(type) {
			case GBWMsgConstant.C_JMS_REAL_RECORD_REQUEST_MSG:
				//录音连接   已改
				recordTaskProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_REAL_STREAM_REQUEST_MSG:
				//实时音频  已改
				streamTaskProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_REAL_ONLINE_OVER_REQUEST_MSG:
				//在线监听任务接收   已改   不知道是啥功能
				onLineListenerProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_REAL_SYNT_DB_REQUEST_MSG:
				//同步数据库消息   不改
				syncDBProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_REAL_TASK_RECOVER_REQUEST_MSG:
				//任务回收消息接收  已改
				recoverTaskProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_MONITOR_INSPECT_REQUEST_MSG:
				//站点检测功能   已改
				newInspectTaskProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_RUNPLAN_STATION_MONITOR_MSG:
				//质量任务下发   已改
				setTaskProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_DD_RUNPLAN_TASK_MSG:  //49
				//突发频率    已改
				setBurstTaskProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_TASK_SCAN_MSG:  //25
				//数据同步    不改
				syncDBProcess(dto);
			case GBWMsgConstant.C_JMS_MONITOR_GROUP_CHANGE_MSG:
				//站点分组信息同步   不改
				syncMonGroupProcess(dto);
				break;
			case GBWMsgConstant.C_JMS_DISPOSE_LEAKAGE_COLLECT_MSG:
				//人工补采   已改
				setUnitCollectProcess(dto);
				break;
			default:
				Log.debug("其它对应jms类型");
				jmsProcess(dto);
				break;
			}
		} catch(ConverException e) {
			Log.error("[构建服务]解析JMS接口任务异常", e);
		} catch (BuildException e) {
			Log.error("[构建服务]构建JMS接口任务异常", e);
		}
	}
	
	private void jmsProcess(JMSDTO dto) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(dto);
		if(null == infoList || infoList.size()<1 || infoList.get(0)==null){
			return;
		}
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
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}
	
	private void streamTaskProcess(JMSDTO dto) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(dto);
		if(null == infoList || infoList.size()<1 || infoList.get(0)==null){
			return;
		}
		RealTimeStreamDTO streamDTO =  (RealTimeStreamDTO)dto.getObj();
		String url = streamDTO.getUrl();
//		String command = streamDTO.getCommand();
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
			sleep(1500);   //延时   实时音频下发
			if(url != null && !url.equals("")){//非强占
				ARSFToolkit.sendEvent(EventServiceTopic.STREAM_RECORD_TASK_POCESS_TOPIC, colTask);
				continue;
			}
			else{
				colTask.getBusTask().setIs_force("1");
				ARSFToolkit.sendEvent(EventServiceTopic.STREAM_RECORD_TASK_POCESS_TOPIC, colTask);
				sleep(500);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
			}
		}
		infoList.clear();
		infoList = null;
	}
	
	private void setTaskProcess(JMSDTO dto) throws ConverException, BuildException {
		List<CollectTask> allList = new ArrayList<CollectTask>();
		List<CollectTask> delList = new ArrayList<CollectTask>();
		List<CollectTask> setList = new ArrayList<CollectTask>();
		//分采集平台   判断是否对站点进行任务处理  20170119
		TaskAppDTO  appDTO = (TaskAppDTO)dto.getObj();
		Map<String, List<TaskDTO>> map = appDTO.getMap();
		for(Map.Entry<String, List<TaskDTO>> entry : map.entrySet()) {
			TaskDTO taskdto = entry.getValue().get(0);
			List<TaskMonitorDTO> monitorDTOs = taskdto.getMonitorList();
			if(!BusinessUtils.judePartformIsDone(monitorDTOs.get(0).getMonitorId())){
				return;
			}
		}
		
		List<BuildInfo> infoList = taskConver.conver(dto);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
 		try{
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("[任务修改]未找到指定的任务构建类型 BuildType=" + info.getType().getKey());
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			CollectTask colTask = build.buildTask(info);
			//获取任务数据，查看任务数据是局部删除还是全部删除
			BusinessTaskType busType = info.getType().getBusTaskType();
			switch(busType) {
			case measure_manual_set:
				setList.add(colTask);
				break;
			case measure_manual_del:
				delList.add(colTask);
				break;
			default:
				break;
			}
 		}
 		allList.addAll(setList);
 		allList.addAll(delList);
		
 		if(!allList.isEmpty()) {
			ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_AUTO_SET_TASK_PROCESS_TOPIC, allList);
			for(CollectTask task : delList) {
				sleep(2000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
			for(CollectTask task : setList) {
				sleep(2000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
 		}
 		}catch(NullPointerException e){
 			Log.debug("NullPointerException   空指针       JMSTaskProcesser");
 		}finally{
 			infoList.clear();
 			infoList = null;
 		}
	}
	
	private void recoverTaskProcess(JMSDTO dto) throws ConverException, BuildException {
		//分采集平台   判断是否对站点进行任务处理  20170119
		TaskDTO vo = (TaskDTO)dto.getObj();
		List<TaskMonitorDTO> monitorDTOs = vo.getMonitorList();
		if(!BusinessUtils.judePartformIsDone(monitorDTOs.get(0).getMonitorId())){
			return;
		}
		
		List<BuildInfo> infoList = taskConver.conver(dto);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		List<CollectTask> colList = new ArrayList<CollectTask>();
		AbstractTaskBuilder build = null;
		try{
 		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			CollectTask colTask = build.buildTask(info);
			colTask.setTaskType(TaskType.temporary); //前台手动任务
			colList.add(colTask);
 		}
 		if(!colList.isEmpty()) {
	 		//将下发任务发送至实时处理服务
	 		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_RECOVER_TASK_PROCESS_TOPIC, colList);
	 		//发送至采集
	 		for(CollectTask task : colList) {
	 			//如果是1个任务，直接下，如果是多个等待
	 			if(colList.size() != 1)
	 				sleep(1000);
	 			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
	 		}
 		}
		}catch(NullPointerException e){
			Log.debug("NullPointerException   空指针  JMSTaskProcesser");
		}finally{
			infoList.clear();
			infoList = null;
		}
	}
	
	private void newInspectTaskProcess(JMSDTO dto) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(dto);
		dto.setTypeId(100);
		List<BuildInfo> recoverList = taskConver.conver(dto);
		MoniInspectResultDTO inspect = (MoniInspectResultDTO)dto.getObj();
		long monitorId =infoList.get(0).getDevice().getMonitor_id();
		MonitorDevice monitor =DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
		Map<String, Object> taskInfoMap = new HashMap<String,Object>();
		Map<String, String> map = new HashMap<String, String>();
		if(monitor.getType_id()==1){ //采集点校验任务
			if(null==recoverList || recoverList.size()<1){
				returnTaskExsit("1",monitorId,inspect);
				return;
			}
		}
		returnTaskExsit("0",monitorId,inspect);
		
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		map.put("taskStreamState", "失败");
		map.put("taskQualityState","失败");
		map.put("streamVadioState","");
		boolean tream =false;
		boolean quality =false;
		AbstractTaskBuilder build = null;
		
		//2.质量任务下发并获取结果
		taskInfoMap.put("inspectDTO", inspect);
		if(null==recoverList || recoverList.size()<1){
			map.put("taskStreamState", "");
			map.put("taskQualityState","");
		}else{
			for(BuildInfo info : recoverList) {
				if (!taskBuildMap.containsKey(info.getType().getKey())) {
					Log.warn("");
					continue;
				}
				if(info.getType().getProType().equals(ProtocolType.QualityHistoryQuery)){
					quality =true;
				}else if(info.getType().getProType().equals(ProtocolType.StreamHistoryQuery)){
					tream =true;
				}else{
					continue;
				}
				build = taskBuildMap.get(info.getType().getKey());
				CollectTask colTask = build.buildTask(info);
				sleep(1000); //任务下发过快，站点无法处理
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
			}
			boolean b =true;
			int i=0;
			Map<String, Object> recoverDate = new HashMap<String, Object>();
			while(b){
				for(i=0;i<20;i++){
					recoverDate =DataMgrCentreModel.getInstance().getRecoverDateList(monitorId + "");
					sleep(1000);
					if(quality && tream){
						if(null!=recoverDate.get("StreamTask") && null!=recoverDate.get("QualityTask")){
							map.put("taskStreamState", "成功");
							map.put("taskQualityState", "成功");
							map.put("streamVadioState", recoverDate.get("StreamTask").toString().split("&")[1]);
							break;
						}
					}else if(quality && !tream){
						if(null!=recoverDate.get("QualityTask")){
							map.put("taskQualityState", "成功");
							break;
						}
					}else if(!quality && tream){
						if(null!=recoverDate.get("QualityTask")){
							map.put("taskStreamState", "成功");
							map.put("streamVadioState", recoverDate.get("taskStreamState").toString().split("_")[1]);
							break;
						}
					}
				}
				if(i==19){
					if(quality && null!=recoverDate.get("QualityTask")){
						map.put("taskQualityState", "成功");
					}
					if(tream && null!=recoverDate.get("StreamTask")){
						map.put("taskStreamState", "成功");
						map.put("streamVadioState", recoverDate.get("taskStreamState").toString().split("_")[1]);
					}
				}
				b =false;
			}
		}
		//分采集平台   判断是否对站点进行任务处理  20170119
//		if(!BusinessUtils.judePartformIsDone(inspect.getMonitorId())){
//			Log.debug("不是本采集平台对应站点  任务不做");
//			return;
//		}
		//构建巡检任务基础信息 获取大任务信息,先注释掉小任务描述信息
//		List<Task> taskList = DataMgrCentreModel.getInstance().getInpectTaskList(infoList.get(0).getDevice().getMonitor_id() + "");
//		for(Task task : taskList) { 
//			String taskId = DataUtils.getTaskID(task, 1) + "";
//			String desc = BusinessUtils.getInspectTaskInfo(task);
//			map.put(taskId, desc);
//		}
		//遥控站 取录音文件
		/*MonitorDevice monitor =DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
		if(monitor.getType_id()==2 && (null==map.get("streamVadioState") ||map.get("streamVadioState").equals(""))){
			Map<String, Object> recoverResult =DataMgrCentreModel.getInstance().getRecoverDateList(monitorId + "");
			if(null!=recoverResult.get("vadioPath")){
				String url = recoverResult.get("vadioPath").toString();
				if(url.contains(new SimpleDateFormat("YYYY-MM-dd").format(new Date()))){
					map.put("streamVadioState", recoverResult.get("vadioPath").toString());
				}
			}
		}*/
		taskInfoMap.put("taskInfo", map);
		//向巡检服务发送本次巡检指令
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_INSPECT_TASK_PROCESS_TOPIC, taskInfoMap);
		
		
		//1.巡检任务下发
		try{
			for(BuildInfo info : infoList) {
				if (!taskBuildMap.containsKey(info.getType().getKey())) {
					Log.warn("");
					continue;
				}
				build = taskBuildMap.get(info.getType().getKey());
				CollectTask colTask = build.buildTask(info);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
			}
		}catch(NullPointerException e){
			Log.debug("NullPointerException  空指针       JMSTaskProcesser");
		}finally{
			infoList.clear();
			recoverList.clear();
			recoverList = null;
			infoList = null;
		}
	
    }
	
	private void returnTaskExsit(String state,long monitorId,MoniInspectResultDTO inspect){
		MoniInspectResultDTO moniDto = new MoniInspectResultDTO();
		moniDto.setInspecCode("taskExsit");
		moniDto.setInspecFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if(state.equals("0")){
			moniDto.setInspecMessage("当前时刻有任务");
			moniDto.setInspecScore("10");
		}else{
			moniDto.setInspecMessage("当前时刻无任务");
			moniDto.setInspecScore("0");
		}
		moniDto.setInspecResult(state);
		moniDto.setMonitorId(monitorId + "");
		moniDto.setTimeStamp(inspect.getTimeStamp() + "");
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(GBWMsgConstant.C_JMS_MONITOR_INSPECT_RESPONSE_MSG);
		jms.setObj(moniDto);
		ARSFToolkit.sendEvent("JMS_SEND_MSG_TOPIC", jms);
	}
//	private void inspectTaskProcess(JMSDTO dto) throws ConverException, BuildException {
//		List<BuildInfo> infoList = taskConver.conver(dto);
//		MoniInspectResultDTO inspect = (MoniInspectResultDTO)dto.getObj();
//		Map<String, Object> taskInfoMap = new HashMap<String,Object>();
//		Map<String, String> map = new HashMap<String, String>();
//		taskInfoMap.put("taskInfo", map);
//		taskInfoMap.put("inspectDTO", inspect);
//		//构建巡检任务基础信息
//		List<Task> taskList = DataMgrCentreModel.getInstance().getInpectTaskList(infoList.get(0).getDevice().getMonitor_id() + "");
//		for(Task task : taskList) {
//			String taskId = DataUtils.getTaskID(task, 1) + "";
//			String desc = DataUtils.getInspectTaskInfo(task);
//			map.put(taskId, desc);
//		}
//		//向巡检服务发送本次巡检指令
//		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_INSPECT_TASK_PROCESS_TOPIC, taskInfoMap);
//		sleep(2000);
//		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
//		AbstractTaskBuilder build = null;
//		for(BuildInfo info : infoList) {
//			if (!taskBuildMap.containsKey(info.getType().getKey())) {
//				Log.warn("");
//				continue;
//			}
//			build = taskBuildMap.get(info.getType().getKey());
//			CollectTask colTask = build.buildTask(info);
//			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
//		}
//		
//	}
	
	private void recordTaskProcess(JMSDTO dto) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(dto);
		if(null == infoList || infoList.size()<1 || infoList.get(0)==null){
			return;
		}
		RealTimeStreamDTO streamDTO =  (RealTimeStreamDTO)dto.getObj();
		String url = streamDTO.getUrl();
		String command = streamDTO.getCommand();
//		String force = streamDTO.getForce();
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		try{
 		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			
			build = taskBuildMap.get(info.getType().getKey());
			try{
				colTask = build.buildTask(info);
			}catch(BuildException e){
				Log.debug(this.getClass().getName()+" 采集任务创建报错", e);
			}
			if(isV8TS(colTask))//如果是泰顺，转换采集任务
				colTask = TaskStreamBuilder.buildTSV8RecordTask(colTask);
	
			if(url != null && !url.equals("")){//非强占
				ARSFToolkit.sendEvent(EventServiceTopic.STREAM_RECORD_TASK_POCESS_TOPIC, colTask);
				continue;
			}
			else{
				ARSFToolkit.sendEvent(EventServiceTopic.STREAM_RECORD_TASK_POCESS_TOPIC, colTask);
				sleep(500);
				if(!command.equals(BuildConstants.STREAM_STOP)) 
					ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
			}
		}
		}catch(NullPointerException e){
			Log.debug("NullPointerException 空指针    JMSTaskProcesser");
		}finally{
			infoList.clear();
			infoList = null;
		}
		//infoList.clear();
		//infoList = null;
	}
	
	private void onLineListenerProcess(JMSDTO dto) throws ConverException, BuildException {
		List<CollectTask> taskList = new ArrayList<CollectTask>();
		List<BuildInfo> infoList = taskConver.conver(dto);
		if(null == infoList || infoList.size()<1 || infoList.get(0)==null){
			return;
		}
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		try{
 		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			taskList.add(colTask);
		}
 		
 		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC, taskList);
		}catch(NullPointerException e){
			Log.debug("NullPointerException   空指针    JMSTaskProcesser ");
		}finally{
			infoList.clear();
			infoList = null;
		}
		//infoList.clear();
		//infoList = null;
	}
	
	private void syncDBProcess(JMSDTO dto) throws ConverException, BuildException {
		Log.debug("[构建服务]接收前台数据库同步消息，进行数据库同步" + dto.toString());
		initMonitorMachine();
		DataMgrCentreModel.getInstance().synt();
		DataMgrCentreModel.getInstance().syntDevice();
	}
	
	private void syncMonGroupProcess(JMSDTO dto) throws ConverException, BuildException {
		Log.debug("[构建服务]接收前台站点分组管理同步消息，进行数据库同步" + dto.toString());
		initMonitorMachine();
		DataMgrCentreModel.getInstance().syntDeviceGroup();
		DataMgrCentreModel.getInstance().syntDevice();
	}
	/**
	 * 更新巡检站点列表 20170316  
	 */
	private void initMonitorMachine(){
		Collection<MonitorDevice> monitorList = DataMgrCentreModel.getInstance().getMonitorDeviceList();
		Collection<MonitorDevice> newList = new ArrayList<MonitorDevice>();
		for(MonitorDevice device : monitorList) {
			if(BusinessUtils.judePartformIsDone(String.valueOf(device.getMonitor_id()))){
				newList.add(device);
			}
		}
		ARSFToolkit.sendEvent(EventServiceTopic.SYNT_MONITOR_DEVICE_TOPIC, newList);
	}
	private void setBurstTaskProcess(JMSDTO dto) throws ConverException, BuildException {
		Log.debug("[构建服务]接收前台突发频率消息，进行突发频率采集" + dto.toString());
		DataMgrCentreModel.getInstance().synt();
		DataMgrCentreModel.getInstance().syntDevice();
		BuildContext.getInstance().getTaskBuildMgr().startBurstTaskBuild();
	}
	
	private void setUnitCollectProcess(JMSDTO dto) throws ConverException, BuildException {
		Log.debug("[构建服务]接收前台补采漏采频率消息，进行补采频率采集" + dto.toString());
		//补采状态，1：重启补采，2：前台触发补采,0:不进行补采
		BuildContext.getInstance().getLeakageCollect().setStauts(2, 0);
		//刷新当前最新的收测单元，如果数据量太大，可以制作收测单元更新，此处可优化
		DataMgrCentreModel.getInstance().synt();
		BuildContext.getInstance().getTaskBuildMgr().startLeakCollectTaskBuild();
	}
	
	private boolean isV8TS(CollectTask colTask) {
		boolean flag = false;
		int proVersion = colTask.getData().getProVersion();
		int manufacturer_id = colTask.getBusTask().getManufacturer_id();
		if(8 == proVersion && 2 == manufacturer_id) {
			flag = true;
		}
		return flag;
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
}
