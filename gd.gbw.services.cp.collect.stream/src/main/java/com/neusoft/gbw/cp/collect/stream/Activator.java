package com.neusoft.gbw.cp.collect.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.collect.stream.event.CollectStreamHandler;
import com.neusoft.gbw.cp.collect.stream.service.CollectStreamCentre;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private CollectStreamCentre centre = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		//获取总线信息需要获取的配置，注册订阅
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new CollectStreamHandler());
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		centre = new CollectStreamCentre();
		centre.start();
	}
	
	@Override
	public void stop() {
		if (centre != null)
			centre.stop();
		
		unbindService(NMSSubject.class);
		unbindCoreServices();
	}
}

