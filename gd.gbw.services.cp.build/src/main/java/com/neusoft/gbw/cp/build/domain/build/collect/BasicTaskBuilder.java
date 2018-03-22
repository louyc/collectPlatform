package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.app.evaluation.intf.dto.KpiRealtimeQueryDTO;
import com.neusoft.gbw.app.evaluation.intf.dto.SpectrumRealtimeQueryDTO;
import com.neusoft.gbw.cp.build.domain.build.CalTaskPriority;
import com.neusoft.gbw.cp.build.domain.build.IQueryBuild;
import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.ParameterValidation;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectAttrInfo;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.CollectTaskType;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.OccupEquType;
import com.neusoft.gbw.cp.core.collect.ScheduleInfo;
import com.neusoft.gbw.cp.core.collect.ScheduleType;
import com.neusoft.gbw.cp.core.collect.ServletTransferAttr;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.collect.TransferAttr;
import com.neusoft.gbw.cp.core.collect.TransferType;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.site.MonitorMachine;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskPriorityInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskType;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public abstract class BasicTaskBuilder extends AbstractTaskBuilder{
	
	/**
	 * 采集任务优先级判断
	 */
	@Override
	TaskPriority buildCollectTaskPriority(BuildInfo info) {
		String runplanId = null;
		TaskPriorityInfo taskInfo = new TaskPriorityInfo();
		Object obj = info.getBuisness();
		if (obj instanceof Task) {
			//表示可能是运行图或是设备端的数据采集
			Task task = (Task) obj;
			if (task.getMeasureTask().getTask_build_mode() == 0) {
				taskInfo.setRole("system");
				taskInfo.setType(TaskType.runplan);
			}else {
				taskInfo.setRole("other_role");
				taskInfo.setType(TaskType.temp);
			} 
			
			try{
				if(null!=task.getScheduleList()&& task.getScheduleList().size()>0){
					runplanId = task.getScheduleList().get(0).getRunplan_id();
				}
			}catch(Exception e) {
				Log.error(this.getClass().getName()+"获取任务优先级抛错", e);
			}
			
			taskInfo.setBasePriority(task.getMeasureTask().getTask_priority());
		}else {
			//此处的权限暂时没有，只区分系统或是其他角色，不是Task肯定是角色生成
			taskInfo.setRole("other_role");
			taskInfo.setType(TaskType.temp);
		}
		
		Map<String, String> map = DataMgrCentreModel.getInstance().getRunplanToStationMap();
		if (runplanId == null || !map.containsKey(runplanId)) {
//			Log.warn("没有查询到运行图ID对应的语言ID和发射台ID，RunPlanID=" + runplanId + ", BuildInfo=" + info.toString());
			taskInfo.setLanguage(0);
			taskInfo.setStation(0);
		} else {
			String[] array = map.get(runplanId).split("#");
			taskInfo.setLanguage(Long.parseLong(array[1]));
			taskInfo.setStation(Long.parseLong(array[0]));
		}
		
		
		ProtocolType proType = info.getType().getProType();
		switch(proType) {
		case QualityRealtimeQuery:
			taskInfo.setTaskProType("real_quality");
			break;
		case SpectrumRealtimeQuery:
			taskInfo.setTaskProType("real_spectrum");
			break;
		case StreamRealtimeQuery:
			taskInfo.setTaskProType("real_stream");
			break;
		default:
			taskInfo.setTaskProType("other_pro");
			break;
		}
		
		return CalTaskPriority.calculatePriority(taskInfo);
	}
	
	/**
	 * 效果类  多站点 
	 * @param info
	 * @return
	 */
	CollectAttrInfo buildCollectAttrInfoMore(BuildInfo info) {  //接收机配置  
		CollectAttrInfo cAttrInfo = new CollectAttrInfo();
//		cAttrInfo.setFirstEquCode(info.getDevice().getFirstEnCode());
		cAttrInfo.setFirstEquCode(info.getDeviceList().get(0).getFirstEnCode());
		List<String> ls =new ArrayList<>();
		for(MonitorDevice md: info.getDeviceList()){
			ls.addAll(getEquCodeList(md));
		}
		cAttrInfo.setEquCodeList(ls);
		cAttrInfo.setColTaskType(getCollectTaskType(info));
//		cAttrInfo.setOccupEquType(getOccupEquType(info));
		cAttrInfo.setTransferType(getTransferType(info));
		cAttrInfo.setTransferAttr(getTransferAttr(info));
		cAttrInfo.setTransferTimeOut(getTransferTimeOut(info));
		cAttrInfo.setEquTimeOut(getEquOccupyTimeOut(info));
		
		Object obj = info.getBuisness();
		if (obj instanceof Task) 
			cAttrInfo.setCheckOccpy(false);//增加是否监测接收机占用属性
		else if(obj instanceof RealTimeStreamDTO) 
			cAttrInfo.setCheckOccpy(true);
		return cAttrInfo;
	}
	
	@Override
	CollectAttrInfo buildCollectAttrInfo(BuildInfo info) { 
		CollectAttrInfo cAttrInfo = new CollectAttrInfo();
		cAttrInfo.setFirstEquCode(info.getDevice().getFirstEnCode());
		cAttrInfo.setEquCodeList(getEquCodeList(info.getDevice()));
		cAttrInfo.setColTaskType(getCollectTaskType(info));
//		cAttrInfo.setOccupEquType(getOccupEquType(info));
		cAttrInfo.setTransferType(getTransferType(info));
		cAttrInfo.setTransferAttr(getTransferAttr(info));
		cAttrInfo.setTransferTimeOut(getTransferTimeOut(info));
		cAttrInfo.setEquTimeOut(getEquOccupyTimeOut(info));
		
		Object obj = info.getBuisness();
		if (obj instanceof Task) 
			cAttrInfo.setCheckOccpy(false);//增加是否监测接收机占用属性
		else if(obj instanceof RealTimeStreamDTO) 
			cAttrInfo.setCheckOccpy(true);
		return cAttrInfo;
	}
	
	@Override
	ScheduleInfo buildScheduleInfo(BuildInfo info) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setType(ScheduleType.realtime);
		//modify by jiahao 其他调度被子类覆盖重写
//		Object obj = info.getBuisness();
//		if (!(obj instanceof Task)) {
//			schedule.setType(ScheduleType.realtime);
//			return schedule;
//		}else {
//			schedule.setType(ScheduleType.realtime);
//		}

//		Task task = (Task) info.getBuisness();
//		TaskAttribute taskAttr = task.getMeasureTask();
//		if(taskAttr.getIs_timelapse() == 0) {
//			schedule.setType(ScheduleType.plan);
//			schedule.setFormat(ScheduleFormatType.yMdHms);
//			schedule.setTime(taskAttr.getTime_lapse());
////			schedule.setOperType(buildOperationType(info));
//		}else {
//			schedule.setType(ScheduleType.realtime);
//		}
		return schedule;
	}
	
	@Override
	Map<String, Object> buildExpandObj(BuildInfo info) {
		Object obj = info.getBuisness();
		Map<String, Object> expandObj = new HashMap<String, Object>();
		expandObj.put(ExpandConstants.REPORT_CONTROL_KEY, info.getBuisness());
		expandObj.put(ExpandConstants.COLLECT_OCCUP_KEY, DataUtils.getMsgID(info));
		
		if(obj instanceof RealTimeStreamDTO) {
			RealTimeStreamDTO dto = (RealTimeStreamDTO)obj;
			String command = dto.getCommand();
			if(BuildConstants.STREAM_STOP.equalsIgnoreCase(command)) 
				expandObj.put(ExpandConstants.COLLECT_OCCUP_KEY, dto.getStopMarkId());
		}
		return expandObj;
	}
	
	@Override
	com.neusoft.gbw.cp.core.collect.TaskType buildTaskType(BuildInfo info) {
		return info.getTaskType();
	}
	@Override
	ProtocolData buildQuery(BuildInfo info, TaskPriority taskPriority){
		int version = info.getDevice().getVersion_id();
		IQueryBuild query = BuildContext.getInstance().getQueryBuildMap().get(version);
		Object result = null;
		/*
		 * 每种协议生成都有个msgID = (System.currentTimeMillis()+ obj.hashCode() + "").substring(4)
		 * 多个 站点一起下发任务可能有影响    obj：协议类型
		 * msgID 会放到一个map中  在站点上报数据后会删除这个id  如果2个任务是一个id 会导致一个任务找不到对应记录 不能上报给前台
		 */
		try {
			Thread.sleep(100);  
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch(info.getType().getProType()) {
		case QualityAlarmHistoryQuery:
			result = query.buildQualityAlarmHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case QualityAlarmParamSet:
			result = query.buildQualityAlarmParamSet(info.getBuisness(),taskPriority);
			break;
		case QualityHistoryQuery:
			result = query.buildQualityHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case QualityRealtimeQuery:
			result = query.buildQualityRealtimeQuery(info.getBuisness(),taskPriority);
			break;
		case QualityTaskSet:
			result = query.buildQualityTaskSet(info.getBuisness(),taskPriority);
			break;
		case SpectrumHistoryQuery:
			result = query.buildSpectrumHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case SpectrumRealtimeQuery:
			result = query.buildSpectrumRealtimeQuery(info.getBuisness(),taskPriority);
			break;
		case SpectrumTaskSet:
			result = query.buildSpectrumTaskSet(info.getBuisness(),taskPriority);
			break;
		case StreamHistoryQuery:
			result = query.buildStreamHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case StreamRealtimeQuery:
			result = query.buildStreamRealtimeQuery(info.getBuisness(),taskPriority);
			break;
		case StreamTaskSet:
			result = query.buildStreamTaskSet(info.getBuisness(),taskPriority);
			break;
		case OffsetHistoryQuery:
			result = query.buildOffsetHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case OffsetTaskSet:
			result = query.buildOffsetTaskSet(info.getBuisness(),taskPriority);
			break;
		case VideoRealtimeQuery:
			result = query.buildVideoRealtimeQuery(info.getBuisness(),taskPriority);
			break;
		case EquAlarmHistoryQuery:
			result = query.buildEquAlarmHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case EquAlarmParamSet:
			result = query.buildEquAlarmParamSet(info.getBuisness(),taskPriority);
			break;
		case EquStatusRealtimeQuery:
			result = query.buildEquStatusRealtimeQuery(info.getBuisness(),taskPriority);
			break;
		case EquInitParamSet:
			result = query.buildEquInitParamSet(info.getBuisness(),taskPriority);
			break;
		case EquLogHistoryQuery:
			result = query.buildEquLogHistoryQuery(info.getBuisness(),taskPriority);
			break;
		case ReceiverControl:
			Log.debug("接收机控制"+"ReceiverControl");
			result = query.buildReceiverControl(info.getBuisness(),taskPriority);
			break;
		case TaskDelete:
			result = query.buildTaskDelete(info.getBuisness(),taskPriority);
			break;
		case TaskStatusQuery:
			result = query.buildTaskStatusQuery(info.getBuisness(),taskPriority);
			break;
		case FileQuery:
			result = query.buildFileQuery(info.getBuisness(),taskPriority);
			break;
		case FileRetrieve:
			result = query.buildFileRetrieve(info.getBuisness(),taskPriority);
			break;
		case ProgramCommand:
			result = query.buildProgramCommand(info.getBuisness(),taskPriority);
			break;
		case StreamRealtimeClientQuery:
			result = query.buildStreamRealtimeClientQuery(info.getBuisness(),taskPriority);
			break;
		case StreamRealtimeClientStop:
			result = query.buildStreamRealtimeClientStop(info.getBuisness(),taskPriority);
			break;
		}
		
		return buildProtocolData(info, result);
	}
	
	private ProtocolData buildProtocolData(BuildInfo info, Object result) {
		ProtocolData data = new ProtocolData();
		data.setQuery(result);
		data.setProVersion(info.getDevice().getVersion_id());
		data.setType(info.getType().getProType());
		data.setOriType(info.getType().getOriProType());
		return data;
	}
	
	protected TransferAttr getTransferAttr(BuildInfo info) {
		TransferType type = getTransferType(info);
		if (type.equals(TransferType.SOCKET)) {
//			SocketTransferAttr transfer = new SocketTransferAttr();
//			transfer.setIpAddress(info.getDevice().getDevice_ip());
//			transfer.setPort(info.getDevice().getPort());
			ServletTransferAttr transfer = new ServletTransferAttr();
			transfer.setUrl(info.getDevice().getUrl());
			return transfer;
		} else if (type.equals(TransferType.SERVLET)) {
			ServletTransferAttr transfer = new ServletTransferAttr();
			transfer.setUrl(info.getDevice().getUrl());
			return transfer;
		}
		return null;
	}
	
	protected TransferType getTransferType(BuildInfo info) {
		if (info.getDevice().getManufacturer_id() == 2) {
			return TransferType.SOCKET;
		} else {
			return TransferType.SERVLET;
		}
	}
	
	/**
	 * 任务响应超时时间
	 * @param info
	 * @return
	 */
	public int getTransferTimeOut(BuildInfo info) {
//		int time = 15;
		int time = 15;
		//针对巡检类任务  延长超时时间 20170224 lyc
		BusinessTaskType type = info.getType().getBusTaskType();
		if (type.equals(BusinessTaskType.measure_inspect)) {
			time = 11;
		}else if(type.equals(BusinessTaskType.measure_realtime)){
			time =16;
		}
		return time;
	}
	
	/**
	 * 增加接收机占用超时，将接收机占用超时移至构建服务，便于播音和录音的超时时间设置
	 * @param info
	 * @return
	 */
	public int getEquOccupyTimeOut(BuildInfo info) {
		int time = BuildConstants.OCCUP_TIME_OUT;
		Object obj = info.getBuisness();
		if (obj instanceof Task) 
			return time;
		else if(obj instanceof RealTimeStreamDTO)  {
			RealTimeStreamDTO dto = (RealTimeStreamDTO)obj;
//			time = Integer.parseInt(dto.getTimeOut()) + 10;
			time = 10;
		}
			return time;
	}
	
	@Override
	boolean validateQuery(Object query,BuildInfo info,CollectTask task) throws BuildException {
		boolean flag = true;
		int version = info.getDevice().getVersion_id();
		ParameterValidation pv = (ParameterValidation) BuildContext.getInstance().getQueryVerifierMap().get(version);
		if(pv == null) 
			return flag;
		
		if (!pv.checkQuery((Query)query)) {
			flag = false;
			throw new BuildException("[数据校验]协议属性不完整  " + getLogMsg(task) +",属性异常详细信息 " + pv.getErrorMessage());
		}
		return flag;
	}
	
	protected CollectTaskType getCollectTaskType(BuildInfo info) {
		ProtocolType type = info.getType().getProType();
		Object obj = info.getBuisness();
		CollectTaskType taskType = CollectTaskType.general;
		switch(type) {
		case StreamRealtimeQuery:
			taskType = CollectTaskType.occup; 
			//如果是手动实时停止，则创建stop占用类型
			if(obj instanceof RealTimeStreamDTO) {
				RealTimeStreamDTO dto = (RealTimeStreamDTO)info.getBuisness();
				String command = dto.getCommand();
				if(BuildConstants.STREAM_STOP.equalsIgnoreCase(command)) 
					taskType = CollectTaskType.stop; 
			}
			
			break;
		case QualityRealtimeQuery:
			taskType = CollectTaskType.occup;
			
			//如果是手动实时停止，则创建stop占用类型
			if(obj instanceof KpiRealtimeQueryDTO) {
				KpiRealtimeQueryDTO dto = (KpiRealtimeQueryDTO)info.getBuisness();
				String command = dto.getAction();
				if(BuildConstants.STREAM_STOP.equalsIgnoreCase(command)) 
					taskType = CollectTaskType.stop; 
			}
			
			break;
		case SpectrumRealtimeQuery:
			taskType = CollectTaskType.occup;
			//如果是手动实时停止，则创建stop占用类型
			if(obj instanceof SpectrumRealtimeQueryDTO) {
				SpectrumRealtimeQueryDTO dto = (SpectrumRealtimeQueryDTO)info.getBuisness();
				String command = dto.getAction();
				if(BuildConstants.STREAM_STOP.equalsIgnoreCase(command)) 
					taskType = CollectTaskType.stop; 
			}
			break;
		default:
			break;
		}
		return taskType;
	}
	
	/**
	 * 优先级算法启用后，采集平台就不需要此逻辑
	 * @param info
	 * @return
	 */
	protected OccupEquType getOccupEquType(BuildInfo info) {
		ProtocolType type = info.getType().getProType();
		Object obj = info.getBuisness();
		OccupEquType occupType = OccupEquType.no_occup;
		switch(type) {
		case StreamRealtimeQuery:
			occupType = OccupEquType.start_occup; 
			//如果是手动实时停止，则创建stop占用类型
			if(obj instanceof RealTimeStreamDTO) {
				RealTimeStreamDTO dto = (RealTimeStreamDTO)info.getBuisness();
				String command = dto.getCommand();
				if(BuildConstants.STREAM_STOP.equalsIgnoreCase(command)) 
					occupType = OccupEquType.stop_occup; 
			}
			
			break;
		case QualityRealtimeQuery:
			occupType = OccupEquType.no_occup;    // add  yanghao
			break;
		case SpectrumRealtimeQuery:
			occupType = OccupEquType.start_occup;
			break;
		default:
			break;
		}
		return occupType;
	}
	
	protected List<String> getEquCodeList(MonitorDevice device) {
		List<String> resultList = new ArrayList<String>();
		List<MonitorMachine> list = device.getMachineList();
		for(MonitorMachine machine : list) {
			if (machine.getIs_default() == BuildConstants.RECEIVE_DEFAULT) {
				resultList.add(machine.getMachine_code());
			}
		}
		
		return resultList;
	}
	
	private String getLogMsg(CollectTask task) {
		return "taskID= " + task.getBusTask().getTask_id() + ",Freq=" + task.getBusTask().getFreq();
	}
}

