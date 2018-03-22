package com.neusoft.np.arsf.cluster.service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.cluster.vo.ClusterConfigInfo;
import com.neusoft.np.arsf.cluster.vo.ClusterType;
import com.neusoft.np.arsf.common.util.NMService;

public class PlatformClusterTypeListener extends NMService {
	
	private SqlSessionProcess session = null;
	private ClusterType lastType = null;
	private PlatformClusterCentre centre = null;
	
	public PlatformClusterTypeListener(PlatformClusterCentre centre) {
		this.centre = centre;
	}

	public void init() {
		Connection conn = FrameContext.getInstance().getConnection();
		session = new SqlSessionProcess(conn);
		// 首次初始化数据库集群配置信息
		this.lastType = ClusterType.valueOf(ConfigVariable.PLATFORM_CLUSTER_TYPE);
	}

	@Override
	public void run() {
		// 如果不是首次根据数据表中的集群类型进行监听
		String name = ConfigVariable.PLATFORM_NAME;
		ClusterConfigInfo info = null;
		while (isThreadRunning()) {
			info = session.selectClusterConfigInfo(name);
			if (info == null) {
				Log.warn("查询集群配置信息为空，请查看对应的配置表");
				continue;
			}

			ClusterType type = ClusterType.valueOf(info.getClusterType());
			if (type.equals(lastType)) {
				sleep(1000);
				continue;
			}

			// 此线程依然继续运行
			switch (type) {
			case Master:
				// 平台 Slave=〉Master
				// 1.关闭Slave心跳线程；
				// 2.切换虚拟IP地址；
				// 3.启动Master心跳服务端；
				// 4.启动当前平台的业务服务
				centre.changeAndStartPlatform();
				break;
			case Slave:
				// 平台 Master=〉Slave
				// 1.切换虚拟IP地址；
				// 2.关闭当前平台（等待人为重新启动平台或通过wrapper重新启动）
				centre.updateVirtualIPAddress();
				Log.info("平台出现业务线程异常无法重新启动，关闭当前平台，集群类型转换为Slave");
				restartByWrapper();
				break;
			}
			
			this.lastType = type;
		}
	}
	
	private void restartByWrapper() {
		//通过Wrapper重启，如果没有绑定Wrapper，程序直接停止；
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		String command = "cmd.exe /c taskkill /F /PID " + pid;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
