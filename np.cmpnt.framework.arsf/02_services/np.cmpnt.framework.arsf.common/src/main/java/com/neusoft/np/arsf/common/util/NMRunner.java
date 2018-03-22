package com.neusoft.np.arsf.common.util;

public abstract class NMRunner extends NMService {

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				doRun();
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void doRun() throws InterruptedException;

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
