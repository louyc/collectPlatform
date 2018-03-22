package com.neusoft.gbw.cp.load.data.build.domain.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.load.data.build.domain.dao.SqlDataAccess;
import com.neusoft.gbw.cp.load.data.build.domain.dao.SqlSessionFactoryHolder;
import com.neusoft.gbw.cp.load.data.build.domain.vo.DBConverType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class DataMgrCentreModel {

	private Lock lock = new ReentrantLock();
	private MonitorDataMgr moniDeviceMgr = null;
	private TaskDataMgr taskDataMgr = null;
	private TaskDBDataConver dataConver = null;
	private DataDisposeMgr disposeMgr = null;
	
	private static class Holder {
		private static final DataMgrCentreModel INSTANCE = new DataMgrCentreModel();
	}

	private DataMgrCentreModel() {
		init();
	}

	public static DataMgrCentreModel getInstance() {
		return Holder.INSTANCE;
	}
	
	private void init() {
		try {
			SqlSessionFactoryHolder.getInstance().init();
		} catch (Exception e) {
			Log.error("", e);;
		}
		SqlSessionFactoryHolder factory = SqlSessionFactoryHolder.getInstance();
		SqlDataAccess sqlAccess = new SqlDataAccess(factory.getSqlSessionFactory());
		
		moniDeviceMgr = new MonitorDataMgr(sqlAccess);
		taskDataMgr = new TaskDataMgr(sqlAccess);
		disposeMgr = new DataDisposeMgr(sqlAccess);
		dataConver = new TaskDBDataConver(taskDataMgr);
	}
	
	public void synt() {
		lock.lock();
		try {
			//任务数据加载
			taskDataMgr.loader();     // selectTaskMonitorList  两库无数据
			//站点分组数据加载
			moniDeviceMgr.loader();    //selectMonMachineList
			//加载告警字典数据
			disposeMgr.loader();     //
			Log.info("数据库同步內存结构完成， Time=" + DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} finally {
			lock.unlock();
		}
	}
	
	public void syntDevice() {
		lock.lock();
		try {
			moniDeviceMgr.loader();
			Log.info("数据库同步站点信息內存结构完成， Time=" + DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} finally {
			lock.unlock();
		}
	}
	
	public void syntDeviceGroup() {
		lock.lock();
		try {
			moniDeviceMgr.loaderMonGroup();
			Log.info("数据库同步站点信息內存结构完成， Time=" + DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} finally {
			lock.unlock();
		}
	}
	
	public void deleteManualUnit(List<Map<String, Object>> list){
		Log.info("清除当前未采集的手动收测单元个数=" + list.size());
		taskDataMgr.deleteManualUnit(list);
	}
	
	public void deleteMeasureUnit(List<Map<String, Object>> list){
		Log.info("清除当前未采集的自动收测单元个数=" + list.size());
		taskDataMgr.deleteMeasureUnit(list);
	}
	
	public Map<Long, Long> getLanguageMap() {
		lock.lock();
		try {
			return taskDataMgr.getLanguageMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<Long, Long> getStationMap() {
		lock.lock();
		try {
			return taskDataMgr.getStationMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getRunplanToStationMap() {
		lock.lock();
		try {
			return taskDataMgr.getRunplanMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<Integer, String> getEquAlarmTypeMap() {
		lock.lock();
		try {
			return disposeMgr.getEquAlarmType();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getUnfinishedUnit() {
		lock.lock();
		try {
			return taskDataMgr.getUnfinishedUnitMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getFinishedUnit() {
		lock.lock();
		try {
			return taskDataMgr.getFinishedUnitMap();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 获取当前时刻的已经生成所有收测单元数据
	 * @return
	 */
	public Map<String, String> getTaskUnit() {
		lock.lock();
		try {
			return taskDataMgr.getUnitTaskMap();
		} finally {
			lock.unlock();
		}
	}
	
	
	public void initRecoverTimeData() {
		lock.lock();
		try {
			taskDataMgr.initRecoverTimeDataMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getRecoverTimeData() {
		lock.lock();
		try {
			return taskDataMgr.getRecoverTimeDataMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<Integer, Boolean> getUnitStatus() {
		lock.lock();
		try {
			return taskDataMgr.getUnitStatusMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getRecordStoreAddr() {
		lock.lock();
		try {
			return taskDataMgr.getRecordStoreAddrMap();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 旧实时收测结构
	 * @return
	 */
	public Map<String, String> getRealMeasureSite() {
		lock.lock();
		try {
			return moniDeviceMgr.getMonitorGoupMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String,String> getMonPartformMap(){
		lock.lock();
		try{
			return moniDeviceMgr.getMonPartformMap();
		}finally{
			lock.unlock();
		}
		
	}
	
	
	/**
	 * 获取要收测站点的收测类型
	 * @param siteKey
	 * @return
	 */
	public int getUnitStatus(String siteKey) {
		lock.lock();
		try {
			int status = -1;
			Map<String, String>  siteMap = moniDeviceMgr.getMonitorGoupMap();
			if(!siteMap.containsKey(siteKey))
				return status;
			return Integer.parseInt(siteMap.get(siteKey));
		}finally {
			lock.unlock();
		}
	}
	
//	/**
//	 * 新实时收测结构
//	 * @return
//	 */
//	public Map<String, String> getNewRealMeasureSite() {
//		lock.lock();
//		try {
//			return taskDataMgr.getNewRealMeasureSiteMap();
//		} finally {
//			lock.unlock();
//		}
//	}
	
	public Map<String, String> getRealMeasureStation() {
		lock.lock();
		try {
			return taskDataMgr.getRealMeasureStationMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getFtpServerMap() {
		lock.lock();
		try {
			return taskDataMgr.getFtpServerMap();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getQualityTypeMap() {
		lock.lock();
		try {
			return taskDataMgr.getQualityTypeMap();
		} finally {
			lock.unlock();
		}
	}
	
	public List<Task> getDeviceTaskSet() {
		lock.lock();
		try {
			return dataConver.getTaskList(DBConverType.DEVICE_TASK_SET);
		} finally {
			lock.unlock();
		}
	}
	public List<Task> getDelTaskSet() {
		lock.lock();
		try {
			return dataConver.getDelTaskList(DBConverType.DEVICE_TASK_SET);
		} finally {
			lock.unlock();
		}
	}
	
	public List<Task> getDeviceTaskRecover() {
		lock.lock();
		try {
			return dataConver.getTaskList(DBConverType.DEVICE_TASK_RECOVER);
		} finally {
			lock.unlock();
		}
	}
	
	public List<Task> getPlatformStream() {
		lock.lock();
		try {
			return dataConver.getTaskList(DBConverType.PLATFORM_STREAM);
		} finally {
			lock.unlock();
		}
	}
	
	public List<RealTimeStreamDTO> getInspectStream() {
		lock.lock();
		try {
			return dataConver.getStreamList(DBConverType.INSPECT_STREAM);
		} finally {
			lock.unlock();
		}
	}
	
	public List<Task> getTaskList(String taskId) {
		lock.lock();
		try {
			return dataConver.getTaskList(taskId);
		} finally {
			lock.unlock();
		}
	}
	
	public MonitorDevice getMonitorDevice(long monitorID) {
		lock.lock();
		try {
			return moniDeviceMgr.getMonitorDevice(monitorID);
		} finally {
			lock.unlock();
		}
	}
	
	public String getMonitorCode(long monitorID) {
		lock.lock();
		try {
			MonitorDevice info = null;
			info = moniDeviceMgr.getMonitorDevice(monitorID);
			if (info == null) {
				return null;
			} else {
				return info.getMonitor_code();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public Collection<MonitorDevice> getMonitorDeviceList() {
		lock.lock();
		try {
			return moniDeviceMgr.getMonitorDeviceList();
		} finally {
			lock.unlock();
		}
	}
	
	public List<Task> getInpectTaskList(String monitorId) {
		lock.lock();
		try {
			return dataConver.getMoniTaskList(monitorId);
		} finally {
			lock.unlock();
		}
	}
	public List<Task> getInspectRecoverTaskList(String monitorId) {
		lock.lock();
		try {
			return dataConver.getMoniTaskRecoverList(monitorId);
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getInpectTotalTaskList(String monitorId) {
		lock.lock();
		try {
			Map<String, String> totalTask = new HashMap<String, String>();
			List<Task> taskList = dataConver.getMoniTaskList(monitorId);
			for(Task task : taskList) {
				String name = task.getMeasureTask().getTask_name();
				if(!totalTask.containsKey(name))
					totalTask.put(name, task.getMeasureTask().getTask_status_id() + "");
			}
			return totalTask;
		} finally {
			lock.unlock();
		}
	}
	
	public Map<String, Object> getRecoverDateList(String monitorId) {
		lock.lock();
		try {
			Map<String, Object> totalTask = new HashMap<String, Object>();
			totalTask = dataConver.getMoniRecoverTaskList(monitorId);
			return totalTask;
		} finally {
			lock.unlock();
		}
	}
	
	public List<EquAlarm> getAlarmList(Map<String, Integer> param) {
		return disposeMgr.getAlarmList(param);
	}
	
}
