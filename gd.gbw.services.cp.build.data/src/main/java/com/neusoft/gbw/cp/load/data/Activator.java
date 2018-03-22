package com.neusoft.gbw.cp.load.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.ServiceRegistration;

import com.neusoft.gbw.cp.core.service.IBuildDataService;
import com.neusoft.gbw.cp.load.data.build.application.TaskBuilderCentre;
import com.neusoft.gbw.cp.load.data.build.domain.services.BuildDataServiceImpl;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private TaskBuilderCentre buildMgr = null;
	private ServiceRegistration<?> service = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		return list;
	}
	
	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPDataSourceFactory.class);
		
		//服务启动
		if (buildMgr == null) {
			buildMgr = new TaskBuilderCentre();
			buildMgr.start();
		}
		registerServices();
	}
	
	private void registerServices() {
		String serviceName = IBuildDataService.class.getName();
		service = getContext().registerService(serviceName, new BuildDataServiceImpl(), null);
	}

	@Override
	public void stop() {
		unbindService(NPDataSourceFactory.class);
		unbindService(NMSSubject.class);
		unbindCoreServices();
		if (buildMgr != null)
			buildMgr.stop();
		if (service != null)
			service.unregister();
	}
	
}
