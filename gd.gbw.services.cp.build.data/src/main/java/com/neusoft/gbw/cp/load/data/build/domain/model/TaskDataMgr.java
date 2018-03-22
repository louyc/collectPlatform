package com.neusoft.gbw.cp.load.data.build.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.load.data.build.domain.dao.SqlDataAccess;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.FtpServerInfo;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.MeasureUnitTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.QualityTypeTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.RunplanView;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskConfTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskFreqTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskMonitorTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskScheduleTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskTable;
import com.neusoft.gbw.cp.load.data.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;


public class TaskDataMgr {

	private SqlDataAccess sqlAccess = null;
	//key:task_id, value:MeasureTask
	private Map<Integer, TaskTable> measureTaskMap = null;
	
	private List<RealTimeStreamDTO> streamList = null;
	
	private Map<Integer,TaskTable> delTaskMap=null;   //lyc  20160808  三满任务删除
	//key:task_id, value:(key:taskfreq_id, value:TaskFreqTable)
	private Map<Integer, Map<Integer, TaskFreqTable>> taskFreqMap = null;
	//key:task_id, value:Map
	private Map<Integer, Map<String, String>> taskConfMap = null; 
	//key:taskfreq_id, value:List
	private Map<Integer, List<TaskScheduleTable>> taskScheduleMap = null;
	//key:task_id, value:list 多个Monitor_id
	private Map<Integer, List<TaskMonitorTable>> taskMonitorMap = null;
	//key:runplan_id, value:list RunplanView
	private Map<String, RunplanView> taskRunplanMap = null;
	//key:code_name, value: table_name
	private Map<String, String> qualityTypeMap = null;
	//key:ip, vale:user#SEP#password
	private Map<String, String> ftpServerMap = null;
	//key:storeAddressKey,value:storeAddressValue
	private Map<String, String> storeAddrMap = null;
	//key:task_build_mode+task_unit_id,value
	private Map<String, String> unfinishedUnitMap = null;
	//key:task_build_mode+task_unit_id,value
	private Map<String, String> finishedUnitMap = null;
	//key:task_build_mode+task_unit_id,value
	private Map<String, String> unitMap = null;
	//key:task_build_mode,value 
	private Map<Integer, Boolean> unitStatusMap = null;
//	//key:REAL_MEASURE_SITE_IDS,value:monitorIds
//	private Map<String, String> realMeasureSiteMap = null;
//	//key:monitorId,value:runplan_type
//	private Map<String, String> newRealMeasureSiteMap = null;
	//key:runpan_id,value:station_name
	private Map<String, String> realMeasureStationMap = null;
	//key:task_id_monitor_id_timeID_taskType,value:time
	private Map<String, String> recoverTimeMap = null;
	//key:runplan_id, value: station_id#language_id
	private Map<String, String> runplanMap = null;
	//key:station_id, value: priority
	private Map<Long, Long> stationMap = null;
	//key:language_id, value: priority
	private Map<Long, Long> languageMap = null;
	
	
	protected TaskDataMgr(SqlDataAccess sqlAccess) {
		this.sqlAccess = sqlAccess;
	}
	
	protected void init() {
		if (measureTaskMap != null) {
			measureTaskMap.clear();
			measureTaskMap = null;
		}
		if(streamList !=null){
			streamList.clear();
			streamList=null;
		}
		if (delTaskMap != null) {
			delTaskMap.clear();
			delTaskMap = null;
		}
		if (taskFreqMap != null) {
			taskFreqMap.clear();
			taskFreqMap = null;
		}
		if (taskConfMap != null) {
			taskConfMap.clear();
			taskConfMap = null;
		}
		if (taskScheduleMap != null) {
			taskScheduleMap.clear();
			taskScheduleMap = null;
		}
		if (taskMonitorMap != null) {
			taskMonitorMap.clear();
			taskMonitorMap = null;
		}
		if (qualityTypeMap != null) {
			qualityTypeMap.clear();
			qualityTypeMap = null;
		}
		if (ftpServerMap != null) {
			ftpServerMap.clear();
			ftpServerMap = null;
		}
		if (storeAddrMap != null) {
			storeAddrMap.clear();
			storeAddrMap = null;
		}
		if (unfinishedUnitMap != null) {
			unfinishedUnitMap.clear();
			unfinishedUnitMap = null;
		}
		if (finishedUnitMap != null) {
			finishedUnitMap.clear();
			finishedUnitMap = null;
		}
		if (realMeasureStationMap != null) {
			realMeasureStationMap.clear();
			realMeasureStationMap = null;
		}
		if (unitMap != null) {
			unitMap.clear();
			unitMap = null;
		}
		if (stationMap != null) {
			stationMap.clear();
			stationMap = null;
		}
		if (languageMap != null) {
			languageMap.clear();
			languageMap = null;
		}
		if (runplanMap != null) {
			runplanMap.clear();
			runplanMap = null;
		}
		if (taskRunplanMap != null) {
			taskRunplanMap.clear();
			taskRunplanMap = null;
		}
	}
	
	protected void loader() {
		init();
		loadMeasureTask();
		loadTaskFreq();
		loadTaskConf();
		loadTaskSchedule();
		loadTaskMonitor();
		loadQualityType();
		loadFtpServer();
		loadRecordAddrMap();
		loadRunplanToStation();
		loadRecoverTaskData();
		loadMeasureStationMap();
		loadAllUnitMap();
		loadAutoUnitMap();
		loadManualUnitMap();
		loadRunplanToStation();
		loadLanguageMap();
		loadStationMap();
		
		loadStreamTask();  //加载巡检 遥控站任务
	}
	
	private void loadLanguageMap() {
		stationMap = new HashMap<Long, Long>();
		for (Map<String, Object> map : sqlAccess.selectLanguageList()) {
			//PG 用
			//long id = Long.valueOf(map.get("language_id") + "");
			//long priority = (Long)map.get("priority_level");
			//PG转 oracle用
			long id = Long.valueOf(map.get("LANGUAGE_ID") + "");
			//long priority = (Long)map.get("PRIORITY_LEVEL");
			long priority = Long.valueOf(map.get("PRIORITY_LEVEL") + "");
			stationMap.put(id, priority);
		}
	}
	
	private void loadStationMap() {
		languageMap = new HashMap<Long, Long>();
		for (Map<String, Object> map : sqlAccess.selectStationList()) {
			//PG用
			//long id = (Long)map.get("station_id");
			//long priority = (Long)map.get("priority_level");
			//PG转 oracle
			long id = Long.valueOf(map.get("STATION_ID") + "");
			//long priority = (Long)map.get("PRIORITY_LEVEL");
			long priority = Long.valueOf(map.get("PRIORITY_LEVEL") + "");
			languageMap.put(id, priority);
		}
	}
	
	private void loadRunplanToStation() {
		runplanMap = new HashMap<String, String>();
		taskRunplanMap = new HashMap<String, RunplanView>();
		for(RunplanView view : sqlAccess.selectRunplanViewList()) {
			taskRunplanMap.put(view.getRunplan_id(), view);
			runplanMap.put(view.getRunplan_id()+"", view.getStation_id() + "#" + view.getLanguage_id());
		}
		loadRecoverTaskData();
		loadMeasureStationMap();
		loadAllUnitMap();
	}
	
	private void loadFtpServer() {
		ftpServerMap = new HashMap<String, String>();
		for(FtpServerInfo table : sqlAccess.selectFtpServerInfoList()) {
			ftpServerMap.put(table.getFtp_ip(), table.getFtp_account() + "#SEP#" + table.getFtp_password());
		}
	}
	
	private void loadQualityType() {
		qualityTypeMap = new HashMap<String, String>();
		for(QualityTypeTable table : sqlAccess.selectQualityTypeList()) {
			qualityTypeMap.put(table.getQuality_code(), table.getPlatform_store());
		}
	}
	
	private void loadMeasureTask() {
		measureTaskMap = new HashMap<Integer, TaskTable>();
		delTaskMap = new HashMap<Integer, TaskTable>();
		taskFreqMap = new HashMap<Integer, Map<Integer, TaskFreqTable>>();
		taskConfMap = new HashMap<Integer,Map<String, String>>();
		taskMonitorMap = new HashMap<Integer, List<TaskMonitorTable>>();
		
		List<TaskTable> taskList = sqlAccess.selectMeasureTaskList();
		for(TaskTable measureTask : taskList) {
			int taskID = measureTask.getTask_id();
			taskConfMap.put(taskID, new HashMap<String, String>());
			taskFreqMap.put(taskID, new HashMap<Integer, TaskFreqTable>());
			taskMonitorMap.put(taskID, new ArrayList<TaskMonitorTable>());
			if(measureTask.getIn_use()==1){
				measureTaskMap.put(taskID, measureTask);
			}else{
				delTaskMap.put(taskID, measureTask);
			}
		}
	}
	
	private void loadTaskFreq() {
		taskScheduleMap = new HashMap<Integer, List<TaskScheduleTable>>();
		
		List<TaskFreqTable> taskItemList = sqlAccess.selectTaskFreqList();
		for(TaskFreqTable item : taskItemList) {
			int taskID = item.getTask_id();
			int taskFreqID = item.getTaskfreq_id();
			if (!taskFreqMap.containsKey(taskID)) {
				continue;
			}
			Map<Integer, TaskFreqTable> map = taskFreqMap.get(taskID);
			map.put(taskFreqID, item);
			taskScheduleMap.put(taskFreqID, new ArrayList<TaskScheduleTable>());
		}
	}
	
	private void loadTaskConf() {
		List<TaskConfTable> taskConfList = sqlAccess.selectTaskConfList();
		for(TaskConfTable conf : taskConfList) {
			int taskID = conf.getTask_id();
			String key = conf.getConf_code();
			String value = conf.getConf_value();
			if (!taskConfMap.containsKey(taskID)) {
				continue;
			}
			Map<String, String> confMap = taskConfMap.get(taskID);
			confMap.put(key, value);
		}
	}
	
	private void loadTaskSchedule() {
		List<TaskScheduleTable> periodConfList = sqlAccess.selectTaskScheduleList();
		for(TaskScheduleTable taskSchedule : periodConfList) {
			int taskFreqID = taskSchedule.getTaskfreq_id();
			if (!taskScheduleMap.containsKey(taskFreqID)) {
				continue;
			}
			List<TaskScheduleTable> list = taskScheduleMap.get(taskFreqID);
			list.add(taskSchedule);
		}
	}
	
	private void loadTaskMonitor() {
		List<TaskMonitorTable> monitorList = sqlAccess.selectTaskMonitorList();
		for(TaskMonitorTable taskMonitor : monitorList) {
			int taskID = taskMonitor.getTask_id();
			if (!taskMonitorMap.containsKey(taskID)) {
				continue;
			}
			List<TaskMonitorTable> list = taskMonitorMap.get(taskID);
			list.add(taskMonitor);
		}
	}
	
//	private void loadRealMeasureSite() {
//		realMeasureSiteMap = new HashMap<String, String>();
//		newRealMeasureSiteMap = new HashMap<String, String>();
//		StringBuffer realMonitors = new StringBuffer();
////		StringBuffer broadcast = new StringBuffer();
////		StringBuffer expriment = new StringBuffer();
//		List<Map<String, Object>>  siteList = sqlAccess.selectRealMeasureSiteList();
//		for(Map<String, Object> map : siteList) {
//			map.get("runplan_type_id");
//			String runplanType = map.get("runplan_type_id").toString();
//			String monitorId = map.get("monitor_id").toString();
//			realMonitors.append(",").append(monitorId);
////			if(BuildConstants.BROADCAST == runplanType)
////				broadcast.append(",").append(monitorId);
////			if(BuildConstants.EXPRIMENT == runplanType)
////				expriment.append(",").append(monitorId);
//			newRealMeasureSiteMap.put(monitorId, runplanType);
//		}
//		
////		realMeasureSiteMap.put(BuildConstants.BROADCAST, broadcast.length() == 0 ? "" : broadcast.substring(1));
////		realMeasureSiteMap.put(BuildConstants.EXPRIMENT, expriment.length() == 0 ? "" : expriment.substring(1));
//		realMeasureSiteMap.put(BuildConstants.REAL_MEASURE_SITE_IDS, realMonitors.length() == 0 ? "" : realMonitors.substring(1));
//	}
	
	private Map<String, String> loadRecoverTaskData() {
		if(recoverTimeMap != null)
			recoverTimeMap.clear();
		else
			recoverTimeMap = new HashMap<String, String>();
		List<Map<String, Object>>  dataList = sqlAccess.selectRecoverTimeList();
		if(dataList == null || dataList.isEmpty())
			return recoverTimeMap;
		for(Map<String, Object> map : dataList) {
			//PG 用
			//recoverTimeMap.put(map.get("reckey").toString(), map.get("recover_time").toString());
			// pg zhuan oracle
			recoverTimeMap.put(map.get("RECKEY").toString(), map.get("RECOVER_TIME").toString());
		}
		return recoverTimeMap;
	}
	/**
	 * lyc
	 * 20170427
	 * 查询要巡检的遥控站任务
	 */
	private void loadStreamTask(){
		streamList = new ArrayList<RealTimeStreamDTO>();
		List<Map<String, String>> streamMapList = sqlAccess.selectStreamList();
		for(Map<String, String>  map : streamMapList) {
			RealTimeStreamDTO rs= new RealTimeStreamDTO(); 
			String monitorId = String.valueOf(map.get("MONITOR_ID"));
			String freq = String.valueOf(map.get("FREQ"));
			String bps = String.valueOf(map.get("BPS"));
			rs.setMonitorId(monitorId);
			rs.setFreq(freq);
			rs.setTimeOut("30");
			rs.setForce("1");
			rs.setCommand("Start");
			String band ="";
			if(Long.valueOf(freq)<=26100 && Long.valueOf(freq)>=2300){//2300~26100
				band = "0";
			}else if(Long.valueOf(freq)<=1602 && Long.valueOf(freq)>=531){//531~1602
				band = "1";
			}else if(Long.valueOf(freq)<=108000 && Long.valueOf(freq)>=87000){//87000~108000
				band = "2";
			}
			rs.setBps(bps);
			rs.setBand(band);
			streamList.add(rs);
		}
	}
	/**
	 * 查询任务回收情况
	 * @return
	 */
	Map<String, Object> queryRecoverTaskData(String monitorId) {
		Map<String, Object> recoverMap = new HashMap<String, Object>();
		List<Map<String, Object>>  dataList = sqlAccess.selectRecoverDateList(); //质量任务回收
		List<Map<String, Object>>  vadioDataList = sqlAccess.selectMonitorInspectVadioList();//遥控站巡检结果
		if(null != dataList && !dataList.isEmpty()){
			for(Map<String, Object> map : dataList) {
				if(!map.get("MONITOR_ID").toString().equals(monitorId)){
					continue;
				}
				recoverMap.put(map.get("TASK_TYPE").toString(), map.get("RECOVER_TIME").toString()+"&"+map.get("URL").toString());
			}
		}
		if(null!=vadioDataList && !dataList.isEmpty()){
			for(Map<String, Object> map : vadioDataList) {
				if(map.get("MONITOR_ID").toString().equals(monitorId) && null!=map.get("RADIO_URL")){
					recoverMap.put("vadioPath", map.get("RADIO_URL").toString());
					break;
				}
			}
		}
		return recoverMap;
	}
	
	private void loadRecordAddrMap() {
		storeAddrMap = new ConcurrentHashMap<String, String>();
		List<Map<String, String>>  addrsList = sqlAccess.selectRecordStoreAddrList();
		for(Map<String, String>  map : addrsList) {
			String key = map.get(BuildConstants.SWITCH_TYPE);//SWITCH_TYPE  BuildConstants.SWITCH_TYPE
			String value = map.get(BuildConstants.SWITCH_VALUE);//SWITCH_VALUE   BuildConstants.SWITCH_VALUE
			storeAddrMap.put(key, value);
		}
	}
	
	private void loadMeasureStationMap() {
		realMeasureStationMap = new ConcurrentHashMap<String, String>();
		List<Map<String, Object>>  stationList = sqlAccess.selectMeasureStationList();
		for(Map<String, Object> map : stationList) {
			String key = map.get(BuildConstants.RUNPLAN_ID) + "";
			String value = map.get(BuildConstants.STATION_NAME) + "";
			realMeasureStationMap.put(key, value);
		}
	}

	private void loadAutoUnitMap() {
		unfinishedUnitMap = new ConcurrentHashMap<String, String>();
		finishedUnitMap = new ConcurrentHashMap<String, String>();
		unitStatusMap = new HashMap<Integer, Boolean>();
		List<MeasureUnitTable>  autoList = sqlAccess.selectMeasureAutoUnitList();
		//如果本次收测单元没有数据，状态为未完成
		if(autoList == null || autoList.size() == 0) {
			unitStatusMap.put(0, false);
			return;
		}
		for(MeasureUnitTable unit : autoList) {
			int unit_status_id = unit.getUnit_status_id();
			StringBuffer key = new StringBuffer();
			String unit_id = unit.getTask_unit_id()  + "";
			key.append(unit.getTask_build_mode());
			key.append("_");
			key.append(unit.getMonitor_id());
			key.append("_");
			key.append(unit.getTask_id());
			key.append("_");
			key.append(unit.getRunplan_id());
			key.append("_");
			key.append(unit_status_id);
			if(unit_status_id != 3 && unit_status_id != -1)
				finishedUnitMap.put(key.toString(), unit_id);
			else if(unit_status_id == -1 || unit_status_id == 3)   // -1  评估失败    3：生成收测单元   1：待评估  0：评估完成    2 评估中
				unfinishedUnitMap.put(key.toString(), unit_id);
		}
		unitStatusMap.put(0, true);
	}
	
	private void loadManualUnitMap() {
//		Map<String, String> unitMap = new HashMap<String, String>();
		List<MeasureUnitTable>  manualList = sqlAccess.selectMeasureManualUnitList();
		//如果本次收测单元没有数据，状态为未完成
		if(manualList == null || manualList.size() == 0) {
			unitStatusMap.put(1, false);
			return;
		}
		for(MeasureUnitTable unit : manualList) {
			int unit_status_id = unit.getUnit_status_id();
			StringBuffer key = new StringBuffer();
			String unit_id = unit.getTask_unit_id()  + "";
			key.append(unit.getTask_build_mode());
			key.append("_");
			key.append(unit.getMonitor_id());
			key.append("_");
			key.append(unit.getTask_id());
			key.append("_");
			key.append(unit.getRunplan_id());
			key.append("_");
			key.append(unit_status_id);
//			unitMap.put(key.toString(), unit_id);
			//收测单元状态，评估失败-1，录音文件采集完成1，正在评估2，评估打分完成0，收测单元生成3，收测单元最终完成4
			if(unit_status_id != 3 && unit_status_id != -1)
				finishedUnitMap.put(key.toString(), unit_id);
			else if(unit_status_id == -1 || unit_status_id == 3)
				unfinishedUnitMap.put(key.toString(), unit_id);
		}
//		if(unitMap.size() > 0) 
//			unfinishedUnitMap.putAll(unitMap);
		unitStatusMap.put(1, true);
	}
	
	private void loadAllUnitMap() {
		unitMap = new HashMap<String, String>();
		List<MeasureUnitTable>  manualList = sqlAccess.selectMeasureAutoUnitList();
		StringBuffer key = null;
		for(MeasureUnitTable unit : manualList) {
			key = new StringBuffer();
			String unit_id = unit.getTask_unit_id()  + "";
			key.append(unit.getTask_build_mode());
			key.append("_");
			key.append(unit.getMonitor_id());
			key.append("_");
			key.append(unit.getTask_id());
			key.append("_");
			key.append(unit.getRunplan_id());
			unitMap.put(key.toString(), unit_id);
		}
	}
	
	public Map<Integer, TaskTable> getDelTaskMap() {
		return delTaskMap;
	}

	public void setDelTaskMap(Map<Integer, TaskTable> delTaskMap) {
		this.delTaskMap = delTaskMap;
	}

	protected Map<Integer, TaskTable> getMeasureTaskMap() {
		return measureTaskMap;
	}

	protected Map<Integer, Map<Integer, TaskFreqTable>> getTaskFreqMap() {
		return taskFreqMap;
	}

	protected Map<Integer, Map<String, String>> getTaskConfMap() {
		return taskConfMap;
	}

	protected Map<Integer, List<TaskScheduleTable>> getTaskScheduleMap() {
		return taskScheduleMap;
	}

	protected Map<Integer, List<TaskMonitorTable>> getTaskMonitorMap() {
		return taskMonitorMap;
	}

	protected Map<String, String> getQualityTypeMap() {
		return qualityTypeMap;
	}

	protected Map<String, String> getFtpServerMap() {
		return ftpServerMap;
	}
	
	protected Map<String, String> getRecordStoreAddrMap() {
		return storeAddrMap;
	}
	
	protected Map<String, String> getUnfinishedUnitMap() {
		return unfinishedUnitMap;
	}
	
	public Map<Integer, Boolean> getUnitStatusMap() {
		return unitStatusMap;
	}
	
	protected Map<String, String> getFinishedUnitMap() {
		return finishedUnitMap;
	}
	
	public Map<String, RunplanView> getTaskRunplanMap() {
		return taskRunplanMap;
	}

	public Map<String, String> getRealMeasureStationMap() {
		return realMeasureStationMap;
	}
	
	protected Map<String, String> getUnitTaskMap() {
		return unitMap;
	}

	protected void deleteManualUnit(List<Map<String, Object>> list){
		sqlAccess.deleteManualUnitRecords(list);
	}
	
	protected void deleteMeasureUnit(List<Map<String, Object>> list){
		sqlAccess.deleteMeasureUnitRecords(list);
	}
	
	protected void initRecoverTimeDataMap() {
		loadRecoverTaskData();
	}

	protected Map<String, String> getRecoverTimeDataMap() {
		return recoverTimeMap;
	}

	protected Map<String, String> getRunplanMap() {
		return runplanMap;
	}

	protected Map<Long, Long> getStationMap() {
		return stationMap;
	}

	protected Map<Long, Long> getLanguageMap() {
		return languageMap;
	}
	
	protected List<RealTimeStreamDTO> getStreamList(){
		return streamList;
	}
}
