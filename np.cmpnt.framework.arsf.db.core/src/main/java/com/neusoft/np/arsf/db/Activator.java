package com.neusoft.np.arsf.db;

import java.util.Collection;

import org.osgi.framework.ServiceRegistration;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.db.dom.service.DataSourceInit;
import com.neusoft.np.arsf.db.dom.service.DataSourceInitException;
import com.neusoft.np.arsf.db.infra.config.ClassContext;
import com.neusoft.np.arsf.db.service.NPDataSourceFactoryImpl;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;

public class Activator extends BaseActivator {

	private ServiceRegistration<?> service;

	private ClassContext context;

	private DataSourceInit dataSourceInit;

	@Override
	public void init() {
		bindCoreServices();
		context = ClassContext.getContext();
		context.setBaseContext(getBaseContext());

		dataSourceInit = new DataSourceInit();
		try {
			dataSourceInit.init();
		} catch (DataSourceInitException e) {
			Log.error("服务初始化失败", e);
			return;
		}
		registerServices();
	}

	private void registerServices() {
		String serviceName = NPDataSourceFactory.class.getName();
		service = getContext().registerService(serviceName, new NPDataSourceFactoryImpl(), null);
	}

	@Override
	public void stop() {
		if (service != null) {
			service.unregister();
		}
		unbindCoreServices();
	}

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		return null;
	}

}
