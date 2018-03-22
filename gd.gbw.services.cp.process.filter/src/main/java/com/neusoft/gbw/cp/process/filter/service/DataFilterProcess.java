package com.neusoft.gbw.cp.process.filter.service;

import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.filter.context.TaskMgrModel;
import com.neusoft.gbw.cp.process.filter.fileconver.FtpDownProcess;
import com.neusoft.np.arsf.base.bundle.Log;

public class DataFilterProcess {
	
	private TaskMgrModel model = TaskMgrModel.getInstance();
	private FtpDownProcess ftpProcess = new FtpDownProcess();
	
	public boolean dispose(CollectData data) {
		boolean isFlag = false;
//		if(data.getRecoveryDateReport() == null) 
//			return false;
		//校验采集数据，
//		if(!validateQuery(data)) 
//			return false;
		
		//校验采集状态,采集数据分类
		ReportStatus status = data.getStatus();
		
		switch(status) {
		case date_collect_success:
			isFlag = true;
			break;
		case date_collect_time_out:
			isFlag = true;
			break;
		case date_collect_occupy:
			isFlag = true;
			break;
		case date_no_collect_time_out:
			isFlag = true;
			break;
		case date_collect_error:
			isFlag = true;
			break;
		case date_collect_active_report:
			disposeActiveReport(data);
			isFlag = true;
			break;
		case date_collect_no_found_task:
			disposeNoCollectTask(data); 
			isFlag = true;
			break;
		case collect_task_analytical_failure:
			isFlag = true;
			break;
		case collect_task_send_failure:
			isFlag = true;
			break;
		case transfer_base_info_validate_failure:
			isFlag = true;
			break;
		case data_collect_null:
			isFlag=true;
			break;
		} 
		return isFlag;
	}	
  
	/**
	 * 处理主动上报数据
	 * @param data
	 */
	private void disposeActiveReport(CollectData data) {
		int version = data.getData().getVersionId();
		switch(version) {
		case 1:
			break;
		case 5:
			data = matchTaskV5(data);
			break;
		case 6:
			data = matchTaskV6(data);
			break;
		case 7:
			data = matchTaskV7(data);
			break;
		case 8:
			data = matchTaskV8(data);
			break;
		}
	}

	/**
	 * 处理没有采集任务数据数据
	 * @param data
	 */
	private void disposeNoCollectTask(CollectData data) {
		int version = data.getData().getVersionId();
		switch(version) {
		case 1:
			break;
		case 5:
			TaskRedirectorProcess.redirectTask(data);
			break;
		case 6:
			TaskRedirectorProcess.redirectTask(data);
			break;
		case 7:
			TaskRedirectorProcess.redirectTask(data);
			break;
		case 8:
			TaskRedirectorProcess.redirectTask(data);
			break;
		}
	}
	
	private CollectData matchTaskV5(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		IReport obj = report.getReport();
		String monitorCode = report.getSrcCode();//增加站点编码，一个任务肯能发送多个站点。
		CollectTask task = null;
		Report taskReport = null;
		
		if(obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) {
			IReport taskObj = null;
			String key = null;
			//解压压缩文件
			String url = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
			String xml = ftpProcess.ftpDownDataFile(url);
			Log.debug("解析任务文件zip数据完成，xml=" + xml);
			if(xml == null) {
				Log.warn("解析任务文件zip数据文件包错误，" + getLogMsg(data));
			}
			try {
				taskReport = disposeAutoReportFile(xml);
			} catch (NXmlException e) {
				Log.error("", e);
			}
			 
			taskObj = taskReport == null ? null :taskReport.getReport();
			
			if (taskObj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport) {
				com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport qReport = (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport) {
				com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport qReport = (com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport) {
				com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport qReport = (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport) {
				com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport qReport = (com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} 
			data.getData().setReportData(taskReport);
			model.remove(key);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport) {
			String code = report.getSrcCode();
			task = model.getValue(code);
			if(task == null)
				Log.warn("[数据过滤]未找到关联设备状态主动上报下发任务， CollectTask=" + task);
			model.remove(code);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieveR) {
			//原key关联
//			String code = report.getSrcCode();
//			task = model.getValue(code);
			//新key关联
			String url = ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieveR)obj).getFileRetrieveR_File().get(0).getFileURL();
			String tmp = url.replaceFirst("ftp://", "");
			String[] array = tmp.split("/");
			String fileName = array[array.length - 1];
			
			task = model.getValue(fileName);
			if(task == null)
				Log.warn("[数据过滤]未找到关联录音文件主动上报下发任务， CollectTask=" + task);
			model.remove(fileName);
		}
		
		if (task == null) {
			task = new CollectTask();
		}
		
		BusinessTask busTask = task.getBusTask();
		if (busTask == null) {
			busTask = new BusinessTask();
		}
		if(busTask.getType() == null || busTask.getType() == BusinessTaskType.measure_realtime)
			busTask.setType(BusinessTaskType.measure_report);
		
		busTask.setType(BusinessTaskType.measure_report);
		task.setBusTask(busTask);
		data.setCollectTask(task);
		
		return data;
	}
	
	private CollectData matchTaskV6(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		IReport obj = report.getReport();
		String monitorCode = report.getSrcCode();//增加站点编码，一个任务肯能发送多个站点。
		CollectTask task = null;
		Report taskReport = null;
		
		if(obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) {
			IReport taskObj = null;
			String key = null;
			//解压压缩文件
			String url = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
			String xml = ftpProcess.ftpDownDataFile(url);
			Log.debug("解析任务文件zip数据完成，xml=" + xml);
			if(xml == null) {
				Log.warn("解析任务文件zip数据文件包错误，" + getLogMsg(data));
			}
			try {
				taskReport = disposeAutoReportFile(xml);
			} catch (NXmlException e) {
				Log.error("", e);
			}
			 
			taskObj = taskReport == null ? null :taskReport.getReport();
			
			if (taskObj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport) {
				com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport qReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport) {
				com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport qReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport) {
				com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport qReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport) {
				com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport qReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} 
			data.getData().setReportData(taskReport);
			model.remove(key);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport) {
			String code = report.getSrcCode();
			task = model.getValue(code);
			if(task == null)
				Log.warn("[数据过滤]未找到关联设备状态主动上报下发任务， CollectTask=" + task);
			model.remove(code);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR) {
			//原key关联
//			String code = report.getSrcCode();
//			task = model.getValue(code);
			//新key关联
			String url = ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR)obj).getFileRetrieveR_File().get(0).getFileURL();
			String tmp = url.replaceFirst("ftp://", "");
			String[] array = tmp.split("/");
			String fileName = array[array.length - 1];
			
			task = model.getValue(fileName);
			if(task == null)
				Log.warn("[数据过滤]未找到关联录音文件主动上报下发任务， CollectTask=" + task);
			
			model.remove(fileName);
		}
		
		if (task == null) {
			task = new CollectTask();
		}
		
		BusinessTask busTask = task.getBusTask();
		if (busTask == null) {
			busTask = new BusinessTask();
		}
		if(busTask.getType() == null || busTask.getType() == BusinessTaskType.measure_realtime)
			busTask.setType(BusinessTaskType.measure_report);
		
		task.setBusTask(busTask);
		data.setCollectTask(task);
		
		return data;
	}
	
	private CollectData matchTaskV7(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		IReport obj = report.getReport();
		String monitorCode = report.getSrcCode();//增加站点编码，一个任务肯能发送多个站点。
		CollectTask task = null;
		Report taskReport = null;
		
		if(obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) {
			IReport taskObj = null;
			String key = null;
			//解压压缩文件
			String url = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
			String xml = ftpProcess.ftpDownDataFile(url);
			Log.debug("解析任务文件zip数据完成，xml=" + xml);
			if(xml == null) {
				Log.warn("解析任务文件zip数据文件包错误，" + getLogMsg(data));
			}
			try {
				taskReport = disposeAutoReportFile(xml);
			} catch (NXmlException e) {
				Log.error("", e);
			}
			 
			taskObj = taskReport == null ? null :taskReport.getReport();
			
			if (taskObj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport) {
				com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport qReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport) {
				com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport qReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport) {
				com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport qReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport) {
				com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport qReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} 
			data.getData().setReportData(taskReport);
			model.remove(key);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport) {
			String code = report.getSrcCode();
			task = model.getValue(code);
			if(task == null)
				Log.warn("[数据过滤]未找到关联设备状态主动上报下发任务， CollectTask=" + task);
			model.remove(code);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR) {
			//原key关联
//			String code = report.getSrcCode();
//			task = model.getValue(code);
			//新key关联
			String url = ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR)obj).getFileRetrieveR_File().get(0).getFileURL();
			String tmp = url.replaceFirst("ftp://", "");
			String[] array = tmp.split("/");
			String fileName = array[array.length - 1];
			
			task = model.getValue(fileName);
			if(task == null)
				Log.warn("[数据过滤]未找到关联录音文件主动上报下发任务， CollectTask=" + task);
			
			model.remove(fileName);
		}

		
		if (task == null) {
			task = new CollectTask();
		}
		
		BusinessTask busTask = task.getBusTask();
		if (busTask == null) {
			busTask = new BusinessTask();
		}
		if(busTask.getType() == null || busTask.getType() == BusinessTaskType.measure_realtime)
			busTask.setType(BusinessTaskType.measure_report);
		
		task.setBusTask(busTask);
		data.setCollectTask(task);
		
		return data;
	}
	
	@SuppressWarnings("restriction")
	private CollectData matchTaskV8(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		IReport obj = report.getReport();
		String monitorCode = report.getSrcCode();//增加站点编码，一个任务肯能发送多个站点。
		CollectTask task = null;
		Report taskReport = null;
		
		if(obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) {
			IReport taskObj = null;
			String key = null;
			//解压压缩文件
			String url = ((com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile)((Report)data.getData().getReportData()).getReport()).getURL();
			String xml = ftpProcess.ftpDownDataFile(url);
			Log.debug("解析任务文件zip数据完成，xml=" + xml);
			if(xml == null) {
				Log.warn("解析任务文件zip数据文件包错误，" + getLogMsg(data));
			}
			try {
				taskReport = disposeAutoReportFile(xml);
			} catch (NXmlException e) {
				Log.error("", e);
			}
			 
			taskObj = taskReport == null ? null :taskReport.getReport();
			
			if (taskObj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport) {
				com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport qReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport) {
				com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport qReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
//				key = taskId + monitorCode;
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport) {
				com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport qReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} else if (taskObj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport) {
				com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport qReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport) taskObj;
				String taskId = qReport.getTaskID();
				key = taskId;
				task = model.getValue(key);
			} 
			data.getData().setReportData(taskReport);
			model.remove(key);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealTimeReport ||
			obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport) {
			String code = report.getSrcCode();
			task = model.getValue(code);
			if(task == null)
				Log.warn("[数据过滤]未找到关联设备状态主动上报下发任务， CollectTask=" + task);
			
			model.remove(code);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieveR) {
			//原key关联
//			String code = report.getSrcCode();
//			task = model.getValue(code);
			//新key关联
			String url = ((com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieveR)obj).getFileRetrieveR_File().get(0).getFileURL();
			String tmp = url.replaceFirst("ftp://", "");
			String[] array = tmp.split("/");
			String fileName = array[array.length - 1];
			
			task = model.getValue(fileName);
			
			if(task == null)
				Log.warn("[数据过滤]未找到关联录音文件主动上报下发任务， CollectTask=" + task);
			
			model.remove(fileName);
		}
		
		if (task == null) {
			task = new CollectTask();
		}
		
		BusinessTask busTask = task.getBusTask();
		if (busTask == null) {
			busTask = new BusinessTask();
		}
		if(busTask.getType() == null || busTask.getType() == BusinessTaskType.measure_realtime)
			busTask.setType(BusinessTaskType.measure_report);
		
		task.setBusTask(busTask);
		data.setCollectTask(task);
		
		return data;
	}
	
	private Report disposeAutoReportFile(String xml) throws NXmlException {
		return ftpProcess.protolXMLToObj(xml);
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code() + ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
	
//	boolean validateQuery(CollectData data){
//		boolean flag = true;
//		ParameterValidation pv =ParameterValidationFactory.getInstance().newParameterValidationService();
//		if (!pv.checkReport((Report)data.getData().getReportData())) {
//			Log.debug("[采集数据校验]协议属性不完整  " + getLogMsg(data.getCollectTask()) +",属性异常详细信息 " + pv.getErrorMessage());
//			flag = false;
//		}
//		return flag;
//	}
	
//	private String getLogMsg(CollectTask task) {
//		return "taskID= " + task.getBusTask().getTask_id() + ",Freq=" + task.getBusTask().getFreq();
//	}
}
