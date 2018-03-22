package com.neusoft.np.arsf.db.service;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.osgi.service.jdbc.DataSourceFactory;

import com.neusoft.np.arsf.common.util.NMCheckArgcUtil;
import com.neusoft.np.arsf.db.dom.entity.DefaultDataSourceFactoryBean;
import com.neusoft.np.arsf.db.dom.pool.DataSourceItemPool;
import com.neusoft.np.arsf.db.dom.service.DataSourceServiceException;
import com.neusoft.np.arsf.db.dom.service.DataSourceServiceUtil;
import com.neusoft.np.arsf.db.pool.NPPooledDataSource;
import com.neusoft.np.arsf.db.pool.NPPooledDataSourceException;
import com.neusoft.np.arsf.service.db.NPDataSourceException;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;

public class NPDataSourceFactoryImpl implements NPDataSourceFactory {

	@Override
	public DataSource createDefaultDataSource() throws NPDataSourceException {
		return getDefaultDataSourceFactoryBean().getDefalutDataSource();
	}

	@Override
	public DataSource createDataSource(Properties props) throws NPDataSourceException {
		check(props);
		String driverClassName = props.getProperty(JDBC_DRIVER_CLASS);
		DataSourceFactory factory = getDataSourceFactory(driverClassName);
		try {
			return factory.createDataSource(props);
		} catch (SQLException e) {
			throw new NPDataSourceException(e);
		}
	}

	@Override
	public ConnectionPoolDataSource createDefaultConnectionPoolDataSource() throws NPDataSourceException {
		return getDefaultDataSourceFactoryBean().getDefalutConnectionPoolDataSource();
	}

	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws NPDataSourceException {
		check(props);
		String driverClassName = props.getProperty(JDBC_DRIVER_CLASS);
		DataSourceFactory factory = getDataSourceFactory(driverClassName);
		try {
			return factory.createConnectionPoolDataSource(props);
		} catch (SQLException e) {
			throw new NPDataSourceException(e);
		}
	}

	@Override
	public Driver createDefaultDriver() throws NPDataSourceException {
		return getDefaultDataSourceFactoryBean().getDefalutDriver();
	}

	@Override
	public Driver createDriver(Properties props) throws NPDataSourceException {
		String driverClassName = props.getProperty(JDBC_DRIVER_CLASS);
		if (!NMCheckArgcUtil.checkParameter(driverClassName)) {
			throw new NPDataSourceException("需要配置参数：JDBC_DRIVER_CLASS。");
		}
		DataSourceFactory factory = getDataSourceFactory(driverClassName);
		try {
			return factory.createDriver(props);
		} catch (SQLException e) {
			throw new NPDataSourceException(e);
		}
	}

	@Override
	public XADataSource createDefaultXADataSource() throws NPDataSourceException {
		return getDefaultDataSourceFactoryBean().getDefalutXADataSource();
	}

	@Override
	public XADataSource createXADataSource(Properties props) throws NPDataSourceException {
		check(props);
		String driverClassName = props.getProperty(JDBC_DRIVER_CLASS);
		DataSourceFactory factory = getDataSourceFactory(driverClassName);
		try {
			return factory.createXADataSource(props);
		} catch (SQLException e) {
			throw new NPDataSourceException(e);
		}
	}

	private void check(Properties props) throws NPDataSourceException {
		if (!NMCheckArgcUtil.checkParameter(props.getProperty(JDBC_DRIVER_CLASS), props.getProperty(JDBC_URL),
				props.getProperty(JDBC_USER), props.getProperty(JDBC_PASSWORD))) {
			throw new NPDataSourceException("需要配置参数：JDBC_DRIVER_CLASS、JDBC_URL、JDBC_USER、JDBC_PASSWORD。");
		}
	}

	private DataSourceFactory getDataSourceFactory(String driverClassName) throws NPDataSourceException {
		try {
			DataSourceFactory factory = DataSourceServiceUtil.getDBService(driverClassName);
			return factory;
		} catch (DataSourceServiceException e) {
			throw new NPDataSourceException(e);
		}
	}

	@Override
	public DataSource createDefaultPooledDataSource() throws NPDataSourceException {
		return getDefaultDataSourceFactoryBean().getPoolDataSource();
	}

	@Override
	public DataSource createPooledDataSource(Properties props) throws NPDataSourceException {
		try {
			String className = props.getProperty(JDBC_DRIVER_CLASS);
			if (!NMCheckArgcUtil.checkString(className)) {
				throw new NPDataSourceException("缺少JDBC_DRIVER_CLASS参数：" + props);
			}
			NPPooledDataSource poolFactory = DataSourceServiceUtil.getPoolService(className);
			DataSource cpds = poolFactory.createPooledDataSource(props);
			return cpds;
		} catch (DataSourceServiceException e) {
			throw new NPDataSourceException(e);
		} catch (NPPooledDataSourceException e) {
			throw new NPDataSourceException(e);
		}
	}

	private DefaultDataSourceFactoryBean getDefaultDataSourceFactoryBean() {
		return DataSourceItemPool.getInstance().getDefault().getDataSourceFactoryBean();
	}

}
