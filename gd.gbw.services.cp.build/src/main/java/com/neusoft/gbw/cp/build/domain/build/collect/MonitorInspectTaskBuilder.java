package com.neusoft.gbw.cp.build.domain.build.collect;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;

public class MonitorInspectTaskBuilder extends BasicTaskBuilder{

	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		MoniInspectResultDTO dto = (MoniInspectResultDTO)info.getBuisness();
		BusinessTask task = null;
		task = new BusinessTask();
		task.setType(info.getType().getBusTaskType());
		task.setMonitor_id(Long.parseLong(dto.getMonitorId()));
		task.setMonitor_code(info.getDevice().getMonitor_code());
		task.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		return task;
	}
}
