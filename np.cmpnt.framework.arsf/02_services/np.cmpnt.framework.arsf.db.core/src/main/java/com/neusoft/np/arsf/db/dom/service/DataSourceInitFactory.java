package com.neusoft.np.arsf.db.dom.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.osgi.service.jdbc.DataSourceFactory;

import com.neusoft.np.arsf.base.bundle.BaseContext;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.db.dom.pool.DataSourceItem;
import com.neusoft.np.arsf.db.infra.config.ClassContext;
import com.neusoft.np.arsf.db.infra.constants.DBConstants;
import com.neusoft.np.arsf.db.pool.NPPooledDataSource;
import com.neusoft.np.arsf.db.pool.NPPooledDataSourceException;

public class DataSourceInitFactory {

	public void initDataFactory(Map<String, DataSourceItem> dcMap) throws DataSourceInitException {
		Iterator<Entry<String, DataSourceItem>> dcIter = dcMap.entrySet().iterator();
		while (dcIter.hasNext()) {
			Entry<String, DataSourceItem> e = dcIter.next();
			initDataSourceItem(e.getKey(), e.getValue());
		}
	}

	private void initDataSourceItem(String key, DataSourceItem value) throws DataSourceInitException {
		try {
			initDefaultDataSource(value);
			createDefalutDataSource(value);
			createDefalutConnectionPoolDataSource(value);
			createDefalutDriver(value);
			createDefaultXADataSource(value);
			createCPDSDataSource(value);
		} catch (SQLException e) {
			throw new DataSourceInitException(e);
		} catch (DataSourceInitException e) {
			throw new DataSourceInitException(e);
		}
	}

	private void initDefaultDataSource(DataSourceItem value) throws DataSourceInitException {
		try {
			DataSourceFactory factory = DataSourceServiceUtil.getDBService(value.getDriverClassName());
			value.getDataSourceFactoryBean().setFactory(factory);
		} catch (DataSourceServiceException e) {
			throw new DataSourceInitException("默认数据源服务获取失败.", e);
		}
	}

	private void createDefalutDataSource(DataSourceItem defaultDataSource) throws SQLException {
		Properties props = getProperties(defaultDataSource.getUrl(), defaultDataSource.getUsername(),
				defaultDataSource.getPassword());
		DataSourceFactory factory = defaultDataSource.getDataSourceFactoryBean().getFactory();
		DataSource ds = factory.createDataSource(props);
		defaultDataSource.getDataSourceFactoryBean().setDefalutDataSource(ds);
		publishDataSource(ds, null);
	}

	private void createDefalutConnectionPoolDataSource(DataSourceItem defaultDataSource) throws SQLException {
		Properties props = getProperties(null, defaultDataSource.getUsername(), defaultDataSource.getPassword());
		DataSourceFactory factory = defaultDataSource.getDataSourceFactoryBean().getFactory();
		ConnectionPoolDataSource cpds = factory.createConnectionPoolDataSource(props);
		defaultDataSource.getDataSourceFactoryBean().setDefalutConnectionPoolDataSource(cpds);
	}

	private void createDefalutDriver(DataSourceItem defaultDataSource) throws SQLException {
		DataSourceFactory factory = defaultDataSource.getDataSourceFactoryBean().getFactory();
		defaultDataSource.getDataSourceFactoryBean().setDefalutDriver(factory.createDriver(null));
	}

	private void createDefaultXADataSource(DataSourceItem defaultDataSource) throws SQLException {
		Properties props = getProperties(null, defaultDataSource.getUsername(), defaultDataSource.getPassword());
		DataSourceFactory factory = defaultDataSource.getDataSourceFactoryBean().getFactory();
		defaultDataSource.getDataSourceFactoryBean().setDefalutXADataSource(factory.createXADataSource(props));
	}

	private void createCPDSDataSource(DataSourceItem defaultDataSource) throws DataSourceInitException {
		if (defaultDataSource.getC3p0Config() == null || defaultDataSource.getC3p0Config().size() == 0) {
			return;
		}
		try {
			NPPooledDataSource poolFactory = DataSourceServiceUtil.getPoolService(defaultDataSource
					.getDriverClassName());
			Properties props = new Properties();
			props.put(NPPooledDataSource.JDBC_DRIVER_CLASS, defaultDataSource.getDriverClassName());
			props.put(NPPooledDataSource.JDBC_URL, defaultDataSource.getUrl());
			props.put(NPPooledDataSource.JDBC_USER, defaultDataSource.getUsername());
			props.put(NPPooledDataSource.JDBC_PASSWORD, defaultDataSource.getPassword());
			props.putAll(defaultDataSource.getC3p0Config());
			DataSource cpds = poolFactory.createPooledDataSource(props);
			defaultDataSource.getDataSourceFactoryBean().setPoolDataSource(cpds);
			publishDataSource(cpds, DBConstants.DEFAULT_C3P0_SOURCE);
		} catch (DataSourceServiceException e) {
			throw new DataSourceInitException(e);
		} catch (NPPooledDataSourceException e) {
			throw new DataSourceInitException(e);
		}
	}

	private Properties getProperties(String url, String username, String password) {
		Properties props = new Properties();
		if (url != null) {
			props.put(DataSourceFactory.JDBC_URL, url);
		}
		props.put(DataSourceFactory.JDBC_USER, username);
		props.put(DataSourceFactory.JDBC_PASSWORD, password);
		return props;
	}

	private void publishDataSource(DataSource ds, String dsId) {
		try {
			Connection conn = ds.getConnection();
			DatabaseMetaData metadata = conn.getMetaData();
			Log.info("Driver accessed by sample Gemini DBAccess client:" + "\n\tName = " + metadata.getDriverName()
					+ "\n\tVersion = " + metadata.getDriverVersion() + "\n\tUser = " + metadata.getUserName()
					+ "\n\tIPAddress = " + metadata.getURL());
			conn.close();
		} catch (SQLException e) {
			Log.error("数据源连接测试失败", e);
		}
		registService(ds, dsId);
	}

	static int i = 0;

	private void registService(DataSource ds, String dsId) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		if (dsId == null) {
			props.put("dsBeanId", "dataSource" + i);
			i++;
		} else {
			props.put("dsBeanId", dsId);
		}
		String serviceName = DataSource.class.getName();
		BaseContext context = ClassContext.getContext().getBaseContext();
		context.getContext().registerService(serviceName, ds, props);
	}
}
