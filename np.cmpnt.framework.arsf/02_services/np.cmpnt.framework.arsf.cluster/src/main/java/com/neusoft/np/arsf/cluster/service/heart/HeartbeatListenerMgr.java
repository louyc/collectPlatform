package com.neusoft.np.arsf.cluster.service.heart;

import java.net.Socket;

import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.cluster.vo.ClusterType;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class HeartbeatListenerMgr {
	
	private final int DEFUALT_PORT = 50000;
	private NMSSocketServer server = null;
	private NMServiceCentre pool = new NMServiceCentre();
	private SlaveHeartbeatListener listener = null;

	public void openHeartbearListener() {
		if (ConfigVariable.MASTER_TRANSFER_PORT == 0) {
			ConfigVariable.MASTER_TRANSFER_PORT = DEFUALT_PORT;
			Log.warn("配置文件中没有赋值监听服务端口，应用默认端口。PORT=" + DEFUALT_PORT);
		}
		
		ClusterType type = ClusterType.valueOf(ConfigVariable.PLATFORM_CLUSTER_TYPE);
		switch(type) {
		case Master:
			startMasterListener();
			break;
		case Slave:
			startSlaveListener();
			break;
		}
	}
	
	private void startMasterListener() {
		server = NMSSocketFactory.createSocketServer(ConfigVariable.MASTER_TRANSFER_PORT, new MasterClientDispose(this));
		try {
			server.listen();
		} catch (NMSSocketException e) {
			Log.error("平台集群心跳端口启动失败", e);
			return;
		}
		String ip = ConfigVariable.CLUSTER_VIRTUAL_INFO.split(",")[0].trim();
		Log.info("平台集群心跳服务，IP=" + ip + "， PORT=" + ConfigVariable.MASTER_TRANSFER_PORT + "，启动完成");
	}
	
	protected void startMasterResponse(Socket socket) {
		String socketKey = getSocketKey(socket);
		Log.info("平台集群心跳接收到客户端的连接，需要进行身份验证。通信信息[" + socketKey + "]");
		MasterHeartbeatResponse response = new MasterHeartbeatResponse(socketKey, server);
		response.setServiceName("平台集群心跳服务端应答线程#" + socketKey);
		pool.addService(response);
	}
	
	private void startSlaveListener() {
		listener = new SlaveHeartbeatListener();
		listener.setServiceName("平台集群心跳客户端线程");
		pool.addService(listener);
		Log.info("平台集群心跳客户端已经启动");
	}
	
	private String getSocketKey(Socket socket) {
	    return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}
	
	public void closeHeartbeatListener() {
		if (server != null)
			try {
				server.unlisten();
			} catch (NMSSocketException e) {
				e.printStackTrace();
			}
		pool.stopAllThreads();
	}
	
//	public void stopSlaveListener() {
//		pool.removeServiceByName(listener.getServiceName());
//	}
}
