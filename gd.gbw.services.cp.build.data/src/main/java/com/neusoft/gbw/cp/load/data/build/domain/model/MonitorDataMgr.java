package com.neusoft.gbw.cp.load.data.build.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runners.ParentRunner;

import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.site.MonitorMachine;
import com.neusoft.gbw.cp.load.data.build.domain.dao.SqlDataAccess;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.MonMachineTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.MonitorInfo;
import com.neusoft.gbw.cp.load.data.build.infrastructure.constants.BuildConstants;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class MonitorDataMgr {
	
	private SqlDataAccess sqlAccess = null;
	
	protected MonitorDataMgr(SqlDataAccess sqlAccess) {
		this.sqlAccess = sqlAccess;
	}
	
	private void init() {
		if (deviceMap != null) {
			deviceMap.clear();
			deviceMap = null;
		}
		
		if (machineMap != null) {
			machineMap.clear();
			machineMap = null;
		}
		
		if (deviceGroupMap != null) {
			deviceGroupMap.clear();
			deviceGroupMap = null;
		}
		
		if(monPartformMap !=null){
			monPartformMap.clear();
			monPartformMap=null;
		}
		deviceMap = new HashMap<Long, MonitorDevice>();
		machineMap = new HashMap<Long, List<MonitorMachine>>();
		deviceGroupMap = new HashMap<String, String>();
		monPartformMap = new HashMap<String, String>();
	}
	
	private void initMonGroup() {
		if (deviceGroupMap != null) {
			deviceGroupMap.clear();
			deviceGroupMap = null;
		}
		if(monPartformMap !=null){
			monPartformMap.clear();
			monPartformMap=null;
		}
		monPartformMap = new HashMap<String, String>();
		deviceGroupMap = new HashMap<String, String>();
	}

	//key:monitor_id, value:MonitorDevice
	private Map<Long, MonitorDevice> deviceMap = null;
	private Map<Long, List<MonitorMachine>> machineMap = null;
	private Map<String, String> deviceGroupMap = null;
	private Map<String, String> monPartformMap = null;    //站点和采集平台关系表
	
	protected void loader() {
		init();
		loadMonMachine();
		loadMonDevice();
		loadRealMeasureSite();
		loadMonPartForm();
	}
	
	protected void loaderMonGroup() {
		initMonGroup();
		loadRealMeasureSite();
		loadMonPartForm();
	}

	private void loadMonDevice() {
		List<MonitorInfo> measureDeviceList = sqlAccess.selectMonitorInfoList();  //查询站点配置
		List<MonitorMachine> list = null;
		MonitorDevice device = null;
		String firstEnCode =null;
		for(MonitorInfo info : measureDeviceList) {
			
			//int monitorID = info.getMonitor_id();
			//oracle 使用 
			long monitorID=info.getMonitor_id();
			
			device = newMonitorDevice(info);
			if (machineMap.containsKey(monitorID)) {
				list = machineMap.get(monitorID);
			} else {
				list = new ArrayList<MonitorMachine>();
			}
			for(MonitorMachine l: list){
				if(l.getIs_default()==1){
					firstEnCode = l.getMachine_code();
					break;
				}else{
					firstEnCode=list.get(0).getMachine_code();
				}
			}
			device.setMachineList(list);
			device.setFirstEnCode(firstEnCode);
			deviceMap.put(monitorID, device);
		}
	}
	/**
	 * 查询站点和采集平台分配关系  lyc  20170118
	 */
	private void loadMonPartForm() {
 		List<Map<String, Object>> monPartformList = sqlAccess.selectMonPartFormList();
		
		for(Map<String, Object> map : monPartformList) {
			String monitorId = map.get("MONITOR_ID").toString();
			String machineIp = map.get("MACHINE_IP").toString();
			monPartformMap.put(monitorId, machineIp);
		}
	}
	private void loadMonMachine() {
		List<MonMachineTable> monMachineList = sqlAccess.selectMonMachineList();
		List<MonitorMachine> list = null;
		for(MonMachineTable table : monMachineList) {
			//int monitorID = table.getMonitor_id();
			long monitorID=table.getMonitor_id();
			//long monitorID=Long.valueOf(table.getMonitor_id());
			if (machineMap.containsKey(monitorID)) {
				list = machineMap.get(monitorID);
			} else {
				list = new ArrayList<MonitorMachine>();
			}
			list.add(newMonitorMachine(table));
			//machineMap.put(monitorID, list);//add by jiahao
			machineMap.put(monitorID, list);
		}
	}
	
	private void loadRealMeasureSite() {
		String key = null;
		List<Map<String, Object>>  siteList = sqlAccess.selectRealMeasureSiteList();
		
		for(Map<String, Object> map : siteList) {
			//PG  用
		//	String monitorId = map.get("monitor_id").toString();
		//	String runplanType = map.get("runplan_type_id").toString();
		//	String monitorStatus = map.get("monitor_status").toString();
			//oracle 用 
			String monitorId = map.get("MONITOR_ID").toString();
			String runplanType = map.get("RUNPLAN_TYPE_ID").toString();
			String monitorStatus = map.get("MONITOR_STATUS").toString();
			key = monitorId + "_" + runplanType;
			deviceGroupMap.put(key, monitorStatus);
		}
	}
	
	protected MonitorDevice getMonitorDevice(long monitorID) {
		if (!deviceMap.containsKey(monitorID))
			return null;
		return this.deviceMap.get(monitorID);
	}
	
	protected Collection<MonitorDevice> getMonitorDeviceList() {
		return this.deviceMap.values();
	}
	
	protected Map<String, String> getMonitorGoupMap() {
		return this.deviceGroupMap;
	}
	
	protected Map<String,String> getMonPartformMap(){
		return this.monPartformMap;
	}
	
	private MonitorMachine newMonitorMachine(MonMachineTable table) {
		MonitorMachine device = new MonitorMachine();
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(table);
			NMBeanUtils.fillObjectAttrsO(device, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}
		return device;
	}
	
	private MonitorDevice newMonitorDevice(MonitorInfo info) {
		MonitorDevice device = new MonitorDevice();
		String protocol = info.getProtocol();
		//if (protocol.equals(BuildConstants.PROTOCOL_SERVLET))
		if (BuildConstants.PROTOCOL_SERVLET.equals(protocol))	
		info.setPort("0");
		
		try {
			Map<String, Object> dataMap = NMBeanUtils.getObjectField(info);
			NMBeanUtils.fillObjectAttrsO(device, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
			return null;
		}
		return device;
	}
}
