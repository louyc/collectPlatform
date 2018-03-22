package com.neusoft.np.arsf.base.bundle;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class DefSrvTrackerCustomizer implements ServiceTrackerCustomizer<Object, Object> {

	private BundleContext context;

	private Object oService;

	public DefSrvTrackerCustomizer(BundleContext context) {
		this.context = context;
	}

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	public Object getoService() {
		return oService;
	}

	public void setoService(Object oService) {
		this.oService = oService;
	}

	@Override
	public Object addingService(ServiceReference<Object> reference) {
		return context.getService(reference);
	}

	@Override
	public void modifiedService(ServiceReference<Object> reference, Object service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removedService(ServiceReference<Object> reference, Object service) {
		if (oService != null) {
			BaseServicePool.getInstance().unregisterService(oService.getClass().getName());
		}
	}

}
