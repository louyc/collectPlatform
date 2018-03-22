package com.neusoft.gbw.cp.collect.service.transfer.servlet;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import com.neusoft.gbw.cp.collect.constants.CollectVariable;
import com.neusoft.gbw.cp.collect.service.transfer.TransferUpMgr;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class HttpTranferServer {
	
	private TransferUpMgr transferUpMgr;
	
	public HttpTranferServer(TransferUpMgr transferUpMgr) {
		this.transferUpMgr = transferUpMgr;
	}

	private ServiceTracker<?, ?> httpServiceTracker; 
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void startServer() {
		BundleContext context = ARSFToolkit.getBaseContext().getContext();
		String httpName = HttpService.class.getName();
		httpServiceTracker = new ServiceTracker(context, httpName, new HttpServiceTrackerCustomizer(transferUpMgr,CollectVariable.RECOVERY_DATE_SERVLET_SPLIT_STRING));  
        httpServiceTracker.open();
	}
	
	public void stopServer() {
		if (httpServiceTracker != null)
			httpServiceTracker.close();
	}
}
