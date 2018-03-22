package com.neusoft.gbw.cp.process.realtime.channel;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.process.realtime.vo.ReportType;

public class ReportIdentityTypeExecutor {
	
	public static ReportType identityReportType(Object object) {
		ReportType type = null;
		IReport report = getReport(object);
		if (report == null) {
//			Return tmp = getReturn(object);
//			String returnType = tmp.getType();
			return null;
		}
		
		if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarmHistoryReport) {
			type = ReportType.quality_alarm;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReport || 
			report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.special.QualityRealTimeReport) {
			type = ReportType.quality_real_query;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport) {
			type = ReportType.quality_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport) {
			type = ReportType.spectrum_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryReport) {
			type = ReportType.stream_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport) {
			type = ReportType.offset_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealTimeReport || 
			report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special.EquipmentStatusRealtimeReport) {
			type = ReportType.equ_status_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmHistoryReport) {
			type = ReportType.equ_alarm;
		} else if(report instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.SpectrumRealtimeReport){
			type= ReportType.spectrum_report;
		}
		return type;
	}
	
	public static ReportType identityReportTypeV7(Object object) {
		ReportType type = null;
		IReport report = getReport(object);
		if (report == null) {
//			Return tmp = getReturn(object);
//			String returnType = tmp.getType();
			return null;
		}
		
		if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryReport) {
			type = ReportType.quality_alarm;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport) {
			type = ReportType.quality_real_query;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport) {
			type = ReportType.quality_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport) {
			type = ReportType.spectrum_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport) {
			type = ReportType.stream_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport) {
			type = ReportType.offset_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport) {
			type = ReportType.equ_status_report;
		} else if(report instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport){
			type= ReportType.spectrum_report;
		}
		return type;
	}
	
	public static ReportType identityReportTypeV6(Object object) {
		ReportType type = null;
		IReport report = getReport(object);
		if (report == null) {
//			Return tmp = getReturn(object);
//			String returnType = tmp.getType();
			return null;
		}
		
		if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmHistoryReport) {
			type = ReportType.quality_alarm;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport) {
			type = ReportType.quality_real_query;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport) {
			type = ReportType.quality_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport) {
			type = ReportType.spectrum_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryReport) {
			type = ReportType.stream_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryReport) {
			type = ReportType.offset_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeReport) {
			type = ReportType.equ_status_report;
		} else if(report instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport){
			type= ReportType.spectrum_report;
		}
		return type;
	}
	
	public static ReportType identityReportTypeV5(Object object) {
		ReportType type = null;
		IReport report = getReport(object);
		if (report == null) {
//			Return tmp = getReturn(object);
//			String returnType = tmp.getType();
			return null;
		}
		
		if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmHistoryReport) {
			type = ReportType.quality_alarm;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReport) {
			type = ReportType.quality_real_query;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport) {
			type = ReportType.quality_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport) {
			type = ReportType.spectrum_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryReport) {
			type = ReportType.stream_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport) {
			type = ReportType.offset_report;
		} else if (report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeReport) {
			type = ReportType.equ_status_report;
		} else if(report instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.SpectrumRealtimeReport){
			type= ReportType.spectrum_report;
		}
		return type;
	}
	
	private static IReport getReport(Object report) {
		if (report instanceof Report) {
			return ((Report) report).getReport();
		}
		return null;
	}

}
