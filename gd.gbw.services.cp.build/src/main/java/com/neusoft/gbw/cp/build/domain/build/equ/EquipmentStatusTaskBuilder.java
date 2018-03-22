package com.neusoft.gbw.cp.build.domain.build.equ;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealtimeQuery;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquipmentStatusTaskBuilder {
//	private static final String reportInterval = "00:00:03";
//	private static final String sampleInterval = "00:00:03";
//	private static final String expirTime = "00:00:05";

	public static Query buildRealTimeStreamEquipmentStatusQuery(Object obj, TaskPriority taskPriority) throws BuildException {
		String monitorId = "";
		String  action="start";
		String  reportInterval="";
		String  sampleInterval="";
		String  expireTime="";
		if(obj instanceof MoniDeviceStatusDTO) {
			MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO) obj;
			monitorId = statusDto.getMonitorId();
			expireTime=statusDto.getExpireTime();
			reportInterval=statusDto.getReportInterval();
			sampleInterval=statusDto.getSampleInterval();
			action = statusDto.getAction();
		}
		if(obj instanceof MoniInspectResultDTO) {
			MoniInspectResultDTO statusDto = (MoniInspectResultDTO) obj;
			monitorId = statusDto.getMonitorId();
		}
		
		Query query = new Query();
		EquipmentStatusRealtimeQuery quert = buildEquipmentStreamQuery(expireTime,reportInterval,sampleInterval,action);
		query.setVersion(BuildConstants.XML_VERSION_8 + "");
		Long msgID = DataUtils.getMsgID(quert);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(quert);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			Log.debug("[任务构建]设备状态任务获取 monitorCode=" + info.getMonitor_code());
			query.setDstCode(info.getMonitor_code());
		}
		return query;
	}
	
	private static EquipmentStatusRealtimeQuery buildEquipmentStreamQuery(String expireTime,String reportInterval,
			String sampleInterval,String action) {
		EquipmentStatusRealtimeQuery quert = new EquipmentStatusRealtimeQuery();
		quert.setSampleInterval((sampleInterval==null || sampleInterval.equals(""))?"00:00:03":timeSecondChange(Long.valueOf(sampleInterval)));
		quert.setReportInterval((reportInterval==null || reportInterval.equals(""))?"00:00:03":timeSecondChange(Long.valueOf(reportInterval)));
		if(action.equals("start")){
			quert.setExpireTime((expireTime==null || expireTime.equals(""))?"00:00:03":timeMinuteChange(Long.valueOf(expireTime)));
		}else if(action.equals("end")){
			quert.setExpireTime("00:00:00");
		}
//		quert.setSampleInterval((reportInterval==null || reportInterval.equals(""))?"00:00:03":timeSecondChange(Long.valueOf(reportInterval)));
//		quert.setExpireTime((sampleInterval==null || sampleInterval.equals(""))?"00:00:03":timeSecondChange(Long.valueOf(sampleInterval)));
//		if(action.equals("start")){
//			quert.setReportInterval((expireTime==null || expireTime.equals(""))?"00:00:03":timeMinuteChange(Long.valueOf(expireTime)));
//		}else if(action.equals("end")){
//			quert.setReportInterval("00:00:00");
//		}
		return quert;
	}
	public static String completeTime(long time){
		String a =String.valueOf(time);
		if(a.length()<2){
			return "0"+a;
		}
		return a;
	}
	public static String timeSecondChange(long time){
		if(time<60){
			return "00:00:"+completeTime(time);
		}else if(time>=60 && time<600){
			return "00:"+completeTime(time/60)+":"+completeTime(time%60);
		}
			return "00:"+completeTime(time/60)+":"+completeTime(time%60);
	}
	public static String timeMinuteChange(long time){
		if(time<60){
			return "00:"+completeTime(time)+":00";
		}else if(time>=60 && time<600){
			return completeTime(time/60)+":"+completeTime(time%60)+":00";
		}
			return completeTime(time/60)+":"+completeTime(time%60)+":00";
	}
}
