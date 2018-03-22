package com.neusoft.gbw.cp.build.domain.build.equ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AlarmParam;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmParam;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmParamSet;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Param;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MonitorConfDTO;

public class EquAlarmParamSetV6Builder {
	
	private final static String SEP = "#";

	public static Object buildEquAlarmParamSet(MoniSetingDTO dto, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		long monitorID = Long.parseLong(dto.getMonitorId());
		EquipmentAlarmParamSet paramSet = new EquipmentAlarmParamSet();
		query.setQuery(paramSet);
		query.setVersion(BuildConstants.XML_VERSION_6 + "");
		query.setMsgID(DataUtils.getMsgID(paramSet)+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
	
//		Map<String, String>  confMap = converMonitorConf(dto.getConfList());
		Map<String, List<Param>> confMap = converlistMonitorConf(dto.getConfList());
		
		EquipmentAlarmParam alarmParam = new EquipmentAlarmParam();
		alarmParam.addAlarmParam(getAlarmParam(BuildConstants.EquAlarmParamSetTask.ELECTRICITY_EXCEPTION_TYPE, 
				BuildConstants.EquAlarmParamSetTask.ELECTRICITY_EXCEPTION_DESC, confMap));
		alarmParam.addAlarmParam(getAlarmParam(BuildConstants.EquAlarmParamSetTask.RECEIVER_EXCEPTION_TYPE, 
				BuildConstants.EquAlarmParamSetTask.RECEIVER_EXCEPTION_DESC, confMap));
		alarmParam.addAlarmParam(getAlarmParam(BuildConstants.EquAlarmParamSetTask.AM_MODULATION_EXCEPTION_TYPE, 
				BuildConstants.EquAlarmParamSetTask.AM_MODULATION_EXCEPTION_DESC, confMap));
		alarmParam.addAlarmParam(getAlarmParam(BuildConstants.EquAlarmParamSetTask.FM_MODULATION_EXCEPTION_TYPE, 
				BuildConstants.EquAlarmParamSetTask.FM_MODULATION_EXCEPTION_DESC, confMap));
		alarmParam.addAlarmParam(getAlarmParam(BuildConstants.EquAlarmParamSetTask.VOICE_EXCEPTION_TYPE, 
				BuildConstants.EquAlarmParamSetTask.VOICE_EXCEPTION_DESC, confMap));
		alarmParam.addAlarmParam(getAlarmParam(BuildConstants.EquAlarmParamSetTask.FREQ_DEV_EXCEPTION_TYPE, 
				BuildConstants.EquAlarmParamSetTask.FREQ_DEV_EXCEPTION_DESC, confMap));
		paramSet.addEquipmentAlarmParam(alarmParam);
		
		return query;
	}
	

	
	private static AlarmParam getAlarmParam(String type, String desc ,Map<String, List<Param>> confMap) {
		AlarmParam param = new AlarmParam();
		param.setDesc(desc);
		param.setType(type);
		param.setParams(confMap.get(type));
		return param;
}
	
	private static Map<String, List<Param>> converlistMonitorConf(List<MonitorConfDTO> list) {
		Map<String, List<Param>> confMap = new HashMap<String, List<Param>>();
		List<Param> paramlist = null;
		String type = null;
		Param para = null;
		for(MonitorConfDTO conf : list) {
			String[] array = conf.getConfCode().split(SEP);
			type = array[0];
			String name = array[1];
			String value = conf.getConfValue();
			if (confMap.containsKey(type)) {
				paramlist = confMap.get(type);
			} else {
				paramlist = new ArrayList<Param>();
			}
			if(name.equals("InputLineLevelUpThreshold")) continue;
			para = getParam(name, value);
			paramlist.add(para);
			confMap.put(type, paramlist);
		}
		return confMap;
	}
	
	private static Param getParam(String name, String value) {
		Param param = new Param();
		param.setName(name);
		param.setValue(value);
		return param;
	}
	
//	private static AlarmParam getAlarmParam(String type, String desc ,Map<String, String>  confMap ) {
//	AlarmParam param = new AlarmParam();
//	param.setDesc(desc);
//	param.setType(type);
//	param.setParams(getParamsList(type,confMap));
//	return param;
//}
	
//	private static List<Param> getParamsList(String type, Map<String, String>  confMap) {
//		List<Param> paramList = new ArrayList<Param>();
//		
//		paramList.add(getParam(BuildConstants.EquAlarmParamSetTask.ABNORMITYLENGTH,confMap));
//		if(type.equals(BuildConstants.EquAlarmParamSetTask.ELECTRICITY_EXCEPTION_TYPE)) {
//			paramList.add(getParam(BuildConstants.EquAlarmParamSetTask.INPUTLINELEVELUPTHRESHOLD,confMap));
//			paramList.add(getParam(BuildConstants.EquAlarmParamSetTask.INPUTLINELEVELDOWNTHRESHOLD,confMap));
//		}
//		return paramList;
//	}
	
//	private static Param getParam(String key, Map<String, String>  confMap) {
//		Param param = new Param();
//		param.setName(key);
//		param.setValue(confMap.get(key));
//		return param;
//		
//	}
	
//	private static Map<String, String> converMonitorConf(List<MonitorConfDTO> list) {
//		Map<String, String> confMap = new HashMap<String, String>();
//		for(MonitorConfDTO conf : list) {
//			String key = conf.getConfCode();
//			String value = conf.getConfValue();
//			if (confMap.containsKey(key)) {
//				continue;
//			}
//			confMap.put(key, value);
//		}
//		return confMap;
//	}
	
}
