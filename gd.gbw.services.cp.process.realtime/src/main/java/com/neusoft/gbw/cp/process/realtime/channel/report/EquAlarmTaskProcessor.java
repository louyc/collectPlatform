package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.EquHardAlarm;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquAlarmTaskProcessor extends SendMsgProcessor implements ITaskProcess {

	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		return disposeV8Data(data);
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		return disposeV7Data(data);
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		return disposeV6Data(data);
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		return disposeV5Data(data);
	}
	
	private Object disposeV8Data(CollectData data) {
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 返回数据report为空");
			return null;
		}
		
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 未找到EquipmentAlarmHistoryReport标签");
			return null;
		}
		
		EquHardAlarm equAlarm = null;
		com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmHistoryReport hisReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmHistoryReport) obj;
		List<com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarm> reportList = hisReport.getEquipmentAlarms();
		for(com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarm alarm : reportList) {
			equAlarm = new EquHardAlarm();
			String srcCode = ((Report)report).getSrcCode();
			equAlarm.setDeivceIp(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getDevice_ip());
			equAlarm.setDeviceId(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getMonitor_id() + "");
			equAlarm.setAlarmID(alarm.getAlarmID());
			equAlarm.setEquCode(alarm.getEquCode() == null ? "" : alarm.getEquCode());
			equAlarm.setAlarmMode(Integer.parseInt(alarm.getMode()));
			equAlarm.setAlarmType(Integer.parseInt(alarm.getType()));
			equAlarm.setAlarmDesc(alarm.getDesc());
			equAlarm.setAlarmReason(alarm.getReason());
			equAlarm.setAlarmTime(alarm.getCheckDateTime());
			
			if (alarm.getType().equals("1")) {
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.other.Param> pList = alarm.getParams();
				for(com.neusoft.gbw.cp.conver.v8.protocol.vo.other.Param para : pList) {
					switch(para.getName()) {
					case "OutputLineLevel":
						equAlarm.setOutputLineLevel(para.getValue());
						break;
					case "InputLineLevel":
						equAlarm.setInputLineLevel(para.getValue());
						break;
					case "LineFrequency":
						equAlarm.setLineFrequency(para.getValue());
						break;
					case "BatteryLevel":
						equAlarm.setBatteryLevel(para.getValue());
						break;
					case "UPSStatus":
						equAlarm.setUpsStatus(para.getValue());
						break;
					}
				}
			}
			//更新站点告警状态
			disposeAlarm(equAlarm);
		}
		
		return null;
	}
	
	private Object disposeV7Data(CollectData data) {
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 返回数据report为空");
			return null;
		}
		
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 未找到EquipmentAlarmHistoryReport标签");
			return null;
		}
		
		EquHardAlarm equAlarm = null;
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmHistoryReport hisReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmHistoryReport) obj;
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarm> reportList = hisReport.getEquipmentAlarms();
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarm alarm : reportList) {
			equAlarm = new EquHardAlarm();
			String srcCode = ((Report)report).getSrcCode();
			equAlarm.setDeivceIp(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getDevice_ip());
			equAlarm.setDeviceId(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getMonitor_id() + "");
			equAlarm.setAlarmID(alarm.getAlarmID());
			equAlarm.setEquCode(alarm.getEquCode() == null ? "" : alarm.getEquCode());
			equAlarm.setAlarmMode(Integer.parseInt(alarm.getMode()));
			equAlarm.setAlarmType(Integer.parseInt(alarm.getType()));
			equAlarm.setAlarmDesc(alarm.getDesc());
			equAlarm.setAlarmReason(alarm.getReason());
			equAlarm.setAlarmTime(alarm.getCheckDateTime());
			
			if (alarm.getType().equals("1")) {
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Param> pList = alarm.getParams();
				for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Param para : pList) {
					switch(para.getName()) {
					case "OutputLineLevel":
						equAlarm.setOutputLineLevel(para.getValue());
						break;
					case "InputLineLevel":
						equAlarm.setInputLineLevel(para.getValue());
						break;
					case "LineFrequency":
						equAlarm.setLineFrequency(para.getValue());
						break;
					case "BatteryLevel":
						equAlarm.setBatteryLevel(para.getValue());
						break;
					case "UPSStatus":
						equAlarm.setUpsStatus(para.getValue());
						break;
					}
				}
			}
			//更新站点告警状态
			disposeAlarm(equAlarm);
		}
		
		return null;
	}
	
	private Object disposeV6Data(CollectData data) {
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 返回数据report为空");
			return null;
		}
		
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 未找到EquipmentAlarmHistoryReport标签");
			return null;
		}
		
		EquHardAlarm equAlarm = null;
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmHistoryReport hisReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmHistoryReport) obj;
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarm> reportList = hisReport.getEquipmentAlarms();
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarm alarm : reportList) {
			equAlarm = new EquHardAlarm();
			String srcCode = ((Report)report).getSrcCode();
			equAlarm.setDeivceIp(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getDevice_ip());
			equAlarm.setDeviceId(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getMonitor_id() + "");
			equAlarm.setAlarmID(alarm.getAlarmID());
			equAlarm.setEquCode(alarm.getEquCode() == null ? "" : alarm.getEquCode());
			equAlarm.setAlarmMode(Integer.parseInt(alarm.getMode()));
			equAlarm.setAlarmType(Integer.parseInt(alarm.getType()));
			equAlarm.setAlarmDesc(alarm.getDesc());
			equAlarm.setAlarmReason(alarm.getReason());
			equAlarm.setAlarmTime(alarm.getCheckDateTime());
			
			if (alarm.getType().equals("1")) {
				List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Param> pList = alarm.getParams();
				for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Param para : pList) {
					switch(para.getName()) {
					case "OutputLineLevel":
						equAlarm.setOutputLineLevel(para.getValue());
						break;
					case "InputLineLevel":
						equAlarm.setInputLineLevel(para.getValue());
						break;
					case "LineFrequency":
						equAlarm.setLineFrequency(para.getValue());
						break;
					case "BatteryLevel":
						equAlarm.setBatteryLevel(para.getValue());
						break;
					case "UPSStatus":
						equAlarm.setUpsStatus(para.getValue());
						break;
					}
				}
			}
			//更新站点告警状态
			disposeAlarm(equAlarm);
		}
		
		return null;
	}
	
	private Object disposeV5Data(CollectData data) {
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 返回数据report为空");
			return null;
		}
		
		Object obj = ((Report)data.getData().getReportData()).getReport();
		if(obj == null) {
			Log.debug("[设备报警主动上报]设备报警DTO对象构建流程# 未找到EquipmentAlarmHistoryReport标签");
			return null;
		}
		
		EquHardAlarm equAlarm = null;
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmHistoryReport hisReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmHistoryReport) obj;
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarm> reportList = hisReport.getEquipmentAlarms();
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarm alarm : reportList) {
			equAlarm = new EquHardAlarm();
			String srcCode = ((Report)report).getSrcCode();
			equAlarm.setDeivceIp(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getDevice_ip());
			equAlarm.setDeviceId(CollectTaskModel.getInstance().getMonitorDevice(srcCode).getMonitor_id() + "");
			equAlarm.setAlarmID(alarm.getAlarmID());
			equAlarm.setEquCode(alarm.getEquCode() == null ? "" : alarm.getEquCode());
			equAlarm.setAlarmMode(Integer.parseInt(alarm.getMode()));
			equAlarm.setAlarmType(Integer.parseInt(alarm.getType()));
			equAlarm.setAlarmDesc(alarm.getDesc());
			equAlarm.setAlarmReason(alarm.getReason());
			equAlarm.setAlarmTime(alarm.getCheckDateTime());
			
			if (alarm.getType().equals("1")) {
				List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Param> pList = alarm.getParams();
				for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Param para : pList) {
					switch(para.getName()) {
					case "OutputLineLevel":
						equAlarm.setOutputLineLevel(para.getValue());
						break;
					case "InputLineLevel":
						equAlarm.setInputLineLevel(para.getValue());
						break;
					case "LineFrequency":
						equAlarm.setLineFrequency(para.getValue());
						break;
					case "BatteryLevel":
						equAlarm.setBatteryLevel(para.getValue());
						break;
					case "UPSStatus":
						equAlarm.setUpsStatus(para.getValue());
						break;
					}
				}
			}
			//更新站点告警状态
			disposeAlarm(equAlarm);
		}
		return null;
	}
	
	private void disposeAlarm(EquHardAlarm equAlarm) {
		//获取故障告警类型，是原发告警，还是解除告警, mode:0报警，1解除报警
//		int alarmMode = equAlarm.getAlarmMode();
//		if(alarmMode == 0) {
//			saveAlarm(equAlarm, "insertEquAlarm");
//		}else {
//			saveAlarm(equAlarm, "updateEquAlarm");
//			saveAlarm(equAlarm, "updateSiteEquAlarmStatus");
//		}
//		sendWeb(equAlarm);
//		sendNPAlarm(equAlarm);
		sendAlarmService(converAlarm(equAlarm));
	}
	
	
	
//	private void sendNPAlarm(EquHardAlarm equAlarm) {
//		//如果为0.则不进行告警发送，1进行告警发送
//		if(ConfigVariable.IS_SEND_EQU_ALARM == 0)
//			return;
//		
//		try {
//			Map<String, String> attrs = NMBeanUtils.getObjectFieldStr(equAlarm);
//			String value = NPJsonUtil.mapToJson(attrs);
//			//需要设置Socket的连接设置，对应NP5.3采集平台
//			ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_ALARM_TOPIC, value);
//		} catch (NMBeanUtilsException e) {
//			Log.error("", e);
//		}
//	}
	
//	private void saveAlarm(EquHardAlarm equAlarm, String label) {
//		StoreInfo info =  buildStoreInfo(equAlarm, label);
//		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
//	}
	
//	private void sendWeb(EquHardAlarm equAlarm){
//		EquHardAlarm alarm = new EquHardAlarm();
//		EquAlarmEventDTO dto = coverDTO(alarm); 
//		JMSDTO jms = createJmsDto(dto, GBWMsgConstant.C_JMS_REAL_EQU_ALARM_RESPONSE_MSG);
//		sendMsg(jms);
//	}
	
//	private EquAlarmEventDTO coverDTO(EquHardAlarm equAlarm) {
//		EquAlarmEventDTO dto = new EquAlarmEventDTO();
//		dto.setEventId(equAlarm.getAlarmID());
//		dto.setMonitorId(equAlarm.getDeviceId());
//		dto.setMonitorIp(equAlarm.getDeivceIp());
//		int mode = equAlarm.getAlarmMode();
//		if(mode == 0) 
//			dto.setEventTime(equAlarm.getAlarmTime());
//		else
//			dto.setResumeTime(equAlarm.getAlarmTime());
//		
//		dto.setIsResume(mode + "");
//		dto.setEquCode(equAlarm.getEquCode());
//		dto.setEventTypeId(equAlarm.getAlarmType() + "");
//		dto.setEventTypeDesc(equAlarm.getAlarmDesc());
//		dto.setEventReason(equAlarm.getAlarmReason());
//		dto.setOutputLineLevel(equAlarm.getOutputLineLevel());
//		dto.setInputLineLevel(equAlarm.getInputLineLevel());
//		dto.setBatteryLevel(equAlarm.getBatteryLevel());
//		dto.setLineFrequency(equAlarm.getLineFrequency());
//		dto.setUpsStatus(equAlarm.getUpsStatus());
//		return dto;
//	}
	
	public static void sendAlarmService(EquAlarm alarm ) {
		ARSFToolkit.sendEvent(EventServiceTopic.MONITOR_ALARM_DISPOSE_TOPIC, alarm);
	}
	/**
	 * 转换站点上报告警
	 * @param equAlarm
	 * @return
	 */
	private EquAlarm converAlarm(EquHardAlarm equAlarm) {
		EquAlarm dto = new EquAlarm();
		dto.setEventId(Integer.parseInt(equAlarm.getAlarmID()));
		dto.setMonitorId(Long.parseLong(equAlarm.getDeviceId()));
		dto.setCenterId(-1); //以后提取直属台ID
		dto.setKgId(-1);
		dto.setKpiItem("");
		dto.setAlarmKind(2); //告警类型1、实时告警；2、业务告警；3、手动创建；
		dto.setAlarmLevelId(-1);
		int alarmTypeId = equAlarm.getAlarmType();
		dto.setAlarmTypeId(alarmTypeId);
		dto.setAlarmContent(getAlarm(alarmTypeId));	//告警描述
		dto.setAlarmTitle(getAlarm(alarmTypeId));      //告警描述
		int alarmMode = equAlarm.getAlarmMode(); // 0 告警， 1解除
		dto.setAlarmState(alarmMode);      //告警状态0：原发、1：恢复、2：确认（删除）
		dto.setAlarmBeginTime(alarmMode == 0 ? getCurrentTime() : "");  //告警时间
		dto.setAlarmEndTime(alarmMode == 1 ? getCurrentTime() : "");    //告警恢复时间
		dto.setAlarmPeriodTime(""); //告警历时
		dto.setAlarmCause("3");      //alarm域 故障原因,1、外电停，2、通讯故障，3、设备故障
		dto.setAlarmAdditionalText(""); //告警的附赠信息，比如电平
		dto.setOperateType(0);	//操作类型(0：自动 1：手动)
		return dto;
	}
	
	public String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	private String getAlarm(int type) {
		return  CollectTaskModel.getInstance().getAlarmType().get(type);
	}
	
	
	//采用长连接的方式
//	public void sendAlarm(String addressIp, String xml) {
//		NMSSocketClient socketClient = null;
//		int addressPort = 50000;
//		try {	
//			socketClient = NMSSocketFactory.createSocketClient(addressIp, addressPort);
//			socketClient.connect();
//			socketClient.send(xml.getBytes("GB2312"));
//			Log.debug("[socket发送]发送设备告警信息成功，IP=" + addressIp + ",potocol=" + xml);
//		} catch (Exception e) {
//			Log.error("发送采集任务失败：告警平台连接信息：IP："+ addressIp + "  PORT:" + addressPort, e);
//		}finally {
//			if(socketClient != null) {
//				socketClient.disconnect();
//			}
//		}
//	}
	
}
