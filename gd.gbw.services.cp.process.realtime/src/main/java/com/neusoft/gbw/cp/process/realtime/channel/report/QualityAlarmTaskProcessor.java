package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.AlarmParam;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarm;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarmHistoryReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.site.MonitorMachine;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
//import com.neusoft.gbw.cp.process.alarm.dao;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.abnormal.intf.dto.AbnormalEventDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class QualityAlarmTaskProcessor implements ITaskProcess {
	
	private static String INSERTQUALITYALARM = "insertQualityAlarm";
	private static String INSERTQUALITYALARM_BYSELF = "insertQualityAlarm1";   // 当回收失败时   插入异太报警库
	
	private static String UPDATEQUALITYALARM = "updateQualityAlarm";    //修改异态告警实时表 gbal_localquality_event_dat
	private static String UPDATECONFQUALITYALARM = "updateConfQualityAlarm";   
	
	private static String UPDATEALLQUALITYALARM = "updateAllQualityAlarm";
	private static String UPDATEALLDELQUALITYALARM = "updateAllDelQualityAlarm";   //修改异态告警确认表      gbal_localquality_alarm_dat
	private static String UPDATEALLCONFQUALITYALARM = "updateAllConfQualityAlarm";
	
	private static String UPDATESITEQUALITYALARMSTATUS = "updateSiteQualityAlarmStatus";  //更新站点相关表  gbmo_moni_location_cft  修改站点报警状态
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		return dispose(data);
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

	private Object dispose(CollectData data) {
		//站点告警上报
		Report report = (Report) data.getData().getReportData();
		
		String srcCode = report.getSrcCode();
		MonitorDevice device = getMonitorDevice(srcCode);
		if (device == null) {
			Log.warn("处理异态信息异常，站点编号不存在。CODE=" + srcCode);
			return null;
		}
		
		List<AbnormalEventDTO> dtoList = new ArrayList<AbnormalEventDTO>();
		List<StoreInfo> storeList = new ArrayList<StoreInfo>();
		AbnormalEventDTO dto = null;
		StoreInfo info = null;
		QualityAlarmHistoryReport alarmReport = (QualityAlarmHistoryReport) report.getReport();
		List<QualityAlarm> alarmList = alarmReport.getQualityAlarm();
		for(QualityAlarm qualityAlarm : alarmList) {
			dto = buildAbnormalEventDTO(qualityAlarm, device);
			//如果alarmId为-1，则需要将该站点该接收机的告警都置为正常
			String alarmId = qualityAlarm.getAlarmID();
			if(alarmId.equals("-1")) {
				info = buildStoreInfo(dto, UPDATEALLQUALITYALARM);
				//增加告警确认和告警删除表的修改逻辑
				storeList.add(buildStoreInfo(dto, UPDATEALLCONFQUALITYALARM));
//				storeList.add(buildStoreInfo(dto, UPDATEALLDELQUALITYALARM));
			}else {
				String resume = dto.getIsResume();
				if (resume.equals("0")) {			
					info = buildStoreInfo(dto, INSERTQUALITYALARM);
				} else if (resume.equals("1")) {
					info = buildStoreInfo(dto,UPDATEQUALITYALARM);
					//增加告警确认和告警删除表的修改逻辑
					storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//		lyc			storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
				}
			}
			dtoList.add(dto);
			storeList.add(info);
			//增加修改站点指标告警状态功能      站点归属区域 表告警信息    20170105
			storeList.add(buildUpdateSiteAlarmStatusStoreInfo(dto));
			sendStore(storeList);
//			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		}
		JMSDTO jms = buildJMSDTO(dtoList);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
		return null;
	}
	
	private Object disposeV7Data(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		
		String srcCode = report.getSrcCode();
		MonitorDevice device = getMonitorDevice(srcCode);
		if (device == null) {
			Log.warn("处理异态信息异常，站点编号不存在。CODE=" + srcCode);
			return null;
		}
		
		List<AbnormalEventDTO> dtoList = new ArrayList<AbnormalEventDTO>();
		List<StoreInfo> storeList = new ArrayList<StoreInfo>();
		AbnormalEventDTO dto = null;
		StoreInfo info = null;
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryReport alarmReport =  getV7AlarmReport(data,report);
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarm> alarmList = alarmReport.getQualityAlarm();
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarm qualityAlarm : alarmList) {
			dto = buildV7AbnormalEventDTO(qualityAlarm, device);
			//如果alarmId为-1，则需要将该站点该接收机的告警都置为正常
			String alarmId = qualityAlarm.getAlarmID();
			if(alarmId.equals("-1")) {
				info = buildStoreInfo(dto, UPDATEQUALITYALARM);
				//增加告警确认和告警删除表的修改逻辑
				storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//				storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
			}else {
				String resume = dto.getIsResume();
				if (resume.equals("0")) {
					info = buildStoreInfo(dto, INSERTQUALITYALARM);
				} else if (resume.equals("1")) {
					info = buildStoreInfo(dto,UPDATEQUALITYALARM);
					//增加告警确认和告警删除表的修改逻辑
					storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//					storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
				}
			}
			dtoList.add(dto);
			storeList.add(info);
			//增加修改站点指标告警状态功能      站点归属区域 表告警信息    20170105
			storeList.add(buildUpdateSiteAlarmStatusStoreInfo(dto));
			sendStore(storeList);
//			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		}
		
		JMSDTO jms = buildJMSDTO(dtoList);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
		return null;
	}
	
	private Object disposeV6Data(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		
		String srcCode = report.getSrcCode();
		MonitorDevice device = getMonitorDevice(srcCode);
		if (device == null) {
			Log.warn("处理异态信息异常，站点编号不存在。CODE=" + srcCode);
			return null;
		}
		
		List<AbnormalEventDTO> dtoList = new ArrayList<AbnormalEventDTO>();
		List<StoreInfo> storeList = new ArrayList<StoreInfo>();
		AbnormalEventDTO dto = null;
		StoreInfo info = null;
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmHistoryReport alarmReport =  getV6AlarmReport(data,report);
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarm> alarmList = alarmReport.getQualityAlarm();
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarm qualityAlarm : alarmList) {
			dto = buildV6AbnormalEventDTO(qualityAlarm, device);
			//如果alarmId为-1，则需要将该站点该接收机的告警都置为正常
			String alarmId = qualityAlarm.getAlarmID();
			if(alarmId.equals("-1")) {
				info = buildStoreInfo(dto, UPDATEQUALITYALARM);
				//增加告警确认和告警删除表的修改逻辑
				storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//				storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
			}else {
				String resume = dto.getIsResume();
				if (resume.equals("0")) {				
					info = buildStoreInfo(dto, INSERTQUALITYALARM);
				} else if (resume.equals("1")) {
					info = buildStoreInfo(dto,UPDATEQUALITYALARM);
					//增加告警确认和告警删除表的修改逻辑
					storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//					storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
				}
			}
			dtoList.add(dto);
			storeList.add(info);
			//增加修改站点指标告警状态功能      站点归属区域 表告警信息    20170105
			storeList.add(buildUpdateSiteAlarmStatusStoreInfo(dto));
			sendStore(storeList);
//			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		}
		
		JMSDTO jms = buildJMSDTO(dtoList);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
		return null;
	}
	
	private Object disposeV5Data(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		
		String srcCode = report.getSrcCode();
		MonitorDevice device = getMonitorDevice(srcCode);
		if (device == null) {
			Log.warn("处理异态信息异常，站点编号不存在。CODE=" + srcCode);
			return null;
		}
		
		List<AbnormalEventDTO> dtoList = new ArrayList<AbnormalEventDTO>();
		List<StoreInfo> storeList = new ArrayList<StoreInfo>();
		AbnormalEventDTO dto = null;
		StoreInfo info = null;
		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmHistoryReport alarmReport =  getV5AlarmReport(data,report);
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarm> alarmList = alarmReport.getQualityAlarm();
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarm qualityAlarm : alarmList) {
			dto = buildV5AbnormalEventDTO(qualityAlarm, device);
			//如果alarmId为-1，则需要将该站点该接收机的告警都置为正常
			String alarmId = qualityAlarm.getAlarmID();
			if(alarmId.equals("-1")) {
				info = buildStoreInfo(dto, UPDATEQUALITYALARM);
				//增加告警确认和告警删除表的修改逻辑
				storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//				storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
			}else {
				String resume = dto.getIsResume();
				if (resume.equals("0")) {		
					info = buildStoreInfo(dto, INSERTQUALITYALARM);
				} else if (resume.equals("1")) {
					info = buildStoreInfo(dto,UPDATEQUALITYALARM);
					//增加告警确认和告警删除表的修改逻辑
					storeList.add(buildStoreInfo(dto, UPDATECONFQUALITYALARM));
//		历史表去掉	20161218 		storeList.add(buildStoreInfo(dto, UPDATEDELQUALITYALARM));
				}
			}
			dtoList.add(dto);
			storeList.add(info);
			//增加修改站点指标告警状态功能      站点归属区域 表告警信息    20170105
			storeList.add(buildUpdateSiteAlarmStatusStoreInfo(dto));
			sendStore(storeList);
//			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		}
		
		JMSDTO jms = buildJMSDTO(dtoList);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
		return null;
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryReport getV7AlarmReport(CollectData data,Report report) {
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryReport) report.getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmHistoryReport getV5AlarmReport(CollectData data,Report report) {
		return (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmHistoryReport) report.getReport();
	}

	private com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmHistoryReport getV6AlarmReport(CollectData data,Report report) {
		return (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmHistoryReport) report.getReport();
	}
	
	private JMSDTO buildJMSDTO(List<AbnormalEventDTO> dtoList) {
		JMSDTO jms = new JMSDTO();
		jms.setObj((Serializable) dtoList);
		jms.setTypeId(GBWMsgConstant.C_JMS_ABNORMAL_MSG);
		
		return jms;
	}
	
	private StoreInfo buildStoreInfo(AbnormalEventDTO dto,String type) {
		StoreInfo info = new StoreInfo();
		info.setDataMap(buildDataMap(dto));
		info.setLabel(type);
		return info;
	}
	
	private StoreInfo buildUpdateSiteAlarmStatusStoreInfo(AbnormalEventDTO dto) {
		StoreInfo info = new StoreInfo();
		info.setDataMap(buildStatusDataMap(dto));
		info.setLabel(UPDATESITEQUALITYALARMSTATUS);
		return info;
	}
	
	
	private Map<String, Object> buildStatusDataMap(AbnormalEventDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		int resume = Integer.parseInt(dto.getIsResume());//0：异常，1：异常解除
		map.put("alarm_type", resume);
		map.put("monitor_id", Long.parseLong(dto.getMonitorId()));
		return map;
	}
	
	private void sendStore(List<StoreInfo> list) {
		for(StoreInfo info : list) {
			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		}
	}
	
	
	private Map<String, Object> buildDataMap(AbnormalEventDTO dto) {
		try {
			return NMBeanUtils.getObjectField(dto);
		} catch (NMBeanUtilsException e) {
			return null;
		}
	}
	
	private AbnormalEventDTO buildAbnormalEventDTO(QualityAlarm qualityAlarm, MonitorDevice device) {
		AbnormalEventDTO dto = new AbnormalEventDTO();
		dto.setEventId(qualityAlarm.getAlarmID());
		dto.setMonitorId(device.getMonitor_id()+"");
		dto.setMachineId(getMachineID(device, qualityAlarm.getEquCode()));
		dto.setModelId(getModelID(device, qualityAlarm.getEquCode()));
		dto.setBandId(qualityAlarm.getBand());
		
		String model = qualityAlarm.getMode()+"";
		dto.setIsResume(model);
		if (model.equals("0"))
			dto.setEventTime(qualityAlarm.getCheckDateTime());
		else
			dto.setResumeTime(qualityAlarm.getCheckDateTime());
		dto.setFreq(qualityAlarm.getFreq());
		dto.setEventTypeId(qualityAlarm.getType());
		if(null==qualityAlarm.getDesc() || qualityAlarm.getDesc()==""){
			dto.setDescription(qualityAlarm.getReason());
		}else{
			dto.setDescription(qualityAlarm.getDesc());
		}
		dto.setEventReason(qualityAlarm.getReason());
		dto.setReportTypeId("0");
		
		List<AlarmParam> list =qualityAlarm.getAlarmParam();
		for(AlarmParam param : list) {
			String name = param.getName();
			String value = param.getValue();
			if (name.equals("Level"))
				dto.seteLevel(value);
			else if (name.equals("FM-Modulation"))
				dto.setFmModulation(value);
			else if (name.equals("AM-Modulation"))
				dto.setAmModulation(value);
			else if (name.equals("Attenuation"))
				dto.setAttenuation(value);
		}
		
		return dto;
	}
	
	private AbnormalEventDTO buildV7AbnormalEventDTO(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarm qualityAlarm, MonitorDevice device) {
		AbnormalEventDTO dto = new AbnormalEventDTO();
		dto.setEventId(qualityAlarm.getAlarmID());
		dto.setMonitorId(device.getMonitor_id()+"");
		dto.setMachineId(getMachineID(device, qualityAlarm.getEquCode()));
		dto.setModelId(getModelID(device, qualityAlarm.getEquCode()));
		dto.setBandId(qualityAlarm.getBand());
		
		String model = qualityAlarm.getMode()+"";
		dto.setIsResume(model);
		if (model.equals("0"))
			dto.setEventTime(qualityAlarm.getCheckDateTime());
		else
			dto.setResumeTime(qualityAlarm.getCheckDateTime());
		dto.setFreq(qualityAlarm.getFreq());
		dto.setEventTypeId(qualityAlarm.getType());
		if(null==qualityAlarm.getDesc() || qualityAlarm.getDesc()==""){
			dto.setDescription(qualityAlarm.getReason());
		}else{
			dto.setDescription(qualityAlarm.getDesc());
		}
		dto.setEventReason(qualityAlarm.getReason());
		dto.setReportTypeId("0");
		
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.AlarmParam> list =qualityAlarm.getAlarmParam();
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.AlarmParam param : list) {
			String name = param.getName();
			String value = param.getValue();
			if (name.equals("Level"))
				dto.seteLevel(value);
			else if (name.equals("FM-Modulation"))
				dto.setFmModulation(value);
			else if (name.equals("AM-Modulation"))
				dto.setAmModulation(value);
			else if (name.equals("Attenuation"))
				dto.setAttenuation(value);
		}
		
		return dto;
	}
	
	private AbnormalEventDTO buildV6AbnormalEventDTO(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarm qualityAlarm, MonitorDevice device) {
		AbnormalEventDTO dto = new AbnormalEventDTO();
		dto.setEventId(qualityAlarm.getAlarmID());
		dto.setMonitorId(device.getMonitor_id()+"");
		dto.setMachineId(getMachineID(device, qualityAlarm.getEquCode()));
		dto.setModelId(getModelID(device, qualityAlarm.getEquCode()));
		dto.setBandId(qualityAlarm.getBand());
		
		String model = qualityAlarm.getMode()+"";
		dto.setIsResume(model);
		if (model.equals("0"))
			dto.setEventTime(qualityAlarm.getCheckDateTime());
		else
			dto.setResumeTime(qualityAlarm.getCheckDateTime());
		dto.setFreq(qualityAlarm.getFreq());
		dto.setEventTypeId(qualityAlarm.getType());
		if(null==qualityAlarm.getDesc() || qualityAlarm.getDesc()==""){
			dto.setDescription(qualityAlarm.getReason());
		}else{
			dto.setDescription(qualityAlarm.getDesc());
		}
		dto.setEventReason(qualityAlarm.getReason());
		dto.setReportTypeId("0");
		
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.AlarmParam> list =qualityAlarm.getAlarmParam();
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.AlarmParam param : list) {
			String name = param.getName();
			String value = param.getValue();
			if (name.equals("Level"))
				dto.seteLevel(value);
			else if (name.equals("FM-Modulation"))
				dto.setFmModulation(value);
			else if (name.equals("AM-Modulation"))
				dto.setAmModulation(value);
			else if (name.equals("Attenuation"))
				dto.setAttenuation(value);
		}
		
		return dto;
	}
	
	private AbnormalEventDTO buildV5AbnormalEventDTO(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarm qualityAlarm, MonitorDevice device) {
		AbnormalEventDTO dto = new AbnormalEventDTO();
		dto.setEventId(qualityAlarm.getAlarmID());
		dto.setMonitorId(device.getMonitor_id()+"");
		dto.setMachineId(getMachineID(device, qualityAlarm.getEquCode()));
		dto.setModelId(getModelID(device, qualityAlarm.getEquCode()));
		dto.setBandId(qualityAlarm.getBand());
		
		String model = qualityAlarm.getMode()+"";
		dto.setIsResume(model);
		if (model.equals("0"))
			dto.setEventTime(qualityAlarm.getCheckDateTime());
		else
			dto.setResumeTime(qualityAlarm.getCheckDateTime());
		dto.setFreq(qualityAlarm.getFreq());
		dto.setEventTypeId(qualityAlarm.getType());
		if(null==qualityAlarm.getDesc() || qualityAlarm.getDesc()==""){
			dto.setDescription(qualityAlarm.getReason());
		}else{
			dto.setDescription(qualityAlarm.getDesc());
		}
		dto.setEventReason(qualityAlarm.getReason());
		dto.setReportTypeId("0");
		
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.AlarmParam> list =qualityAlarm.getAlarmParam();
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.AlarmParam param : list) {
			String name = param.getName();
			String value = param.getValue();
			if (name.equals("Level"))
				dto.seteLevel(value);
			else if (name.equals("FM-Modulation"))
				dto.setFmModulation(value);
			else if (name.equals("AM-Modulation"))
				dto.setAmModulation(value);
			else if (name.equals("Attenuation"))
				dto.setAttenuation(value);
		}
		
		return dto;
	}
	
	private String getModelID(MonitorDevice device, String code) {
		for(MonitorMachine machine : device.getMachineList()) {
			if (machine.getMachine_code().equals(code))
				return machine.getModel_id();
		}
		
		return null;
	}
	
	private String getMachineID(MonitorDevice device, String code) {
		for(MonitorMachine machine : device.getMachineList()) {
			if (machine.getMachine_code().equals(code))
				return machine.getMachine_id()+"";
		}
		
		return null;
	}
	
	private MonitorDevice getMonitorDevice(String srcCode) {
		return CollectTaskModel.getInstance().getMonitorDevice(srcCode);
	}
}
