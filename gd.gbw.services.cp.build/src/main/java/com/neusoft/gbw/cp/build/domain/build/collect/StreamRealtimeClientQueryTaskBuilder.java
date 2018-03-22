package com.neusoft.gbw.cp.build.domain.build.collect;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;

public class StreamRealtimeClientQueryTaskBuilder extends BasicTaskBuilder {

	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		MoniClientLinkDTO clientDto = (MoniClientLinkDTO) info.getBuisness();
		BusinessTask task = new BusinessTask();
		task.setType(BusinessTaskType.measure_realtime);
		task.setMonitor_id(Long.parseLong(clientDto.getMonitorId()));
		task.setIs_force(BuildConstants.NO_FORCE);
		return task;
	}
	
}
