package com.neusoft.gbw.cp.collect.recording.monitor.mgr;

import java.util.Map;

import com.neusoft.gbw.cp.collect.recording.monitor.comstant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.monitor.comstant.MoniContants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.JMSConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferModelType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

public class ConfigLoader {

	public void loadConfig() {
		//获取配置文件，配置文件所在位置com.neusoft.np.arsf.core.config工程下的config包配置文件，自己新建配置文件，然后再fileListConfig文件中将配置文件写入
		Map<String, String> configMap = ARSFToolkit.getConfiguration().getAllBusinessProperty("gbw_monitor");
		try {
			NMBeanUtils.fillClassStaticFields(ConfigVariable.class, configMap);
		} catch (NMBeanUtilsException e) {
			Log.error("初始化配置文件错误", e);
		}
	}
	
	public void initRecordMonitorConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName(MoniContants.JMS_RECEIVE_RECORD_MONITOR_MSG_TOPIC);
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
		jmsConf.setSessionName("monitor");
		jmsConf.setTopicName("msg2monitor");
		jmsConf.setAsyncDispatch(false);
		jmsConf.setDeliveryMode(false);
		jmsConf.setJmsType(1);
		jmsConf.setThreadNum(1);
		jmsConf.setDataType(TransferDataType.STRING);
		config.setConfig(jmsConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
		Log.debug("站点重启通信接口初始化完成，name=" + MoniContants.JMS_RECEIVE_RECORD_MONITOR_MSG_TOPIC);
	}
}
