package com.neusoft.gbw.cp.process.alarm.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.alarm.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.alarm.model.DataDisposeMgr;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.fault.intf.dto.EquAlarmOderDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NMService;

public class AlarmDisposeProcess extends NMService{
	
	private AlarmService service = null;
	
	public AlarmDisposeProcess(AlarmService service) {
		this.service = service;
	}

	@Override
	public void run() {
		EquAlarm alarm = null;
		while(isThreadRunning()) {
			try {
				alarm = service.take();
			} catch (InterruptedException e) {
				Log.error(this.getClass().getName()+"队列存储报错", e);
				break;
			}
			//告警分类存储，告警：0   和   告警恢复：1                     状态 0：原发 1：确认 2：确认取消
			int alarmState = alarm.getAlarmState();
			switch(alarmState) {
			case 0:
				storeAlarm(alarm);
				break;
			case 1:
				storeHAlarm(alarm);
				break;
			}
			
			alarm = null;
		}
	}
	
	private void storeAlarm(EquAlarm alarm) {
		//（1）告警，如果是告警，加入告警信息表
		List<EquAlarm> alarmList = getAlarmList(alarm);
//		if(alarmList != null && alarmList.size() > 0) 
//			return;
		if(null==alarmList || alarmList.size()<1){
			storeInfo(alarm, ProcessConstants.INSERT_EQU_ALARM_RECORDS);
			buildUpdateSiteAlarmStatusStoreInfo(alarm);
			EquAlarmOderDTO  dto = converDTO(alarm);
			sendWeb(dto);
			alarmList.clear();
			alarmList = null;
		}else{
			if(alarmList.get(0).getAlarmState()==0){
				return;
			}else{
				long timeLong = getAlarmDuration(alarm.getAlarmBeginTime(),alarmList.get(0).getAlarmBeginTime());
				if(timeLong<180){
					return;
				}else{
					storeInfo(alarm, ProcessConstants.INSERT_EQU_ALARM_RECORDS);
					buildUpdateSiteAlarmStatusStoreInfo(alarm);
					EquAlarmOderDTO  dto = converDTO(alarm);
					sendWeb(dto);
					alarmList.clear();
					alarmList = null;
				}
			}
		}
	}
	
	private void storeHAlarm(EquAlarm reAlarm) {
		//（2）如果是恢复，则查看告警活动表中是否存在告警，存在，删除告警活动中数据，并将告警插入历史表,记录告警历时
		//    如果不存在，则不对告警进行操作
		List<EquAlarm> alarmList = getAlarmList(reAlarm);
		if(alarmList == null || alarmList.size() == 0) {
			return;
		}else{
			if(alarmList.get(0).getAlarmState()==0){
				disposeHisAlarm(alarmList, reAlarm);
				EquAlarmOderDTO  dto = converDTO(reAlarm);
				sendWeb(dto);
				alarmList.clear();
				alarmList = null;
			}else{
				return;
			}
		}
	}
	
	private List<EquAlarm> getAlarmList(EquAlarm alarm) {
		Map<String, Long> param = new HashMap<String, Long>();
		param.put("monitor_id", alarm.getMonitorId());
		param.put("alarm_type_id", (long)alarm.getAlarmTypeId());
		return DataDisposeMgr.getInstance().getAlarmList(param);
	}
	
	private void  disposeHisAlarm(List<EquAlarm> alarmList, EquAlarm reAlarm) {
		for(EquAlarm alarm : alarmList) {
			//计算告警历时
			alarm.setAlarmState(reAlarm.getAlarmState());
			alarm.setAlarmEndTime(reAlarm.getAlarmEndTime());
			alarm.setAlarmPeriodTime(getAlarmDuration(alarm.getAlarmBeginTime(), reAlarm.getAlarmEndTime()) + "");
			// 早期需求 PG 用
			/*
			 * alarm.setTerminationOper("system");
			 * alarm.setTerminationOpinion("告警自动恢复");
			 * alarm.setTerminationTime(reAlarm.getAlarmEndTime());
			 */
			//
			//不需要 了
			//alarm.setRenewOper("system");
			//alarm.setRenewOpinion("告警自动恢复");
			//alarm.setRenewTime(reAlarm.getAlarmEndTime());

			//storeInfo(alarm, ProcessConstants.DELETE_EQU_ALARM_RECORDS);
			//storeInfo(alarm, ProcessConstants.INSERT_EQU_ALARM_HIS_RECORDS);
			
			// 更改为 直接  直接 update  gbal_equ_alarm_t表中数据 
			storeInfo(alarm,ProcessConstants.UPDATE_EQU_ALARM_RECORDS);
			//  增加修改站点指标告警状态功能      站点归属区域 表告警信息    20170105
			buildUpdateSiteAlarmStatusStoreInfo(alarm);
		}
	}
	
	private long getAlarmDuration(String startTime, String endTime) {
		long start;
		long end;
		try {
			start = getTimeToMilliSecond(startTime);
			end = getTimeToMilliSecond(endTime);
		} catch (Exception e) {
			return 0;
		}
		return Math.abs(end - start) / 1000;
	}
	
	private long getTimeToMilliSecond(String time) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(time).getTime();
	}
	
	private void storeInfo(EquAlarm alarm, String label) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> map = null;
		try {
			map = NMBeanUtils.getObjectField(alarm);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	
	private EquAlarmOderDTO converDTO(EquAlarm alarm) {
		EquAlarmOderDTO dto = new EquAlarmOderDTO();
		Map<String, Object> dataMap = null;
		try {
			dataMap = NMBeanUtils.getObjectField(alarm);
			NMBeanUtils.fillObjectAttrsO(dto, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		return dto;
	}
	
	private void sendWeb(EquAlarmOderDTO dto) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(GBWMsgConstant.C_JMS_EQU_ALARM_MSG);
		jms.setObj(dto);
		ARSFToolkit.sendEvent(EventServiceTopic.JMS_SEND_MSG_TOPIC, jms);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	private void buildUpdateSiteAlarmStatusStoreInfo(EquAlarm dto) {
		StoreInfo info = new StoreInfo();
		info.setDataMap(buildStatusDataMap(dto));
		info.setLabel(ProcessConstants.UPDATESITEQUALARMSTATUS);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	private Map<String, Object> buildStatusDataMap(EquAlarm dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		int resume = dto.getAlarmState();//告警：0   和   告警恢复：1
		map.put("alarm_type", resume);
		map.put("monitor_id", dto.getMonitorId());
		return map;
	}

}
