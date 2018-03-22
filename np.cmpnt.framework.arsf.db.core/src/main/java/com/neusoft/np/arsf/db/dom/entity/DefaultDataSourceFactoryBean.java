package com.neusoft.np.arsf.db.dom.entity;

import java.sql.Driver;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osgi.service.jdbc.DataSourceFactory;

public class DefaultDataSourceFactoryBean {

	private DataSourceFactory factory;

	private DataSource defalutDataSource;

	private ConnectionPoolDataSource defalutConnectionPoolDataSource;

	private Driver defalutDriver;

	private XADataSource defalutXADataSource;

	private DataSource poolDataSource;

	public DataSourceFactory getFactory() {
		return factory;
	}

	public void setFactory(DataSourceFactory factory) {
		this.factory = factory;
	}

	public DataSource getDefalutDataSource() {
		return defalutDataSource;
	}

	public void setDefalutDataSource(DataSource defalutDataSource) {
		this.defalutDataSource = defalutDataSource;
	}

	public ConnectionPoolDataSource getDefalutConnectionPoolDataSource() {
		return defalutConnectionPoolDataSource;
	}

	public void setDefalutConnectionPoolDataSource(ConnectionPoolDataSource defalutConnectionPoolDataSource) {
		this.defalutConnectionPoolDataSource = defalutConnectionPoolDataSource;
	}

	public Driver getDefalutDriver() {
		return defalutDriver;
	}

	public void setDefalutDriver(Driver defalutDriver) {
		this.defalutDriver = defalutDriver;
	}

	public XADataSource getDefalutXADataSource() {
		return defalutXADataSource;
	}

	public void setDefalutXADataSource(XADataSource defalutXADataSource) {
		this.defalutXADataSource = defalutXADataSource;
	}

	public DataSource getPoolDataSource() {
		return poolDataSource;
	}

	public void setPoolDataSource(DataSource poolDataSource) {
		this.poolDataSource = poolDataSource;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
