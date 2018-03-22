package com.neusoft.gbw.cp.station.service.transfer.servlet;

import java.util.concurrent.BlockingQueue;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import com.neusoft.gbw.cp.station.service.transfer.AbstractTransfer;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class ServletServerProcess extends AbstractTransfer{
	private ServiceTracker<?, ?> serviceTracker;
	private BlockingQueue<String> queue;
	private HttpServiceTrackerCustomizer custom = null;
	
	public ServletServerProcess(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void init() {
		super.init(queue);
		custom = new HttpServiceTrackerCustomizer(this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void start() {
		init();
		BundleContext context = ARSFToolkit.getBaseContext().getContext();
		//返回数据地址http协议
        serviceTracker = new ServiceTracker(context, HttpService.class.getName(), custom);  //返回数据地址http协议
        serviceTracker.open();
	}
	
	public void stop() {
		
	}

}
