package com.neusoft.np.arsf.core.transfer.service;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.transfer.event.SubscribeDataHandler;
import com.neusoft.np.arsf.core.transfer.jms.JMSChannel;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferModelType;


public class TransferConfigProcess {

	public void openChannel(TransferConfig sendConfig) {
		createChannel(sendConfig);
		addSubscribeDataHandler(sendConfig.getTopicName());
	}
	
	private void addSubscribeDataHandler(String topic) {
		SubscribeDataHandler handler = new SubscribeDataHandler();
		handler.setTopicName(topic);
		//订阅注册
		ARSFToolkit.addEventHandler(handler);
	}
	
	private void createChannel(TransferConfig transferConfig) {
		TransferModelType type = transferConfig.getModeType();
		Channel channel = null;
		switch(type) {
		case JMS:
			channel = new JMSChannel(transferConfig);
			channel.open();
			break;
		case SOCKET:
			channel = new SocketChannel(transferConfig);
			channel.open();
			break;
		}
		ChannelPool.getInstance().addChannel(channel);
	}
}
