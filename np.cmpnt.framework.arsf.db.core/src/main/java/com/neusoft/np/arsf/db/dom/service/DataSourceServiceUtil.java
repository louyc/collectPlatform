package com.neusoft.np.arsf.db.dom.service;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.util.tracker.ServiceTracker;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.db.infra.config.ClassContext;
import com.neusoft.np.arsf.db.infra.config.DBParameter;
import com.neusoft.np.arsf.db.pool.NPPooledDataSource;

public class DataSourceServiceUtil {

	public static DataSourceFactory getDBService(String driverClassName) throws DataSourceServiceException {
		try {
			Filter filter = getDataSourceFactoryFilter(driverClassName);
			Object trackerService = getService(driverClassName, filter);
			DataSourceFactory service = (DataSourceFactory) trackerService;
			return service;
		} catch (InvalidSyntaxException e) {
			throw new DataSourceServiceException(e);
		}
	}

	public static NPPooledDataSource getPoolService(String driverClassName) throws DataSourceServiceException {
		try {
			Filter filter = getPoolSourceServiceFilter(driverClassName);
			Object trackerService = getService(driverClassName, filter);
			NPPooledDataSource service = (NPPooledDataSource) trackerService;
			return service;
		} catch (InvalidSyntaxException e) {
			throw new DataSourceServiceException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object getService(String driverClassName, Filter filter) throws DataSourceServiceException {
		ServiceTracker tracker = new ServiceTracker(ClassContext.getContext().getBaseContext().getContext(), filter,
				null);
		tracker.open();
		int count = 0;
		Object trackerService = null;
		try {
			while (trackerService == null && count < DBParameter.MAX_RECON) {
				trackerService = tracker.getService();
				if (trackerService != null) {
					break;
				}
				Log.info("db.service 获取 " + driverClassName + " 数据源服务失败，正在等待重连");
				Thread.sleep(DBParameter.SLEEP_TIME);
				count++;
			}
		} catch (InterruptedException e) {
			Log.info("获取数据库服务时，线程中断异常。InterruptedException");
		}
		tracker.close();
		if (trackerService == null) {
			throw new DataSourceServiceException("默认数据源获取失败，超出等待时间.");
		}
		return trackerService;
	}

	private static Filter getDataSourceFactoryFilter(String driverClassName) throws InvalidSyntaxException {
		return getFilter("org.osgi.service.jdbc.DataSourceFactory", driverClassName);
	}

	private static Filter getPoolSourceServiceFilter(String driverClassName) throws InvalidSyntaxException {
		return getFilter("com.neusoft.np.arsf.db.pool.NPPooledDataSource", driverClassName);
	}

	private static Filter getFilter(String interfaceName, String driverClassName) throws InvalidSyntaxException {
		BundleContext context = ClassContext.getContext().getBaseContext().getContext();
		String filterString = "(&(objectClass=" + interfaceName + ")" + "(" + DataSourceFactory.OSGI_JDBC_DRIVER_CLASS
				+ "=" + driverClassName + "))";
		return context.createFilter(filterString);
	}

}
