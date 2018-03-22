package com.neusoft.gbw.cp.process.inspect.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.service.transfer.TransferInspectService;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class InspectMonitorStatusHandler implements BaseEventHandler {
	
	private TransferInspectService service = null;
	
	public InspectMonitorStatusHandler(TransferInspectService service) {
		this.service = service;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.SITE_CHECK_TRANSFER_TOPIC;
	}

	@Override
	public boolean processEvent(Object eventData) {
		//启动巡检动作
		dispose();
		return true;
	}
	
	private void dispose() {
		Map<String, MonitorDevice> map = InspectTaskContext.getInstance().getAllMoniDevice();
		Log.debug("[巡检服务]巡检站点状态，巡检站点个数=" + map.size());
		for(MonitorDevice device : map.values()) {
			service.put(device);
		}
	}

}
