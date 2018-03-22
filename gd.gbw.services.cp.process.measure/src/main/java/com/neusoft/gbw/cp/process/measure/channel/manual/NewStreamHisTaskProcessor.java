package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.Record;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.TaskRecord;
import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.process.measure.vo.manual.QualityDataStore;
import com.neusoft.gbw.cp.process.measure.vo.manual.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;

public class NewStreamHisTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String taskRecord = "taskRecord";
			
	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(disposeData(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isActive(data))  
				break;
			
			list.addAll(disposeData(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(disposeV7Data(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV7Active(data))  
				break;
			
			list.addAll(disposeV7Data(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(disposeV6Data(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV6Active(data))  
				break;
			
			list.addAll(disposeV6Data(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(disposeV5Data(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV5Active(data))  
				break;
			
			list.addAll(disposeV5Data(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.StreamTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			break;
		}
		return null;
	}
	
	private List<StoreInfo> disposeData(CollectData data) {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
//		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
//		Map<String, Object> map = null;
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return list;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
		}else {
			report = (StreamHistoryReport)ireport;
		}
		List<TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			return list;
		}
		for(TaskRecord task : reList) {
			for(Record  result : task.getRecords()) {
				
				QualityDataStore qData = new QualityDataStore();
				qData.setBand(Integer.parseInt(task.getBand()));
				qData.setCollect_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setFrequency(task.getFreq());
				qData.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				qData.setQuality_code(taskRecord);
				qData.setQuality_value(result.getFileName());
				qData.setReceiver_code(report.getEquCode());
				qData.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setUrl(result.getURL());
				qData.setRecord_stime(result.getStartDateTime());
				qData.setRecord_etime(result.getEndDateTime());
				qData.setRecord_size((Integer.parseInt(result.getSize())));
				qData.setFile_name(result.getFileName());
				qData.setRecord_id(Integer.parseInt(result.getRecordID()));
				qData.setRecover_status(0);
				qData.setTask_unique_id(getTaskID(data.getCollectTask()));
				
				String label = CollectTaskModel.getInstance().getQualLabel(taskRecord);
				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		reList.clear();
		reList = null;
		return list;
	}
	
	private List<StoreInfo> disposeV7Data(CollectData data) {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return list;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
		}else {
			report = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport)ireport;
		}
		
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			return list;
		}
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Record  result : task.getRecords()) {
				
				QualityDataStore qData = new QualityDataStore();
				qData.setBand(Integer.parseInt(task.getBand()));
				qData.setCollect_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setFrequency(task.getFreq());
				qData.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				qData.setQuality_code(taskRecord);
				qData.setQuality_value(result.getFileName());
				qData.setReceiver_code(report.getEquCode());
				qData.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setUrl(result.getURL());
				qData.setRecord_stime(result.getStartDateTime());
				qData.setRecord_etime(result.getEndDateTime());
				qData.setRecord_size((Integer.parseInt(result.getSize())));
				qData.setFile_name(result.getFileName());
				qData.setRecord_id(Integer.parseInt(result.getRecordID()));
				qData.setRecover_status(0);
				qData.setTask_unique_id(getTaskID(data.getCollectTask()));
				String label = CollectTaskModel.getInstance().getQualLabel(taskRecord);
				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		reList.clear();
		reList = null;
		return list;
	}
	
	private List<StoreInfo> disposeV6Data(CollectData data) {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return list;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
		}else {
			report = (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport)ireport;
		}
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			return list;
		}
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.Record  result : task.getRecords()) {
				
				QualityDataStore qData = new QualityDataStore();
				qData.setBand(Integer.parseInt(task.getBand()));
				qData.setCollect_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setFrequency(task.getFreq());
				qData.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				qData.setQuality_code(taskRecord);
				qData.setQuality_value(result.getFileName());
				qData.setReceiver_code(report.getEquCode());
				qData.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setUrl(result.getURL());
				qData.setRecord_stime(result.getStartDateTime());
				qData.setRecord_etime(result.getEndDateTime());
				qData.setRecord_size((Integer.parseInt(result.getSize())));
				qData.setFile_name(result.getFileName());
				qData.setRecord_id(Integer.parseInt(result.getRecordID()));
				qData.setRecover_status(0);
				qData.setTask_unique_id(getTaskID(data.getCollectTask()));
				String label = CollectTaskModel.getInstance().getQualLabel(taskRecord);
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		reList.clear();
		reList = null;
		return list;
	}
	
	/**
	 * v5版本无回收文件功能
	 * @param data
	 * @return
	 */
	private List<StoreInfo> disposeV5Data(CollectData data) {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return list;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
		}else {
			report = (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport)ireport;
		}
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			return list;
		}
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.Record  result : task.getRecords()) {
				
				QualityDataStore qData = new QualityDataStore();
				qData.setBand(Integer.parseInt(task.getBand()));
				qData.setCollect_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setFrequency(task.getFreq());
				qData.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				qData.setQuality_code(taskRecord);
//				qData.setQuality_value(result.getFileName());
				qData.setReceiver_code(report.getEquCode());
				qData.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				qData.setRecord_stime(result.getStartDateTime());
				qData.setRecord_etime(result.getEndDateTime());
//				qData.setRecord_size((Integer.parseInt(result.getSize())));
//				qData.setFile_name(result.getFileName());
				qData.setRecord_id(Integer.parseInt(result.getRecordID()));
				qData.setTask_unique_id(getTaskID(data.getCollectTask()));
				qData.setRecover_status(0);
				String label = CollectTaskModel.getInstance().getQualLabel(taskRecord);
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		reList.clear();
		reList = null;
		return list;
	}
	
//	private StoreInfo buildStoreInfo(String label, Object data) {
//		Map<String, Object> map = null;
//		StoreInfo info = new StoreInfo();
//		try {
//			map = NMBeanUtils.getObjectField(data);
//			info.setDataMap(map);
//			info.setLabel(label);
//		} catch (NMBeanUtilsException e) {
//			Log.error("", e);
//		}
//		return info;
//	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code() + ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
	
	private boolean isActive(CollectData data) {
		//查看回收数据信息，1、返回格式解析失败，2、没有数据，3、主动上报，4、
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false; //如果没有数据，继续进行下一步流程
					
		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}
	
	private boolean isV7Active(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false; //如果没有数据，继续进行下一步流程
					
		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}
	
	private boolean isV6Active(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false; //如果没有数据，继续进行下一步流程
					
		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}
	
	private boolean isV5Active(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false; //如果没有数据，继续进行下一步流程
					
		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}
//	private Report disposeAutoReportFile(String xml) throws NXmlException {
//		return protolXMLToObj(xml);
//	}
	
//	private void sleep(long millis) {
//		try {
//			Thread.sleep(millis);
//		} catch (InterruptedException e) {
//		}
//	}
}
