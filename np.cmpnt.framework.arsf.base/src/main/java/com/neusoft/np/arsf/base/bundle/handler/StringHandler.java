package com.neusoft.np.arsf.base.bundle.handler;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.exception.NMException;

public abstract class StringHandler implements BaseEventHandler {

	@Override
	public boolean processEvent(Object eventData) {
		if (eventData == null) {
			return false;
		}
		initHandler();
		try {
			return processStrEvent(String.valueOf(eventData));
		} catch (NMException e) {
			Log.error("字符串类Event处理异常", e);
		} catch (Exception e) {
			Log.error("Event处理异常", e);
		}
		clearHandler();
		return true;
	}

	public abstract boolean processStrEvent(String eventData) throws NMException;

	public void initHandler() {
		// 批量操作
	}

	public void clearHandler() {
		// 批量操作
	}

	public void batchUpdata() throws NMException {
		// 批量操作
	}

}
