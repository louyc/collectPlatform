package com.neusoft.np.arsf.cluster.context;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.service.db.NPDataSourceException;

public class SqlConnectionProcess {
	
	private Connection connect = null;
	
	protected void init() {
		DataSource dataSource = null;
		try {
			dataSource = ARSFToolkit.getDefaultDataSource().createDefaultPooledDataSource();
		} catch (NPDataSourceException e) {
			Log.error("获取数据源失败，服务初始化失败", e);
			return;
		}
		
		try {
			connect = dataSource.getConnection();
		} catch (SQLException e) {
		}
	}

	protected Connection getConnect() {
		return connect;
	}
}
