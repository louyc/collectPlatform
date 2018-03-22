package com.neusoft.gbw.cp.process.measure.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.measure.vo.manual.ManualTaskInfo;
import com.neusoft.gbw.cp.process.measure.vo.manual.RecordRecover;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectTaskModel {

	private static class Holder {
		private static final CollectTaskModel INSTANCE = new CollectTaskModel();
	}
	
	private CollectTaskModel(){
		collectTaskMap = new HashMap<Long, Map<Long, Set<CollectTask>>>();
		siteMap = new HashMap<String, MonitorDevice>();
		qualTypeMap = new HashMap<String, String>();
		recordAddrMap = new HashMap<String, String>();
		realSiteMap = new HashMap<String, String>();
		recoverDataMap = new HashMap<String, String>();
		ftpServerMap = new HashMap<String, String>();
		stationMap = new HashMap<String, String>();
		manualTaskSize = new HashMap<String, ManualTaskInfo>();
		recordRecoverMap = new HashMap<String, RecordRecover>();
	}
	
	public static CollectTaskModel getInstance() {
		return Holder.INSTANCE;
	}
	
	private Lock lock = new ReentrantLock();
	//Key: task_id,  value: (Key: taskfreq_id, value: Set<CollectTask>) 暂时不应用此数据结构
	private Map<Long, Map<Long, Set<CollectTask>>> collectTaskMap = null;
	//key: monitor_code, value: MonitorDevice
	private Map<String, MonitorDevice> siteMap = null;
	//key: code, value: table_name
	private Map<String, String> qualTypeMap = null;
	//key：recordkey，value：recordvalue
	private Map<String, String> recordAddrMap = null;
	//key：monitorId_runplanType，value：runplanType //收测单元实时收测站点信息
	private Map<String, String> realSiteMap = null;
	//key:task_id_monitor_id_frequency_quality_code_collect_time,value:id
	private Map<String, String> recoverDataMap = null;
	//key: taskId_cliendId,value: ManualTask 本次大任务对象  manualTask taskSize recoverSize;
	private Map<String , ManualTaskInfo> manualTaskSize= null;
	//key:ip, value:user#SEP#password
	private Map<String, String> ftpServerMap = null;
	//key:runplan_id, value:station_name
	private Map<String, String> stationMap = null;
	//key:task_id,value:RecordRecover
	private Map<String, RecordRecover> recordRecoverMap = null;
	
	
	public MonitorDevice getMonitorDevice(String code) {
		if (siteMap.containsKey(code))
			return siteMap.get(code);
		return null;
	}
	
	public void addSiteMap(Collection<MonitorDevice> siteList) {
		lock.lock();
		try {
			for(MonitorDevice device : siteList) {
				siteMap.put(device.getMonitor_code(), device);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void put(CollectTask task) {
		lock.lock();
		try {
			long taskID = task.getBusTask().getTask_id();
			long taskFreqID = task.getBusTask().getTaskfreq_id();
			Set<CollectTask> list = null;
			Map<Long, Set<CollectTask>> map = null;
			if (collectTaskMap.containsKey(taskID)) {
				map = this.collectTaskMap.get(taskID);
				if (map.containsKey(taskFreqID)) {
					list = map.get(taskFreqID);
				} else {
					list = new HashSet<CollectTask>();
				}
				list.add(task);
				map.put(taskFreqID, list);
			} else {
				map = new HashMap<Long, Set<CollectTask>>();
				list = new HashSet<CollectTask>();
				list.add(task);
				map.put(taskFreqID, list);
				collectTaskMap.put(taskID, map);
			}
		} finally {
			lock.unlock();
		}
	}
	
	
	public void remove(CollectTask task) {
		lock.lock();
		try {
			long taskID = task.getBusTask().getTask_id();
			if(collectTaskMap.containsKey(taskID)) {
				collectTaskMap.remove(taskID);
			}
		}finally {
			lock.unlock();
		}
	}

	public String getQualLabel(String code) {
		lock.lock();
		try {
			if (qualTypeMap.containsKey(code))
				return qualTypeMap.get(code);
		}finally {
			lock.unlock();
		}
		return null;
	}

	public void setQualTypeMap(Map<String, String> qualTypeMap) {
		lock.lock();
		try {
			this.qualTypeMap = qualTypeMap;
		}finally {
			lock.unlock();
		}
	}
	
	public void setRecordAddr(Map<String, String> map) {
		lock.lock();
		try {
			this.recordAddrMap = map;
		}finally {
			lock.unlock();
		}
	}
	
	public String getRecordAddr(String key) {
		lock.lock();
		try {
			return recordAddrMap.get(key);
		}finally {
			lock.unlock();
		}
	}

	public Map<String, String> getRealSiteMap() {
		lock.lock();
		try {
			return realSiteMap;
		}finally {
			lock.unlock();
		}
	}

	public void setRealSiteMap(Map<String, String> realSiteMap) {
		lock.lock();
		try {
			this.realSiteMap = realSiteMap;
		}finally {
			lock.unlock();
		}
	}

	public boolean getRecoverDataMap(String key) {
		boolean isExist = false;
		lock.lock();
		try {
			if(recoverDataMap.containsKey(key)) {
				isExist = true;
				recoverDataMap.remove(key);
			}
		}finally {
			lock.unlock();
		}
		return isExist;
	}

	public void setRecoverDataMap(Map<String, String> recoverDataMap) {
		lock.lock();
		try {
			this.recoverDataMap = recoverDataMap;
		}finally {
			lock.unlock();
		}
	}
	
	public ManualTaskInfo getManualTaskInfo(String key) {
		ManualTaskInfo info = null;
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key))
				info = manualTaskSize.get(key);
		} finally {
			lock.unlock();
		}
		return info;
	}
	
	public Map<String , ManualTaskInfo> getManualTaskMap() {
		lock.lock();
		try {
			return manualTaskSize;
		} finally {
			lock.unlock();
		}
	}
	
	public void addManualTask(List<CollectTask> taskList, String key, ManualTaskInfo  mTask) {
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key))
				manualTaskSize.remove(key);
			
			manualTaskSize.put(key, mTask);
		} finally {
			lock.unlock();
		}
	}
	
	public void removeTask(String key) {
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key)) {
//				manualTaskSize.get(key).getTimer().cancel();
				manualTaskSize.remove(key);
			}
		} finally {
			lock.unlock();
		}
	}
	
//	public void cancelTaskTime(String key) {
//		lock.lock();
//		try {
//			if(manualTaskSize.containsKey(key)) {
//				manualTaskSize.get(key).getTimer().cancel();
//			}
//		} finally {
//			lock.unlock();
//		}
//	}
	
	public boolean containsKey(String key) {
		boolean flag = false;
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key))
				flag = true;
		} finally {
			lock.unlock();
		}
		return flag;
	}
	
	public void updateRecoverCount(String key, boolean status) {
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key)) {
				//返回一个小任务，则更新任务数量，如果任务中有插入失败的，则将任务置为失败
				int size = manualTaskSize.get(key).getRecoverSize();
				manualTaskSize.get(key).setRecoverSize(size + 1);
				manualTaskSize.get(key).setTaskStatus(manualTaskSize.get(key).isTaskStatus() == false ? false:status);
			}
		} finally {
			lock.unlock();
		}
	}
	public void updateNullSize(String key){
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key)) {
				int size = manualTaskSize.get(key).getNullSize();
				manualTaskSize.get(key).setNullSize(size + 1);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public String getFtpServerByIp(String key) {
		lock.lock();
		try {
			if (ftpServerMap.containsKey(key))
				return ftpServerMap.get(key);
		}finally {
			lock.unlock();
		}
		return null;
	}

	public void setFtpServerMap(Map<String, String> ftpServerMap) {
		lock.lock();
		try {
			this.ftpServerMap = ftpServerMap;
		}finally {
			lock.unlock();
		}
	}
	
	public void setRecordSize(CollectTask task, int recordSize, long time) {
		lock.lock();
		try {
			String taskId = task.getBusTask().getTask_id() + "";
			String key = taskId + task.getTaskType().name();
			if(recordRecoverMap.containsKey(key))
				return;
			
			RecordRecover recover = new RecordRecover();
			recover.setData(task);
			recover.setTime(time);
			recover.setRecordSize(recordSize);
			recordRecoverMap.put(key, recover);
		}finally {
			lock.unlock();
		}
	}
	
	public void updataRecordSize(CollectData data, boolean recordStatus) {
		lock.lock();
		try {
			String taskId = data.getCollectTask().getBusTask().getTask_id() + "";
			String key = taskId + data.getCollectTask().getTaskType().name();
			if(!recordRecoverMap.containsKey(key)) {
				Log.warn("未找到回收记录，taskName=" + key);
				return;
			}
			
			int recordSize = recordRecoverMap.get(key).getRecoverSize() + 1;
			recordRecoverMap.get(key).setRecoverSize(recordSize);
			recordRecoverMap.get(key).setRecordStatus(recordStatus);
			Log.debug("录音文件回收，回收数据。回收总数=" +recordRecoverMap.get(key).getRecordSize() + ",已回收文件个数=" + recordSize);
		}finally {
			lock.unlock();
		}
	}
	
	public Map<String, RecordRecover> getRecordRecoverMap() {
		lock.lock();
		try {
			return recordRecoverMap;
		}finally {
			lock.unlock();
		}
	}
	
	public void removeRecordTask(String key) {
		lock.lock();
		try {
			if(recordRecoverMap.containsKey(key)) {
//				recordRecoverMap.get(key).getTimer().cancel();
				recordRecoverMap.remove(key);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public String getStationById(String key) {
		lock.lock();
		try {
			if (stationMap.containsKey(key))
				return stationMap.get(key);
		}finally {
			lock.unlock();
		}
		return null;
	}

	public void setStationMap(Map<String, String> stationMap) {
		lock.lock();
		try {
			this.stationMap = stationMap;
		}finally {
			lock.unlock();
		}
	}
	
	
//	public int getSizeBytaskIDAndtaskFreqID(int taskID, int taskFreqID) {
//		lock.lock();
//		try {
//			if (!this.collectTaskMap.containsKey(taskID)) {
//				return 0;
//			}
//			
//			Map<Integer, Set<CollectTask>> map = collectTaskMap.get(taskID);
//			if (!map.containsKey(taskFreqID)) {
//				return 0;
//			}
//			
//			return map.get(taskFreqID).size();
//		} finally {
//			lock.unlock();
//		}
//	}
//	
//	public int getSizeByTaskID(int taskID) {
//		lock.lock();
//		try {
//			if (!this.collectTaskMap.containsKey(taskID)) {
//				return 0;
//			}
//			
//			int size = 0;
//			Map<Integer, Set<CollectTask>> freqMap = collectTaskMap.get(taskID);
//			for(Set<CollectTask> list : freqMap.values()) {
//				size += list.size();
//			}
//			
//			return size;
//		} finally {
//			lock.unlock();
//		}
//	}
}
