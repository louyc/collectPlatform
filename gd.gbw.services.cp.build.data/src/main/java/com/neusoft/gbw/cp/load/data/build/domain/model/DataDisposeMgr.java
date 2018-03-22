package com.neusoft.gbw.cp.load.data.build.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.load.data.build.domain.dao.SqlDataAccess;

public class DataDisposeMgr {
	
	private SqlDataAccess sqlAccess = null;
	
	protected DataDisposeMgr(SqlDataAccess sqlAccess) {
		this.sqlAccess = sqlAccess;
	}
	
	private Map<Integer, String> alarmTypeMap = null;
	
	protected void init() {
		if(alarmTypeMap != null) {
			alarmTypeMap.clear();
			alarmTypeMap = null;
		}
	}
	
	protected void loader() {
		init();
		loadAlarmType();
		
	}
	
	private void loadAlarmType() {
		alarmTypeMap = new HashMap<Integer, String>();
		for (Map<String, Object> map : sqlAccess.selectEquAlarmTypeList()) {
			//PG  yong
			//int id = Integer.parseInt(map.get("alarm_value").toString());
			//String name = map.get("alarm_name").toString();
			
			//int id = Integer.valueOf(map.get("ALARM_VALUE").toString());
			int id = Integer.parseInt(map.get("ALARM_VALUE").toString());
			String name = map.get("ALARM_NAME").toString();
			alarmTypeMap.put(id, name);
			
		}
	}
	
	protected List<EquAlarm> getAlarmList(Map<String, Integer> param) {
		return sqlAccess.selectEquAlarmList(param);
	}
	
	protected Map<Integer, String> getEquAlarmType() {
		return alarmTypeMap;
	}
}
