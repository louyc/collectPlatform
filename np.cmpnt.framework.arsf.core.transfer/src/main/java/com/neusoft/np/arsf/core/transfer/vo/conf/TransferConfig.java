package com.neusoft.np.arsf.core.transfer.vo.conf;

public class TransferConfig {
	//主题名称
	private String topicName;
	//通信类型,发送(下行)、接收(上行)
	private TransferType type;
	//通信方式类型，jms、socket
	private TransferModelType modeType;
	//通信配置参数
	private ITransferAttrConfig config;
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public TransferType getType() {
		return type;
	}
	public void setType(TransferType type) {
		this.type = type;
	}
	public TransferModelType getModeType() {
		return modeType;
	}
	public void setModeType(TransferModelType modeType) {
		this.modeType = modeType;
	}
	public void setConfig(ITransferAttrConfig config) {
		this.config = config;
	}
	public ITransferAttrConfig getConfig() {
		return config;
	}
	public void addSocketConfig(SocketConfig config) {
		this.config = config;
	}
	public void addJMSConfig(JMSConfig config) {
		this.config = config;
		}
}
