package com.neusoft.gbw.cp.build.domain.conver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.app.evaluation.intf.dto.KpiRealtimeQueryDTO;
import com.neusoft.gbw.app.evaluation.intf.dto.SpectrumRealtimeQueryDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskOnlineListener;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.OnlineListenerDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.OnlineMonitorDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskAppDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.Log;

public class JMSTaskConver extends BasicTaskConver implements ITaskConver {

	@Override
	public List<BuildInfo> conver(Object obj) throws ConverException {
		if (!(obj instanceof JMSDTO)) {
			return null;
		}
		JMSDTO dto = (JMSDTO) obj;
		int type = dto.getTypeId();
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		
		switch(type) {
		case GBWMsgConstant.C_JMS_MONITOR_STATUS_REQUEST_MSG:
			//站点  状态     已改
			infoList.add(converEquStatus(dto));
			break;
		case 99999999:
			//设备日志历史查询   20161013 lyc    已改
			infoList.add(converEquStatusHistoryQuery(dto));
		case GBWMsgConstant.C_JMS_MONITOR_LINK_REQUEST_MSG:
			//客户端连接  已改
			infoList.add(converStreamClient(dto));
			break;
		case GBWMsgConstant.C_JMS_REAL_KPI_REQUEST_MSG:
			// 实时指标查询请求  已改
			infoList.add(converRealtimeQuality(dto));
			break;
		case GBWMsgConstant.C_JMS_REAL_SPECTRUM_REQUEST_MSG:
			// 实时频谱查询请求   已改
			infoList.add(converSpectrumRealtimeQuality(dto));
			break;
		case GBWMsgConstant.C_JMS_REAL_STREAM_REQUEST_MSG:
			// 实时播音接收   已改
			infoList.add(converRealtimeStream(dto));
			break;
		case GBWMsgConstant.C_JMS_REAL_RECORD_REQUEST_MSG:
			// 实时录音接收  已改
			infoList.add(converRealtimeRecord(dto));
			break;
		case GBWMsgConstant.C_JMS_REAL_ONLINE_OVER_REQUEST_MSG:
			//在线监听自动评估 发送    已改
			infoList.addAll(converOnlineListener(dto));
			break;
		case GBWMsgConstant.C_JMS_REAL_TASK_RECOVER_REQUEST_MSG:
			// 手动任务回收请求  已改
			infoList.addAll(converRecoverTask(dto));
			break;
		case GBWMsgConstant.C_JMS_MONITOR_INSPECT_REQUEST_MSG:
			//站点检测   已改
			infoList.addAll(newConverInspectTask(dto));
			break;
		case GBWMsgConstant.C_JMS_RUNPLAN_STATION_MONITOR_MSG:
			//质量任务下发  已改
			infoList.addAll(converSetTask(dto));
			break;
		case 100:
			//手动巡检 采集点回收
			MoniInspectResultDTO statusDto = (MoniInspectResultDTO)dto.getObj();
			infoList.addAll(converRecoverTaskData(statusDto.getMonitorId()));
		default:
			break;
		}
		
		return infoList;
	}
	
	private BuildInfo converRealtimeStream(JMSDTO dto) throws ConverException {
		RealTimeStreamDTO realDto = (RealTimeStreamDTO)dto.getObj();
		if(!BusinessUtils.judePartformIsDone(realDto.getMonitorId())){
			return null;
		}
		Log.debug("[构建服务]接收JMS实时音频任务，RealTimeStreamDTO=" + realDto.toString());
		//根据不同的任务去提取相应的接收机 modify by jiahao
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_realtime, ProtocolType.StreamRealtimeQuery), realDto.getMonitorId(), realDto.getEquCode(), 0);
		BuildInfo info = buildInfo(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_realtime, ProtocolType.StreamRealtimeQuery, realDto.getMonitorId(),firstCode, realDto, TaskType.temporary);
		
		return info;
	}
	
	private BuildInfo converRealtimeRecord(JMSDTO dto) throws ConverException {
		RealTimeStreamDTO realDto = (RealTimeStreamDTO)dto.getObj();
		if(!BusinessUtils.judePartformIsDone(realDto.getMonitorId())){
			return null;
		}
		Log.debug("[构建服务]接收JMS实时录音任务，RealTimeStreamDTO=" + realDto.toString());
		//根据不同的任务去提取相应的接收机 modify by jiahao
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_real_record, ProtocolType.StreamRealtimeQuery), realDto.getMonitorId(), realDto.getEquCode(), 0);
		BuildInfo info = buildInfo(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_real_record, ProtocolType.StreamRealtimeQuery, realDto.getMonitorId(),firstCode, realDto, TaskType.temporary);
	
		return info;
	}
	//指标实时
	private BuildInfo converRealtimeQuality(JMSDTO dto) throws ConverException {
		KpiRealtimeQueryDTO kpiDto = (KpiRealtimeQueryDTO)dto.getObj();
		if(!BusinessUtils.judePartformIsDone(kpiDto.getMonitorID())){
			return null;
		}
		Log.debug("[构建服务]接收JMS实时指标查询任务，KpiRealtimeQueryDTO=" + kpiDto.toString());
		//根据不同的任务去提取相应的接收机 modify by jiahao
//		String firstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_real_record , ProtocolType.QualityRealtimeQuery), kpiDto.getMonitorID(), kpiDto.getEquCode(), 0);
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.QualityRealtimeQuery, BusinessTaskType.measure_realtime , ProtocolType.QualityRealtimeQuery), kpiDto.getMonitorID(), kpiDto.getEquCode(), 0);
		BuildInfo info = buildInfo(ProtocolType.QualityRealtimeQuery, BusinessTaskType.measure_realtime, ProtocolType.QualityRealtimeQuery, kpiDto.getMonitorID(), firstCode, kpiDto, TaskType.temporary);
		
		return info;
	}
	//频谱实时
	private BuildInfo converSpectrumRealtimeQuality(JMSDTO dto) throws ConverException {
		SpectrumRealtimeQueryDTO strDto = (SpectrumRealtimeQueryDTO)dto.getObj();
		if(!BusinessUtils.judePartformIsDone(strDto.getMonitorID())){
			return null;
		}
		Log.debug("[构建服务]接收JMS实时频谱查询任务，SpectrumRealtimeQueryDTO=" + strDto.toString());
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.SpectrumRealtimeQuery, BusinessTaskType.measure_realtime , ProtocolType.SpectrumRealtimeQuery), strDto.getMonitorID(), strDto.getEquCode(), 0);
		BuildInfo info = buildInfo(ProtocolType.SpectrumRealtimeQuery, BusinessTaskType.measure_realtime, ProtocolType.SpectrumRealtimeQuery, strDto.getMonitorID(), firstCode, strDto, TaskType.temporary);
		
		return info;
	}
	
	private BuildInfo converEquStatus(JMSDTO dto) throws ConverException {
		MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO)dto.getObj();
		if(!BusinessUtils.judePartformIsDone(statusDto.getMonitorId())){
			return null;
		}
		Log.debug("[构建服务]接收JMS设备状态查询任务，MoniDeviceStatusDTO=" + statusDto.toString());
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.EquStatusRealtimeQuery, BusinessTaskType.measure_realtime , ProtocolType.EquStatusRealtimeQuery), statusDto.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.EquStatusRealtimeQuery, BusinessTaskType.measure_realtime, ProtocolType.EquStatusRealtimeQuery, statusDto.getMonitorId(), firstCode, statusDto, TaskType.temporary);
		
		return info;
	}
	//20161013   设备日志查询    lyc
	private BuildInfo converEquStatusHistoryQuery(JMSDTO dto) throws ConverException {
//		MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO)dto.getObj();
		MoniDeviceStatusDTO statusDto = new MoniDeviceStatusDTO();
		statusDto.setMonitorId("40000000176");
		if(!BusinessUtils.judePartformIsDone(statusDto.getMonitorId())){
			return null;
		}
		Log.debug("[构建服务]接收JMS设备历史日志查询任务，MoniDeviceStatusDTO=" + statusDto.toString());
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.EquLogHistoryQuery, BusinessTaskType.measure_manual_recover , ProtocolType.EquLogHistoryQuery), statusDto.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.EquLogHistoryQuery, BusinessTaskType.measure_manual_recover, ProtocolType.EquLogHistoryQuery, statusDto.getMonitorId(), firstCode, statusDto, TaskType.temporary);
		return info;
	}
	
	private BuildInfo converStreamClient(JMSDTO dto) throws ConverException {
		MoniClientLinkDTO clientDto = (MoniClientLinkDTO)dto.getObj();
		if(!BusinessUtils.judePartformIsDone(clientDto.getMonitorId())){
			return null;
		}
		Log.debug("[构建服务]接收JMS客户端连接查询任务，MoniClientLinkDTO=" + clientDto.toString());
		String firstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeClientQuery, BusinessTaskType.measure_realtime , ProtocolType.StreamRealtimeClientQuery), clientDto.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.StreamRealtimeClientQuery, BusinessTaskType.measure_realtime, ProtocolType.StreamRealtimeClientQuery, clientDto.getMonitorId(), firstCode, clientDto, TaskType.temporary);
		
		return info;
	}
	
	private List<BuildInfo> converOnlineListener(JMSDTO dto) throws ConverException {
		OnlineListenerDTO onlDto = (OnlineListenerDTO)dto.getObj();
		List<OnlineMonitorDTO> monDTOList = onlDto.getMonitorList();
		if(!BusinessUtils.judePartformIsDone(monDTOList.get(0).getMonitorId())){
			return null;
		}
		Log.debug("[构建服务]接收JMS在线监听任务，OnlineListenerDTO=" + onlDto.toString());
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		TaskOnlineListener task = null;
		for(OnlineMonitorDTO moniDTO : monDTOList) {
			task = new TaskOnlineListener();
			task.setId(Integer.parseInt(moniDTO.getId()));
			task.setListenerId(Integer.parseInt(onlDto.getListenerTaskId()));
			task.setBand(Integer.parseInt(onlDto.getBand()));
			long monitorId = Long.parseLong(moniDTO.getMonitorId());
			task.setMonitorId(monitorId);
			MonitorDevice info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
			if (info == null) {
				Log.warn("[构建服务]在线监听未找到指定的站点信息 monitorId=" + monitorId);
			} else {
				task.setMonitorCode(info.getMonitor_code());
			}
			task.setEquCode(moniDTO.getReceiverCode());
			task.setFreq(onlDto.getFreq());
			task.setRunplanId(moniDTO.getRunplanId());
			task.setScordTypeId(Integer.parseInt(onlDto.getScoreTypeId()));
			task.setExpandObj(onlDto);
			//根据不同的任务去提取相应的接收机 modify by jiahao
			String firstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_online, ProtocolType.StreamRealtimeQuery), moniDTO.getMonitorId(),moniDTO.getReceiverCode(), 0);
			BuildInfo bInfo = buildInfo(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_online, ProtocolType.StreamRealtimeQuery, moniDTO.getMonitorId(),firstCode, task, TaskType.temporary);
			infoList.add(bInfo);
		}
		return infoList;
	}
	
	public List<BuildInfo> converRecoverTask(JMSDTO dto) throws ConverException {
		AbstractTaskConverHandler coverHandler = null;
		TaskDTO vo = (TaskDTO)dto.getObj();
		List<Task> taskList = null;
		
		coverHandler = new RecoverTaskConverHandler();
		taskList = coverHandler.converTaskList(vo);

		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		for(Task task : taskList) {
			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			if(!isStartSite(monitorID)) {
				Log.debug("[构建服务]该设备不存在，或处于维护状态，monitorId=" + monitorID);
				continue;
			}
			task.setExpandObj(vo);
			//获取任务时间戳KEY
//			String b_time = DataMgrCentreModel.getInstance().getRecoverTimeData().get(DataUtils.getTimeKey(task));
			task.getMeasureTask().setRecover_b_time(vo.getRecoverBTime());
			task.getMeasureTask().setRecover_e_time(vo.getRecoverETime());
			String firstEncode = task.getTaskFreq().getReceiver_code();//add by jiahao 
			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstEncode, task.getMeasureTask().getTask_build_mode());
			infoList.add(buildInfo(task.getBuildType(), monitorID ,newFirstCode, task, TaskType.temporary));
		}
		return infoList;
	}
	
	/**
	 * 质量任务  中波运行图任务自动下发  收集所有任务
	 * @param dto
	 * @return
	 * @throws ConverException
	 */
	public List<BuildInfo> converSetTask(JMSDTO dto) throws ConverException {
		TaskDTO delDto = null;
		TaskDTO setDto = null;
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		//提取taskAppDto中的任务对象
		TaskAppDTO  appDTO = (TaskAppDTO)dto.getObj();
		Map<String, List<TaskDTO>> map = appDTO.getMap();
		for(Map.Entry<String, List<TaskDTO>> entry : map.entrySet()) {
			delDto = entry.getValue().get(0);
			if(delDto != null) 
				delDto.setOperationTypeFlg("2");   //0 ：增加    1 ：修改    2：删除
			
			setDto = entry.getValue().get(1);
			if(setDto != null) 
				setDto.setOperationTypeFlg("0");
			
		}
		if(null!=delDto && null ==setDto){
			List<Task> taskList = new DelTaskConverHandler().converTaskList(delDto);
			for(Task task : taskList) {
				task.setExpandObj(appDTO);
				String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
				if(!isStartSite(monitorID)) {
					Log.warn("[任务构建]该任务采集站点已停用，monitorId=" + monitorID);
					continue;
				}
				String firstEncode = task.getTaskFreq().getReceiver_code();//add by jiahao 
				String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del, task.getBuildType().getOriProType()), monitorID, firstEncode, task.getMeasureTask().getTask_build_mode());
				infoList.add(buildInfo(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del, task.getBuildType().getOriProType(), monitorID, newFirstCode, task, TaskType.temporary));
			}
		}
		
		if(setDto == null)
			return infoList;
		
		List<Task> setTaskList = new SetTaskConverHandler().converTaskList(setDto);
		for(Task task : setTaskList) {
			task.setExpandObj(appDTO);
			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			if(!isStartSite(monitorID)) {
				Log.warn("[任务构建]该任务采集站点已停用，monitorId=" + monitorID);
				continue;
			}
			String firstEncode = task.getTaskFreq().getReceiver_code();//add by jiahao 
			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstEncode, task.getMeasureTask().getTask_build_mode());
			infoList.add(buildInfo(task.getBuildType(), monitorID , newFirstCode, task, TaskType.temporary));
			}
		return infoList;
	}
	
	private boolean isStartSite(String monitorID) throws ConverException {
		boolean startFlag = false;
		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Long.parseLong(monitorID));
		if (device != null)
			startFlag = true;
		return startFlag;
	}
	
	public List<BuildInfo> newConverInspectTask(JMSDTO dto) throws ConverException {
//		List<Task> taskList = null;
		MoniInspectResultDTO statusDto = (MoniInspectResultDTO)dto.getObj();
		statusDto.setTimeStamp(TimeUtils.getCurrentTime());//创建巡检时间
//		String monitorId = statusDto.getMonitorId();
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		//创建程序版本控制任务   2017.5.4     巡检去掉 版本查询
//		BuildInfo equStatus = converProgramStatus(statusDto);	
//		infoList.add(equStatus);
		//创建设备状态任务
		BuildInfo moniStatus =  converMoniStatus(statusDto);
		infoList.add(moniStatus);
		//创建任务状态任务
		//不检查任务状态，任务状态通过查询数据库中任务状态去实现，目前只保留大任务，不对小任务进行状态查询
//		taskList = DataMgrCentreModel.getInstance().getInpectTaskList(monitorId);
//		StringBuffer taskIds = new StringBuffer();
//		for(Task task : taskList) {
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.warn("[构建服务]该设备不存在或处于维护状态，monitorId=" + monitorID);
//				continue;
//			}
//			taskIds.append(",").append(DataUtils.getTaskID(task, 1));
//		} 
//		
//		if(taskIds.length() != 0) {
//			statusDto.setResultId(taskIds.toString().substring(1));
//			infoList.add(buildInfo(ProtocolType.TaskStatusQuery, BusinessTaskType.measure_inspect, ProtocolType.TaskStatusQuery, monitorId , "", statusDto, TaskType.temporary));
//		}
		return infoList;
	}
	
	
	
	private List<BuildInfo> converRecoverTaskData(String monitorID) throws ConverException {
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		List<Task> taskList = DataMgrCentreModel.getInstance().getDeviceTaskRecover();
		Map<String,Integer> map = new HashMap<String,Integer>();
		Log.debug("[手动巡检]巡检站点名称monitorID=" + monitorID);
		for(Task task : taskList) {
			if(null!=map.get(monitorID+"_"+task.getBuildType().getProType()) &&
					map.get(monitorID+"_"+task.getBuildType().getProType())==1){
				continue;
			}
			if(!String.valueOf(task.getTaskMonitor().getMonitor_id()).equals(monitorID)){
				continue;
			}
			if(!BusinessUtils.judePartformIsDone(monitorID)){
				continue;
			}
			try {
				String start_time ="";
				String end_time ="";
				String b_time = "";
				String e_time = "";
				String b_date = task.getMeasureTask().getTask_create_time().split(" ")[0];  //任务创建日期
				String now_date =TimeUtils.getCurrentTime().split(" ")[0];  //当前日期
				String nowTime = TimeUtils.getCurrentTime();  //当前时间
				//去掉任务中已经停用的
				int inUse = task.getMeasureTask().getIn_use();
				if(inUse != 1) {  
					continue;
				}
				//去掉不是下发成功的任务
				if(task.getMeasureTask().getTask_status_id()!=1){
					continue;
				}
				//去掉 当天 回收成功 的任务
//				if(task.getMeasureTask().getRecycle_status_id()==1 && 
//						task.getMeasureTask().getRecycle_time().split(" ")[0].compareTo(now_date)==0){
//					continue;
//				}

				int type = 0;  //时间周期类型  1：独立   2：周期
				int exporDate = 0;
				if(task.getScheduleList().size()>1){
					for(TaskSchedule ts:task.getScheduleList()){
						if(ts.getIs_overhaul() ==0){  //1 ：检修   0：有效
							type = ts.getSchedule_type();
							if(type==1){
								start_time =ts.getStarttime();
								end_time =ts.getEndtime();
							}else{
								start_time = TimeUtils.getYMdTime()+ " " + ts.getStarttime();
								end_time = TimeUtils.getYMdTime()+ " " + ts.getEndtime();
							}
							exporDate = ts.getExpiredays();
							break;
						}
					}
				}else{
					type = task.getScheduleList().get(0).getSchedule_type();
					if(type==1){
						start_time = task.getScheduleList().get(0).getStarttime();
						end_time = task.getScheduleList().get(0).getEndtime();
					}else{
						start_time = TimeUtils.getYMdTime()+ " " + task.getScheduleList().get(0).getStarttime();
						end_time = TimeUtils.getYMdTime()+ " " + task.getScheduleList().get(0).getEndtime();
					}
					exporDate = task.getScheduleList().get(0).getExpiredays();
				}

				if(type==1) {  //独立时间  直接回收对应时间段内数据  
					if(nowTime.compareTo(start_time)<=0){  //当前时间 <= 任务开始时间
						continue;
					}
					if(end_time.compareTo(nowTime)<=0){  //当前时间 >=任务结束时间
						continue;
					}
					b_time = TimeUtils.getBeforeTime(start_time, 0);
					e_time = TimeUtils.getAfterTime(start_time, ConfigVariable.RECOVER_DATA_TIME_POINT);
				}else{ //周期
					b_date = TimeUtils.getAfterDayTime(b_date, exporDate);
					if(now_date.compareTo(b_date)>0){    //当前时间 > 周期任务结束时间
						continue;
					}else{
						if(nowTime.compareTo(start_time)>=0 && nowTime.compareTo(end_time)<=0   ||  
								nowTime.compareTo(start_time)>=0 && nowTime.compareTo(end_time)>=0){   //判断是否在运行图的播放时间段内
							b_time = TimeUtils.getBeforeTime(nowTime, ConfigVariable.RECOVER_DATA_TIME + ConfigVariable.RECOVER_DATA_TIME_POINT);
							e_time = TimeUtils.getAfterTime(nowTime, ConfigVariable.RECOVER_DATA_TIME_POINT);
						}else{
							continue;
						}
					}
				}


				task.getMeasureTask().setRecover_b_time(b_time);
				task.getMeasureTask().setRecover_e_time(e_time);
			} catch (Exception e) {
				Log.error("", e);
				continue;
			}
			if(!isStartSite(monitorID)) {
				Log.warn("[构建服务]该设备不存在或处于维护状态，monitorId=" + monitorID);
				continue;
			}
			map.put(monitorID+"_"+task.getBuildType().getProType(), 1);
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
			infoList.add(buildInfo(task.getBuildType(), monitorID, newFirstCode, task, TaskType.system));
		}
		Log.debug("[构建服务]本批次生成过滤后定时回收 任务个数：taskSize=" + infoList.size());
		map.clear();
		return infoList;
	}
//	public List<BuildInfo> converInspectTask(JMSDTO dto) throws ConverException {
//		List<Task> taskList = null;
//		MoniInspectResultDTO statusDto = (MoniInspectResultDTO)dto.getObj();
//		statusDto.setTimeStamp(TimeUtils.getCurrentTime());//创建巡检时间
//		String monitorId = statusDto.getMonitorId();
//		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
//		//创建程序版本控制任务
//		BuildInfo equStatus = converProgramStatus(statusDto);	
//		infoList.add(equStatus);
//		//创建设备状态任务
//		BuildInfo moniStatus =  converMoniStatus(statusDto);
//		infoList.add(moniStatus);
//		//创建任务状态任务
//		taskList = DataMgrCentreModel.getInstance().getInpectTaskList(monitorId);
//		
//		StringBuffer taskIds = new StringBuffer();
//		for(Task task : taskList) {
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.warn("[构建服务]该设备不存在或处于维护状态，monitorId=" + monitorID);
//				continue;
//			}
//			taskIds.append(",").append(DataUtils.getTaskID(task, 1));
//		} 
//		
//		if(taskIds.length() != 0) {
//			statusDto.setResultId(taskIds.toString().substring(1));
//			infoList.add(buildInfo(ProtocolType.TaskStatusQuery, BusinessTaskType.measure_inspect, ProtocolType.TaskStatusQuery, monitorId , "", statusDto, TaskType.temporary));
//		}
//		return infoList;
//	}
	
	private BuildInfo converProgramStatus(MoniInspectResultDTO statusDto) throws ConverException {
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.ProgramCommand, BusinessTaskType.measure_inspect, ProtocolType.ProgramCommand), statusDto.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.ProgramCommand, BusinessTaskType.measure_inspect, ProtocolType.ProgramCommand,statusDto.getMonitorId(), newFirstCode, statusDto, TaskType.temporary);//没有添加接收机code，版本查询不需要指定接收机
		return info;
	}
	
	private BuildInfo converMoniStatus(MoniInspectResultDTO statusDto) throws ConverException {
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.EquStatusRealtimeQuery, BusinessTaskType.measure_inspect, ProtocolType.EquStatusRealtimeQuery), statusDto.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.EquStatusRealtimeQuery, BusinessTaskType.measure_inspect, ProtocolType.EquStatusRealtimeQuery,statusDto.getMonitorId(), newFirstCode, statusDto, TaskType.temporary);//没有添加接收机code，版本查询不需要指定接收机
		return info;
	}
}
