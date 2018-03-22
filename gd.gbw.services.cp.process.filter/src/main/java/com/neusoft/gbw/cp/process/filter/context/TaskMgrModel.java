package com.neusoft.gbw.cp.process.filter.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskMgrModel {

	private static class Holder {
		private static final TaskMgrModel INSTANCE = new TaskMgrModel();
	}

	private TaskMgrModel() {
		taskMap = new HashMap<String, CollectTask>();
		ftpServerMap = new HashMap<String, String>();
		recordAddrMap = new HashMap<String, String>();
	}

	public static TaskMgrModel getInstance() {
		return Holder.INSTANCE;
	}
	
	private Lock lock = new ReentrantLock();
	//key：通过taskID、freqID、scheduleID拼成
	private Map<String, CollectTask> taskMap = null;
	//key:ip, value:user#SEP#password
	private Map<String, String> ftpServerMap = null;
	//key：recordkey，value：recordvalue
	private Map<String, String> recordAddrMap = null;	
	
	@Autowired
	public void addTask(CollectTask task) {
		BusinessTaskType type = task.getBusTask().getType();
//		switch(type) {
//		case measure_auto:
//			break;
//		case measure_manual_del:
//			break;
//		case measure_manual_recover:
//			break;
//		case measure_manual_set:
//			taskMap.put(getTaskID(task), task);
//			break;
//		case measure_online:
//			break;
//		case measure_real_record:
//			break;
//		case measure_realtime:
//			ProtocolType pType = task.getData().getType();
//			if (pType.equals(ProtocolType.EquStatusRealtimeQuery)) {
//				Log.debug("[数据过滤]设备状态实时查询任务缓存，monitorCode=" + task.getBusTask().getMonitor_code());
//				taskMap.put(task.getBusTask().getMonitor_code(), task);
//			}
//			break;
//		case measure_report:
//			break;
//		default:
//			break;
//		}
		if(type.equals(BusinessTaskType.measure_manual_set)) {
//			Log.debug("[数据过滤]设置任务缓存，monitorCode=" + task.getBusTask().getMonitor_code());
//			taskMap.put(getTaskID(task), task);
		} else if(type.equals(BusinessTaskType.measure_manual_recover)) {
			ProtocolData data = task.getData();
			if(data == null) 
				return;
			ProtocolType pType = task.getData().getType();
			if (pType.equals(ProtocolType.FileRetrieve)) {
				//原Key
//				taskMap.put(task.getBusTask().getMonitor_code(), task);
				//新key，以文件名称做key
				//目前一次下多个
				cacheTask(task);
			}
			else if(pType.equals(ProtocolType.QualityHistoryQuery)  ||
					pType.equals(ProtocolType.SpectrumHistoryQuery) ||
					pType.equals(ProtocolType.StreamHistoryQuery) ||
					pType.equals(ProtocolType.OffsetHistoryQuery)) {
				//日后是否做内存删除待定
				taskMap.put(getTaskID(task), task);
			}
		}else if(type.equals(BusinessTaskType.measure_realtime) || type.equals(BusinessTaskType.measure_inspect)) {
			ProtocolData data = task.getData();
			if(data == null) 
				return;
			ProtocolType pType = task.getData().getType();
			if (pType.equals(ProtocolType.EquStatusRealtimeQuery)) {
//				Log.debug("[数据过滤]设备状态实时查询任务缓存，monitorCode=" + task.getBusTask().getMonitor_code());
				taskMap.put(task.getBusTask().getMonitor_code(), task);
			}
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
	
	public boolean containsKey(String key) {
		return taskMap.containsKey(key);
	}
	
	public CollectTask getValue(String key) {
		if (!containsKey(key))
			return null;
		return taskMap.get(key);
	}
	
	@SuppressWarnings("unchecked")
	private void cacheTask(CollectTask task) {
		Map<String, String> fileMap = (Map<String, String>)task.getExpandObject(ExpandConstants.RECORD_FILE_RECOVER_KEY);
		for(String key : fileMap.keySet()) {
			taskMap.put(key, task);
		}
	}
//	private String getOldTaskID(CollectTask task) {
//		int taskID = task.getBusTask().getTask_id();
//		int freqID = task.getBusTask().getTaskfreq_id();
//		String monitorCode = task.getBusTask().getMonitor_code();
//		StringBuffer buffer = new StringBuffer();
//		buffer.append(getPositionData(taskID, 4).substring(1));
//		buffer.append(getPositionData(freqID, 5));
//		ProtocolType type = task.getData().getType();
//		if(type.name().startsWith("Quality"))
//			buffer.append("1");
//		if(type.name().startsWith("Spectrum"))
//			buffer.append("2");
//		if(type.name().startsWith("Stream"))
//			buffer.append("3");
//		if(type.name().startsWith("Offset"))
//			buffer.append("4");
//		//增加站点code，一个任务下发多个站点，  add by jiahao
//		buffer.append(monitorCode);
//		
//		return buffer.toString();
//	}
	
	/**
	 * 唯一ID确认
	 * taskName+monitorCode+freq+taskType+taskMode + schedule信息
	 * @param task
	 * @param operNum
	 * @return
	 */
	public static String getTaskID(CollectTask task) {
		String taskId = task.getExpandObject(ExpandConstants.TASK_UNIQUE_ID).toString();
		return taskId;
	}
//	private String getPositionData(int data, int num) {
//		String tmp = data+"";
//		int length = tmp.length();
//		for(int i=0;i<num-length;i++) {
//			tmp = tmp + "0";
//		}
////		System.out.println(tmp);
//		return tmp;
//	}
	
	public void remove(String key) {
		taskMap.remove(key);
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
}
