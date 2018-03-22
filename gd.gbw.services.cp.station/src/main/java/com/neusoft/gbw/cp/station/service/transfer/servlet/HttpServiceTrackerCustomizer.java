package com.neusoft.gbw.cp.station.service.transfer.servlet;


import javax.servlet.ServletException;

import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.neusoft.gbw.cp.station.service.transfer.ITransfer;
import com.neusoft.gbw.cp.station.service.transfer.socket.SocketServerProcess;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class HttpServiceTrackerCustomizer implements ServiceTrackerCustomizer<Object, Object>{

	private HttpService httpService; 
	private ITransfer transer;
	
	public HttpServiceTrackerCustomizer(ITransfer transer) {
		this.transer = transer;
	}
	
	@SuppressWarnings("unused")
	private SocketServerProcess socketServerMgr;

	@Override
	public Object addingService(ServiceReference<Object> arg0) {  
		Object service = ARSFToolkit.getBaseContext().getContext().getService(arg0);
        httpService = (HttpService) service;  
        try {  
        	httpService.registerServlet("/gbjg/station",new ServletServerHandler(transer),null,null);
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
        httpService.unregister("/gbjg/station");  
        httpService = null;  
    }
}
