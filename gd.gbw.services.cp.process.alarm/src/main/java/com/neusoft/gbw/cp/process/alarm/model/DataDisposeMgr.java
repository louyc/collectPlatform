package com.neusoft.gbw.cp.process.alarm.model;

import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.alarm.dao.SqlDataAccess;
import com.neusoft.gbw.cp.process.alarm.dao.SqlSessionFactoryHolder;
import com.neusoft.np.arsf.base.bundle.Log;

public class DataDisposeMgr {
	
	private static class Holder {
		private static final DataDisposeMgr INSTANCE = new DataDisposeMgr();
	}
	
	private DataDisposeMgr(){
		init();
	}
	
	public static DataDisposeMgr getInstance() {
		return Holder.INSTANCE;
	}
	
	private SqlDataAccess sqlAccess = null;
	
	public void init() {
		SqlSessionFactoryHolder factory = SqlSessionFactoryHolder.getInstance();
		sqlAccess = new SqlDataAccess(factory.getSqlSessionFactory());
	}
	
	public List<EquAlarm> getAlarmList(Map<String, Long> param) {
		return sqlAccess.selectEquAlarmList(param);
	}
}
