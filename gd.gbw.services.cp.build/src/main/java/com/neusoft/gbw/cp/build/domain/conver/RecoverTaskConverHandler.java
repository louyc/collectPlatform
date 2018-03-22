package com.neusoft.gbw.cp.build.domain.conver;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskAttribute;

public class RecoverTaskConverHandler extends AbstractTaskConverHandler{
	
	@Override
	BuildType buildBuildType(TaskAttribute taskAttr,String taskType, ProtocolType oriProType) {
		BuildType type = null;
		int taskTypeID = taskAttr.getTask_type_id();
		switch(taskTypeID) {
		case BuildConstants.INTEGRATE_TASK_TYPE:
			if (taskType.equals(BuildConstants.QualityTask.OFFSET_TASK)) {
				type = new BuildType(ProtocolType.OffsetHistoryQuery, BusinessTaskType.measure_manual_recover, oriProType);
			
			}
			if (taskType.equals(BuildConstants.EffectTask.COLLECT_STREAM_TASK)) {
				type = new BuildType(ProtocolType.StreamHistoryQuery, BusinessTaskType.measure_manual_recover, oriProType);
			}
			
			if (taskType.equals(BuildConstants.QualityTask.LEVEL_KPI)) {
				type = new BuildType(ProtocolType.QualityHistoryQuery, BusinessTaskType.measure_manual_recover , oriProType);
			}
			
			break;
		case BuildConstants.SPECTRUM_TASK_TYPE:
			type = new BuildType(ProtocolType.SpectrumHistoryQuery, BusinessTaskType.measure_manual_recover, oriProType);
			break;
		}
		return type;
	}
}
