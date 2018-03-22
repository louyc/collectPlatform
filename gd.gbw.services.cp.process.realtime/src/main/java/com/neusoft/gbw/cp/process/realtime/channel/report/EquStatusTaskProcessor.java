package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniStatusDTO;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentRealTimeStatus;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealTimeReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.Parameter;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.RealtimeStatusEquipment;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentRealtimeStatus;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.core.NetEventType;

public class EquStatusTaskProcessor implements ITaskProcess{
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		Object obj = dispose(data);
		if(obj == null) 
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj);
		else
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		Object obj = dispose(data);
		if(obj == null) 
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj);
		else
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		Object obj = dispose(data);
//		Object obj = null;
		if(obj == null) 
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj);
		else
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		Object obj = dispose(data);
		if(obj == null) 
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj);
		else
			Log.debug("[设备状态主动上报]设备状态主动上报消息发送至前台, data=" + obj.toString());
		sendTask(obj);
		return null;
	}
	
	public void sendTask(Object syntObj) {
		if (syntObj instanceof JMSDTO) {
			ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, syntObj);
		} else if (syntObj instanceof String) {
			ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), (String)syntObj);
		} else {
			Log.warn("[设备状态主动上报]实时任务处理类型不存在，" + syntObj);
		}
	}

	private Object dispose(CollectData data) {
		if(!checkReport(data)) {
			return null;
		}
		JMSDTO dto = null;
		MoniDeviceStatusDTO devDTO = (MoniDeviceStatusDTO)data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);;
		List<MoniStatusDTO> resList = new ArrayList<MoniStatusDTO>();
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealTimeReport) 
			resList = getSpecialMoniStatusList(obj);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport)
			resList = getMoniStatusList(obj);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport)
			resList = getV7MoniStatusList(obj);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport)
			resList = getV6MoniStatusList(obj);
		else if(obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport)
			resList = getV5MoniStatusList(obj);
		
		devDTO.setResList(resList);
		dto = createJmsDto(GBWMsgConstant.C_JMS_MONITOR_STATUS_RESPONSE_MSG,devDTO);
		return dto;
	}
	
	private boolean checkReport(CollectData data) {
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("[设备状态主动上报]设备状态DTO对象构建流程# 返回数据report为空");
			return false;
		}
		
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj == null) {
			Log.debug("[设备状态主动上报]设备状态DTO对象构建流程# 未找到EquipmentStatusRealtimeReport标签");
			return false;
		}
		
//		EquipmentStatusRealTimeReport equ = (EquipmentStatusRealTimeReport)((Report)data.getData().getReportData()).getReport();
//		if(equ == null ) {
//			Log.debug("[设备状态主动上报]设备状态DTO对象构建流程# 未找到EquipmentStatusRealtimeReport标签");
//			return false;
//		}
//		
//		List<EquipmentRealTimeStatus> setList = equ.getEquipmentRealtimeStatuses();
//		if(setList == null || setList.isEmpty()) {
//			Log.debug("[设备状态主动上报]设备状态DTO对象构建流程# 返回数据EquipmentRealtimeStatuses标签数据为空");
//			return false;
//		}
		
		Map<String, Object> expandMap = data.getCollectTask().getExpandMap();
		if(expandMap == null) {
			Log.debug("[设备状态主动上报]未找到设备状态任务信息");
			return false;
		}
		
		return true;
	}
	
	private List<MoniStatusDTO> getSpecialMoniStatusList(Object obj) {
		List<MoniStatusDTO> resList = new ArrayList<MoniStatusDTO>();
		List<EquipmentRealTimeStatus> setList = ((EquipmentStatusRealTimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(EquipmentRealTimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(RealtimeStatusEquipment equ : status.getEquipments()) {
						MoniStatusDTO staDTO = new MoniStatusDTO();
						staDTO.setCode(equ.getEquCode());
						staDTO.setDesc(equ.getDesc());
						staDTO.setStatus(equ.getEquStatus());
						staDTO.setType(equ.getType() + "");
						staDTO.setParamMap(getParamMap(equ));
						resList.add(staDTO);
					}
				}
			}
		}
		return resList;
	}
	
	private List<MoniStatusDTO> getMoniStatusList(Object obj) {
		List<MoniStatusDTO> resList = new ArrayList<MoniStatusDTO>();
		List<EquipmentRealtimeStatus> setList = ((EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(RealtimeStatusEquipment equ : status.getEquipments()) {
						MoniStatusDTO staDTO = new MoniStatusDTO();
						staDTO.setCode(equ.getEquCode());
						staDTO.setDesc(equ.getDesc());
						staDTO.setStatus(equ.getEquStatus());
						staDTO.setType(equ.getType() + "");
						staDTO.setParamMap(getParamMap(equ));
						resList.add(staDTO);
					}
				}
			}
		}
		return resList;
	}
	
	private List<MoniStatusDTO> getV7MoniStatusList(Object obj) {
		List<MoniStatusDTO> resList = new ArrayList<MoniStatusDTO>();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentRealtimeStatus> setList = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.RealtimeStatusEquipment equ : status.getEquipments()) {
						MoniStatusDTO staDTO = new MoniStatusDTO();
						staDTO.setCode(equ.getEquCode());
						staDTO.setDesc(equ.getDesc());
						staDTO.setStatus(equ.getEquStatus());
						staDTO.setType(equ.getType() + "");
						staDTO.setParamMap(getParamMap(equ));
						resList.add(staDTO);
					}
				}
			}
		}
		return resList;
	}
	
	private List<MoniStatusDTO> getV5MoniStatusList(Object obj) {
		List<MoniStatusDTO> resList = new ArrayList<MoniStatusDTO>();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentRealtimeStatus> setList = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.RealtimeStatusEquipment equ : status.getEquipments()) {
						MoniStatusDTO staDTO = new MoniStatusDTO();
						staDTO.setCode(equ.getEquCode());
						staDTO.setDesc(equ.getDesc());
						staDTO.setStatus(equ.getEquStatus());
						staDTO.setType(equ.getType() + "");
						staDTO.setParamMap(getParamMap(equ));
						resList.add(staDTO);
					}
				}
			}
		}
		return resList;
	}
	
	private List<MoniStatusDTO> getV6MoniStatusList(Object obj) {
		List<MoniStatusDTO> resList = new ArrayList<MoniStatusDTO>();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentRealtimeStatus> setList = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport)obj).getEquipmentRealtimeStatuses();
		if(!setList.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentRealtimeStatus status : setList) {
				if(status.getEquipments() != null || !status.getEquipments().isEmpty()) {
					for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.RealtimeStatusEquipment equ : status.getEquipments()) {
						MoniStatusDTO staDTO = new MoniStatusDTO();
						staDTO.setCode(equ.getEquCode());
						staDTO.setDesc(equ.getDesc());
						staDTO.setStatus(equ.getEquStatus());
						staDTO.setType(equ.getType() + "");
						staDTO.setParamMap(getParamMap(equ));
						resList.add(staDTO);
					}
				}
			}
		}
		return resList;
	}
	
	private Map<Object, Object> getParamMap(RealtimeStatusEquipment equ) {
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		List<Parameter> parameters = equ.getParameters();
		if(parameters != null && !parameters.isEmpty()) {
			for(Parameter param : parameters) {
				paramMap.put(param.getParammeterType(), param.getValue());
			}
		}
		return paramMap;
	}
	
	private Map<Object, Object> getParamMap(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.RealtimeStatusEquipment equ) {
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Parameter> parameters = equ.getParameters();
		if(parameters != null && !parameters.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Parameter param : parameters) {
				paramMap.put(param.getParammeterType(), param.getValue());
			}
		}
		return paramMap;
	}
	
	private Map<Object, Object> getParamMap(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.RealtimeStatusEquipment equ) {
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.other.Parameter> parameters = equ.getParameters();
		if(parameters != null && !parameters.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.Parameter param : parameters) {
				paramMap.put(param.getParammeterType(), param.getValue());
			}
		}
		return paramMap;
	}
	
	private Map<Object, Object> getParamMap(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.RealtimeStatusEquipment equ) {
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Parameter> parameters = equ.getParameters();
		if(parameters != null && !parameters.isEmpty()) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Parameter param : parameters) {
				paramMap.put(param.getParammeterType(), param.getValue());
			}
		}
		return paramMap;
	}
	
	private JMSDTO createJmsDto(int type_id,MoniDeviceStatusDTO devDTO) {
		JMSDTO dto = new JMSDTO();
		dto.setTypeId(type_id);
		dto.setObj(devDTO);
		return dto;
	}

}
