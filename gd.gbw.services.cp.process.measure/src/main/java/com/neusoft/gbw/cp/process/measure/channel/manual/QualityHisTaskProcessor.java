package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReportQuality;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex;
import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.process.measure.vo.manual.QualityDataStore;
import com.neusoft.gbw.cp.process.measure.vo.manual.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class QualityHisTaskProcessor extends RecoverTaskProcessor implements ITaskProcess{
	private final static String level = "taskLevel";
	private final static String amodulation = "taskAMModulation";
	private final static String attenuation = "taskAttenuation";
	private final static String bandwidth = "taskBandwidth";
	private final static String fmodulation = "taskFMModulation";
	private final static String ammodulationRate = "taskAMModulationRate";//V5版本调幅
	private final static String fmmodulationRate = "taskFMModulationRate";//V5调制度
//	private final static String quality_desc = "指标任务回收";
	private CollectTaskModel model = CollectTaskModel.getInstance();
//	private FtpDownProcess ftpProcess = new FtpDownProcess();

	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		Log.debug("解析状态  status**********************"+status);
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV8(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isActive(data))  
				break;
			
			list.addAll(saveDataV8(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,3);
			break;
		default:
			Log.warn("[任务回收服务]指标任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			disposeTaskRecoverStatus(data);
			//异太报警插入  20170413 取消
//			insertGbalLocalqualityEventDat(data);
//			disposeError(data);
			break;
		}
		
		return list;
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
		dataMap.put("eventReason", "指标回收失败");
		dataMap.put("eLevel", "");
		dataMap.put("fmModulation", "");
		dataMap.put("amModulation", "");
		dataMap.put("attenuation", "");
		dataMap.put("reportTypeId", "0");
		dataMap.put("bandId", "0");
		Log.debug("报警插入的数据：：：：：：：：：********：："+dataMap.entrySet());
		info.setLabel("insertQualityAlarm1");
		info.setDataMap(dataMap);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	public void disposeTaskRecoverStatus(CollectData data) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("recycle_user_id", "system");
		dataMap.put("recycle_time", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		dataMap.put("recycle_status_id", 3);
		//0：未回收  1：回收成功   	2：录音回收失败    3：指标回收失败   4：频谱回收失败 
		//然后返回StoreInfo对象，一个collectData产生一个StoreInfo，
		info.setLabel("updateTaskRecoverStatusRecords");
		info.setDataMap(dataMap);
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		infoList.add(info);
		sendStore(infoList);
	//	ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	
	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		Log.debug("解析状态  status**********************"+status);
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV7(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV7Active(data))  
				break;
			list.addAll(saveDataV7(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,3);
			break;
		default:
			Log.warn("[任务回收服务]指标任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			disposeTaskRecoverStatus(data);
			//异太报警插入  20170413 取消
//			insertGbalLocalqualityEventDat(data);
//			disposeError(data);
			break;
		}
		
		return list;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		Log.debug("解析状态  status**********************"+status);
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV6(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			//如果是主动上报，如果没有数据就更新状态，有数据再丢弃
			if(isV6Active(data)) 
				break;
			
			list.addAll(saveDataV6(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,3);
			break;
		default:
			Log.warn("[任务回收服务]指标任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			disposeTaskRecoverStatus(data);
			//异太报警插入  20170413 取消
//			insertGbalLocalqualityEventDat(data);
//			disposeError(data);
			break;
		}
		
		return list;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		Log.debug("解析状态  status**********************"+status);
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV5(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV5Active(data)) 
				break;
			
			list.addAll(saveDataV5(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.QualityTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,3);
			break;
		default:
			Log.warn("[任务回收服务]指标任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
			//更新回收状态
			disposeTaskRecoverStatus(data);
			//异太报警插入  20170413 取消
//			insertGbalLocalqualityEventDat(data);
//			disposeError(data);
			break;
		}
		
		return list;
	}
	
	private List<StoreInfo> saveDataV8(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			if(xml == null) {
//				Log.debug("解析指标zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV8QualityHistoryReport(data);
		}
		if(report == null)
			return list;
		List<com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReportQuality> qaList = report.getQualityAlarm();
		if(qaList == null || qaList.size() <= 0)
			return list;
		String equCode = report.getEquCode();
		for(QualityHistoryReportQuality quality : qaList) {
			if(quality == null || quality.getQualityHistoryReport_Quality_QualityIndexs() == null)
				return list;
			for(QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(amodulation);
					qData.setQuality_code(amodulation);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				case 5:
					label = model.getQualLabel(fmodulation);
					qData.setQuality_code(fmodulation);
					qData.setMax_value(index.getMaxValue());
					qData.setMin_value(index.getMinValue());
					break;
				case 8:
					label = model.getQualLabel(bandwidth);
					qData.setQuality_code(bandwidth);
					qData.setQuality_value(index.getValue());
					break;
				}
				//去除主动上报数据功能，采用定时回收功能，所以不进行去重操作
//				if(isExistData(qData))
//					continue;
//				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		qaList.clear();
		qaList = null;
		return list;
	}
	
	private List<StoreInfo> saveDataV7(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析指标zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析指标zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV7QualityHistoryReport(data);
		}
		if(report == null)
			return list;
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality> qaList = report.getQualityAlarm();
		if(qaList == null || qaList.size() <= 0)
			return list;
		String equCode = report.getEquCode();
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality quality : qaList) {
			if(quality == null || quality.getQualityHistoryReport_Quality_QualityIndexs() == null)
				return list;
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(amodulation);
					qData.setQuality_code(amodulation);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				case 5:
					label = model.getQualLabel(fmodulation);
					qData.setQuality_code(fmodulation);
					qData.setMax_value(index.getMaxValue());
					qData.setMin_value(index.getMinValue());
					break;
				}
				//去除主动上报数据功能，采用定时回收功能，所以不进行去重操作
//				if(isExistData(qData))
//					continue;
//				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		qaList.clear();
		qaList = null;
		return list;
	}
	
	private List<StoreInfo> saveDataV5(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析指标zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析指标zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV5QualityHistoryReport(data);
		}
		if(report == null)
			return list;
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQuality> qaList = report.getQualityAlarm();
		if(qaList == null || qaList.size() <= 0)
			return list;
		String equCode = report.getEquCode();
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQuality quality : qaList) {
			if(quality == null || quality.getQualityHistoryReport_Quality_QualityIndexs() == null)
				return list;
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 2:
					label = model.getQualLabel(ammodulationRate);
					qData.setQuality_code(ammodulationRate);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(fmmodulationRate);
					qData.setQuality_code(fmmodulationRate);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				}
				
				//去除主动上报数据功能，采用定时回收功能，所以不进行去重操作
//				if(isExistData(qData))
//					continue;
//				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		qaList.clear();
		qaList = null;
		return list;
	}
	
	private List<StoreInfo> saveDataV6(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			//更新任务回收计数
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析指标zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析指标zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV6QualityHistoryReport(data);
		}
		
		if(report == null)
			return list;
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQuality> qaList = report.getQualityAlarm();
		if(qaList == null || qaList.size() <= 0)
			return list;
		String equCode = report.getEquCode();
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQuality quality : qaList) {
			if(quality == null || quality.getQualityHistoryReport_Quality_QualityIndexs() == null)
				return list;
			
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(amodulation);
					qData.setQuality_code(amodulation);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				case 5:
					label = model.getQualLabel(fmodulation);
					qData.setQuality_code(fmodulation);
					qData.setMax_value(index.getMaxValue());
					qData.setMin_value(index.getMinValue());
					break;
				}
				//去除主动上报数据功能，采用定时回收功能，所以不进行去重操作
//				if(isExistData(qData))
//					continue;
//				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		qaList.clear();
		qaList = null;
		return list;
	}
	
	private com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport getV8QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport getV7QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport getV5QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport getV6QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
//	private void disposeError(CollectData data) {
//		CollectTask task = data.getCollectTask();
//		if(task == null) 
//			return;
//		if(task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY) == null)
//			return;
////		sendWebMsg(data.getCollectTask(),ProcessConstants.TASK_RECOVER_FAUIL, quality_desc);
//		model.cancelTaskTime(task.getBusTask().getTask_id() + "");
//	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code()+ ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
	
//	private Report disposeAutoReportFile(String xml) throws NXmlException {
//		return protolXMLToObj(xml);
//	}
	
	private boolean isActive(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false;
					
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
			return false;
					
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
			return false;
					
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
			return false;
					
		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}
	
//	private boolean isExistData(QualityDataStore qData) {
//		String uniKey = getUniKey(qData);
//		return model.getRecoverDataMap(uniKey);
//	}
//	
//	private String getUniKey(QualityDataStore qData) {
//		return qData.getTask_id() + "_" + qData.getMonitor_id() + "_" + qData.getFrequency() + "_" + qData.getQuality_code() + "_" + qData.getCollect_time();
//	}
}
