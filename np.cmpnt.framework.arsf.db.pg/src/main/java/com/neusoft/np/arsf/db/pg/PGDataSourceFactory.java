package com.neusoft.np.arsf.db.pg;

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
		return new org.postgresql.Driver();
	}

	@Override
	public DataSource newDataSource() throws SQLException {
		return new org.postgresql.ds.PGSimpleDataSource();
	}

	@Override
	public ConnectionPoolDataSource newConnectionPoolDataSource() throws SQLException {
		return new org.postgresql.ds.PGConnectionPoolDataSource();
	}

	@Override
	public XADataSource newXADataSource() throws SQLException {
		return new org.postgresql.xa.PGXADataSource();
	}

}
