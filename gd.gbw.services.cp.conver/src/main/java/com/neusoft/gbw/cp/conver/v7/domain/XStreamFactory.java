package com.neusoft.gbw.cp.conver.v7.domain;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.Return;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamFactory {

	static Map<String, XStream> streamMap = new HashMap<String, XStream>();

	static XStream returnStream;

	static {
		init();
	}

	static void init() {
		returnStream = newXStream();
		returnStream.processAnnotations(Return.class);
		special();
	}

	static void special() {
		XStream qualityAlarmParamAlarmParam = newXStream();
		qualityAlarmParamAlarmParam.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryReport.class);
		qualityAlarmParamAlarmParam.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarm.class);
		qualityAlarmParamAlarmParam.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.AlarmParam.class);
		streamMap.put("QualityAlarmHistoryReport", qualityAlarmParamAlarmParam);

		XStream spectrumRealtimeReport = newXStream();
		spectrumRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport.class);
		spectrumRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReportSpectrumScan.class);
		spectrumRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ScanResult.class);
		streamMap.put("SpectrumRealtimeReport", spectrumRealtimeReport);

		XStream spectrumHistoryReport = newXStream();
		spectrumHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport.class);
		spectrumHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan.class);
		spectrumHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.HistoryScanResult.class);
		streamMap.put("SpectrumHistoryReport", spectrumHistoryReport);

		XStream qualityRealtimeReport = newXStream();
		qualityRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport.class);
		qualityRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReportQuality.class);
		qualityRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReportQualityQualityIndex.class);
		streamMap.put("QualityRealtimeReport", qualityRealtimeReport);

		XStream qualityHistoryQuery = newXStream();
		qualityHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryQuery.class);
		qualityHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryQueryQualityIndex.class);
		streamMap.put("QualityHistoryQuery", qualityHistoryQuery);

		XStream qualityTaskSet = newXStream();
		qualityTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTaskSet.class);
		qualityTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.Channel.class);
		qualityTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTask.class);
		qualityTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTaskSetQualityIndex.class);
		streamMap.put("QualityTaskSet", qualityTaskSet);

		XStream qualityRealtimeQuery = newXStream();
		qualityRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeQuery.class);
		streamMap.put("QualityRealtimeQuery", qualityRealtimeQuery);

		XStream videoRealtimeQuery = newXStream();
		videoRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.VideoRealtimeQuery.class);
		videoRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.VideoRealtimeStream.class);
		streamMap.put("VideoRealtimeQuery", videoRealtimeQuery);

		XStream qualityHistoryReport = newXStream();
		qualityHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport.class);
		qualityHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality.class);
		qualityHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex.class);
		streamMap.put("QualityHistoryReport", qualityHistoryReport);

		XStream equipmentAlarmHistoryReport = newXStream();
		equipmentAlarmHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmHistoryReport.class);
		equipmentAlarmHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarm.class);
		equipmentAlarmHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Param.class);
		streamMap.put("EquipmentAlarmHistoryReport", equipmentAlarmHistoryReport);

		XStream equipmentStatusRealtimeReport = newXStream();
		equipmentStatusRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeReport.class);
		equipmentStatusRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentRealtimeStatus.class);
		equipmentStatusRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.RealtimeStatusEquipment.class);
		equipmentStatusRealtimeReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Parameter.class);
		streamMap.put("EquipmentStatusRealtimeReport", equipmentStatusRealtimeReport);

		XStream offsetHistoryReport = newXStream();
		offsetHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport.class);
		offsetHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetIndex.class);
		streamMap.put("OffsetHistoryReport", offsetHistoryReport);

		XStream receiverControlReport = newXStream();
		receiverControlReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ReceiverControlReport.class);
		streamMap.put("ReceiverControlReport", receiverControlReport);

		XStream streamHistoryReport = newXStream();
		streamHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryReport.class);
		streamHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.TaskRecord.class);
		streamHistoryReport.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Record.class);
		streamMap.put("StreamHistoryReport", streamHistoryReport);


		XStream streamRealtimeQueryR = newXStream();
		streamRealtimeQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR.class);
		streamRealtimeQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream.class);
		streamMap.put("StreamRealtimeQueryR", streamRealtimeQueryR);

		XStream fileRetrieveR = newXStream();
		fileRetrieveR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR.class);
		fileRetrieveR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR_File.class);
		streamMap.put("FileRetrieveR", fileRetrieveR);

		XStream fileRetrieve = newXStream();
		fileRetrieve.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieve.class);
		fileRetrieve.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieve_File.class);
		streamMap.put("FileRetrieve", fileRetrieve);

		XStream offsetTaskSet = newXStream();
		offsetTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetTaskSet.class);
		offsetTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.QualityTask.class);
		offsetTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.Channel.class);
		offsetTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetCycleReportTime.class);
		offsetTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetSingleReportTime.class);
		streamMap.put("OffsetTaskSet", offsetTaskSet);

		XStream equipmentAlarmParamSet = newXStream();
		equipmentAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmParamSet.class);
		equipmentAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmParam.class);
		equipmentAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AlarmParam.class);
		equipmentAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Param.class);
		streamMap.put("EquipmentAlarmParamSet", equipmentAlarmParamSet);

		XStream qualityAlarmParamSet = newXStream();
		qualityAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmParamSet.class);
		qualityAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmParam.class);
		qualityAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmParamAlarmParam.class);
		qualityAlarmParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.Param.class);
		streamMap.put("QualityAlarmParamSet", qualityAlarmParamSet);

		XStream spectrumTaskSet = newXStream();
		spectrumTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTaskSet.class);
		spectrumTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTask.class);
		spectrumTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SingleReportTime.class);
		spectrumTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.CycleReportTime.class);
		streamMap.put("SpectrumTaskSet", spectrumTaskSet);

		XStream streamTaskSet = newXStream();
		streamTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTaskSet.class);
		streamTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTask.class);
		streamTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamCycleReportTime.class);
		streamTaskSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamSingleReportTime.class);
		streamMap.put("StreamTaskSet", streamTaskSet);

		XStream equipmentInitParamSet = newXStream();
		equipmentInitParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentInitParamSet.class);
		equipmentInitParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.InitEquipment.class);
		equipmentInitParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Center.class);
		equipmentInitParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Center.class);
		equipmentInitParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.LogInfo.class);
		equipmentInitParamSet.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Param.class);
		streamMap.put("EquipmentInitParamSet", equipmentInitParamSet);

		XStream autoReportFile = newXStream();
		autoReportFile.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile.class);
		streamMap.put("AutoReportFile", autoReportFile);

		XStream equipmentAlarmHistoryQuery = newXStream();
		equipmentAlarmHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmHistoryQuery.class);
		streamMap.put("EquipmentAlarmHistoryQuery", equipmentAlarmHistoryQuery);

		XStream equipmentLogHistoryQuery = newXStream();
		equipmentLogHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentLogHistoryQuery.class);
		streamMap.put("EquipmentLogHistoryQuery", equipmentLogHistoryQuery);

		XStream equipmentLogHistoryQueryR = newXStream();
		equipmentLogHistoryQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentLogHistoryQueryR.class);
		equipmentLogHistoryQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.LogItem.class);
		streamMap.put("EquipmentLogHistoryQueryR", equipmentLogHistoryQueryR);

		XStream equipmentStatusRealtimeQuery = newXStream();
		equipmentStatusRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeQuery.class);
		streamMap.put("EquipmentStatusRealtimeQuery", equipmentStatusRealtimeQuery);

		XStream fileQuery = newXStream();
		fileQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileQuery.class);
		fileQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Frequency.class);
		streamMap.put("FileQuery", fileQuery);

		XStream fileQueryR = newXStream();
		fileQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileQueryR.class);
		fileQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ResultInfo.class);
		streamMap.put("FileQueryR", fileQueryR);

		XStream offsetHistoryQuery = newXStream();
		offsetHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryQuery.class);
		streamMap.put("OffsetHistoryQuery", offsetHistoryQuery);

		XStream programCommand = newXStream();
		programCommand.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommand.class);
		programCommand.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramInfoQuery.class);
		programCommand.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Command.class);
		programCommand.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ReviseTime.class);
		streamMap.put("ProgramCommand", programCommand);

		XStream programCommandR = newXStream();
		programCommandR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR.class);
		programCommandR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramInfo.class);
		streamMap.put("ProgramCommandR", programCommandR);

		XStream qualityAlarmHistoryQuery = newXStream();
		qualityAlarmHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryQuery.class);
		streamMap.put("QualityAlarmHistoryQuery", qualityAlarmHistoryQuery);

		XStream receiverControl = newXStream();
		receiverControl.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ReceiverControl.class);
		receiverControl.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ReceiverParam.class);
		streamMap.put("ReceiverControl", receiverControl);

		XStream spectrumHistoryQuery = newXStream();
		spectrumHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryQuery.class);
		streamMap.put("SpectrumHistoryQuery", spectrumHistoryQuery);

		XStream spectrumRealtimeQuery = newXStream();
		spectrumRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumRealtimeQuery.class);
		streamMap.put("SpectrumRealtimeQuery", spectrumRealtimeQuery);

		XStream streamHistoryQuery = newXStream();
		streamHistoryQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryQuery.class);
		streamMap.put("StreamHistoryQuery", streamHistoryQuery);

		XStream streamRealtimeQuery = newXStream();
		streamRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery.class);
		streamRealtimeQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.RealtimeStream.class);
		streamMap.put("StreamRealtimeQuery", streamRealtimeQuery);

		XStream taskDelete = newXStream();
		taskDelete.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskDelete.class);
		streamMap.put("TaskDelete", taskDelete);

		XStream taskStatusQuery = newXStream();
		taskStatusQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatusQuery.class);
		taskStatusQuery.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Task.class);
		streamMap.put("TaskStatusQuery", taskStatusQuery);

		XStream taskStatusQueryR = newXStream();
		taskStatusQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatusQueryR.class);
		taskStatusQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatus.class);
		streamMap.put("TaskStatusQueryR", taskStatusQueryR);

		XStream videoRealtimeQueryR = newXStream();
		videoRealtimeQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.VideoRealtimeQueryR.class);
		videoRealtimeQueryR.processAnnotations(com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream.class);
		streamMap.put("VideoRealtimeQueryR", videoRealtimeQueryR);
	}

	private static XStream newXStream() {
		return new XStream(new DomDriver());
	}

	protected static XStream getXStream(String name) {
		if (streamMap.containsKey(name)) {
			return streamMap.get(name);
		}
		return null;
	}

	protected static XStream getReturnXStream() {
		return returnStream;
	}
}
