package com.neusoft.gbw.cp.build.domain.build;

import com.neusoft.gbw.cp.core.collect.TaskPriority;


public interface IQueryBuild {

	Object buildQualityAlarmHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildQualityAlarmParamSet(Object obj, TaskPriority taskPriority);
	
	Object buildQualityHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildQualityRealtimeQuery(Object obj, TaskPriority taskPriority);
	
	Object buildQualityTaskSet(Object obj, TaskPriority taskPriority);
	
	Object buildSpectrumHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildSpectrumRealtimeQuery(Object obj, TaskPriority taskPriority);
	
	Object buildSpectrumTaskSet(Object obj, TaskPriority taskPriority);
	
	Object buildStreamHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildStreamRealtimeQuery(Object obj, TaskPriority taskPriority);
	
	Object buildStreamTaskSet(Object obj, TaskPriority taskPriority);
	
	Object buildOffsetHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildOffsetTaskSet(Object obj, TaskPriority taskPriority);
	
	Object buildVideoRealtimeQuery(Object obj, TaskPriority taskPriority);
	
	Object buildEquAlarmHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildEquAlarmParamSet(Object obj, TaskPriority taskPriority);
	
	Object buildEquStatusRealtimeQuery(Object obj, TaskPriority taskPriority);
	
	Object buildEquInitParamSet(Object obj, TaskPriority taskPriority);
	
	Object buildEquLogHistoryQuery(Object obj, TaskPriority taskPriority);
	
	Object buildReceiverControl(Object obj, TaskPriority taskPriority);
	
	Object buildTaskDelete(Object obj, TaskPriority taskPriority);
	
	Object buildTaskStatusQuery(Object obj, TaskPriority taskPriority);
	
	Object buildFileQuery(Object obj, TaskPriority taskPriority);
	
	Object buildFileRetrieve(Object obj, TaskPriority taskPriority);
	
	Object buildProgramCommand(Object obj, TaskPriority taskPriority);
	
	Object buildStreamRealtimeClientQuery(Object obj, TaskPriority taskPriority);
	
	Object buildStreamRealtimeClientStop(Object obj, TaskPriority taskPriority);
	
}
