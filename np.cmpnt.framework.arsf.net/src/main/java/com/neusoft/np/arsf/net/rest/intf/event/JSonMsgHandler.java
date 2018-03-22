package com.neusoft.np.arsf.net.rest.intf.event;

import java.util.Map;

import com.neusoft.np.arsf.base.bundle.handler.StringHandler;
import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;

public abstract class JSonMsgHandler extends StringHandler {

	public boolean processStrEvent(String eventData) throws NMException {
		try {
			Map<String, String> message = NPJsonUtil.jsonValueToMap(eventData);
			processMapEvent(message);
			batchUpdata();
			return true;
		} catch (NMFormateException e) {
			throw new NMException(e);
		}
	}

	public abstract boolean processMapEvent(Map<String, String> eventData);

}
