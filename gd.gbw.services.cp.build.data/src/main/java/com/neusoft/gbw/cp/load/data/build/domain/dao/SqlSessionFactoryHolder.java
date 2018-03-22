package com.neusoft.gbw.cp.load.data.build.domain.dao;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.service.db.NPDataSourceException;

public class SqlSessionFactoryHolder {
	
	private static class SessionFactoryHolder{
		private static final SqlSessionFactoryHolder INSTNCE = new SqlSessionFactoryHolder();
	}
	private SqlSessionFactoryHolder(){}
	
	public static SqlSessionFactoryHolder getInstance() {
		return SessionFactoryHolder.INSTNCE;
	}

	private SqlSessionFactory sqlSessionFactory;
	
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	private void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public synchronized void init() throws Exception {
		DataSource dataSource = null;
		TransactionFactory factory = new JdbcTransactionFactory();
		try {
			dataSource = ARSFToolkit.getDefaultDataSource().createDefaultPooledDataSource();
		} catch (NPDataSourceException e) {
			Log.error("获取数据源失败，服务初始化失败", e);
			return;
		}
		
		Environment ment = new Environment("dev", factory, dataSource);
		Configuration config = new Configuration(ment);
		config.addMapper(CPMapper.class);
		setSqlSessionFactory(new SqlSessionFactoryBuilder().build(config));
	}
}
