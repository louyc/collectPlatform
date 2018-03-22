package com.neusoft.np.arsf.db.oracle;

import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.eclipse.gemini.dbaccess.AbstractDataSourceFactory;

public class PGDataSourceFactory extends AbstractDataSourceFactory {

	public PGDataSourceFactory() {
	}

	@Override
	public Driver newJdbcDriver() throws SQLException {
		return new oracle.jdbc.driver.OracleDriver();
	}

	@Override
	public DataSource newDataSource() throws SQLException {
		return new oracle.jdbc.pool.OracleDataSource();
	}

	@Override
	public ConnectionPoolDataSource newConnectionPoolDataSource() throws SQLException {
		return new oracle.jdbc.pool.OracleConnectionPoolDataSource();
	}

	@Override
	public XADataSource newXADataSource() throws SQLException {
//		throw new SQLException("undone exception.");
		return new oracle.jdbc.xa.client.OracleXADataSource();
	}

}
