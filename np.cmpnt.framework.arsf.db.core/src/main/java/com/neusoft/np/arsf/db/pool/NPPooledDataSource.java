package com.neusoft.np.arsf.db.pool;

import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.service.jdbc.DataSourceFactory;

public interface NPPooledDataSource {

	String JDBC_URL = DataSourceFactory.JDBC_URL;

	String JDBC_USER = DataSourceFactory.JDBC_USER;

	String JDBC_PASSWORD = DataSourceFactory.JDBC_PASSWORD;

	String JDBC_DRIVER_CLASS = DataSourceFactory.OSGI_JDBC_DRIVER_CLASS;

	String JDBC_DRIVER_NAME = DataSourceFactory.OSGI_JDBC_DRIVER_NAME;

	DataSource createPooledDataSource(Properties props) throws NPPooledDataSourceException;

}
