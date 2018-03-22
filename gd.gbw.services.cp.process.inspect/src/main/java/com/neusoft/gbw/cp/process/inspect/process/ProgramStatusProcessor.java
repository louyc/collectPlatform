package com.neusoft.gbw.cp.process.inspect.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.exception.InspectDisposeException;
import com.neusoft.gbw.cp.process.inspect.util.InspectUtils;
import com.neusoft.gbw.cp.process.inspect.vo.InspectMonStat;
import com.neusoft.gbw.cp.process.inspect.vo.InspectResultStore;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MonitorDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class ProgramStatusProcessor extends SendTaskProcessor implements ITaskProcess{
	
	private InspectTaskContext context = InspectTaskContext.getInstance(); 
	public static void main(String[] args) {
		String type = ReportStatus.date_collect_time_out.name();
		switch (type) {
		case "date_collect_time_out":
			System.out.println("代码进入成功");
			break;

		default:
			break;
		}
	}
	
	@SuppressWarnings("unused")
	@Override
	public void disposeV8(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
//		String type = status.name();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV8Data(data);
			Log.info("[巡检服务]版本控制任务处理成功，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		default:
			Log.warn("[巡检服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]版本控制任务处理消息发送至前台，msg=" + inspectList.toString());
		//判断校验巡检是日常巡检还是软件状态监测巡检
		Object obj = data.getCollectTask().getExpandObject(ExpandConstants.INSPECT_MONITOR_KEY);
		String inspectKey = obj == null ? "":obj.toString();
		if(InspectConstants.MONITOR_SOFT_ONLINE_STATUS.equals(inspectKey)) {
			//软件状态监测巡检
			disposeOnlineStatus(data);
		}else {
			//日常巡检
			disposeData(inspectList);
//			storeInfo(inspectList);
//			updateInspectStatus(inspectList);
//			sendTask(inspectList);
		}
		
		//内存回收
		inspectList = null;
		CollectTask task = data.getCollectTask();
		task = null;
		data = null;
		
	}

	@SuppressWarnings("unused")
	@Override
	public void disposeV7(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
//		String type = status.name();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV7Data(data);
			Log.info("[巡检服务]版本控制任务处理成功，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		default:
			Log.warn("[巡检服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]版本控制任务处理消息发送至前台，msg=" + inspectList.toString());
		//判断校验巡检是日常巡检还是软件状态监测巡检
		Object obj = data.getCollectTask().getExpandObject(ExpandConstants.INSPECT_MONITOR_KEY);
		String inspectKey = obj == null ? "":obj.toString();
		if(InspectConstants.MONITOR_SOFT_ONLINE_STATUS.equals(inspectKey)) {
			disposeOnlineStatus(data);
		}else {
			disposeData(inspectList);
//			storeInfo(inspectList);
//			updateInspectStatus(inspectList);
//			sendTask(inspectList);
		}
		
		//内存回收
		inspectList = null;
		CollectTask task = data.getCollectTask();
		task = null;
		data = null;
	}

	@SuppressWarnings("unused")
	@Override
	public void disposeV6(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
//		String type = status.name();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV6Data(data);
			Log.info("[巡检服务]版本控制任务处理成功，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		default:
			Log.warn("[巡检服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]版本控制任务处理消息发送至前台，msg=" + inspectList.toString());
		
		//判断校验巡检是日常巡检还是软件状态监测巡检
		Object obj = data.getCollectTask().getExpandObject(ExpandConstants.INSPECT_MONITOR_KEY);
		String inspectKey = obj == null ? "":obj.toString();
		if(InspectConstants.MONITOR_SOFT_ONLINE_STATUS.equals(inspectKey)) {
			disposeOnlineStatus(data);
		}else {
			disposeData(inspectList);
//			storeInfo(inspectList);
//			updateInspectStatus(inspectList);
//			sendTask(inspectList);
		}
		
		//内存回收
		inspectList = null;
		CollectTask task = data.getCollectTask();
		task = null;
		data = null;
	}

	@SuppressWarnings("unused")
	@Override
	public void disposeV5(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
//		String type = status.name();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV5Data(data);
			Log.info("[巡检服务]版本控制任务处理成功，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		default:
			Log.warn("[巡检服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]版本控制任务处理消息发送至前台，msg=" + inspectList.toString());
		//判断校验巡检是日常巡检还是软件状态监测巡检
		Object obj = data.getCollectTask().getExpandObject(ExpandConstants.INSPECT_MONITOR_KEY);
		String inspectKey = obj == null ? "":obj.toString();
		if(InspectConstants.MONITOR_SOFT_ONLINE_STATUS.equals(inspectKey)) {
			disposeOnlineStatus(data);
		}else {
			disposeData(inspectList);
//			storeInfo(inspectList);
//			updateInspectStatus(inspectList);
//			sendTask(inspectList);
		}
		
		//内存回收
		inspectList = null;
		CollectTask task = data.getCollectTask();
		task = null;
		data = null;
		
	}
	
	private List<InspectResultStore> disposeError(CollectData data) {
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		List<InspectResultStore> inspectList = fillErrorInfo(inspectDTO);
		return inspectList;
	}
	
	private List<InspectResultStore> fillErrorInfo(MoniInspectResultDTO inspectDTO) {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		InspectResultStore versionDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_VERSION_CODE);
		versionDTO.setInspec_message("程序版本正确性检查任务执行失败");
		versionDTO.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		versionDTO.setInspec_finish_status(0);
		inspectList.add(versionDTO);
		
		InspectResultStore statusDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_RUNNING_CODE);
		statusDTO.setInspec_message("程序运行状态检查任务执行失败");
		statusDTO.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		statusDTO.setInspec_finish_status(0);
		inspectList.add(statusDTO);
		
		return inspectList;
	}
	
	
	private List<InspectResultStore> disposeV8Data(CollectData data) {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		
		Report report = (Report) data.getData().getReportData();
		try{
			com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR) report.getReport();
			String value = report.getReportReturn().getValue();
			String desc = report.getReportReturn().getDesc();
			//1.版本控制
			InspectResultStore versionDTO = fillProgramInfoV8(inspectDTO, pro, value, desc);
			inspectList.add(versionDTO);
			//2.监控器状态
			InspectResultStore statusDTO = fillAliveInfo(inspectDTO, pro, value, desc);
			inspectList.add(statusDTO);
		}catch(Exception e){
			InspectResultStore storeDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_VERSION_CODE);
			storeDTO.setInspec_message("程序版本获取异常,异常信息");
			storeDTO.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
			storeDTO.setInspec_finish_status(0);
			inspectList.add(storeDTO);
		}
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV7Data(CollectData data) {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR) report.getReport();
//		if(pro == null) {
//			dto = fillSuccess(dto, report);
//		}
		String value = report.getReportReturn().getValue();
		String desc = report.getReportReturn().getDesc();
		//1.版本控制、2.监控器状态
		InspectResultStore versionDTO = fillProgramInfoV7(inspectDTO, pro, value, desc);
		inspectList.add(versionDTO);
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV6Data(CollectData data) {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommandR) report.getReport();
//		if(pro == null) {
//			dto = fillSuccess(dto, report);
//		}
		String value = report.getReportReturn().getValue();
		String desc = report.getReportReturn().getDesc();
		//1.版本控制、2.监控器状态
		InspectResultStore versionDTO = fillProgramInfoV6(inspectDTO, pro, value, desc);
		inspectList.add(versionDTO);
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV5Data(CollectData data) {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommandR) report.getReport();
//		if(pro == null) {
//			dto = fillSuccess(dto, report);
//		}
		String value = report.getReportReturn().getValue();
		String desc = report.getReportReturn().getDesc();
		//1.版本控制、2.监控器状态
		InspectResultStore versionDTO = fillProgramInfoV5(inspectDTO, pro, value, desc);
		inspectList.add(versionDTO);
		return inspectList;
	}
	
	private InspectResultStore fillAliveInfo(MoniInspectResultDTO inspectDTO, com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR pro, String value, String desc) {
		int inspect = 0;
		int inspect_status = 0;
		String proDesc = null;
		InspectResultStore storeDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_RUNNING_CODE);
		if(!"0".equals(value)) {
			proDesc = "广播监测软件状态异常,异常信息：" + desc;
			inspect = InspectConstants.INSPECT_TASK_FAILURE;
			inspect_status = 0; 
		}else {
			if(null!=pro.getAliveInfo()){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AliveInfo aInfo = pro.getAliveInfo().get(0);
				if (Integer.parseInt(aInfo.getValue()) == 0) {
					proDesc = "广播监测软件正常";
					inspect = InspectConstants.INSPECT_TASK_SUCCESS;
					inspect_status = 1; 
				}else {
					proDesc = "广播监测软件无法收测";
					inspect = InspectConstants.INSPECT_TASK_FAILURE;
					inspect_status = 0; 
				}
			}
		}
		storeDTO.setInspec_message(proDesc);
		storeDTO.setInspec_result(inspect);
		storeDTO.setInspec_finish_status(inspect_status);
		return storeDTO;
	}
	
	private InspectResultStore fillProgramInfoV8(MoniInspectResultDTO inspectDTO, com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR pro, String value, String desc) {
		boolean isProgram = false;
		int inspect = 0;
		int inspect_status = 0;
		String proDesc = null;
		InspectResultStore storeDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_VERSION_CODE);
		if(!"0".equals(value)) {
			proDesc = "程序版本获取异常,异常信息：" + desc;
			inspect = InspectConstants.INSPECT_TASK_FAILURE;
			inspect_status = 0; 
		}else {
			com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
			//确认设备版本是否正常
			isProgram = context.isProgram(storeDTO.getMonitor_id(), proInfo.getVersion());
			if(isProgram) {
				proDesc = "程序版本正常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 1; 
			}else{
				proDesc = "程序版本异常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 0; 
			}
		}
		storeDTO.setInspec_message(proDesc);
		storeDTO.setInspec_result(inspect);
		storeDTO.setInspec_finish_status(inspect_status);
		return storeDTO;
	}
	
	private InspectResultStore fillProgramInfoV7(MoniInspectResultDTO inspectDTO, com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR pro, String value, String desc) {
		boolean isProgram = false;
		int inspect = 0;
		int inspect_status = 0;
		String proDesc = null;
		InspectResultStore storeDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_VERSION_CODE);
		if(!"0".equals(value)) {
			proDesc = "程序版本获取异常,异常信息：" + desc;
			inspect = InspectConstants.INSPECT_TASK_FAILURE;
			inspect_status = 0;
		}else {
			com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
			//确认设备版本是否正常
			isProgram = context.isProgram(storeDTO.getMonitor_id(), proInfo.getVersion());
			if(isProgram) {
				proDesc = "程序版本正常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 1;
			}else{
				proDesc = "程序版本异常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 0;
			}
		}
		storeDTO.setInspec_message(proDesc);
		storeDTO.setInspec_result(inspect);
		storeDTO.setInspec_finish_status(inspect_status);
		return storeDTO;
	}
	
	private InspectResultStore fillProgramInfoV6(MoniInspectResultDTO inspectDTO, com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommandR pro, String value, String desc) {
		boolean isProgram = false;
		int inspect = 0;
		int inspect_status = 0;
		String proDesc = null;
		InspectResultStore storeDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_VERSION_CODE);
		if(!"0".equals(value)) {
			proDesc = "程序版本获取异常,异常信息：" + desc;
			inspect = InspectConstants.INSPECT_TASK_FAILURE;
			inspect_status = 0;
		}else {
			com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
			//确认设备版本是否正常
			isProgram = context.isProgram(storeDTO.getMonitor_id(), proInfo.getVersion());
			if(isProgram) {
				proDesc = "程序版本正常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 1;
			}else {
				proDesc = "程序版本异常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 0;
			}
		}
		storeDTO.setInspec_message(proDesc);
		storeDTO.setInspec_result(inspect);
		storeDTO.setInspec_finish_status(inspect_status);
		return storeDTO;
	}
	
	private InspectResultStore fillProgramInfoV5(MoniInspectResultDTO inspectDTO, com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommandR pro, String value, String desc) {
		boolean isProgram = false;
		int inspect = 0;
		String proDesc = null;
		int inspect_status = 0;
		InspectResultStore storeDTO = getResultDto(inspectDTO, InspectConstants.inspectProject.PROGRAM_VERSION_CODE);
		if(!"0".equals(value)) {
			proDesc = "程序版本获取异常,异常信息：" + desc;
			inspect = InspectConstants.INSPECT_TASK_FAILURE;
			inspect_status = 0;
		}else {
			com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
			//确认设备版本是否正常
			isProgram = context.isProgram(storeDTO.getMonitor_id(), proInfo.getVersion());
			if(isProgram) {
				proDesc = "程序版本正常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 1;
			}else {
				proDesc = "程序版本异常,当前版本：" + proInfo.getCompany() + "(" + proInfo.getName() + "),version=" + proInfo.getVersion();
				inspect_status = 0;
			}
		}
		storeDTO.setInspec_message(proDesc);
		storeDTO.setInspec_result(inspect);
		storeDTO.setInspec_finish_status(inspect_status);
		return storeDTO;
	}
	
	private void disposeOnlineStatus(CollectData data) {
		InspectMonStat monistat = null;
		MonitorDTO moniDTO = null;
		int softStatus = 1;
		long monitorId = data.getCollectTask().getBusTask().getMonitor_id();
		
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			//改为1   20170424   lyc 
//			softStatus = 4;
			softStatus = 1;
			break;
		default:
			softStatus = 2;
			break;
		}
		monistat = createMonStat(monitorId, softStatus);
		moniDTO = createDto(data.getCollectTask(), softStatus);
		InspectUtils.sendStore(monistat);
		//站点软件未启动   暂时不通知前台也不插入到报警    20170227 lyc
//		InspectUtils.sendWeb(moniDTO);    
//		sendAlarmService(converAlarm(monitorId, softStatus));
		//发送站点状态更新消息（lyc）
		sendMonitorStatus(monitorId, softStatus);
	}
	
	public static void sendAlarmService(EquAlarm alarm ) {
		ARSFToolkit.sendEvent(EventServiceTopic.MONITOR_ALARM_DISPOSE_TOPIC, alarm);
	}
	
	public static void sendMonitorStatus(long monitorId, int status) {
		Log.debug("站点巡检 更新站点 在线状态  (效果类任务)"+status);
		HashMap<Long,Integer> info = new HashMap<Long,Integer>();
		info.put(monitorId, status);
		ARSFToolkit.sendEvent(EventServiceTopic.MONITOR_STATUS_NOTIFY_TOPIC, info);
	}
	
	private EquAlarm converAlarm(long monitorId, int status) {
		EquAlarm dto = new EquAlarm();
		dto.setEventId(1);
		dto.setMonitorId(monitorId);
		dto.setCenterId(-1); //以后提取直属台ID
		dto.setKgId(-1);
		dto.setKpiItem("");
		dto.setAlarmKind(1); //告警类型
		dto.setAlarmLevelId(0);
		int alarmTypeId = InspectUtils.converAlarmType(status);
		dto.setAlarmTypeId(alarmTypeId);
		dto.setAlarmContent(getAlarm(alarmTypeId));	//告警描述
		dto.setAlarmTitle(getAlarm(alarmTypeId));      //告警描述
		dto.setAlarmState(status == 4 ? 1 : 0);      //告警状态0：原发、1：恢复、2：确认（删除）
		dto.setAlarmBeginTime(status == 2 ? getCurrentTime() : "");  //告警时间
		dto.setAlarmEndTime(status == 4 ? getCurrentTime() : "");    //告警恢复时间
		dto.setAlarmPeriodTime(""); //告警历时
		dto.setAlarmCause("2");      //alarm域 故障原因,1、外电停，2、通讯故障，2、设备故障
		dto.setAlarmAdditionalText(""); //告警的附赠信息，比如电平
		dto.setOperateType(0);	//操作类型(0：自动 1：手动)
		return dto;
	}
	
	private String getAlarm(int type) {
		return  InspectTaskContext.getInstance().getAlarmType().get(type);
	}
	
	private MonitorDTO createDto(CollectTask task, int monitorStatus) {
		MonitorDTO dto = new MonitorDTO();
		dto.setOnlineState(monitorStatus + "");
		dto.setMonitorCode(task.getBusTask().getMonitor_code());
		dto.setMonitorId(task.getBusTask().getMonitor_id()+ "");
		return dto;
	}
	
	private InspectMonStat createMonStat(long monitorId, int monitorStatus) {
		InspectMonStat stat = new InspectMonStat();
		stat.setMonitor_id(monitorId);
		stat.setOnline_state(monitorStatus);
		return stat;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + data.getCollectTask().getBusTask().getMonitor_code());
		return buffer.toString();
	}

}
