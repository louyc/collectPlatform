package com.neusoft.np.arsf.db.dom.pool;

import java.util.Map;

import com.neusoft.np.arsf.db.dom.entity.DefaultDataSourceFactoryBean;

public class DataSourceItem {

	private String url;

	private String driverClassName;

	private String username;

	private String password;

	private DefaultDataSourceFactoryBean dataSourceFactoryBean;

	private Map<String, String> c3p0Config;

	public DataSourceItem() {
		init();
	}

	public DataSourceItem(String url, String driverClassName, String username, String password) {
		init();
		this.url = url;
		this.driverClassName = driverClassName;
		this.username = username;
		this.password = password;
	}

	public void init() {
		this.dataSourceFactoryBean = new DefaultDataSourceFactoryBean();
	}

	public void clear() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DefaultDataSourceFactoryBean getDataSourceFactoryBean() {
		return dataSourceFactoryBean;
	}

	public void setDataSourceFactoryBean(DefaultDataSourceFactoryBean dataSourceFactoryBean) {
		this.dataSourceFactoryBean = dataSourceFactoryBean;
	}

	public Map<String, String> getC3p0Config() {
		return c3p0Config;
	}

	public void setC3p0Config(Map<String, String> c3p0Config) {
		this.c3p0Config = c3p0Config;
	}

}
