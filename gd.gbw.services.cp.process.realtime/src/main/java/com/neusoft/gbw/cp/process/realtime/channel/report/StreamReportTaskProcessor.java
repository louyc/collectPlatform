package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.Record;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.TaskRecord;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.ManualMeasureUnit;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class StreamReportTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String FILE_NAME = "FILE_NAME";
	private final static String FILE_SIZE = "FILE_SIZE";
	private final static String START_TIME = "START_TIME";
	private final static String END_TIME = "END_TIME";
	private final static String RECORD_BAND = "BAND";
	private final static String EQU_CODE = "EQU_CODE";
	private final static String UNIT_STORE = "UNIT_STORE";
	
			
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			disposeData(data);
			break;
		default:
			Log.warn("[实时处理服务]录音任务主动上报处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		}
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			disposeV7Data(data);
			break;
		default:
			Log.warn("[实时处理服务]录音任务主动上报处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		}
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			disposeV6Data(data);
			break;
		default:
			Log.warn("[实时处理服务]录音任务主动上报处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		}
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		ReportStatus status = data.getStatus();
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		switch(status) {
		case date_collect_active_report:
			list.addAll(disposeV5Data(data));
			sendStore(list);
			break;
		default:
			Log.warn("[实时处理服务]录音任务主动上报处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		}
		return null;
	}
	
	private void disposeData(CollectData data) {
		StreamHistoryReport report = (StreamHistoryReport)((Report)data.getData().getReportData()).getReport();
		List<TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			return;
		}
		for(TaskRecord task : reList) {
			for(Record  result : task.getRecords()) {
				String file_name = result.getFileName();
				String file_size = result.getSize();
				String start_time = result.getStartDateTime();
				String end_time = result.getEndDateTime();
				
				ManualMeasureUnit unit = new ManualMeasureUnit();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				
				StoreInfo info = buildStoreInfo("insertMeasureManualUnit", unit);
				uploadStreamFile(data.getCollectTask(), file_name, file_size, start_time, end_time, report.getEquCode(), task.getBand(), info); 
			}
		}
	}
	
	private void disposeV7Data(CollectData data) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport report = getV7Report(data);
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			return;
		}
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Record  result : task.getRecords()) {
				String file_name = result.getFileName();
				String file_size = result.getSize();
				String start_time = result.getStartDateTime();
				String end_time = result.getEndDateTime();
				
				ManualMeasureUnit unit = new ManualMeasureUnit();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				
				StoreInfo info = buildStoreInfo("insertMeasureManualUnit", unit);
				uploadStreamFile(data.getCollectTask(), file_name, file_size, start_time, end_time, report.getEquCode(), task.getBand(), info); 
			}
		}
	}
	
	private void disposeV6Data(CollectData data) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport report = getV6Report(data);
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			return;
		}
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.Record  result : task.getRecords()) {
				String file_name = result.getFileName();
				String file_size = result.getSize();
				String start_time = result.getStartDateTime();
				String end_time = result.getEndDateTime();
				
				ManualMeasureUnit unit = new ManualMeasureUnit();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				
				StoreInfo info = buildStoreInfo("insertMeasureManualUnit", unit);
				uploadStreamFile(data.getCollectTask(), file_name, file_size, start_time, end_time, report.getEquCode(), task.getBand(), info); 
			}
		}
	}
	
	/**
	 * v5版本无回收文件功能
	 * @param data
	 * @return
	 */
	private List<StoreInfo> disposeV5Data(CollectData data) {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport report = getV5Report(data);
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.TaskRecord> reList = report.getTaskRecords();
		if(reList == null || reList.size() <= 0) {
			Log.warn("[实时处理服务]录音任务主动上报处理未找到record记录，" + "," + getLogMsg(data));
			return list;
		}
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.TaskRecord task : reList) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.Record  result : task.getRecords()) {
				StoreInfo info = new StoreInfo();
				
				ManualMeasureUnit unit = new ManualMeasureUnit();
				unit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				unit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
				unit.setReciever_code(report.getEquCode());
				unit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
				unit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
				unit.setUnit_create_time(result.getStartDateTime());
				unit.setUnit_collect_time(data.getCollectTime());
				unit.setFreq(task.getFreq());
				unit.setRadio_url(result.getURL());
				unit.setUnit_status_id(1);
				
				info = buildStoreInfo("insertMeasureManualUnit", unit);
				list.add(info);
			}
		}

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
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport getV7Report(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport getV6Report(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport)((Report)data.getData().getReportData()).getReport();
	}

	private com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport getV5Report(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport)((Report)data.getData().getReportData()).getReport();
	}

	private void uploadStreamFile(CollectTask task, String file_name, String file_size, String start_time,String end_time, String code, String band, StoreInfo info) {
		//添加文件名、文件大小
		task.addExpandObj(FILE_NAME, file_name);
		task.addExpandObj(FILE_SIZE, file_size);
		task.addExpandObj(START_TIME, start_time);
		task.addExpandObj(END_TIME, end_time);
		task.addExpandObj(EQU_CODE, code);
		task.addExpandObj(RECORD_BAND, band);
		task.addExpandObj(UNIT_STORE, info);
		ARSFToolkit.sendEvent(EventServiceTopic.UPLOAD_STREAM_FILE_TOPIC, task);
		sleep(2000);
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
}
