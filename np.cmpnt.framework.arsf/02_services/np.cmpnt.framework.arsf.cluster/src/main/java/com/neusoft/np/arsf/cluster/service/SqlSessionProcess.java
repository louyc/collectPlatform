package com.neusoft.np.arsf.cluster.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.vo.ClusterConfigInfo;

public class SqlSessionProcess {
	
	private Connection connect = null;

	public SqlSessionProcess(Connection connect) {
		this.connect = connect;
	}
	
//	protected void connect() {
//		DataSource dataSource = null;
//		while(true) {
//			try {
//				dataSource = ARSFToolkit.getDefaultDataSource().createDefaultPooledDataSource();
//				connect = dataSource.getConnection();
//				break;
//			} catch (Exception e) {
//				Log.error("获取数据源失败，服务初始化失败", e);
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e1) {
//				}
//				continue;
//			}
//		}
//	}
//	
	protected void createClusterTable() {
		Statement stat = null;
		try {
			stat = connect.createStatement();
			stat.execute(ConfigVariable.CLUSTER_SQL);
		} catch (SQLException e) {
			Log.error("数据库中的集群配置表创建失败", e);
		} finally {
			if (stat != null)
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	protected boolean insertClusterCurrentStatus(String name, String type, String status) {
		boolean result = false;
		PreparedStatement pre = null;
		String sql = "insert into platform_cluster_info_t(cluster_name, cluster_type, platform_run_status) values(?,?,?)";
		try {
			pre = connect.prepareStatement(sql);
			pre.setString(1, name);
			pre.setString(2, type);
			pre.setString(3, status);
			pre.execute();
			result = true;
		} catch (SQLException e) {
			Log.error("插入数据库中的集群配置表信息失败", e);
		} finally {
			if (pre != null)
				try {
					pre.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		} 
		
		return result;
	}
	
	protected boolean containTable(String tableName) {
		boolean result = false;
		ResultSet rs = null;
		try {
			rs = connect.getMetaData().getTables(null, null, tableName, null);
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return result;
	}
	
	protected ClusterConfigInfo selectClusterConfigInfo(String name) {
		String sql = "select * from platform_cluster_info_t where cluster_name=?";
		PreparedStatement pre = null;
		ClusterConfigInfo info = null;
		try {
			pre = connect.prepareStatement(sql);
			pre.setString(1, name);
			ResultSet rs = pre.executeQuery();
			while(rs.next()) {
				info = new ClusterConfigInfo();
				info.setClusterName(rs.getString(1));
				info.setClusterType(rs.getString(2));
				info.setClusterStatus(rs.getString(3));
				break;
			}
		} catch (SQLException e) {
			Log.error("插入数据库中的集群配置表信息失败", e);
		} finally {
			if (pre != null)
				try {
					pre.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return info; 
	}
	
	protected boolean updateClusterConfigInfo(String name, String type, String status) {
		boolean result = false;
		String sql = "update platform_cluster_info_t set cluster_type=?, platform_run_status=? where cluster_name=?";
		PreparedStatement pre = null;
		try {
			pre = connect.prepareStatement(sql);
			pre.setString(1, type);
			pre.setString(2, status);
			pre.setString(3, name);
			pre.executeUpdate();
			result = true;
		} catch (SQLException e) {
			Log.error("更新数据库中的集群配置表信息失败", e);
		} finally {
			if (pre != null)
				try {
					pre.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return result;
	}
}
