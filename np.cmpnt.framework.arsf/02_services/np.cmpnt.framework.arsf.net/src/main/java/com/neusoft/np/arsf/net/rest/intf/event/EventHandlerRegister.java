package com.neusoft.np.arsf.net.rest.intf.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.net.rest.intf.event.handler.SyntHandler;

public class EventHandlerRegister {

	public static Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> handlers = new ArrayList<BaseEventHandler>();
		handlers.add(new SyntHandler());
		return handlers;
	}
}
