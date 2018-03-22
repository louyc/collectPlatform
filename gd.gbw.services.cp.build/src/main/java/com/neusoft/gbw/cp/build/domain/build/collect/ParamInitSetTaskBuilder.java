package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class ParamInitSetTaskBuilder extends BasicTaskBuilder {

	@SuppressWarnings("unchecked")
	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		Map<String, String> map = (Map<String, String>) info.getBuisness();
		MoniSetingDTO streamDTO = null;
		BusinessTask task = null;
		try {
			streamDTO = getDTO(map);
			task = new BusinessTask();
			task.setType(info.getType().getBusTaskType());
			task.setMonitor_id(Long.parseLong(streamDTO.getMonitorId()));
			task.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		} catch (Exception e) {
			Log.error("", e);
		}
		
		return task;
	}
	
	private MoniSetingDTO getDTO(Map<String, String> map) throws Exception {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniSetingDTO vo = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
		return vo;
	}

}
