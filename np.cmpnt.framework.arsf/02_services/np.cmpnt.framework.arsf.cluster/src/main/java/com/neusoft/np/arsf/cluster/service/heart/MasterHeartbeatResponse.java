package com.neusoft.np.arsf.cluster.service.heart;

import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ClusterConstants;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.common.util.NMService;

public class MasterHeartbeatResponse extends NMService {
	
	private NMSSocketServer server = null;
	private String key = null;

	public MasterHeartbeatResponse(String key, NMSSocketServer server) {
		this.key = key;
		this.server = server;
	}

	@Override
	public void run() {
		String str = null;
		//建立通信信道
		while(true) {
			try {
				str = server.receive(key);
				if (str == null)
					continue;
				
				if (str.trim().startsWith(ClusterConstants.CHECK_INFO)) {
					server.send(key, ClusterConstants.CONN_INFO);
					Log.info("集群心跳通过身份验证，建立连接=" + key);
					//发送集群服务端平台的名称
					server.send(key, ConfigVariable.PLATFORM_NAME);
					Log.info("平台集群服务端发送客户端所需的平台名称。Name=" + ConfigVariable.PLATFORM_NAME);
					break;
				} else {
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				Log.error("集群通信异常", e);
				break;
			}
		}
		
		//实时心跳验证
		while(true) {
			String result = FrameContext.getInstance().getPlatformStatus().name();
			try {
				server.send(key, result);
				Thread.sleep(1000);
			} catch (Exception e) {
				Log.error("集群通信异常", e);
				break;
			}
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
