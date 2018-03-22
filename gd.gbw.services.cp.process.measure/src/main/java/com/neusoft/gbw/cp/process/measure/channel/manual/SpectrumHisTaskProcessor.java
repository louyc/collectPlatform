package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.HistoryScanResult;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumScan;
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

public class SpectrumHisTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String spectrum = "taskSpectrum";
//	private final static String quality_desc = "频谱任务回收";
	private CollectTaskModel model = CollectTaskModel.getInstance();
//	private FtpDownProcess ftpProcess = new FtpDownProcess();

	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV8(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isActive(data))  
				break;
			
			list.addAll(saveDataV8(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,4);
			break;
		default:
			Log.warn("[任务回收服务]频谱任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		
		return list;
	}

	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV7(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV7Active(data))  
				break;
			
			list.addAll(saveDataV7(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,4);
			break;
		default:
			Log.warn("[任务回收服务]频谱任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		
		return list;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV6(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV6Active(data))  
				break;
			
			list.addAll(saveDataV6(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,4);
			break;
		default:
			Log.warn("[任务回收服务]频谱任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		
		return list;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV5(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV5Active(data))  
				break;
			
			list.addAll(saveDataV5(data));
			//处理任务数据，更新任务状态
			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			break;
		case data_collect_null:
			updateRecoverNullSize(data,4);
			break;
		default:
			Log.warn("[任务回收服务]频谱任务回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
			updateRecoverCount(data, false);
//			disposeError(data);
			break;
		}
		
		return list;
	}

	private List<StoreInfo> saveDataV8(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析频谱zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析频谱zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV8SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		String equCode = report.getEquCode();
		List<com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumScan> ssList = report.getSpectrumScan();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(SpectrumScan quality : ssList) {
			if(null==quality.getScanResults() || quality.getScanResults().size()<1){
				continue;
			}
			for(HistoryScanResult result : quality.getScanResults()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getScanDateTime());
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
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
		ssList.clear();
		ssList = null;
		return list;
	}
	
//	private Report disposeAutoReportFile(String xml) throws NXmlException {
//		return protolXMLToObj(xml);
//	}
	
	private List<StoreInfo> saveDataV7(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析频谱zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析频谱zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV7SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		String equCode = report.getEquCode();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan> ssList = report.getSpectrumScan();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan quality : ssList) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.HistoryScanResult result : quality.getScanResults()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setCollect_time(quality.getScanDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
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
		ssList.clear();
		ssList = null;
		return list;
	}
	
	private List<StoreInfo> saveDataV6(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析频谱zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析频谱zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV6SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		String equCode = report.getEquCode();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumScan> ssList = report.getSpectrumScan();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumScan quality : ssList) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.HistoryScanResult result : quality.getScanResults()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getScanDateTime());
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
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

		return list;
	}
	
	private List<StoreInfo> saveDataV5(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport report = null;
		
		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) {
			//上报的zip解压处理的过程放入过滤的filter服务
			return list;
//			String url = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
//			String xml = ftpProcess.ftpDownDataFile(url);
//			Log.debug("解析频谱zip数据完成，xml=" + xml);
//			if(xml == null) {
//				Log.debug("解析频谱zip数据文件包错误，" + getLogMsg(data));
//				return list;
//			}
//			try {
//				report = (com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport)disposeAutoReportFile(xml).getReport();
//			} catch (NXmlException e) {
//				Log.error("", e);
//				return list;
//			}
		}else {
			report = getV5SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		String equCode = report.getEquCode();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumScan> ssList = report.getSpectrumScan();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumScan quality : ssList) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.HistoryScanResult result : quality.getScanResults()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setCollect_time(quality.getScanDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
				//去除主动上报数据功能，采用定时回收功能，所以不进行去重操作
//				if(isExistData(qData))
//					continue;
				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		ssList.clear();
		ssList = null;
		return list;
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport getV7SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}

	private com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport getV6SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
	
	private com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport getV5SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
	
	private com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport getV8SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
	
//	private void disposeError(CollectData data) {
//		CollectTask task = data.getCollectTask();
//		if(task == null) 
//			return;
//		if(task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY) == null)
//			return;
////		sendWebMsg(data.getCollectTask(),ProcessConstants.TASK_RECOVER_FAUIL, quality_desc);
////		model.cancelTaskTime(task.getBusTask().getTask_id() + "");
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
	
//	private FileVO getFileVOByUrl(String url) {
//		String tmp = url.replaceFirst("ftp://", "");
//		String[] array = tmp.split("/");
//		FileVO vo = new FileVO();
//		vo.ip = array[0].split(":").length >= 2 ? array[0].split(":")[0] : array[0];
//		vo.zipFileName = array[array.length - 1];
//		vo.remotePath = tmp.replaceAll(array[0], "").replaceAll(array[array.length - 1], "");
//		
//		return vo;
//	}
//	
//	private class FileVO {
//		private String ip;
//		private String remotePath;
//		private String zipFileName;
////		private String fileName;
//	}
	
//	private boolean isExistData(QualityDataStore qData) {
//		String uniKey = getUniKey(qData);
//		return model.getRecoverDataMap(uniKey);
//	}
//	
//	private String getUniKey(QualityDataStore qData) {
//		return qData.getTask_id() + "_" + qData.getMonitor_id() + "_" + qData.getFrequency() + "_" + qData.getQuality_code() + "_" + qData.getCollect_time();
//	}
}
