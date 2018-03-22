package com.neusoft.np.arsf.core.transfer.test.jms;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.transfer.service.ChannelPool;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.JMSConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferModelType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

public class JMSConfigBuilder  implements Runnable{

	public TransferConfig buildConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName("DATA_TOPIC");
		config.setType(TransferType.SEND);
		config.setModeType(TransferModelType.JMS);
		
		
		JMSConfig jmsConf = new JMSConfig();
		jmsConf.setIp("10.242.5.204");
		jmsConf.setPort(61616);
		jmsConf.setUsername("neusoft");
		jmsConf.setPassword("neusoft");
		jmsConf.setSessionName("mgr");
		jmsConf.setTopicName("msg2mgr");
		jmsConf.setAsyncDispatch(false);
		jmsConf.setDeliveryMode(false);
		jmsConf.setJmsType(1);
		jmsConf.setThreadNum(1);
		jmsConf.setDataType(TransferDataType.OBJECT);
		config.setConfig(jmsConf);
		
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
		return config;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			//创建配置
			TransferConfig config = buildConfig();
			Thread.sleep(5000);
			//上报发送主题数据
			for(int i=0;i<1;i++) {
				ChannelPool.getInstance().put(config.getTopicName(), "发送" + i + "次数据");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
