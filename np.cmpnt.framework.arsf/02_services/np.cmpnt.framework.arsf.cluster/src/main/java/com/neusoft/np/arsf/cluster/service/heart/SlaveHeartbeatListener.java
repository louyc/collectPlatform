package com.neusoft.np.arsf.cluster.service.heart;

import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.client.NMSSocketClient;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ClusterConstants;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.cluster.vo.PlatformStatus;
import com.neusoft.np.arsf.common.util.NMService;

public class SlaveHeartbeatListener extends NMService {
	
	private NMSSocketClient client = null;
	private int count = 1;
	
	@Override
	public void run() {
		try {
			while(isThreadRunning()) {
				try {
					connect();
				} catch (NMSSocketException e) {
					Log.error("第" + (count++) + "次重新连接失败", e);
					sleep(1000);
					continue;
				}
				
				String result = null;
				try {
					client.send(ClusterConstants.CHECK_INFO);
					while(true) {
						result = client.receive();
						if (result.trim().startsWith(ClusterConstants.CONN_INFO)) {
							Log.info("平台客户端心跳连接接收到服务端的连接验证信息。" + ClusterConstants.CONN_INFO + ",通过验证可以进行心跳通信。");
							//首先发送本平台的名称
							result = client.receive();
							Log.info("平台集群客户端接收服务端平台的名称。Name=" + result);
							ConfigVariable.OTHER_PLATFORM_NAME = result;
							break;
						}
					}
				} catch (NMSSocketException e) {
					Log.error("[Slave]通信异常", e);
					client.disconnect();
					continue;
				}
				
				while(isThreadRunning()) {
					try {
						result = client.receive();
						PlatformStatus status = PlatformStatus.valueOf(result);
						FrameContext.getInstance().changePlatformStatus(status);
					} catch (NMSSocketException e) {
						//平台异常,被认定通信异常Master已经Dead状态
						FrameContext.getInstance().changePlatformStatus(PlatformStatus.DEAD);
						Log.error("集群服务端通信连接出现异常，无法继续通信，设置集群Master端平台无法正常运行，客户端心态线程停止", e);
						break;
					}
				}
			}
		} finally {
			Log.info(this.serviceName + "已经关闭");
		}
	}
	
	private void connect() throws NMSSocketException {
		String ip = ConfigVariable.CLUSTER_VIRTUAL_INFO.split(",")[0];
		int port = ConfigVariable.MASTER_TRANSFER_PORT;
		client = NMSSocketFactory.createSocketClient(ip, port);
		client.connect();
		Log.info("平台客户端心跳连接建立，IP=" + ip + ",PORT=" + port);
	}
	
	private void sleep(long million) {
		try {
			Thread.sleep(million);
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
