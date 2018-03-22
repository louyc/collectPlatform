package com.neusoft.gbw.cp.build.domain.build.collect;

import com.neusoft.gbw.app.evaluation.intf.dto.KpiRealtimeQueryDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.ScheduleInfo;
import com.neusoft.gbw.cp.core.collect.ScheduleType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;

public class QualityRealtimeQueryTaskBuilder extends BasicTaskBuilder {
	
	@Override
	ScheduleInfo buildScheduleInfo(BuildInfo info) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setType(ScheduleType.realtime);
		return schedule;
	}

	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		KpiRealtimeQueryDTO dto = (KpiRealtimeQueryDTO) info.getBuisness();
		BusinessTask task = new BusinessTask();
		task.setType(BusinessTaskType.measure_realtime);
		task.setMonitor_id(Long.parseLong(dto.getMonitorID()));
		//有前台控制是否进行强制，目前由采集平台自己定义为强制，控制采集是否下发指标
		task.setIs_force(BuildConstants.IS_FORCE);
//		task.setIs_force(BuildConstants.NO_FORCE);
		return task;
	}
	
}
