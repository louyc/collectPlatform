package com.neusoft.gbw.cp.build.domain.build.collect;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;

public class EquStatusRealtimeQueryTaskBuilder extends BasicTaskBuilder{

	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO) info.getBuisness();
		BusinessTask task = new BusinessTask();
		task.setType(info.getType().getBusTaskType());
		task.setMonitor_id(Long.parseLong(statusDto.getMonitorId()));
		task.setMonitor_code(info.getDevice().getMonitor_code());
		task.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		return task;
	}
	
}
