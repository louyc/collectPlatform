package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.vo.QualityDataStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class RecoverTaskProcessor {
	
/*	public QualityDataStore fillQualityDataStore(CollectData data) {
		QualityDataStore qdata = new QualityDataStore();
		qdata.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		qdata.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		//设置任务默认不配置接收机，接收机code改从返回的数据中获取
		//
//		qdata.setReceiver_code(data.getCollectTask().getAttrInfo().getFirstEquCode());
		return qdata;
	}*/
	public QualityDataStore fillQualityDataStore(CollectData data) {
		QualityDataStore qdata = new QualityDataStore();
		qdata.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		qdata.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		qdata.setTask_unique_id(getTaskID(data.getCollectTask()));
//		qdata.setReceiver_code(data.getCollectTask().getAttrInfo().getFirstEquCode());
		return qdata;
	}
	
	public static int getTaskID(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
		String taskName = task.getBusTask().getTask_name();
		buffer.append(taskName).append("_");
		String monitorCode = task.getBusTask().getMonitor_code();
		buffer.append(monitorCode).append("_");
		String freq = task.getBusTask().getFreq();
		buffer.append(freq).append("_");
		if(null!=task.getData() && null!=task.getData().getOriType().name()){
			String taskType = task.getData().getOriType().name();
			buffer.append(taskType).append("_");
		}
		int taskMode = task.getBusTask().getTask_build_mode();
		buffer.append(taskMode);
		
		//hashCode可能为负数
		String taskCode = (buffer.toString().hashCode() + "").replaceAll("-", "1");
		int length = taskCode.length();
		//不足8位进行补0
		for(int i=0;i<8-length;i++) {
			taskCode = taskCode + "0";
		}
		//头位可能有0,直接用hashcode7位
		String taskId = ("1" + taskCode).substring(0, 8);
		return Integer.parseInt(taskId);
	}
	
	
	public void sendStore(List<StoreInfo> infoList) {
		if(infoList != null && !infoList.isEmpty()) {
			for(StoreInfo info : infoList) {
				ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
			}
		}
	}
	
	public StoreInfo updateRecoverStatus(CollectData data) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("recycle_user_id", 1);//人员ID
		dataMap.put("recycle_time", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		info.setLabel("updateTaskRecoverStatusRecords");
		info.setDataMap(dataMap);
		return info;
	}
}
