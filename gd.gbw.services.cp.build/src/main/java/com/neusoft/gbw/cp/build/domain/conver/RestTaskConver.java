package com.neusoft.gbw.cp.build.domain.conver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniControlDTO;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.RestType;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.DownLoadFileDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class RestTaskConver extends BasicTaskConver implements ITaskConver {

	@Override
	public List<BuildInfo> conver(Object obj) throws ConverException {
		if (!(obj instanceof String)) {
			return null;
		}
		
		Map<String, String> map = decode(obj.toString());
//		Map<String, String> map = (Map<String, String>) obj;
		String typeID = map.get(NetEventProtocol.SYNT_TYPE_ID);
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		RestType type = RestType.valueOf(typeID);
		switch(type) {
		case editTaskEvents:
			//修改任务直接调取删除和下发流程
//			infoList = converEditTask(map);
			break;
		case deleteTaskEvents:
//			infoList = converDeleteTask(map);
			break;
		case sendTaskEvents:
//			infoList = converSetTask(map);
			break;
//		case recoveryTaskEvents:
//			infoList = converRecoverTask(map);
//			break;
		case dropMonitorClientLinks:
			infoList.add(converMoniorClientLinks(map));
			break;
		case controlMonitor:   //版本查询   授时  监控器状态  重启
			infoList.add(converControlMonitor(map));
			break;
		case saveMonitorKpi:  //指标报警设置
			infoList.add(converQualitypmentAlarmSet(map));
			break;
		case saveMonitorDev:   //设备报警设置
			infoList.add(converEqupmentAlarmSet(map));
			break;
		case saveMonitorBase:  //设备初始化
			infoList.add(converEqupmentInitSet(map));
			break;
		case recoveryTaskRecordEvents:  //录音文件下载
			infoList.add(converDownLoad(map));
			break;
		}
		return infoList;
	}
	
	private BuildInfo converControlMonitor(Map<String, String> map) throws ConverException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniControlDTO vo = null;
		try {
//			Map<String,String> hashmap = NPJsonUtil.jsonValueToMap(request);
//			vo = (MoniControlDTO)NMBeanUtils.createObject(MoniControlDTO.class, hashmap);
			vo = (MoniControlDTO)ConverterUtil.xmlToObj(request);
			Log.debug("[构建服务]接收REST接收机控制任务，MoniControlDTO=" + vo.toString());
		} catch (Exception e) {
			throw new ConverException("[数据转换]版本查询接口转换对象出现异常，" + map);
		}
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.ProgramCommand, BusinessTaskType.measure_realtime, ProtocolType.ProgramCommand), vo.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.ProgramCommand, BusinessTaskType.measure_realtime, ProtocolType.ProgramCommand, vo.getMonitorId(),newFirstCode, map, TaskType.temporary);
		return info;
	}
	
	public BuildInfo converMoniorClientLinks(Map<String, String> map) throws ConverException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniClientLinkDTO vo = null;
		try {
			vo = (MoniClientLinkDTO)ConverterUtil.xmlToObj(request);
			Log.debug("[构建服务]接收REST中断客户端连接任务，MoniClientLinkDTO=" + vo.toString());
		} catch (Exception e) {
			throw new ConverException("[数据转换]中断客户端连接Rest接口转换对象出现异常，" + map);
		}
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeClientStop, BusinessTaskType.measure_realtime, ProtocolType.StreamRealtimeClientStop), vo.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.StreamRealtimeClientStop, BusinessTaskType.measure_realtime, ProtocolType.StreamRealtimeClientStop,vo.getMonitorId(), newFirstCode, map, TaskType.temporary);
		return info;
	}
	
//	public List<BuildInfo> converRecoverTask(Map<String, String> map) throws ConverException {
//		String request = map.get(NetEventProtocol.SYNT_REQUEST);
//		AbstractTaskConverHandler coverHandler = null;
//		TaskDTO vo = null;
//		List<Task> taskList = null;
//		try {
//			vo = (TaskDTO)ConverterUtil.xmlToObj(request);
//			Log.debug("[构建服务]接收REST手动回收任务，TaskDTO=" + vo.toString());
//		} catch (Exception e) {
//			throw new ConverException("[数据转换]中手动回收任务数据Rest接口转换对象出现异常，" + map);
//		}
//		
//		coverHandler = new RecoverTaskConverHandler();
//		taskList = coverHandler.converTaskList(vo);
//
//		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
//		for(Task task : taskList) {
//			task.setExpandObj(map);
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.debug("[构建服务]该设备不存在，或处于维护状态，monitorId=" + monitorID);
//				continue;
//			}
//			infoList.add(buildInfo(task.getBuildType(), monitorID ,"", task, TaskType.temporary));
//		}
//		return infoList;
//	}
	
//	public List<BuildInfo> converDeleteTask(Map<String, String> map) throws ConverException {
//		String request = map.get(NetEventProtocol.SYNT_REQUEST);
//		AbstractTaskConverHandler coverHandler = null;
//		List<Task> taskList = null;
//		TaskDTO vo = null;
//		try {
//			vo = (TaskDTO)ConverterUtil.xmlToObj(request);
//			Log.debug("[构建服务]接收REST手动删除任务，TaskDTO=" + vo.toString());
//		} catch (Exception e) {
//			throw new ConverException("[数据转换]中手动删除任务数据Rest接口转换对象出现异常，" + map);
//		}
//		
//		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
//		
//		
//		taskList = DataMgrCentreModel.getInstance().getTaskList(vo.getTaskId());
//		if(taskList.isEmpty()) {
//			coverHandler = new DelTaskConverHandler();
//			taskList = coverHandler.converTaskList(vo);
//		}
//		
//		for(Task task : taskList) {
//			task.setExpandObj(map);
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.warn("[任务构建]该任务采集站点已停用，monitorId=" + monitorID);
//				continue;
//			}
//			infoList.add(buildInfo(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del, task.getBuildType().getOriProType(), monitorID, "", task, TaskType.temporary));
//		}
//		return infoList;
//	}
	
//	public List<BuildInfo> converSetTask(Map<String, String> map) throws ConverException {
//		String request = map.get(NetEventProtocol.SYNT_REQUEST);
//		AbstractTaskConverHandler coverHandler = null;
//		List<Task> taskList = null;
//		TaskDTO vo = null;
//		try {
//			vo = (TaskDTO)ConverterUtil.xmlToObj(request);
//			Log.debug("[构建服务]接收REST手动设置任务，TaskDTO=" + vo.toString());
//		} catch (Exception e) {
//			throw new ConverException("[数据转换]中手动下发任务数据Rest接口转换对象出现异常，" + map);
//		}
//		
//		taskList = DataMgrCentreModel.getInstance().getTaskList(vo.getTaskId());
//		if(taskList.isEmpty()) {
//			coverHandler = new SetTaskConverHandler();
//			taskList = coverHandler.converTaskList(vo);
//		}
//		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
//		for(Task task : taskList) {
//			task.setExpandObj(map);
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.warn("[任务构建]该任务采集站点已停用，monitorId=" + monitorID);
//				continue;
//			}
//			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
//			//根据不同的任务去提取相应的接收机 modify by jiahao
//			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
//			infoList.add(buildInfo(task.getBuildType(), monitorID , newFirstCode , task, TaskType.temporary));
//		}
//		return infoList;
//	}
	
//	public List<BuildInfo> converEditTask(Map<String, String> map) throws ConverException {
//		String request = map.get(NetEventProtocol.SYNT_REQUEST);
//		AbstractTaskConverHandler coverHandler = null;
//		List<Task> taskList = new ArrayList<Task>();
//		TaskDTO vo = null;
//		try {
//			vo = (TaskDTO)ConverterUtil.xmlToObj(request);
//			Log.debug("[构建服务]接收REST手动修改任务，TaskDTO=" + vo.toString());
//		} catch (Exception e) {
//			throw new ConverException("[数据转换]中手动修改任务数据Rest接口转换对象出现异常，" + map);
//		}
//		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
//		
//		//删除task 用原有的task
//		taskList = DataMgrCentreModel.getInstance().getTaskList(vo.getTaskId());
//		if(taskList.isEmpty()) {
//			coverHandler = new DelTaskConverHandler();
//			taskList.addAll(coverHandler.converTaskList(vo));
//		}
//		for(Task task : taskList) {
//			task.setExpandObj(map);
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.warn("[任务构建]该任务采集站点已停用，monitorId=" + monitorID);
//				continue;
//			}
//			infoList.add(buildInfo(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del, task.getBuildType().getOriProType(),monitorID, "", task, TaskType.temporary));
//		}
//		taskList.clear();
//		//添加的task 采用dto中的任务
//		coverHandler = new SetTaskConverHandler();
//		taskList.addAll(coverHandler.converTaskList(vo));
//		
//		
//		for(Task task : taskList) {
//			task.setExpandObj(map);
//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
//			if(!isStartSite(monitorID)) {
//				Log.warn("[任务构建]该任务采集站点已停用，monitorId=" + monitorID);
//				continue;
//			}
//			infoList.add(buildInfo(task.getBuildType(), monitorID , "", task, TaskType.temporary));
//		}
//		return infoList;
//	}
	
//	private boolean isStartSite(String monitorID) throws ConverException {
//		boolean startFlag = false;
//		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Integer.parseInt(monitorID));
//		if (device != null)
//			startFlag = true;
//		return startFlag;
//	}
	
	
	private BuildInfo converQualitypmentAlarmSet(Map<String, String> map) throws ConverException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniSetingDTO vo = null;
		try {
			vo = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
			Log.debug("[构建服务]接收REST指标报警设置接口任务，MoniSetingDTO=" + vo.toString());
		} catch (Exception e) {
			throw new ConverException("[数据转换]指标报警设置接口转换对象出现异常，" + map);
		}
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.QualityAlarmParamSet, BusinessTaskType.measure_realtime, ProtocolType.QualityAlarmParamSet), vo.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.QualityAlarmParamSet, BusinessTaskType.measure_realtime, ProtocolType.QualityAlarmParamSet, vo.getMonitorId(), newFirstCode, map, TaskType.temporary);
		return info;
	}
	
	private BuildInfo converEqupmentAlarmSet(Map<String, String> map) throws ConverException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniSetingDTO svo = null;
		try {
			svo = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
			Log.debug("[构建服务]接收REST设备报警设置接口任务，MoniSetingDTO=" + svo.toString());
		} catch (Exception e) {
			throw new ConverException("[数据转换]设备报警设置接口转换对象出现异常，" + map);
		}
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.EquAlarmParamSet, BusinessTaskType.measure_realtime, ProtocolType.EquAlarmParamSet), svo.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.EquAlarmParamSet, BusinessTaskType.measure_realtime, ProtocolType.EquAlarmParamSet,svo.getMonitorId(), newFirstCode, map, TaskType.temporary);
		return info;
	}
	
	private BuildInfo converEqupmentInitSet(Map<String, String> map) throws ConverException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniSetingDTO svo = null;
		try {
			svo = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
			Log.debug("[构建服务]接收REST设备初始化设置接口任务，MoniSetingDTO=" + svo.toString());
		} catch (Exception e) {
			throw new ConverException("[数据转换]设备初始化设置接口转换对象出现异常，" + map);
		}
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.EquInitParamSet, BusinessTaskType.measure_realtime, ProtocolType.EquInitParamSet), svo.getMonitorId(), null, 0);
		BuildInfo info = buildInfo(ProtocolType.EquInitParamSet, BusinessTaskType.measure_realtime, ProtocolType.EquInitParamSet,svo.getMonitorId(), newFirstCode, map, TaskType.temporary);
		return info;
	}
	
	
	private BuildInfo converDownLoad(Map<String, String> map) throws ConverException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		DownLoadFileDTO dto = null;
		try {
			Object obj = ConverterUtil.xmlToObj(request);
			dto = (DownLoadFileDTO)obj;
				
			Log.debug("[构建服务]接收REST下载录音文件接口任务，DownLoadFileDTO=" + dto.toString());
		} catch (Exception e) {
			throw new ConverException("[数据转换]下载录音文件接口转换对象出现异常，" + map);
		}
		String monitorId = dto.getTaskRecordList().get(0).getMonitorId(); //monitorId随便取一个就行，这个ID主要是控制底下用哪个接收机，回收文件不控制接收机
		Log.debug("***********lyc*********"+monitorId);
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.FileRetrieve, BusinessTaskType.measure_manual_recover, ProtocolType.FileRetrieve), monitorId, null, 0);
		BuildInfo info = buildInfo(ProtocolType.FileRetrieve, BusinessTaskType.measure_manual_recover, ProtocolType.FileRetrieve, monitorId, newFirstCode, map, TaskType.temporary);;
		if(null!=DataMgrCentreModel.getInstance().getMonitorDevice(Long.valueOf(monitorId)).getFtp_path()){
			info.setUseFtp(true);
		}
		return info;

	}
	
	public static Map<String, String> decode(String task){
		Map<String, String> attrs = null;
		try {
			attrs = NPJsonUtil.jsonToMap(task);
		} catch (NMFormateException e) {
		}
		return attrs;
	}
	
}
