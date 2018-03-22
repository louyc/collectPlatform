package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.Map;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniControlDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class ControlMonitorTaskBuilder extends BasicTaskBuilder{

	@SuppressWarnings("unchecked")
	BusinessTask buildBusinessTask(BuildInfo info) {
		Map<String, String> map = (Map<String, String>) info.getBuisness();
		MoniControlDTO dto = null;
		BusinessTask task = null;
		try {
			dto = getDTO(map);
			task = new BusinessTask();
			task.setType(info.getType().getBusTaskType());
			task.setMonitor_id(Long.parseLong(dto.getMonitorId()));
			task.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		} catch (BuildException e) {
			Log.error("", e);
		}
		
		return task;
	}
	
	private MoniControlDTO getDTO(Map<String, String> map) throws BuildException {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniControlDTO dto = null;
		try {
			dto = (MoniControlDTO)ConverterUtil.xmlToObj(request);
		} catch (Exception e) {
			throw new BuildException("[构建服务]版本查询接口转换对象出现异常，" + map);
		}
		return dto;
	}
}
