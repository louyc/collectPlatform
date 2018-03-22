package com.neusoft.gbw.cp.station.service;

import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.station.vo.ProtocolType;

public class Dispatcher {

	public ProtocolData dispatch(Query query) {
		ProtocolData data = new ProtocolData();
		data.setMsgID(Long.parseLong(query.getMsgID()));
		data.setType(getType(query));
		data.setSrcCode(query.getSrcCode());
		data.setDstCode(query.getDstCode());
		return data;
	}
	
	private ProtocolType getType(Query query) {
		ProtocolType type = null;
		Object obj = query.getQuery();
		if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityTaskSet) 
			type = ProtocolType.quality_set;
		else if(obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTaskSet)
			type = ProtocolType.v7_quality_set;
		else if(obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityTaskSet)
			type = ProtocolType.v5_quality_set;
		else if(obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityTaskSet)
			type = ProtocolType.v5_quality_set;
			
	    else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQuery) 
			type = ProtocolType.quality_history;
	    else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryQuery) 
			type = ProtocolType.v7_quality_history;
	    else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryQuery) 
			type = ProtocolType.v6_quality_history;
	    else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryQuery) 
			type = ProtocolType.v5_quality_history;
			
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumTaskSet) 
			type = ProtocolType.spectrum_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTaskSet) 
			type = ProtocolType.v7_spectrum_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTaskSet) 
			type = ProtocolType.v5_spectrum_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTaskSet) 
			type = ProtocolType.v5_spectrum_set;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryQuery) 
			type = ProtocolType.spectrum_history;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryQuery) 
			type = ProtocolType.v7_spectrum_history;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryQuery) 
			type = ProtocolType.v6_spectrum_history;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryQuery) 
			type = ProtocolType.v5_spectrum_history;
			
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetTaskSet)
				type = ProtocolType.offset_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetTaskSet)
				type = ProtocolType.v7_offset_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetTaskSet)
				type = ProtocolType.v5_offset_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetTaskSet)
				type = ProtocolType.v5_offset_set;
			
	   else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryQuery)
				type = ProtocolType.offset_history;
	   else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryQuery)
				type = ProtocolType.v7_offset_history;
	   else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetHistoryQuery)
				type = ProtocolType.v5_offset_history;
	   else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryQuery)
				type = ProtocolType.v5_offset_history;
		
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTaskSet) 
				type = ProtocolType.stream_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTaskSet) 
				type = ProtocolType.v7_stream_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTaskSet) 
				type = ProtocolType.v5_stream_set;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTaskSet) 
				type = ProtocolType.v5_stream_set;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryQuery) 
				type = ProtocolType.stream_history;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryQuery) 
				type = ProtocolType.v7_stream_history;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryQuery) 
				type = ProtocolType.v6_stream_history;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryQuery) 
				type = ProtocolType.v5_stream_history;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery) 
				type = ProtocolType.real_stream;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery) 
				type = ProtocolType.v7_real_stream;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQuery) 
				type = ProtocolType.v5_real_stream;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQuery) 
				type = ProtocolType.v5_real_stream;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtimeClientQuery) 
				type = ProtocolType.stream_realtime_client;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentStatusRealtimeQuery) 
				type = ProtocolType.equipment_status_realtime;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentStatusRealtimeQuery) 
				type = ProtocolType.v7_equipment_status_realtime;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeQuery) 
				type = ProtocolType.v5_equipment_status_realtime;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentStatusRealtimeQuery) 
				type = ProtocolType.v5_equipment_status_realtime;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtimeClientStop) 
				type = ProtocolType.stream_client_stop;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarmHistoryQuery) 
				type = ProtocolType.quality_alarm_history_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmHistoryQuery) 
				type = ProtocolType.v7_quality_alarm_history_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmHistoryQuery) 
				type = ProtocolType.v5_quality_alarm_history_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmHistoryQuery) 
				type = ProtocolType.v5_quality_alarm_history_query;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarmParamSet) 
				type = ProtocolType.quality_alarm_param_set;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityAlarmParamSet) 
				type = ProtocolType.v7_quality_alarm_param_set;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmParamSet) 
				type = ProtocolType.v5_quality_alarm_param_set;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityAlarmParamSet) 
				type = ProtocolType.v5_quality_alarm_param_set;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeQuery ) 
				type = ProtocolType.quality_realtime_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeQuery ) 
				type = ProtocolType.v7_quality_realtime_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeQuery ) 
				type = ProtocolType.v6_quality_realtime_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeQuery ) 
				type = ProtocolType.v5_quality_realtime_query;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumRealtimeQuery ) 
				type = ProtocolType.spectrum_realtime_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumRealtimeQuery ) 
				type = ProtocolType.v7_spectrum_realtime_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumRealtimeQuery ) 
				type = ProtocolType.v5_spectrum_realtime_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumRealtimeQuery ) 
				type = ProtocolType.v5_spectrum_realtime_query;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentInitParamSet ) 
				type = ProtocolType.task_init;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentInitParamSet )
				type = ProtocolType.v7_task_init;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentInitParamSet )
				type = ProtocolType.v5_task_init;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentInitParamSet )
				type = ProtocolType.v5_task_init;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmHistoryQuery ) 
				type = ProtocolType.equipment_alarm_history_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmHistoryQuery ) 
				type = ProtocolType.v7_equipment_alarm_history_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmHistoryQuery ) 
				type = ProtocolType.v5_equipment_alarm_history_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentAlarmHistoryQuery ) 
				type = ProtocolType.v5_equipment_alarm_history_query;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmParamSet) 
				type = ProtocolType.equipment_alarmParam_set;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentAlarmParamSet) 
				type = ProtocolType.v7_equipment_alarmParam_set;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentAlarmParamSet) 
				type = ProtocolType.v5_equipment_alarmParam_set;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentAlarmParamSet) 
				type = ProtocolType.v5_equipment_alarmParam_set;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentLogHistoryQuery ) 
				type = ProtocolType.equipment_logHistory_Query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentLogHistoryQuery ) 
				type = ProtocolType.v7_equipment_logHistory_Query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentLogHistoryQuery ) 
				type = ProtocolType.v5_equipment_logHistory_Query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentLogHistoryQuery ) 
				type = ProtocolType.v5_equipment_logHistory_Query;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ReceiverControl ) 
				type = ProtocolType.receiver_control;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ReceiverControl ) 
				type = ProtocolType.v7_receiver_control;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ReceiverControl ) 
				type = ProtocolType.v5_receiver_control;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ReceiverControl ) 
				type = ProtocolType.v5_receiver_control;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.TaskDelete ) 
				type = ProtocolType.task_delete;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskDelete ) 
				type = ProtocolType.v7_task_delete;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskDelete ) 
				type = ProtocolType.v6_task_delete;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.TaskDelete ) 
				type = ProtocolType.v5_task_delete;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.TaskStatusQuery ) 
				type = ProtocolType.task_status_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatusQuery ) 
				type = ProtocolType.v7_task_status_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskStatusQuery ) 
				type = ProtocolType.v5_task_status_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.TaskStatusQuery ) 
				type = ProtocolType.v5_task_status_query;
			
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileQuery ) 
				type = ProtocolType.file_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileQuery ) 
				type = ProtocolType.v7_file_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileQuery ) 
				type = ProtocolType.v5_file_query;
		 else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileQuery ) 
				type = ProtocolType.v5_file_query;
			
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieve ) 
				type = ProtocolType.file_retrieve;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieve ) 
				type = ProtocolType.v7_file_retrieve;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieve ) 
				type = ProtocolType.v5_file_retrieve;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieve ) 
				type = ProtocolType.v5_file_retrieve;
			
		else if (obj instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommand ) 
				type = ProtocolType.program_command;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommand ) 
				type = ProtocolType.v7_program_command;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommand ) 
				type = ProtocolType.v5_program_command;
		else if (obj instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommand ) 
				type = ProtocolType.v5_program_command;
		return type;
	}
}
