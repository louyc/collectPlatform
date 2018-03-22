package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.Map;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class StreamRealtimeClientStopTaskBuilder extends BasicTaskBuilder {

	@SuppressWarnings("unchecked")
	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		Map<String, String> map = (Map<String, String>) info.getBuisness();
		MoniClientLinkDTO clientDto = null;
		BusinessTask task = null;
		try {
			clientDto = getDTO(map);
			task = new BusinessTask();
			task.setType(info.getType().getBusTaskType());
			task.setMonitor_id(Long.parseLong(clientDto.getMonitorId()));
			task.setIs_force(BuildConstants.NO_FORCE);
		} catch (Exception e) {
			Log.error("", e);
		}
		
		return task;
	}
	
	
	private MoniClientLinkDTO getDTO(Map<String, String> map) throws Exception {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniClientLinkDTO vo = (MoniClientLinkDTO)ConverterUtil.xmlToObj(request);
		return vo;
	}
}
