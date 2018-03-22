package com.neusoft.gbw.cp.collect.service.handle;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.collect.model.CollectTaskModel;
import com.neusoft.gbw.cp.collect.service.build.ReportDataBuilder;
import com.neusoft.gbw.cp.conver.IProtocolConver;
import com.neusoft.gbw.cp.conver.ProtocolConverFactory;
import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecoveryTransferUpHandler extends NMService{
	
	private BlockingQueue<String> queue = null;
	
	public RecoveryTransferUpHandler(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		String protcolXML = null;
		CollectData info = null;
		Report reportData = null;
		while (isThreadRunning()) {
			try {
				protcolXML = queue.take();
			} catch (InterruptedException e) {
				Log.error("回收数据队列提取数据失败", e);
				break;
			}
		
			try {
//				int version = ProtocolUtils.getProtocolVersion(protcolXML);
				reportData = protolXMLToObj(protcolXML);
			} catch (NXmlException e) {
				Log.error("返回协议数据解析失败，失败协议数据："+ protcolXML, e);
				builderAndPulisherDate(null,null,ReportStatus.date_collect_error);
				continue;
			}
			/*if(isQualityAlarmHistoryReport(reportData)){
				Log.debug("报警历史查询   暂不支持    过滤掉");
				continue;
			}*/
			if(null!=reportData.getReportReturn() &&
					!reportData.getReportReturn().getValue().equals("0") &&  
					!reportData.getReportReturn().getValue().equals("101") &&
					!reportData.getReportReturn().getValue().equals("102")){
				Log.info("变更采集状态：" + ReportStatus.date_collect_error);
				builderAndPulisherDate(info,reportData,ReportStatus.date_collect_error);
				deleteCollectTask(reportData);
				continue;
			}
			//主动上报
			if (isActiveReport(reportData)) {
				Log.info("主动上报数据 任务ID：[" + reportData.getReplyID() +"] 变更采集状态：" + ReportStatus.date_collect_active_report);
				builderAndPulisherDate(info,reportData,ReportStatus.date_collect_active_report);
				continue;
			}
			
			//未找到采集任务
			if (!isHaveCollectTask(reportData)) {
				Log.info("未发现采集任务 任务ID：[" + reportData.getReplyID() +"] 变更采集状态：" + ReportStatus.date_collect_no_found_task);
				builderAndPulisherDate(info,reportData,ReportStatus.date_collect_no_found_task);
				continue;
			}
			
			//取消timer计时
			cancelTimer(reportData);
			//检验是否超时
			//采集超时状态判断
			if (getTimerStatus(reportData)) {
				Log.info("采集超时 任务ID：[" + reportData.getReplyID() +"] 变更采集状态：" + ReportStatus.date_collect_time_out);
				builderAndPulisherDate(info,reportData,ReportStatus.date_collect_time_out);
//				deleteCollectTask(reportData);
				continue;
			}
			//判断返回收据是否为空
			if(isNullReport(reportData)){
				Log.info("变更采集状态：  数据为空 [" + reportData.getReplyID()+"] 变更采集状态：" + ReportStatus.data_collect_null);
				builderAndPulisherDate(info,reportData,ReportStatus.data_collect_null);
				sleep(1000);
			}
			
			//正常任务返回
			Log.info("采集正常 任务ID：[" + reportData.getReplyID() +"] 变更采集状态：" + ReportStatus.date_collect_success);
			builderAndPulisherDate(info,reportData,ReportStatus.date_collect_success);
			if(null!=CollectTaskModel.getModel().get(reportData.getReplyID())){
				Query query = (Query)(CollectTaskModel.getModel().get(reportData.getReplyID()).getTaskInfo().getData().getQuery());
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeQuery){
					com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeQuery qrq = (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeQuery )
							query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumRealtimeQuery){
					com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumRealtimeQuery qrq = (com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumRealtimeQuery )
							query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeQuery){
					com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeQuery qrq = (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeQuery )
							query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumRealtimeQuery){
					com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumRealtimeQuery qrq = 
							(com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumRealtimeQuery)query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeQuery){
					com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeQuery qrq = (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeQuery )
							query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumRealtimeQuery){
					com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumRealtimeQuery qrq = 
							(com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumRealtimeQuery )query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeQuery){
					com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeQuery qrq = (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeQuery )
							query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumRealtimeQuery){
					com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumRealtimeQuery qrq = (com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumRealtimeQuery )
							query.getQuery();
					if(qrq.getAction().equals("Start"))
						continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieve){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieve){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieve){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieve){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryQuery){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryQuery){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryQuery){
					continue;
				}
				if(query.getQuery() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQuery){
					continue;
				}
			}
			deleteCollectTask(reportData);
			protcolXML = null;
		}
		
	}
//	/**
//	 * 对象数据转换成协议数据
//	 * 
//	 * @param task
//	 * @return
//	 * @throws NXmlException
//	 */
//	private Report protolXMLToObjV8(String protcolXML) throws NXmlException {
//		IProtocolConver iConver = ProtocolConverFactory.getInstance().newProtocolConverServiceV8();
//		Report report  = iConver.decodeReport(protcolXML);
//
//		return report;
//	}
//	private Report protolXMLToObjV7(String protcolXML) throws NXmlException {
//		IProtocolConver iConver = ProtocolConverFactory.getInstance().newProtocolConverServiceV7();
//		Report report  = iConver.decodeReport(protcolXML);
//		
//		return report;
//	}
	
/**
	 * 对象数据转换成协议数据
	 * 
	 * @param task
	 * @return
	 * @throws NXmlException
	 */
		IProtocolConver iConver = null;
		private Report protolXMLToObj(String protcolXML) throws NXmlException {
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
		report  = iConver.decodeReport(protcolXML);
		//校验转换的协议版本是否有异常
		report = ThransferProtcolCheckHandler.chectProtcol(report);
		return report;
	}


	private void cancelTimer(Report report) {
		String taskID = report.getReplyID();
		CollectTaskModel.getModel().cancelTimer(taskID);
	}
	
	/**
	 * 验证是否存在该任务
	 * @param report
	 * @return
	 */
	private boolean isHaveCollectTask(Report report) {
		String taskID = report.getReplyID().trim();
		boolean isHave = CollectTaskModel.getModel().constainKey(taskID);
		if(!isHave) {
			sleep(500);
			isHave = CollectTaskModel.getModel().constainKey(taskID);
		}
		return isHave;
	}
	/**
	 * 判断回收数据是否为空   ：指标数据   录音数据  频偏数据  频谱数据
	 * @param report
	 * @return
	 */
	private boolean isNullReport(Report report) {
		if(report.getReportReturn().getType().equals("QualityHistoryQuery")
				||report.getReportReturn().getType().equals("StreamHistoryQuery")
				||report.getReportReturn().getType().equals("OffsetHistoryQuery")
				||report.getReportReturn().getType().equals("SpectrumHistoryQuery")){
			if(null == report.getReport()){
				return true;
			}
		}
		if(report.getVersion().equals("8")){
			//指标
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport qualityV8 = 
						(com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReportQuality> qaList = qualityV8.getQualityAlarm();
				if(qaList == null || qaList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport streamV8 = 
						(com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.TaskRecord> sList = streamV8.getTaskRecords();
				if(sList == null || sList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport offsetV8 = 
						(com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetIndex> osList = offsetV8.getOffsetIndexs();
				if(osList == null || osList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport spectrumV8 = 
						(com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumScan> osList = spectrumV8.getSpectrumScan();
				if(osList == null || osList.size() <= 0)
					return true;
			}
		}
		if(report.getVersion().equals("7")){
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport qualityV7 = 
						(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality> qaList = qualityV7.getQualityAlarm();
				if(qaList == null || qaList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport streamV7 = 
						(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord> sList = streamV7.getTaskRecords();
				if(sList == null || sList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport offsetV7 = 
						(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetIndex> osList = offsetV7.getOffsetIndexs();
				if(osList == null || osList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport spectrumV7 = 
						(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan> osList = spectrumV7.getSpectrumScan();
				if(osList == null || osList.size() <= 0)
					return true;
			}
		}
		if(report.getVersion().equals("6")){
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport qualityV6 = 
						(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQuality> qaList = qualityV6.getQualityAlarm();
				if(qaList == null || qaList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport streamV6 = 
						(com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.TaskRecord> sList = streamV6.getTaskRecords();
				if(sList == null || sList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport offsetV6 = 
						(com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetIndex> osList = offsetV6.getOffsetIndexs();
				if(osList == null || osList.size() <= 0)
					return true;
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport offsetV6 = 
						(com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetIndex> osList = offsetV6.getOffsetIndexs();
				if(osList == null || osList.size() <= 0)
					return true;
			}
		}
		if(report.getVersion().equals("5")){
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport){
				com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport qualityV5 = 
						(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQuality> qaList = qualityV5.getQualityAlarm();
				if(qaList == null || qaList.size() <= 0)
					return true;
			}
			//录音
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport){
				com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport streamV5 = 
						(com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.TaskRecord> sList = streamV5.getTaskRecords();
				if(sList == null || sList.size() <= 0)
					return true;
			}
			//频偏
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport){
				com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport offsetV5 = 
						(com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetIndex> osList = offsetV5.getOffsetIndexs();
				if(osList == null || osList.size() <= 0)
					return true;
			}
			//频谱
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport){
				com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport spectrumV5 = 
						(com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport)(report).getReport();
				List<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumScan> osList = spectrumV5.getSpectrumScan();
				if(osList == null || osList.size() <= 0)
					return true;
			}
		}
		return false;
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * 验证是否是主动上报任务
	 * @param report
	 * @return
	 */
	private boolean isActiveReport(Report report) {
		boolean isFlag = report.getReplyID().trim().equals("-1")?true:false;
		//V8 协议  站点主动上报的数据  reportReturn 都有数据   只根据-1  不能唯一确定是否是主动上报
		/*if(!isFlag && report.getVersion().equals("8") && null ==report.getReportReturn()){
			return isFlag=true;
		}*/
		if(!isFlag){
			//站点实时状态
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport qualityReport = 
						(com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport)report.getReport();
				if(null !=qualityReport.getEquipmentRealtimeStatuses()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport qualityReport = 
						(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport)report.getReport();
				if(null !=qualityReport.getEquipmentRealtimeStatuses()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport qualityReport = 
						(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport)report.getReport();
				if(null !=qualityReport.getEquipmentRealtimeStatuses()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport){
				com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport qualityReport = 
						(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport)report.getReport();
				if(null !=qualityReport.getEquipmentRealtimeStatuses()){
					return isFlag = true;
				}
			}
			//实时指标
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport)report.getReport();
				if(null !=qualityReport.getQualityRealtimeReport_Quality()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReport){
				com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReport)report.getReport();
				if(null !=qualityReport.getQualityRealtimeReport_Quality()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport)report.getReport();
				if(null !=qualityReport.getQualityRealtimeReport_Quality()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReport)report.getReport();
				if(null !=qualityReport.getQualityRealtimeReport_Quality()){
					return isFlag = true;
				}
			}
			//频谱实时
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.SpectrumRealtimeReport){
				com.neusoft.gbw.cp.conver.v8.protocol.vo.other.SpectrumRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v8.protocol.vo.other.SpectrumRealtimeReport)report.getReport();
				if(null !=qualityReport.getSpectrumScans()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport){
				com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport)report.getReport();
				if(null !=qualityReport.getSpectrumScans()){
					return isFlag = true;
				}
			}
			if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport){
				com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport qualityReport = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport)report.getReport();
				if(null !=qualityReport.getSpectrumScans()){
					return isFlag = true;
				}
			}
		}
		return isFlag;
	}
	/**
	 * 报警历史查询 暂时不支持
	 * @param query
	 * @return
	 */
	private boolean isQualityAlarmHistoryReport(Report report){
		boolean b = false;
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmHistoryReport){
			return b = true;
		}
		if(report.getReport() instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentAlarmHistoryReport){
			return b = true;
		}
		return b;
	}
	/**
	 *  根据回收数据，获取采集任务
	 * @param report
	 * @return
	 */
	private CollectTask getCollectTask(Report report) {
		if(report!=null && !report.getReplyID().equals("-1")) {
			String taskID = report.getReplyID();
			return CollectTaskModel.getModel().get(taskID)==null?null:CollectTaskModel.getModel().get(taskID).getTaskInfo();
			
		}
		return  null;
	} 
	
	private boolean getTimerStatus(Report report) {
		String taskID = report.getReplyID();
		return CollectTaskModel.getModel().get(taskID).isTimeOut();
	}
	
	private void deleteCollectTask(Report report) {
		String taskID = report.getReplyID();
		Log.debug(taskID+"***************删除id");
		CollectTaskModel.getModel().delete(taskID);
	}
	
	/**
	 * 构建并上报采集数据
	 * @param info
	 */
	private void builderAndPulisherDate(CollectData info,Report reportData,ReportStatus reportStatus) {
		info = ReportDataBuilder.buildRecoveryData(getCollectTask(reportData), reportData, reportStatus);
		pulisher(info);
	}

	/**
	 * 上报采集数据
	 * @param info
	 */
	private void pulisher(CollectData info) {
		//调取上报总线接口
		ARSFToolkit.sendEvent(EventServiceTopic.REPORT_COLLECT_DATA_TOPIC, info);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
	
	public static void main(String[] args) {
		String xml = "standalone=\"yes\"?><Msg Version=\"5\" MsgID=\"365189377\"";
		System.out.println(getNewVersion(xml));
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

}
