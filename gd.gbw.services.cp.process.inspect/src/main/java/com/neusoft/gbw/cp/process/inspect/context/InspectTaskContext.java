package com.neusoft.gbw.cp.process.inspect.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.process.ITaskProcess;
import com.neusoft.gbw.cp.process.inspect.vo.InspectTaskTimeOut;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.service.config.Configuration;

public class InspectTaskContext {

	private static class InspectTaskContextHolder {
		private static final InspectTaskContext INSTANCE = new InspectTaskContext();
	}
	
	public InspectTaskContext() {
		taskProcessMap = new HashMap<String, ITaskProcess>();
		taskOutMap = new HashMap<String, InspectTaskTimeOut>();
		moniProgramMap = new HashMap<Long, String>();
		inspectTaskMap = new HashMap<String, Map<String, String>>();
		siteMap = new HashMap<String, MonitorDevice>();
		alarmTypeMap = new HashMap<Integer, String>();
	}
	
	public static InspectTaskContext getInstance() {
		return InspectTaskContextHolder.INSTANCE;
	}
	
	private Lock lock = new ReentrantLock();
	private Map<String, ITaskProcess> taskProcessMap = null;
	//记录超时 key:monitor_id , value (key: inspect_code,value:InspectTask)
	private Map<String, InspectTaskTimeOut> taskOutMap = null;
	// key:monitor_id, value: version
	private Map<Long, String> moniProgramMap = null;
	// key:monitor_id, value (key: task_id,value: taskDesc)
	private Map<String, Map<String, String>> inspectTaskMap = null;
	//key: monitor_id, value: MonitorDevice
	private Map<String, MonitorDevice> siteMap = null;
	//key: alarm_type_id, value: alarm_type
	private Map<Integer, String> alarmTypeMap = null;
	
	
	public void loadRealtimeProcess() {
		Configuration config = ARSFToolkit.getConfiguration();
		Map<String, String> map = config.getAllBusinessProperty(InspectConstants.INSPECT_TASK_PROCESS_SCOPE);
		
		ITaskProcess process = null;
		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, String> element = iter.next();
			try {
				process = (ITaskProcess) Class.forName(element.getValue()).newInstance();
				taskProcessMap.put(element.getKey(), process);
			} catch (ClassNotFoundException e) {
				Log.error("处理通道没有找到加载处理的指定类，Name" + element.getValue(), e);
			} catch (Exception e) {
				Log.error("处理通道构建处理类出现异常。", e);
			} 
		}
	}
	
	public ITaskProcess getTaskProcessor(String name) {
		if (taskProcessMap.containsKey(name)) {
			return taskProcessMap.get(name);
		}
		return null;
	}
	
	public void initInpectTask(String key, InspectTaskTimeOut task) {
		lock.lock();
		try {
			if(!taskOutMap.containsKey(key)) {
				taskOutMap.put(key, task);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void removeInspectTask(String key) {
		lock.lock();
		try {
			if(taskOutMap.containsKey(key)) {
				taskOutMap.get(key).getTimer().cancel();
				taskOutMap.remove(key);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void updateInspectStata(String key, String inspect_code) {
		lock.lock();
		try {
			if(!taskOutMap.containsKey(key))
				return;
			Log.debug("[巡检服务]更新巡检项状态，monitorId=" + key + ",inspectCode=" + inspect_code);
			taskOutMap.get(key).setStatus(inspect_code);
		} finally {
			lock.unlock();
		}
	}
	
	public boolean isNetConn(String monitorId) {
		if(taskOutMap.containsKey(monitorId))
			return true;
		return false;
	}
	
	public void saveMoniPro(long monitorId, String version) {
		if(!moniProgramMap.containsKey(monitorId))
			moniProgramMap.put(monitorId, version);
	}
	
	public boolean isProgram(long monitorId, String version) {
		saveMoniPro(monitorId,version);
		if(moniProgramMap.containsKey(monitorId) && moniProgramMap.get(monitorId).equals(version))
			return true;
		return false;
	}
	
	public MonitorDevice getMonitorDevice(String monitorId) {
		lock.lock();
		try {
			if (siteMap.containsKey(monitorId))
				return siteMap.get(monitorId);
		} finally {
			lock.unlock();
		}
		return null;
	}
	
	public Map<String, MonitorDevice> getAllMoniDevice() {
		lock.lock();
		try {
			return siteMap;
		} finally {
			lock.unlock();
		}
	}
	
	public void addSiteMap(Collection<MonitorDevice> siteList) {
		lock.lock();
		try {
			for(MonitorDevice device : siteList) {
				siteMap.put(device.getMonitor_id() + "", device);
			}
		} finally {
			lock.unlock();
		}
	}

	public  Map<String, String> getTaskMap(String inspectKey) {
		lock.lock();
		try {
			if(inspectTaskMap.containsKey(inspectKey))
				return inspectTaskMap.get(inspectKey);
		} finally {
			lock.unlock();
		}
		return null;
	}

	public void setInspectTaskMap(String monitorId, Map<String, String> taskMap) {
		lock.lock();
		try {
			if(!inspectTaskMap.containsKey(monitorId))
				inspectTaskMap.put(monitorId, taskMap);
		} finally {
			lock.unlock();
		}
	}
	
	public int getInspectTaskSize(String inspectKey) {
		lock.lock();
		try {
			if(inspectTaskMap.containsKey(inspectKey)) {
				return inspectTaskMap.get(inspectKey).size();
			}
			return 0;
		} finally {
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
