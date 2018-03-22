package com.neusoft.np.arsf.db.oracle;

import java.util.Collection;
import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jdbc.DataSourceFactory;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.db.oracle.pool.NPPooledDataSourceImpl;
import com.neusoft.np.arsf.db.oracle.service.PGServiceProperties;
import com.neusoft.np.arsf.db.pool.NPPooledDataSource;

public class Activator extends BaseActivator {

	private ServiceRegistration<?> dataSourceFactoryService;

	private ServiceRegistration<?> pooledDataSourceService;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		return null;
	}

	@Override
	public void init() {
		bindCoreServices();
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(DataSourceFactory.OSGI_JDBC_DRIVER_NAME, PGServiceProperties.ORACLE_DRIVER_NAME);
		props.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS, PGServiceProperties.ORACLE_DRIVER_CLASS);

		dataSourceFactoryService = getContext().registerService(DataSourceFactory.class.getName(), new PGDataSourceFactory(),
				props);
		pooledDataSourceService = getContext().registerService(NPPooledDataSource.class.getName(),
				new NPPooledDataSourceImpl(), props);
	}

	@Override
	public void stop() {
		if (dataSourceFactoryService != null) {
			dataSourceFactoryService.unregister();
		}
		if (pooledDataSourceService != null) {
			pooledDataSourceService.unregister();
		}
		unbindCoreServices();
	}

}