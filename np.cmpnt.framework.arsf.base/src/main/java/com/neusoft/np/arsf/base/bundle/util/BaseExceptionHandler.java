package com.neusoft.np.arsf.base.bundle.util;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseContextImpl;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;
import com.neusoft.np.arsf.base.bundle.event.NPEventUtil;
import com.neusoft.np.arsf.common.util.NPExceptionHandler;
import com.neusoft.np.arsf.common.util.NPMessage;

public class BaseExceptionHandler implements NPExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		handlerThreadException(t.getName(), ThrowableLogUtil.getThrowableLog(e));
	}

	@Override
	public void uncaughtThreadPoolException(Runnable t, Throwable e) {
		handlerThreadException(t.getClass().getName(), ThrowableLogUtil.getThrowableLog(e));
	}

	protected static void handlerThreadException(String name, String date) {
		NPMessage message = new NPMessage();
		message.put(NPBaseConstant.Util.DATE, date);
		message.put(NPBaseConstant.Util.NAME, name);
		message.put(NPBaseConstant.Util.BUNDLE_NAME, BaseContextImpl.getInstance().getBundleName());
		Log.warn("线程异常：线程(" + name + ")，问题：" + date);
		ARSFToolkit.sendEvent(NPBaseConstant.EventTopic.THREAD_THROWABLE_TOPIC, NPEventUtil.buildXml(message));
	}

}
