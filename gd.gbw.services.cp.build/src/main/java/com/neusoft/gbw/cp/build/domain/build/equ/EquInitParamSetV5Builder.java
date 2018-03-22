package com.neusoft.gbw.cp.build.domain.build.equ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v5.protocol.vo.other.Center;
import com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentInitParamSet;
import com.neusoft.gbw.cp.conver.v5.protocol.vo.other.InitEquipment;
import com.neusoft.gbw.cp.conver.v5.protocol.vo.other.LogInfo;
import com.neusoft.gbw.cp.conver.v5.protocol.vo.other.Param;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniDeviceDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniReportDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MonitorConfDTO;
import com.neusoft.gbw.infrastructure.sys.intf.dto.ServerDTO;
import com.neusoft.gbw.infrastructure.sys.intf.dto.SwitchDataDTO;

public class EquInitParamSetV5Builder {
	
	private final static String SEP = "#";

	public static Object buildEquInitParamSet(MoniSetingDTO dto, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		long monitorID = Long.parseLong(dto.getMonitorId());
		EquipmentInitParamSet paramSet = new EquipmentInitParamSet();
		query.setQuery(paramSet);
		query.setVersion(BuildConstants.XML_VERSION_5+"");
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
		List<InitEquipment> initList = new ArrayList<InitEquipment>();
		initList.add(getSystemInit(dto));
		initList.addAll(getOtherInit(dto));
		
		paramSet.setInitEquipments(initList);
		return query;
	}
	
	private static InitEquipment getSystemInit(MoniSetingDTO dto) {
		InitEquipment equipment  = new InitEquipment();
		equipment.setType("System");
		equipment.setCenters(getCenterList(dto));
		equipment.setLogInfos(getLogList(dto.getSwitchList()));
		equipment.setParams(getParamsList(dto.getConfList()));
		return equipment;
	}
	
	private static List<InitEquipment> getOtherInit(MoniSetingDTO dto) {
		List<InitEquipment> list = new ArrayList<InitEquipment>();
		List<MoniDeviceDTO> monList = dto.getDeviceList();
		for(MoniDeviceDTO moidto : monList) {
			InitEquipment equipment  = new InitEquipment();
			equipment.setType(moidto.getModelName());
			equipment.setEquCode(moidto.getMachineName());
			list.add(equipment);
		}
		return list;
	}
	
	private static List<Center> getCenterList(MoniSetingDTO monidto) {
		List<Center> centerList = new ArrayList<Center>();
		Map<String, Map<String, String>> ftpMap = getServerDTO(monidto.getServerList());
		Center center = null;
		for(MoniReportDTO dto : monidto.getReportList()) {
			center = new Center();
//			center.setType(dto.getSystemTypId());
			center.setSrcCode(dto.getSubscribeCode());
			center.setParams(getCenterParamsList(dto,ftpMap.get(dto.getServerId())));
			centerList.add(center);
		}
		return centerList;
	}
	
	private static List<Param> getCenterParamsList(MoniReportDTO dto,Map<String, String> ftpMap) {
		List<Param> paramList = new ArrayList<Param>();
		
		
		paramList.add(getParam("FTP",ftpMap.get("FTP")));
		paramList.add(getParam("FTPPassword",ftpMap.get("FTPPassword")));
		paramList.add(getParam("FTPUser",ftpMap.get("FTPUser")));
		paramList.add(getParam("UpURL",dto.getSubscribeUrl()));
		paramList.add(getParam("FTPPort",ftpMap.get("FTPPort")));
		paramList.add(getParam("FTPPath",ftpMap.get("FTPPATH")));
		return paramList;
	}
	
	private static Map<String, Map<String, String>> getServerDTO(List<ServerDTO> serverList) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		Map<String, String> ftpMap = null;
		for(ServerDTO dto : serverList) {
			String serverId = dto.getServerId();
			if(map.containsKey(serverId)) {
				continue;
			}
			ftpMap = new HashMap<String, String>();
			ftpMap.put("FTPUser", dto.getUserName());
			ftpMap.put("FTPPassword", dto.getPasswd());
			ftpMap.put("FTP", dto.getUploadIp());
			ftpMap.put("FTPPort", dto.getUploadPort());
			ftpMap.put("FTPPATH", dto.getUploadUrl() == null ? "upload" : dto.getUploadUrl());
			map.put(serverId, ftpMap);
		}
		return map;
	}
	
	private static List<Param> getParamsList(List<MonitorConfDTO> confList) {
		List<Param> paramListPrepare = new ArrayList<Param>();
		List<Param> paramList = new ArrayList<Param>();
		Param param = null;
		for(MonitorConfDTO dto : confList) {
			String code = dto.getConfCode();
			if (!code.equals("TimeServer") && !code.equals("BatteryLevelDownThreshold") && 
					!code.equals("ShutdownDelayTime") && !code.endsWith("CheckUPS") &&
					!code.equals("CompressSize") && !code.equals("OffLineTime")
					&& !code.equals("LogExpireDays"))
				continue;
			param = getParam(dto.getConfCode(), dto.getConfValue());
			if(code.equals("OffLineTime"))
				param = getParam(dto.getConfCode(), getReportInterval(dto.getConfValue()==null?"00:30:00":dto.getConfValue()));
			
			paramListPrepare.add(param);
		}
		sequenceParamsList(paramList,paramListPrepare,"OffLineTime");
		sequenceParamsList(paramList,paramListPrepare,"LogExpireDays");
		sequenceParamsList(paramList,paramListPrepare,"CompressSize");
		sequenceParamsList(paramList,paramListPrepare,"ShutdownDelayTime");
		sequenceParamsList(paramList,paramListPrepare,"CheckUPS");
		sequenceParamsList(paramList,paramListPrepare,"TimeServer");
		sequenceParamsList(paramList,paramListPrepare,"BatteryLevelDownThreshold");
		return paramList;
	}
	private static List<Param> sequenceParamsList(List<Param> paramList,List<Param> paramListPrepare,String name){
		for(Param pl: paramListPrepare){
			if(pl.getName().equals(name)){
				paramList.add(pl);
			}
		}
		return paramList;
	}

	private static List<LogInfo> getLogList(List<SwitchDataDTO> switchListO) {
		List<LogInfo> logList = new ArrayList<LogInfo>();
		List<Param> paramList = null;
		Map<String, List<Param>> paramMap = new HashMap<String, List<Param>>();
		Param param = null;
		String type = null;
		for(SwitchDataDTO dto : switchListO) {
			String switchType = dto.getSwitchType();
			if (switchType == null)
				continue;
			
			String[] array = switchType.split(SEP);
			type = array[0];
			param = getParam(array[1], dto.getSwitchValue());
			
			if (paramMap.containsKey(type)) {
				paramList = paramMap.get(type);
			} else {
				paramList = new ArrayList<Param>();
			}
			paramList.add(param);
			paramMap.put(type, paramList);
		}
		
		LogInfo info = null;
		for(String logType : paramMap.keySet()) {
			info = getLogInfo(logType, paramMap.get(logType));
			logList.add(info);
		}
		List<LogInfo> logListafter = new ArrayList<LogInfo>();
		for(LogInfo li: logList){
			LogInfo infoAfter = new LogInfo();
			List<Param> paramListafter = new ArrayList<Param>();
			if(li.getType().equals("Main") || li.getType().equals("Slave")){
				List<Param> paramListbefore = li.getParams();
				sequenceParamsList(paramListafter,paramListbefore,"LogPassword");
				sequenceParamsList(paramListafter,paramListbefore,"Phone");
				sequenceParamsList(paramListafter,paramListbefore,"LogName");
			}else{
				paramListafter = li.getParams();
			}
			infoAfter.setType(li.getType());
			infoAfter.setParams(paramListafter);
			logListafter.add(infoAfter);
		}
		return logListafter;
	}
	
	private static LogInfo getLogInfo(String type ,List<Param> list) {
		LogInfo info = new LogInfo();
		info.setType(type);
		info.setParams(list);
		return info;
	}
	
	private static Param getParam(String name, String value) {
		Param param = new Param();
		param.setName(name);
		param.setValue(value);
		return param;
	}
	
  	private static String getReportInterval(String in){
		int interval = Integer.parseInt(in);
		String time = "" ;
		if (interval<60) {
			time ="00:00:"+unitFormat(interval);
		}else {
			int minute = interval/60;
			int second = interval%60;
			time = "00"+":"+unitFormat(minute)+":"+unitFormat(second);
		}
		return time;
	}
  	
  	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
	
//	public static void main(String[] args) {
//		String time = "00:00:10";
////		System.out.println(converterToTime(time));
//	}
}
