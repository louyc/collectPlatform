package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.IProtocolConver;
import com.neusoft.gbw.cp.conver.ProtocolConverFactory;
import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.vo.manual.QualityDataStore;
import com.neusoft.gbw.cp.process.measure.vo.manual.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class RecoverTaskProcessor {
	
	public void disposeTaskData(List<StoreInfo> list, CollectData data, TaskType taskType, boolean collStatus) {
		//100  条批量插入删除
		delData(list);
		sleep(1000);
		if(list.size()>4000){
			List<StoreInfo> listNew = new ArrayList<StoreInfo>();
			for(int i=0;i<list.size();i++){
				listNew.add(list.get(i));
				if(listNew.size() ==4000){
					//在构建插入数据，并执行
					insertData(listNew);
					listNew = new ArrayList<StoreInfo>();
					sleep(1000);
				}
				if(i+1 == list.size()){
					insertData(listNew);
				}
			}
		}else{
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
		}
		//更新回收状态
		disposeTaskRecoverStatus(data,1);
		//更新任务回收计数
		updateRecoverCount(data, collStatus);
		//更新任务回收时间
		updateRecoverTime(data, taskType);
		//更新站点状态
		updateMonitorStatus(data.getCollectTask().getBusTask().getMonitor_id());
		clearData(data);
	}
	
	@SuppressWarnings("unused")
	private void clearData(CollectData data) {
		if(data != null) {
			CollectTask task = data.getCollectTask();
			data = null;
			task = null;
		}
	}
	
	private void delData(List<StoreInfo> list) {
		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
		for(StoreInfo info : list) {
			if (info.getLabel().startsWith("delete"))
				resultList.add(info);
		}
		sendStore(resultList);
	}
	
	private void insertData(List<StoreInfo> list) {
		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
		for(StoreInfo info : list) {
			if (info.getLabel().startsWith("insert"))
				resultList.add(info);
		}
		sendStore(resultList);
	}
	
//	private void updateData(List<StoreInfo> list) {
//		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
//		for(StoreInfo info : list) {
//			if (info.getLabel().startsWith("update"))
//				resultList.add(info);
//		}
//		sendStore(resultList);
//	}
	/**
	 * 构建 任务数据
	 * @param data
	 * @return
	 */
	public QualityDataStore fillQualityDataStore(CollectData data) {
		QualityDataStore qdata = new QualityDataStore();
		qdata.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		qdata.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		qdata.setTask_unique_id(getTaskID(data.getCollectTask()));
//		qdata.setReceiver_code(data.getCollectTask().getAttrInfo().getFirstEquCode());
		return qdata;
	}
	
	/**
	 * 唯一ID确认，该ID只给前台用，用于前台查询历史数据
	 * taskName+monitorCode+freq+taskType+taskMode
	 * 
	 * 	 * 
	 * taskType中任务类型
	 * QualityTaskSet
	 * SpectrumTaskSet
	 * StreamTaskSet
	 * OffsetTaskSet
	 * @param task
	 * @param operNum
	 * @return
	 */
	public static int getTaskID(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
		String taskName = task.getBusTask().getTask_name();
		buffer.append(taskName).append("_");
		String monitorCode = task.getBusTask().getMonitor_code();
		buffer.append(monitorCode).append("_");
		String freq = task.getBusTask().getFreq();
		buffer.append(freq).append("_");
		String taskType = task.getData().getOriType().name();
		buffer.append(taskType).append("_");
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
	
	public void updateRecoverCount(CollectData data, boolean status) {
		String key = data.getCollectTask().getBusTask().getTask_id() + data.getCollectTask().getTaskType().name();
		CollectTaskModel.getInstance().updateRecoverCount(key, status);
	}
	
	public void updateRecoverNullSize(CollectData data,int status){
		String key = data.getCollectTask().getBusTask().getTask_id() + data.getCollectTask().getTaskType().name();
		if(null!=CollectTaskModel.getInstance().getManualTaskMap().get(key) &&
				CollectTaskModel.getInstance().getManualTaskMap().get(key).getNullSize()+1==
				CollectTaskModel.getInstance().getManualTaskMap().get(key).getManualTask().size()){
			Log.warn("[任务回收服务]回收数据都为空：");
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			disposeTaskRecoverStatus(data,status);
		}
			Log.warn("[任务回收服务]回收数据为空：  +1");
			CollectTaskModel.getInstance().updateNullSize(key);
	}
	
	public void disposeTaskRecoverStatus(CollectData data,int status) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("recycle_user_id", "system");
		dataMap.put("recycle_time", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		dataMap.put("recycle_status_id", status);
		//然后返回StoreInfo对象，一个collectData产生一个StoreInfo，
		info.setLabel("updateTaskRecoverStatusRecords");
		info.setDataMap(dataMap);
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		infoList.add(info);
		sendStore(infoList);
	//	ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	public void updateMonitorStatus(long monitorId){
		StoreInfo info = new StoreInfo();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("monitor_id", monitorId);
		map.put("online_state", 1);
		info.setDataMap(map);
		info.setLabel("updateMoniTransferResultStore");
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	public void updateRecoverTime(CollectData data, TaskType type) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("freq_id", data.getCollectTask().getBusTask().getTaskfreq_id());
		dataMap.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		dataMap.put("task_type", type);
		dataMap.put("recover_time", data.getCollectTask().getBusTask().getRecover_end());//回收时间从收测单元结束时间属性中提取
		info.setDataMap(dataMap);
		info.setLabel(ProcessConstants.StoreTopic.UPDATE_MANUAL_TASK_RECOVER_TIME);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	
	public void sendStore(List<StoreInfo> infoList) {
		if(infoList != null && !infoList.isEmpty()) {
			for(StoreInfo info : infoList) {
				ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
			}
		}
	}
	
	public StoreInfo buildStore(String label, QualityDataStore qData) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> map = null;
		try {
			map = NMBeanUtils.getObjectField(qData);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}finally {
			qData = null;
		}
		return info;
	}
	
	public String createTaskMsg(CollectTask task, String taskInfo, String taskStatus) {
		StringBuffer descStr = new StringBuffer();
		String taskSta = null;
		switch(taskStatus) {
		case ProcessConstants.TASK_RECOVER_TIME_OUT:
			taskSta = "超时";
			break;
		case ProcessConstants.TASK_RECOVER_SUCCESS:
			taskSta = "成功";
			break;
		case ProcessConstants.TASK_RECOVER_FAUIL:
			taskSta = "失败";
			break;
			
		}
		descStr.append(taskInfo + taskSta);
		descStr.append(";任务信息=");
		descStr.append("站点：" + task.getBusTask().getMonitor_code() + ",");
		descStr.append("任务名称：" + task.getBusTask().getTask_name() + ",");
		descStr.append("频率：" + task.getBusTask().getFreq() + ",");
		ProtocolType type = task.getData().getOriType();
		String taskType = null;
		switch(type) {
		case QualityTaskSet:
			taskType = "指标任务";
			break;
		case SpectrumTaskSet:
			taskType = "频谱任务";
			break;
		case StreamTaskSet:
			taskType = "音频任务";
			break;
		case OffsetTaskSet:
			taskType = "频偏任务";
			break;
		case TaskDelete:
			taskType = "删除任务";
			break;
		default:
			Log.warn("未找到对象的任务类型");
			break;
		}
		descStr.append("任务类型：" + taskType);
		
		return descStr.toString();
	}
	

	
//	public void recordFileSize(CollectData data, int listSize) {
////		Timer timer = new Timer(true);
////		TimeOutTask task = new TimeOutTask(data);
////		timer.schedule(task, 120000);
//		//不采用timer计时，只进行轮训时间比对计时
//		long time = System.currentTimeMillis();
//		CollectTaskModel.getInstance().setRecordSize(data, listSize,time);
//	}
	
	/**
	 * 对象数据转换成协议数据
	 * 
	 * @param task
	 * @return
	 * @throws NXmlException
	 */
	protected Report protolXMLToObj(String protcolXML) throws NXmlException {
		IProtocolConver iConver = null;
		Report report = null;
//		int versionId = ProtocolUtils.getProtocolVersion(protcolXML);
		int versionId = getNewVersion(protcolXML);
		switch(versionId) {
		case 8:
			iConver = ProtocolConverFactory.getInstance().newProtocolConverServiceV8();
			break;
		case 7:
			iConver = ProtocolConverFactory.getInstance().newProtocolConverServiceV7();
			break;	
		case 6:
			iConver = ProtocolConverFactory.getInstance().newProtocolConverServiceV6();
			break;	
		case 5:
			iConver = ProtocolConverFactory.getInstance().newProtocolConverServiceV5();
			break;	
		}
		System.out.println(protcolXML);
		report  = iConver.decodeReport(protcolXML);
		return report;
	}
	
	private static int getNewVersion(String protcolXML) {
		int version = 8;
		if(protcolXML.contains("Version=\"8\""))
			version = 8;
		else if(protcolXML.contains("Version=\"7\""))
			version = 7;
		else if(protcolXML.contains("Version=\"6\""))
			version = 6;
		else if(protcolXML.contains("Version=\"5\""))
			version = 5;
		else if(protcolXML.contains("Version=\"Version\""))
			version = 6;
		return version;
		
	}
	
//	private class TimeOutTask extends TimerTask {
//	
//		private CollectData data;
//		private CollectTaskModel model = CollectTaskModel.getInstance();
//	
//		public TimeOutTask(CollectData data) {
//			this.data = data;
//		}
//	
//		@Override
//		public void run() {
//			com.neusoft.gbw.cp.core.collect.TaskType type = data.getCollectTask().getTaskType();
//			if(type== null || !type.equals("system")) //不是系统任务，暂时不进行系统时间更新
//				return;
//			
//			Log.debug("本批次录音文件接收超时，taskId=" + data.getCollectTask().getBusTask().getTask_id());
//			//更新任务回收时间
//			updateRecoverTime(data, TaskType.StreamTask);
//			//更新任务回收计数
//			updateRecoverCount(data, false);
//			model.removeRecordTask(data.getCollectTask().getBusTask().getTask_id() + "");
//		}
//    }
	public void sleep(long millin) {
		try {
			Thread.sleep(millin);
		} catch (InterruptedException e) {
		}
	}
}
