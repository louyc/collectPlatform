package com.neusoft.np.arsf.common.util;

public abstract class NMBaseService extends NMService {

	@Override
	public void run() {
		try {
			while (isThreadRunning() && !Thread.currentThread().isInterrupted()) {
				execute();
			}
		} catch (NMServiceExecuteException e) {
			log(getServiceName() + "线程中断");
		}
	}

	public abstract void execute() throws NMServiceExecuteException;

	@Override
	public String getServiceName() {
		return Thread.currentThread().getName();
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public abstract void log(String info);

}
