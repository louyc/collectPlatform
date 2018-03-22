package com.neusoft.gbw.cp.build.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class Activator extends BaseActivator {
	
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		return list;
	}
	
	@Override
	public void init() {
		bindCoreServices();
		
	}

	@Override
	public void stop() {
		unbindCoreServices();
	}
	
}
