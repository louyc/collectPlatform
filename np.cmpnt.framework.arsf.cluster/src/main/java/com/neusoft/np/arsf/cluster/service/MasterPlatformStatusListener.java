package com.neusoft.np.arsf.cluster.service;

import java.sql.Connection;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.cluster.vo.ClusterType;
import com.neusoft.np.arsf.cluster.vo.PlatformStatus;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.service.cluster.FrameWorkError;

public class MasterPlatformStatusListener extends NMService {
	
	private FrameContext context = FrameContext.getInstance();
	private SqlSessionProcess session = null;
	
	public void init() {
		Connection conn = FrameContext.getInstance().getConnection();
		session = new SqlSessionProcess(conn);
	}
	
	@Override
	public void run() {
		try {
			while (isThreadRunning()) {
				PlatformStatus status = context.getPlatformStatus();
				if (status.equals(PlatformStatus.ACTIVE) || status.equals(PlatformStatus.PREPARE)) {
					sleep(1000);
					continue;
				}
				
				// 平台状态为DEAD，需要重新启动
				//写入数据库状态转变为Master, 原有的Master修改为Slave
				String name = ConfigVariable.PLATFORM_NAME;
				session.updateClusterConfigInfo(name, ClusterType.Master.name(), PlatformStatus.ACTIVE.name());
				session.updateClusterConfigInfo(ConfigVariable.OTHER_PLATFORM_NAME, ClusterType.Slave.name(), PlatformStatus.PREPARE.name());
				Log.info("集群Master平台状态为DEAD，更新数据库中主备平台的集群类型和平台状态完成。");
				ConfigVariable.PLATFORM_CLUSTER_TYPE=ClusterType.Master.name();
				ARSFToolkit.sendEvent("", buildFrameWorkError());
				break;
			}
		} finally {
			Log.info(this.serviceName + "已经关闭");
		}
	}
	
	private FrameWorkError buildFrameWorkError() {
		FrameWorkError error = new FrameWorkError();
		return error;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}