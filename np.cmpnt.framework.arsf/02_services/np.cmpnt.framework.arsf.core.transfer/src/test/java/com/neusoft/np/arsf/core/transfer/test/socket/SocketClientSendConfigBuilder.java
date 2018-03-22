package com.neusoft.np.arsf.core.transfer.test.socket;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.transfer.service.ChannelPool;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferModelType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

public class SocketClientSendConfigBuilder  implements Runnable{

	public TransferConfig buildConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName("SOCKET_CLIENT_RECEIVE_TOPIC");
		config.setType(TransferType.SEND);
		config.setModeType(TransferModelType.SOCKET);
		
		SocketConfig socketConf = new SocketConfig();
		socketConf.setIp("127.0.0.1");
		socketConf.setPort(450);
		/**
		 * 连接类型，0:长连接/1:短连接
		 */
		socketConf.setConnectType(0);
		socketConf.setThreadNum(1);
		/**
		 * socket类型，0:服务端/1:客户端
		 */
		socketConf.setSocketType(1);
		socketConf.setDataType(TransferDataType.STRING);
		config.addSocketConfig(socketConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
		return config;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			//创建配置
			TransferConfig config = buildConfig();
		
			//上报发送主题数据
			for(int i=1;i<=10;i++) {
				Thread.sleep(5000);
				ChannelPool.getInstance().put(config.getTopicName(), "发送【测试数据" + i + "次】");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
