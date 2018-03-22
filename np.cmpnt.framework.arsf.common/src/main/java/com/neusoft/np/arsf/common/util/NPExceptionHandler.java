package com.neusoft.np.arsf.common.util;

import java.lang.Thread.UncaughtExceptionHandler;

public interface NPExceptionHandler extends UncaughtExceptionHandler {

	void uncaughtThreadPoolException(Runnable t, Throwable e);

}
