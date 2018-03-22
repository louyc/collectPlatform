package com.neusoft.gbw.cp.collect.service.transfer.servlet;

import javax.servlet.ServletException;

import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.neusoft.gbw.cp.collect.service.transfer.TransferUpMgr;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class HttpServiceTrackerCustomizer implements ServiceTrackerCustomizer<Object, Object>{

	private HttpService httpService; 
	
	private String servlet_url;
	
	private TransferUpMgr transferUpMgr = null;
	
	public HttpServiceTrackerCustomizer(TransferUpMgr transferUpMgr,String servlet_url) {
		this.servlet_url = servlet_url;
		this.transferUpMgr = transferUpMgr;
	}

	@Override
	public Object addingService(ServiceReference<Object> arg0) {  
        Object service = ARSFToolkit.getBaseContext().getContext().getService(arg0);  
        httpService = (HttpService) service;  
        try {  
        	httpService.registerServlet(servlet_url,new RecoveryDateServlet(transferUpMgr),null,null);
        } catch (ServletException e) {  
            e.printStackTrace();  
        } catch (NamespaceException e) {  
            e.printStackTrace();  
        }  
        return service;  
    }

	@Override
	public void modifiedService(ServiceReference<Object> arg0, Object arg1) {
		
	}

	@Override
	public void removedService(ServiceReference<Object> arg0, Object arg1) {  
        if(arg1 != httpService){  
            return;  
        }  
        httpService.unregister(servlet_url);  
        httpService = null;  
    }
}
