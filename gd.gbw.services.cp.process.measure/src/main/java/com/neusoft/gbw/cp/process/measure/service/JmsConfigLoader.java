package com.neusoft.gbw.cp.process.measure.service;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.measure.constants.ConfigVariable;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.JMSConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferModelType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

public class JmsConfigLoader {
	
	public void initJMSSendConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName(ProcessConstants.JMS_SEND_MSG_TOPIC);
		config.setType(TransferType.SEND);
		config.setModeType(TransferModelType.JMS);
		JMSConfig jmsConf = new JMSConfig();
		//dp_send|1=sendType:JMS,ip:127.0.0.1,port:61616,username:patrol,password:patrol,sessionName:mgr,
		//topicName:msg2mgr,asyncDispatch:true,deliveryMode:false,jmsType:1,threadNum:1
		jmsConf.setConfigType(ConfigVariable.TRANSFER_APP_TYPE);//配置类型，集群：1  单机：0
		switch(ConfigVariable.TRANSFER_APP_TYPE) {
		case 1:
			jmsConf.setUrl(ConfigVariable.TRANSFER_APP);
			break;
		case 0:
			jmsConf.setIp(ConfigVariable.TRANSFER_APP);
		}
//		jmsConf.setUrl("failover:(tcp:/10.10.90.220:61616,tcp://10.10.90.221:61616)");
//		jmsConf.setUrl("failover:(tcp://10.242.5.152:61616,tcp://10.242.5.153:61616)");
//		jmsConf.setIp("10.242.5.170");
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
	}
	
	public void initEvaluationSendConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName(ProcessConstants.JMS_SEND_EVALUATION_MSG_TOPIC);
		config.setType(TransferType.SEND);
		config.setModeType(TransferModelType.JMS);
		JMSConfig jmsConf = new JMSConfig();
		//dp_send|1=sendType:JMS,ip:127.0.0.1,port:61616,username:patrol,password:patrol,sessionName:mgr,
		//topicName:msg2mgr,asyncDispatch:true,deliveryMode:false,jmsType:1,threadNum:1
		jmsConf.setConfigType(ConfigVariable.TRANSFER_APP_TYPE);//配置类型，集群：1  单机：0
		switch(ConfigVariable.TRANSFER_APP_TYPE) {
		case 1:
			jmsConf.setUrl(ConfigVariable.TRANSFER_APP);
			break;
		case 0:
			jmsConf.setIp(ConfigVariable.TRANSFER_APP);
		}
//		jmsConf.setUrl("failover:(tcp:/10.10.90.220:61616,tcp://10.10.90.221:61616)");
//		jmsConf.setUrl("failover:(tcp://10.242.5.152:61616,tcp://10.242.5.153:61616)");
//		jmsConf.setIp("10.242.5.170");
		jmsConf.setPort(61616);
		jmsConf.setUsername("neusoft");
		jmsConf.setPassword("neusoft");
		jmsConf.setSessionName("eval");
		jmsConf.setTopicName("msg2eval");
		jmsConf.setAsyncDispatch(false);
		jmsConf.setDeliveryMode(false);
		jmsConf.setJmsType(1);
		jmsConf.setThreadNum(1);
		jmsConf.setDataType(TransferDataType.OBJECT);
		config.setConfig(jmsConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
		
	}
	
	public void initEvaluationRecieveConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName(ProcessConstants.JMS_RECEIVE_EVALUATION_MSG_TOPIC);
		config.setType(TransferType.RECEIVE);
		config.setModeType(TransferModelType.JMS);
		JMSConfig jmsConf = new JMSConfig();
		//dp_send|1=sendType:JMS,ip:127.0.0.1,port:61616,username:patrol,password:patrol,sessionName:mgr,
		//topicName:msg2mgr,asyncDispatch:true,deliveryMode:false,jmsType:1,threadNum:1
		jmsConf.setConfigType(ConfigVariable.TRANSFER_APP_TYPE);//配置类型，集群：1  单机：0
		switch(ConfigVariable.TRANSFER_APP_TYPE) {
		case 1:
			jmsConf.setUrl(ConfigVariable.TRANSFER_APP);
			break;
		case 0:
			jmsConf.setIp(ConfigVariable.TRANSFER_APP);
		}
//		jmsConf.setUrl("failover:(tcp:/10.10.90.220:61616,tcp://10.10.90.221:61616)");
//		jmsConf.setUrl("failover:(tcp://10.242.5.152:61616,tcp://10.242.5.153:61616)");
//		jmsConf.setIp("10.242.5.170");
		jmsConf.setPort(61616);
		jmsConf.setUsername("neusoft");
		jmsConf.setPassword("neusoft");
		jmsConf.setSessionName("gbw");
		jmsConf.setTopicName("msg2gbw");
		jmsConf.setAsyncDispatch(false);
		jmsConf.setDeliveryMode(false);
		jmsConf.setJmsType(1);
		jmsConf.setThreadNum(1);
		jmsConf.setDataType(TransferDataType.OBJECT);
		config.setConfig(jmsConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
	}
	
	public void initRecordMonitorConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName(ProcessConstants.JMS_SEND_RECORD_MONITOR_MSG_TOPIC);
		config.setType(TransferType.SEND);
		config.setModeType(TransferModelType.JMS);
		JMSConfig jmsConf = new JMSConfig();
		//dp_send|1=sendType:JMS,ip:127.0.0.1,port:61616,username:patrol,password:patrol,sessionName:mgr,
		//topicName:msg2mgr,asyncDispatch:true,deliveryMode:false,jmsType:1,threadNum:1
		jmsConf.setConfigType(ConfigVariable.TRANSFER_APP_TYPE);//配置类型，集群：1  单机：0
		switch(ConfigVariable.TRANSFER_APP_TYPE) {
		case 1:
			jmsConf.setUrl(ConfigVariable.TRANSFER_APP);
			break;
		case 0:
			jmsConf.setIp(ConfigVariable.TRANSFER_APP);
		}
//		jmsConf.setUrl("failover:(tcp:/10.10.90.220:61616,tcp://10.10.90.221:61616)");
//		jmsConf.setUrl("failover:(tcp://10.242.5.152:61616,tcp://10.242.5.153:61616)");
//		jmsConf.setIp("10.242.5.170");
		jmsConf.setPort(61616);
		jmsConf.setUsername("neusoft");
		jmsConf.setPassword("neusoft");
		jmsConf.setSessionName("monitor");
		jmsConf.setTopicName("msg2monitor");
		jmsConf.setAsyncDispatch(false);
		jmsConf.setDeliveryMode(false);
		jmsConf.setJmsType(1);
		jmsConf.setThreadNum(1);
		jmsConf.setDataType(TransferDataType.STRING);
		config.setConfig(jmsConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
	}
	
	public void initSocketSendConfig(){
		TransferConfig config = new TransferConfig();
		config.setTopicName(EventServiceTopic.SOCKET_SERVICE_TOPIC);
		config.setType(TransferType.SEND);
		config.setModeType(TransferModelType.SOCKET);
		
		SocketConfig socketConf = new SocketConfig();
//		socketConf.setIp("10.131.2.113");
		socketConf.setPort(40005);
		/**
		 * 连接类型，0:长连接/1:短连接
		 */
		socketConf.setConnectType(0);
		socketConf.setThreadNum(1);
		/**
		 * socket类型，0:服务端/1:客户端
		 */
		socketConf.setSocketType(0);
		socketConf.setDataType(TransferDataType.BYTE);
		config.addSocketConfig(socketConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
		
	}
}
