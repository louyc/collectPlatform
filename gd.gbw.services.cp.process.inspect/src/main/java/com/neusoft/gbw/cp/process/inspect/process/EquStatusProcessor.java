package com.neusoft.gbw.cp.process.inspect.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentRealTimeStatus;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealTimeReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.RealtimeStatusEquipment;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentRealtimeStatus;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.report.ReportData;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.exception.InspectDisposeException;
import com.neusoft.gbw.cp.process.inspect.vo.InspectResultStore;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquStatusProcessor extends SendTaskProcessor implements ITaskProcess{
	
	private static Map<String, String> statusInspectMap = null;
	
	static {
		statusInspectMap = new HashMap<String, String>();
		statusInspectMap.put(InspectConstants.inspectProject.EQU_UPS_CODE, InspectConstants.inspectProject.EQU_UPS_CODE);
		statusInspectMap.put(InspectConstants.inspectProject.EQU_RECEIVE_CODE, InspectConstants.inspectProject.EQU_RECEIVE_CODE);
		statusInspectMap.put(InspectConstants.inspectProject.EQU_AM_CARD_CODE, InspectConstants.inspectProject.EQU_AM_CARD_CODE);
		statusInspectMap.put(InspectConstants.inspectProject.EQU_FM_CARD_CODE, InspectConstants.inspectProject.EQU_FM_CARD_CODE);
		statusInspectMap.put(InspectConstants.inspectProject.EQU_VOICE_CARD_CODE, InspectConstants.inspectProject.EQU_VOICE_CARD_CODE);
		statusInspectMap.put(InspectConstants.inspectProject.EQU_OFFSET_CARD_CODE, InspectConstants.inspectProject.EQU_OFFSET_CARD_CODE);
	}
	
	@Override
	public void disposeV8(CollectData data) throws InspectDisposeException {
		dispose(data);
	}

	@Override
	public void disposeV7(CollectData data) throws InspectDisposeException {
		dispose(data);
	}

	@Override
	public void disposeV6(CollectData data) throws InspectDisposeException {
		dispose(data);
	}

	@Override
	public void disposeV5(CollectData data) throws InspectDisposeException {
		dispose(data);
		
	}
	
	private void dispose(CollectData data) {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		inspectList = converStore(data);
		if(inspectList == null || inspectList.size() <= 0) 
			return;
		
		inspectList.addAll(getUnFoundInspect(inspectList));
//		Log.debug("[巡检服务]版本控制任务处理消息发送至前台,msg=" + inspectList.toString());
		disposeData(inspectList);
		
//		//存储数据
//		storeInfo(inspectList);
//		//更改巡检完成状态
//		updateInspectStatus(inspectList);
//		//发送至前台
//		sendTask(inspectList);
	}
	
	private List<InspectResultStore> converStore(CollectData data) {
		if(!checkReport(data)) {
			return null;
		}
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO)data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		List<InspectResultStore> resList = new ArrayList<InspectResultStore>();
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealTimeReport) 
			resList = getSpecialMoniStatusList(obj, inspectDTO);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport)
			resList = getMoniStatusList(obj, inspectDTO);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport)
			resList = getV7MoniStatusList(obj,inspectDTO);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport)
			resList = getV6MoniStatusList(obj,inspectDTO);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport)
			resList = getV5MoniStatusList(obj,inspectDTO);
		
		return resList;
	}
	
	private boolean checkReport(CollectData data) {
		ReportData rdata = data.getData();
		if(rdata == null) {
			Log.debug("[设备巡检]设备状态DTO对象构建流程# 返回数据ReportData为空");
			return false;
		}
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("[设备巡检]设备状态DTO对象构建流程# 返回数据report为空");
			return false;
		}
		
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj == null) {
			Log.debug("[设备巡检]设备状态DTO对象构建流程# 未找到EquipmentStatusRealtimeReport标签");
			return false;
		}
		
		Map<String, Object> expandMap = data.getCollectTask().getExpandMap();
		if(expandMap == null) {
			Log.debug("[设备巡检]未找到设备状态任务信息");
			return false;
		}
		
		return true;
	}
	
	private List<InspectResultStore> getSpecialMoniStatusList(Object obj, MoniInspectResultDTO inspectDto) {
		InspectResultStore resultDto = null;
		String startDesc = null;
		List<InspectResultStore> resList = new ArrayList<InspectResultStore>();
		List<EquipmentRealTimeStatus> setList = ((EquipmentStatusRealTimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(EquipmentRealTimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(RealtimeStatusEquipment equ : status.getEquipments()) {
						resultDto = getInspectStore(inspectDto,equ.getType());
						startDesc = getInspectDesc(inspectDto,equ.getType());
						if(equ.getEquStatus().equalsIgnoreCase("Work") || equ.getEquStatus().equalsIgnoreCase("Idle")) {
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
							resultDto.setInspec_finish_status(1);
						}
						else { 
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
							resultDto.setInspec_finish_status(0);
						}
						String msg = startDesc + "EquStatus=" + equ.getEquStatus() + ",Desc=" + equ.getDesc()+",EquCode="+equ.getEquCode();
						resultDto.setInspec_message(msg);
						resList.add(resultDto);
					}
				}
				break;
			}
		}
		return resList;
	}
	
	private List<InspectResultStore> getUnFoundInspect(List<InspectResultStore> inspectList) {
		List<InspectResultStore> unFoundList = new ArrayList<InspectResultStore>();
		InspectResultStore inspectData = inspectList.get(0);
		String inspectCode = "";
		for(InspectResultStore store : inspectList) {
			String code = store.getInspec_code();
			inspectCode += code + ";";
		}
		for(String inspect : statusInspectMap.keySet()) {
			if(inspectCode.contains(inspect))
				continue;
			InspectResultStore unFoundStore = new InspectResultStore();
			unFoundStore.setInspec_code(inspect);
			unFoundStore.setInspec_finish_time(inspectData.getInspec_finish_time());
			unFoundStore.setInspec_message("该站点没有返回该巡检项数据");
			unFoundStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
			unFoundStore.setInspec_finish_status(0); //没有该巡检项也定义为异常数据
			unFoundStore.setMonitor_id(inspectData.getMonitor_id());
			unFoundStore.setTime_stamp(inspectData.getTime_stamp());
			unFoundList.add(unFoundStore);
		}
		return unFoundList;
	}
	
	private InspectResultStore getInspectStore(MoniInspectResultDTO inspectDto,String equType) {
		InspectResultStore resultDto = null;
		int type = Integer.parseInt(equType);
		switch(type) {
		case 1:
			resultDto = getResultDto(inspectDto,InspectConstants.inspectProject.EQU_UPS_CODE);
			break;
		case 2:
			resultDto = getResultDto(inspectDto,InspectConstants.inspectProject.EQU_RECEIVE_CODE);
			break;
		case 3:
			resultDto = getResultDto(inspectDto,InspectConstants.inspectProject.EQU_AM_CARD_CODE);
			break;
		case 4:
			resultDto = getResultDto(inspectDto,InspectConstants.inspectProject.EQU_FM_CARD_CODE);
			break;
		case 5:
			resultDto = getResultDto(inspectDto,InspectConstants.inspectProject.EQU_VOICE_CARD_CODE);
			break;
		case 6:
			resultDto = getResultDto(inspectDto,InspectConstants.inspectProject.EQU_OFFSET_CARD_CODE);
			break;
		}
		return resultDto;
	}
	
	private String getInspectDesc(MoniInspectResultDTO inspectDto,String equType) {
		String startResult = null;
		int type = Integer.parseInt(equType);
		switch(type) {
		case 1:
			startResult = "UPS电源：";
			break;
		case 2:
			startResult = "接收机：";
			break;
		case 3:
			startResult = "调制度卡：";
			break;
		case 4:
			startResult = "调幅度卡：";
			break;
		case 5:
			startResult = "语音压缩卡：";
			break;
		case 6:
			startResult = "频偏卡：";
			break;
		}
		return startResult;
	}
	
	private List<InspectResultStore> getMoniStatusList(Object obj, MoniInspectResultDTO inspectDto) {
		InspectResultStore resultDto = null;
		String startDesc = null;
		List<InspectResultStore> resList = new ArrayList<InspectResultStore>();
		List<EquipmentRealtimeStatus> setList = ((EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(RealtimeStatusEquipment equ : status.getEquipments()) {
						resultDto = getInspectStore(inspectDto,equ.getType());
						startDesc = getInspectDesc(inspectDto,equ.getType());
						if(equ.getEquStatus().equalsIgnoreCase("Work") || equ.getEquStatus().equalsIgnoreCase("Idle")) {
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
							resultDto.setInspec_finish_status(1);
							resultDto.setInspec_finish_score(10);
						}else { 
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
							resultDto.setInspec_finish_status(0);
							resultDto.setInspec_finish_score(0);
						}
						String msg =startDesc + "EquStatus=" + equ.getEquStatus() + ",Desc=" + equ.getDesc()+",EquCode="+equ.getEquCode();
						resultDto.setInspec_message(msg);
						resList.add(resultDto);
					}
				}
				break;
			}
		}
		return resList;
	}
	
	private List<InspectResultStore> getV7MoniStatusList(Object obj, MoniInspectResultDTO inspectDto) {
		InspectResultStore resultDto = null;
		String startDesc = null;
		List<InspectResultStore> resList = new ArrayList<InspectResultStore>();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentRealtimeStatus> setList = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.RealtimeStatusEquipment equ : status.getEquipments()) {
						resultDto = getInspectStore(inspectDto,equ.getType());
						startDesc = getInspectDesc(inspectDto,equ.getType());
						if(equ.getEquStatus().equalsIgnoreCase("Work") || equ.getEquStatus().equalsIgnoreCase("Idle")) {
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
							resultDto.setInspec_finish_status(1);
						}else { 
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
							resultDto.setInspec_finish_status(0);
						}
						String msg =startDesc + "EquCode=" + equ.getEquCode() + ",EquStatus=" + equ.getEquStatus() + ",Desc=" + equ.getDesc()+",EquCode="+equ.getEquCode();
						resultDto.setInspec_message(msg);
						resList.add(resultDto);
					}
				}
				break;
			}
		}
		return resList;
	}
	
	private List<InspectResultStore> getV5MoniStatusList(Object obj, MoniInspectResultDTO inspectDto) {
		InspectResultStore resultDto = null;
		String startDesc = null;
		List<InspectResultStore> resList = new ArrayList<InspectResultStore>();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentRealtimeStatus> setList = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.RealtimeStatusEquipment equ : status.getEquipments()) {
						resultDto = getInspectStore(inspectDto,equ.getType());
						startDesc = getInspectDesc(inspectDto,equ.getType());
						if(equ.getEquStatus().equalsIgnoreCase("Work") || equ.getEquStatus().equalsIgnoreCase("Idle")) {
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
							resultDto.setInspec_finish_status(1);
						}else { 
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
							resultDto.setInspec_finish_status(0);
						}
						String msg =startDesc + "EquStatus=" + equ.getEquStatus() + ",Desc=" + equ.getDesc()+",EquCode="+equ.getEquCode();
						resultDto.setInspec_message(msg);
						resList.add(resultDto);
					}
				}
				break;
			}
		}
		return resList;
	}
	
	private List<InspectResultStore> getV6MoniStatusList(Object obj, MoniInspectResultDTO inspectDto) {
		InspectResultStore resultDto = null;
		String startDesc = "";
		List<InspectResultStore> resList = new ArrayList<InspectResultStore>();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentRealtimeStatus> setList = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.RealtimeStatusEquipment equ : status.getEquipments()) {
						resultDto = getInspectStore(inspectDto,equ.getType());
						startDesc = getInspectDesc(inspectDto,equ.getType());
						if(equ.getEquStatus().equalsIgnoreCase("Work") || equ.getEquStatus().equalsIgnoreCase("Idle")) {
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
							resultDto.setInspec_finish_status(1);
						}else { 
							resultDto.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
							resultDto.setInspec_finish_status(0);
						}
						String msg =startDesc + "EquStatus=" + equ.getEquStatus() + ",Desc=" + equ.getDesc()+",EquCode="+equ.getEquCode();
						resultDto.setInspec_message(msg);
						resList.add(resultDto);
					}
				}
				break;
			}
		}
		return resList;
	}

}
