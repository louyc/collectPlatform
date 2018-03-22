package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.Record;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.TaskRecord;
import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.process.measure.vo.auto.ManualMeasureUnitStore;
import com.neusoft.gbw.cp.process.measure.vo.manual.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
/**
 * 旧录音回收 已废弃
 * @author yanghao
 *
 */
public class StreamHisTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String FILE_NAME = "FILE_NAME";
	private final static String FILE_SIZE = "FILE_SIZE";
	private final static String START_TIME = "START_TIME";
	private final static String END_TIME = "END_TIME";
	private final static String RECORD_BAND = "BAND";
	private final static String FREQ = "FREQ";
	private final static String EQU_CODE = "EQU_CODE";
	private final static String UNIT_STORE = "UNIT_STORE";
	private final static String RECORD_DATA = "RECORD_DATA";
	private final static String RECORD_ID = "RECORD_ID";
//	private final static String stream_desc = "录音任务回收";
//	private CollectTaskModel model = CollectTaskModel.getInstance();
//	private FtpDownProcess ftpProcess = new FtpDownProcess();
			
	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			disposeData(data);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isActive(data))  {
				updateRecoverCount(data, true);
				break;
			}
			
			disposeData(data);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			//0：未回收  1：回收成功   	2：录音回收失败    3：指标回收失败   4：频谱回收失败 
			disposeTaskRecoverStatus(data,2);
//			disposeError(data);
			//异太报警插入  20170413 取消
//			insertGbalLocalqualityEventDat(data);
			break;
		}
		return null;
	}

	public void insertGbalLocalqualityEventDat(CollectData data){
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("monitorId", data.getCollectTask().getBusTask().getMonitor_id()+"");
		dataMap.put("machineId", data.getCollectTask().getBusTask().getManufacturer_id()+"");
		dataMap.put("modelId", "1");
		dataMap.put("eventTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());
		dataMap.put("isResume", "0"); // 0 未恢复   1已处理
		dataMap.put("freq", data.getCollectTask().getBusTask().getFreq());
		dataMap.put("eventTypeId", "4");
		dataMap.put("description", "回收失败");
		dataMap.put("eventReason", "录音回收失败");
		dataMap.put("eLevel", "");
		dataMap.put("fmModulation", "");
		dataMap.put("amModulation", "");
		dataMap.put("attenuation", "");
		dataMap.put("reportTypeId", "0");
		dataMap.put("bandId", "0");
		info.setLabel("insertQualityAlarm1");
		info.setDataMap(dataMap);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			disposeV7Data(data);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV7Active(data))  {
				updateRecoverCount(data, true);
				break;
			}
			disposeV7Data(data);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			disposeTaskRecoverStatus(data,2);
			//异太报警插入
//			insertGbalLocalqualityEventDat(data);
//			disposeError(data);
			break;
		}
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			disposeV6Data(data);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV6Active(data))  {
				updateRecoverCount(data, true);
				break;
			}
			
			disposeV6Data(data);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			disposeTaskRecoverStatus(data,2);
			//异太报警插入
//			insertGbalLocalqualityEventDat(data);
//			disposeError(data);
			break;
		}
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		ReportStatus status = data.getStatus();
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		switch(status) {
		case date_collect_active_report:
			list.addAll(disposeV5Data(data));
			sendStore(list);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV5Active(data))  {
				updateRecoverCount(data, true);
				break;
			}
			
			list.addAll(disposeV5Data(data));
			sendStore(list);
			//更新任务回收时间
			updateRecoverTime(data, TaskType.StreamTask);
			//更新任务回收计数
			updateRecoverCount(data, true);
			//更新回收状态
			disposeTaskRecoverStatus(data,1);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,2);
			break;
		default:
			Log.warn("[实时处理服务]录音任务手动回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			disposeTaskRecoverStatus(data,2);
			//异太报警插入
//			insertGbalLocalqualityEventDat(data);
			break;
		}
		return null;
	}
	
	private void disposeData(CollectData data) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return;
//			String url = ((com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析录音文件zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析录音文件zip数据文件包错误，" + getLogMsg(data));
//				return;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return;
//			}
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
			return;
		}
		for(TaskRecord task : reList) {
			for(Record  result : task.getRecords()) {
				map = new HashMap<String, Object>();
				
				ManualMeasureUnitStore unit = new ManualMeasureUnitStore();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setUnit_begin(result.getStartDateTime());
				unit.setUnit_end(result.getEndDateTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				unit.setUnit_interval(data.getCollectTask().getBusTask().getMeasure_unit_time());
				
				StoreInfo info = buildStoreInfo("insertMeasureManualUnit", unit);
				map.put(UNIT_STORE, info);
				map.put(FREQ, task.getFreq());
				map.put(EQU_CODE, report.getEquCode());
				map.put(RECORD_BAND, task.getBand());
				map.put(FILE_NAME, result.getFileName());
				map.put(FILE_SIZE, result.getSize());
				map.put(RECORD_ID, result.getRecordID());
				map.put(START_TIME, result.getStartDateTime());
				map.put(END_TIME, result.getEndDateTime());
				dataList.add(map);
			}
		}
		reList.clear();
		reList = null;
		if(!dataList.isEmpty()) {
			//记录本次录音要下载的文件个数
//			recordFileSize(data, dataList.size());
			uploadStreamFile(data.getCollectTask(),dataList); 
			
		}
		
	}
	
	private void disposeV7Data(CollectData data) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return;
//			String url = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析录音文件zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析录音文件zip数据文件包错误，" + getLogMsg(data));
//				return;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return;
//			}
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
			return;
		}
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Record  result : task.getRecords()) {
				map = new HashMap<String, Object>();
				
				ManualMeasureUnitStore unit = new ManualMeasureUnitStore();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setUnit_begin(result.getStartDateTime());
				unit.setUnit_end(result.getEndDateTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				unit.setUnit_interval(data.getCollectTask().getBusTask().getMeasure_unit_time());
				
				StoreInfo info = buildStoreInfo("insertMeasureManualUnit", unit);
				map.put(UNIT_STORE, info);
				map.put(EQU_CODE, report.getEquCode());
				map.put(FREQ, task.getFreq());
				map.put(RECORD_ID, result.getRecordID());
				map.put(RECORD_BAND, task.getBand());
				map.put(FILE_NAME, result.getFileName());
				map.put(FILE_SIZE, result.getSize());
				map.put(START_TIME, result.getStartDateTime());
				map.put(END_TIME, result.getEndDateTime());
				dataList.add(map);
			}
		}
		reList.clear();
		reList = null;
		if(!dataList.isEmpty()) {
//			recordFileSize(data, dataList.size());
			uploadStreamFile(data.getCollectTask(),dataList); 
		}
	}
	
	private void disposeV6Data(CollectData data) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport report = null;
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport == null)
			return;
		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return;
//			String url = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析录音文件zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析录音文件zip数据文件包错误，" + getLogMsg(data));
//				return;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return;
//			}
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
			return;
		}
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.Record  result : task.getRecords()) {
				map = new HashMap<String, Object>();
				
				ManualMeasureUnitStore unit = new ManualMeasureUnitStore();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setUnit_begin(result.getStartDateTime());
				unit.setUnit_end(result.getEndDateTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				unit.setUnit_interval(data.getCollectTask().getBusTask().getMeasure_unit_time());
				
				StoreInfo info = buildStoreInfo("insertMeasureManualUnit", unit);
				map.put(UNIT_STORE, info);
				map.put(EQU_CODE, report.getEquCode());
				map.put(FREQ, task.getFreq());
				map.put(RECORD_ID, result.getRecordID());
				map.put(RECORD_BAND, task.getBand());
				map.put(FILE_NAME, result.getFileName());
				map.put(FILE_SIZE, result.getSize());
				map.put(START_TIME, result.getStartDateTime());
				map.put(END_TIME, result.getEndDateTime());
				dataList.add(map);
			}
		}
		reList.clear();
		reList = null;
		if(!dataList.isEmpty()) {
//			recordFileSize(data, dataList.size());
			uploadStreamFile(data.getCollectTask(),dataList); 
		}
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
//			String url = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析录音文件zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析录音文件zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
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
				StoreInfo info = new StoreInfo();
				
				ManualMeasureUnitStore unit = new ManualMeasureUnitStore();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setUnit_begin(result.getStartDateTime());
				unit.setUnit_end(result.getEndDateTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				unit.setUnit_interval(data.getCollectTask().getBusTask().getMeasure_unit_time());
				
				info = buildStoreInfo("insertMeasureManualUnit", unit);
				list.add(info);
			}
		}
		reList.clear();
		reList = null;
		return list;
	}
	
	private StoreInfo buildStoreInfo(String label, Object data) {
		Map<String, Object> map = null;
		StoreInfo info = new StoreInfo();
		try {
			map = NMBeanUtils.getObjectField(data);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		return info;
	}
	
	private void uploadStreamFile(CollectTask task, List<Map<String, Object>> dataList) {
		//添加文件名、文件大小
		task.addExpandObj(RECORD_DATA, dataList);//多个文件同时发送
		ARSFToolkit.sendEvent(EventServiceTopic.UPLOAD_STREAM_FILE_TOPIC, task);
	}
	
//	private void disposeError(CollectData data) {
//		CollectTask task = data.getCollectTask();
//		if(task == null) 
//			return;
//		if(task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY) == null)
//			return;
////		sendWebMsg(data.getCollectTask(),ProcessConstants.TASK_RECOVER_FAUIL, stream_desc);
//		model.cancelTaskTime(task.getBusTask().getTask_id() + "");
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
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return isActive;
					
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
			return isActive;
					
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
			return isActive;
					
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
			return isActive;
					
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
