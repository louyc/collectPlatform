package com.neusoft.np.arsf.db.oracle.pool;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.neusoft.np.arsf.db.infra.constants.DBConstants;
import com.neusoft.np.arsf.db.pool.NPPooledDataSource;
import com.neusoft.np.arsf.db.pool.NPPooledDataSourceException;

public class NPPooledDataSourceImpl implements NPPooledDataSource {

	@Override
	public DataSource createPooledDataSource(Properties props) throws NPPooledDataSourceException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(props.getProperty(JDBC_DRIVER_CLASS));
			cpds = initComboPooledDataSource(cpds, props);
			return cpds;
		} catch (PropertyVetoException e) {
			throw new NPPooledDataSourceException(e);
		}
	}

	protected ComboPooledDataSource initComboPooledDataSource(ComboPooledDataSource cpds, Properties props) {
		cpds.setJdbcUrl(props.getProperty(JDBC_URL));
		cpds.setUser(props.getProperty(JDBC_USER));
		cpds.setPassword(props.getProperty(JDBC_PASSWORD));
		cpds.setAcquireIncrement(Integer.valueOf(props.getProperty(DBConstants.ACQUIREINCREMENT)));
		cpds.setInitialPoolSize(Integer.valueOf(props.getProperty(DBConstants.INITIALPOOLSIZE)));
		cpds.setIdleConnectionTestPeriod(Integer.valueOf(props.getProperty(DBConstants.IDLECONNECTIONTESTPERIOD)));
		cpds.setMinPoolSize(Integer.valueOf(props.getProperty(DBConstants.MINPOOLSIZE)));
		cpds.setMaxPoolSize(Integer.valueOf(props.getProperty(DBConstants.MAXPOOLSIZE)));
		cpds.setMaxStatements(Integer.valueOf(props.getProperty(DBConstants.MAXSTATEMENTS)));
		cpds.setMaxStatementsPerConnection(Integer.valueOf(props.getProperty(DBConstants.MAXSTATEMENTSPERCONNECTION)));
		cpds.setMaxIdleTime(Integer.valueOf(props.getProperty(DBConstants.MAXIDLETIME)));
		cpds.setAcquireRetryAttempts(Integer.valueOf(props.getProperty(DBConstants.ACQUIRERETRYATTEMPTS)));
		cpds.setBreakAfterAcquireFailure("true".equals(props.getProperty(DBConstants.BREAKAFTERACQUIREFAILURE)));
		cpds.setTestConnectionOnCheckout("true".equals(props.getProperty(DBConstants.TESTCONNECTIONONCHECKOUT)));
		return cpds;
	}

}
