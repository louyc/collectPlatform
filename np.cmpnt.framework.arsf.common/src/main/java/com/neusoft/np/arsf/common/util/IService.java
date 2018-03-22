package com.neusoft.np.arsf.common.util;

public interface IService extends Runnable {
	
	String getServiceName();

	void setServiceName(String serviceName);

	boolean isThreadRunning();

	void setThreadRunning(boolean runThread);

	void stopThreadRunning();
}
