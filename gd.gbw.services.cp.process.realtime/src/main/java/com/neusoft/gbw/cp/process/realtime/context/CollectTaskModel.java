package com.neusoft.gbw.cp.process.realtime.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.realtime.vo.ManualTaskInfo;
import com.neusoft.gbw.cp.process.realtime.vo.ManualTaskStore;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskAppDTO;

public class CollectTaskModel {

	private static class Holder {
		private static final CollectTaskModel INSTANCE = new CollectTaskModel();
	}
	
	private CollectTaskModel(){
		siteMap = new HashMap<String, MonitorDevice>();
		manualTaskSize = new HashMap<String, ManualTaskInfo>();
		qualTypeMap = new HashMap<String, String>();
		recordAddrMap = new HashMap<String, String>();
		recoverDataMap = new HashMap<String, String>();
		alarmTypeMap = new HashMap<Integer, String>();
//		delQualityMap = new HashMap<Integer, ManualTaskInfo>();
	}
	
	public static CollectTaskModel getInstance() {
		return Holder.INSTANCE;
	}
	
	private Lock lock = new ReentrantLock();
	//key: monitor_code, value: MonitorDevice
	private Map<String, MonitorDevice> siteMap = null;
	//key: taskId_cliendId,value: ManualTask 本次大任务对象  manualTask taskSize recoverSize;
	private Map<String , ManualTaskInfo> manualTaskSize= null;
	//key: code, value: table_name
	private Map<String, String> qualTypeMap = null;
	//key:ip, vale:user#SEP#password
	private Map<String, String> ftpServerMap = null;
	//key：recordkey，value：recordvalue
	private Map<String, String> recordAddrMap = null;
	//key:task_id_monitor_id_frequency_quality_code_collect_time,value:id
	private Map<String, String> recoverDataMap = null;
	//key: alarm_type_id, value: alarm_type
	private Map<Integer, String> alarmTypeMap = null;
//	private Map<Integer, ManualTaskInfo> delQualityMap = null;
	
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
	
	public void addManualTask(List<Object> taskList) {
		ManualTaskInfo mTask = new ManualTaskInfo();
		CollectTask task = (CollectTask)taskList.get(0);
		long task_id = task.getBusTask().getTask_id();
//		String clientId = getUniqueId(task);
		String clientId = "";
		String key = task_id + "_" + clientId;
		mTask.setManualTask(taskList);
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key))
				manualTaskSize.remove(key);
			
			manualTaskSize.put(key, mTask);
		} finally {
			lock.unlock();
		}
	}
	
	public void addManualAutoTask(List<Object> taskList, String key) {
		ManualTaskInfo mTask = new ManualTaskInfo();
		mTask.setManualTask(taskList);
		mTask.setDelStore(getDelTask(taskList));
		lock.lock();
		try {
			//中波运行图中用 任务名称+任务类型（系统自动任务，前台触发任务） 做key
			if(manualTaskSize.containsKey(key))
				manualTaskSize.remove(key);
			
			manualTaskSize.put(key, mTask);
		} finally {
			lock.unlock();
		}
	}
	
	private ManualTaskStore getDelTask(List<Object> taskList) {
		ManualTaskStore store = new ManualTaskStore();
		//操作（0添加、1修改, 2删除）
		String operType = null;
		for(Object obj : taskList) {
			CollectTask task = (CollectTask)obj;
			TaskAppDTO taskdto = (TaskAppDTO) (task).getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
			operType = taskdto.getMap().values().iterator().next().get(0).getOperationTypeFlg();
			if(operType.equals("2")) {
				store.setTask_id(task.getBusTask().getTask_id());
				break;
			}
		}
		return store;
	}
	
	public void removeTask(String key) {
		lock.lock();
		try {
			if(manualTaskSize.containsKey(key))
				manualTaskSize.remove(key);
		} finally {
			lock.unlock();
		}
	}
	
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
	
	public void setRecordAddr(Map<String, String> addrMap) {
		lock.lock();
		try {
			this.recordAddrMap = addrMap;
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
	
	public void addAlarmType(Map<Integer, String> typeMap) {
		lock.lock();
		try {
			alarmTypeMap.clear();
			alarmTypeMap.putAll(typeMap);
		} finally {
			lock.unlock();
		}
	}
	
	public Map<Integer, String> getAlarmType() {
		lock.lock();
		try {
			return alarmTypeMap;
		} finally {
			lock.unlock();
		}
	}
}