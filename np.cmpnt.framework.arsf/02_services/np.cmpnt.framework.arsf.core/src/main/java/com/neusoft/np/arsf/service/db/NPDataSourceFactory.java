package com.neusoft.np.arsf.service.db;

import java.sql.Driver;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 数据源获取接口<br>
 * 创建日期: 2013年12月10日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年12月10日       黄守凯        创建
 * </pre>
 */
public interface NPDataSourceFactory {

	String JDBC_URL = "url";

	String JDBC_USER = "user";

	String JDBC_PASSWORD = "password";

	String JDBC_DRIVER_CLASS = "osgi.jdbc.driver.class";

	String JDBC_DRIVER_NAME = "osgi.jdbc.driver.name";

	// -------------------- 默认数据源 -------------------- 

	/**
	 * 获取框架默认数据源
	 */
	@Deprecated
	DataSource createDefaultDataSource() throws NPDataSourceException;

	/**
	 * 获取框架默认数据源连接池
	 */
	ConnectionPoolDataSource createDefaultConnectionPoolDataSource() throws NPDataSourceException;

	/**
	 * 获取框架默认数据源驱动
	 */
	Driver createDefaultDriver() throws NPDataSourceException;

	/**
	 * 获取框架默认JTA数据源
	 */
	XADataSource createDefaultXADataSource() throws NPDataSourceException;

	// -------------------- 定制化 -------------------- 

	/**
	 * 根据配置文件信息，获取指定数据源
	 */
	@Deprecated
	DataSource createDataSource(Properties props) throws NPDataSourceException;

	/**
	 * 根据配置文件信息，获取指定数据源连接池
	 */
	ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws NPDataSourceException;

	/**
	 * 根据配置文件信息，获取指定数据源驱动
	 */
	Driver createDriver(Properties props) throws NPDataSourceException;

	/**
	 * 根据配置文件信息，获取指定JTA数据源
	 */
	XADataSource createXADataSource(Properties props) throws NPDataSourceException;

	// -------------------- 第三方数据池 -------------------- 
	/**
	 * 推荐使用的第三方数据库连接池。
	 */
	DataSource createDefaultPooledDataSource() throws NPDataSourceException;

	DataSource createPooledDataSource(Properties props) throws NPDataSourceException;
}
