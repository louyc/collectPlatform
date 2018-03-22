package com.neusoft.gbw.cp.process.filter.service;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class TaskRedirectorProcess {
	
	public static void redirectTask(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		IReport obj = report.getReport();
		//将V7中的设备状态重置为主动上报数据
		if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport ||
			obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport	) {
			((Report)data.getData().getReportData()).setReplyID("-1");
			data.setStatus(ReportStatus.date_collect_active_report);
			ARSFToolkit.sendEvent(EventServiceTopic.REPORT_COLLECT_DATA_TOPIC, data);
		}
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReport ||
			obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport  ||
			obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport) {
			((Report)data.getData().getReportData()).setReplyID("-1");
			data.setStatus(ReportStatus.date_collect_active_report);
			ARSFToolkit.sendEvent(EventServiceTopic.REPORT_COLLECT_DATA_TOPIC, data);
		}
		
	}
	
}
