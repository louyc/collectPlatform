package com.neusoft.gbw.cp.process.inspect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.process.inspect.event.handler.InspectMonitorStatusHandler;
import com.neusoft.gbw.cp.process.inspect.event.handler.ReceiveAlarmTypeHandler;
import com.neusoft.gbw.cp.process.inspect.event.handler.ReceiveCollectDataHandler;
import com.neusoft.gbw.cp.process.inspect.event.handler.ReceiveInspectTaskHandler;
import com.neusoft.gbw.cp.process.inspect.event.handler.SyntMonitorDeviceHandler;
import com.neusoft.gbw.cp.process.inspect.service.InspectCentre;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private InspectCentre centre = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new ReceiveCollectDataHandler(centre.getMgr())); //采集数据业务处理主题
		list.add(new ReceiveInspectTaskHandler());//巡检指令处理主题
		list.add(new ReceiveAlarmTypeHandler());//同步告警类型字典数据主题
		list.add(new InspectMonitorStatusHandler(centre.getService()));//站点连通性监测以及软件存活状态校验
		list.add(new SyntMonitorDeviceHandler());//站点设备信息同步主题
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		centre = new InspectCentre();
		centre.start();
	}

	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();
		centre.stop();
	}

	
}
