package com.neusoft.np.arsf.base.bundle.util;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMCheckArgcUtil;
import com.neusoft.np.arsf.common.util.NMService;

public abstract class NPRunnable extends NMService {

	@Override
	public void run() {
		try {
			if (!initService()) {
				Log.warn("NPRunnable任务初始化失败，原因：调用initService方法返回false");
				return;
			}
			beforeRun();
			while (!Thread.interrupted() && isThreadRunning) {
				doRun();
			}
			afterRun();
		} catch (InterruptedException e) {
			Log.info("线程中断：" + Thread.currentThread().getName());
		} catch (Throwable e) {
			Log.error("NPRunnable捕获未处理异常，线程终止运行:", e);
			BaseExceptionHandler.handlerThreadException(getServiceName(), ThrowableLogUtil.getThrowableLog(e));
		}
	}

	/**
	 * 初始化，验证初始化是否成功
	 */
	public boolean initService() {
		return true;
	}

	/**
	 * 初始化，不验证初始化是否成功。
	 * 【遗留接口】
	 */
	public void beforeRun() {
	}

	public abstract void doRun() throws InterruptedException;

	public void afterRun() {
	}

	@Override
	public String getServiceName() {
		if (NMCheckArgcUtil.checkString(serviceName)) {
			return serviceName;
		}
		return this.getClass().getName();
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
