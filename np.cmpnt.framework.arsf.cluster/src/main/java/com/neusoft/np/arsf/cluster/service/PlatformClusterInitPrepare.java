package com.neusoft.np.arsf.cluster.service;

import java.sql.Connection;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ClusterConstants;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.cluster.vo.ClusterConfigInfo;
import com.neusoft.np.arsf.cluster.vo.ClusterType;
import com.neusoft.np.arsf.cluster.vo.PlatformStatus;

public class PlatformClusterInitPrepare {
	
	private SqlSessionProcess session = null; 

	public PlatformClusterInitPrepare() {
		Connection conn = FrameContext.getInstance().getConnection();
		session = new SqlSessionProcess(conn);
	}
	
	public void init() {
		String name = ConfigVariable.PLATFORM_NAME;
		String type = ConfigVariable.PLATFORM_CLUSTER_TYPE;
		
		boolean isContain = session.containTable(ClusterConstants.TABLE_NAME);
		ClusterConfigInfo info = null;
		if (isContain) {
			//赋值当前ClusterTpye和ClusterStatus
			info = session.selectClusterConfigInfo(name);
			if (info != null) {
				ConfigVariable.PLATFORM_CLUSTER_TYPE = info.getClusterType();
				PlatformStatus status = PlatformStatus.valueOf(info.getClusterStatus());
				FrameContext.getInstance().changePlatformStatus(status);
				return;
			}
		}
		
		ClusterType clusterType = ClusterType.valueOf(ConfigVariable.PLATFORM_CLUSTER_TYPE);
		if (clusterType.equals(ClusterType.Master)) {
			if (!isContain) {
				// 首次启动根据配置文件的集群类型为Master的进行表的创建和插入信息
				session.createClusterTable();
				Log.info("通过数据库创建平台集群信息表完成。");
				session.insertClusterCurrentStatus(name, type, PlatformStatus.ACTIVE.name());
				Log.info("首次平台启动插入集群配置信息完成");
			}
		} else if (clusterType.equals(ClusterType.Slave)) {
			while (true) {
				sleep(200);
				if (isContain) {
					info = session.selectClusterConfigInfo(name);
					if (info == null) {
						boolean result = session.insertClusterCurrentStatus(name, type, PlatformStatus.PREPARE.name());
						if (result) {
							Log.info("首次平台启动插入集群配置信息完成");
							break;
						}
					}
					break;
				}
			}
		}
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
