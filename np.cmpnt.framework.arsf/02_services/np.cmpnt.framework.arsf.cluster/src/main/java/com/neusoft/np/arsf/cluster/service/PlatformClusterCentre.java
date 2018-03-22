package com.neusoft.np.arsf.cluster.service;

import java.util.Map;

import com.neusoft.np.arsf.base.bundle.BaseServicePool;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.cluster.service.heart.HeartbeatListenerMgr;
import com.neusoft.np.arsf.cluster.vo.ClusterType;
import com.neusoft.np.arsf.common.util.NMServiceCentre;
import com.neusoft.np.arsf.laucher.NPLauncherService;

public class PlatformClusterCentre {
	
	private PlatformClusterTypeListener clusterListener = null;
	private MasterPlatformStatusListener statusListener = null;
	private NMServiceCentre pool = null;
	private HeartbeatListenerMgr heartMgr = null;
	private ClusterSetVirtualIPAddress change = null;
	private PlatformClusterInitPrepare clusterPre = null;
	private Map<String, Integer> levelMap = null;
	
	public void init() {
		pool = new NMServiceCentre();
		ConfigLoader load = new ConfigLoader();
		load.loadConfig();
		levelMap = load.loadBussinessConfig();
		FrameContext.getInstance();
		change = new ClusterSetVirtualIPAddress();
		clusterPre = new PlatformClusterInitPrepare();
	}

	public void start() {
		clusterPre.init();
		updateVirtualIPAddress();		
		
		clusterListener = new PlatformClusterTypeListener(this);
		clusterListener.setServiceName("平台集群类型监听");
		clusterListener.init();
		pool.addService(clusterListener);
		
		heartMgr = new HeartbeatListenerMgr();
		heartMgr.openHeartbearListener();
		
		ClusterType type = ClusterType.valueOf(ConfigVariable.PLATFORM_CLUSTER_TYPE);
		if (type.equals(ClusterType.Slave)) {
			statusListener = new MasterPlatformStatusListener();
			statusListener.setServiceName("平台状态监听");
			statusListener.init();
			pool.addService(statusListener);
		}
		
		if (type.equals(ClusterType.Master)) {
			startBussinessServices();
		}
	}
	
	protected void changeAndStartPlatform() {
		// 平台 Slave=〉Master
		// 1.关闭Slave心跳线程；
		// 2.切换虚拟IP地址；
		// 3.启动Master心跳服务端；
		// 4.启动当前平台的业务服务
		heartMgr.closeHeartbeatListener();
		clusterPre.init();
		updateVirtualIPAddress();
		heartMgr.openHeartbearListener();
		startBussinessServices();
	}
	
	protected void updateVirtualIPAddress() {
		//启动根据数据库中集群类型确定，如果为Master需要切换虚拟IP地址，如果是Slave需要切换为本地访问的IP地址
		ClusterType type = ClusterType.valueOf(ConfigVariable.PLATFORM_CLUSTER_TYPE);
		switch(type) {
		case Master:
			//如果为Master需要切换虚拟IP地址;
			change.platformSetIPAddress(ConfigVariable.CLUSTER_VIRTUAL_INFO);
			break;
		case Slave:
			//如果是Slave需要切换为本地访问的IP地址;
			change.platformSetIPAddress(ConfigVariable.CLUSTER_LOCAL_INFO);
			break;
		}
	}
	
	private void startBussinessServices() {
		Log.info("开始启动业务服务");
		NPLauncherService service = null;
		service = (NPLauncherService) BaseServicePool.getInstance().findService(NPLauncherService.class.getName());
		while(true) {
			if (service == null) {
				Log.warn("集群获取Laucher服务失败，需要重新获取Laucher服务");
				continue;
			}
			break;
		}
		
		try {
			service.startServices(levelMap);
		} catch (Exception e) {
			Log.error("集群启动业务服务出现异常", e);
		}
	}
	
	public void stop() {
		pool.stopAllThreads();
		heartMgr.closeHeartbeatListener();
	}
}
