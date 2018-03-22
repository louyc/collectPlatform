package com.neusoft.np.arsf.net.rest;

import java.util.Collection;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.net.rest.domain.Centre;
import com.neusoft.np.arsf.net.rest.intf.event.EventHandlerRegister;

public class Activator extends BaseActivator {

	private Centre centre = new Centre();

	@Override
	public void init() {
		bindCoreServices();
		centre.init();
	}

	@Override
	public void stop() {
		centre.stop();
		unbindCoreServices();
	}

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		return EventHandlerRegister.getEventHandler();
	}

}
