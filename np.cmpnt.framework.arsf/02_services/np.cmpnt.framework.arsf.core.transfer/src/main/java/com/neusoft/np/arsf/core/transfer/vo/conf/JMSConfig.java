package com.neusoft.np.arsf.core.transfer.vo.conf;

import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;


/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: JMS配置信息类<br>
* 创建日期: 2012-6-29 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-29上午10:16:19      马仲佳       创建
* </pre>
 */
public class JMSConfig implements ITransferAttrConfig {
	
	/**
	 * url集群
	 */
	private String url;
	/**
	 * IP地址
	 */
	private String ip;
	
	/**
	 * 端口号
	 */
	private int port;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 	客户端唯一ID
	 */
	private String clientID;
	
	/**
	 * 会话名称
	 */
	private String sessionName;
	
	/**
	 * 主题名称
	 */
	private String topicName;
	
	/**
	 * 通道是否异步传输
	 */
	private boolean asyncDispatch;
	
	/**
	 * 是否持久化保存
	 */
	private boolean deliveryMode;
	
	/**
	 * jms类型，点对点&发布订阅
	 */
	private int jmsType;
	
	/**
	 * jms配置类型，集群1/单机0
	 */
	private int configType;
	
	/**
	 * 该配置的处理线程数
	 */
	private int threadNum;
	
	/**
	 * 传输数据类型，可以为空，默认根据实际对象判断
	 */
	private TransferDataType dataType;
	
	public int getJmsType() {
		return jmsType;
	}
	public void setJmsType(int jmsType) {
		this.jmsType = jmsType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public boolean isAsyncDispatch() {
		return asyncDispatch;
	}
	public void setAsyncDispatch(boolean asyncDispatch) {
		this.asyncDispatch = asyncDispatch;
	}
	public boolean isDeliveryMode() {
		return deliveryMode;
	}
	public void setDeliveryMode(boolean deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public TransferDataType getDataType() {
		return dataType;
	}
	public void setDataType(TransferDataType dataType) {
		this.dataType = dataType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getConfigType() {
		return configType;
	}
	public void setConfigType(int configType) {
		this.configType = configType;
	}
	@Override
	public boolean validate() {
		if(configType == 0 && (ip == null || ip.equals(""))){
			return false;
		}
		if(configType == 0 && port <=0){
			return false;
		}
		if(configType == 1 && (url == null || url.equals(""))){
			return false;
		}
		if(username == null || username.equals("")){
			return false;
		}
		if(password == null || password.equals("")){
			return false;
		}
		if(sessionName == null || sessionName.equals("")){
			return false;
		}
		if(jmsType == TransferConstants.JMS_TYPE_TOPIC){
			if(topicName == null || topicName.equals("")){
				return false;
			}
		}
		if(threadNum <=0){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("#");
		if(this.getConfigType() == 0) {
			builder.append("ip");
			builder.append("=");
			builder.append(this.getIp());
			builder.append(",");
			
			builder.append("port");
			builder.append("=");
			builder.append(this.getPort());
			builder.append(",");
		}
		
		if(this.getConfigType() == 1) {
			builder.append("url");
			builder.append("=");
			builder.append(this.getUrl());
			builder.append(",");
		}

		builder.append("username");
		builder.append("=");
		builder.append(this.getUsername());
		builder.append(",");
		
		builder.append("password");
		builder.append("=");
		builder.append(this.getPassword());
		builder.append(",");
		
		builder.append("sessionName");
		builder.append("=");
		builder.append(this.getSessionName());
		builder.append(",");
		
		builder.append("topicName");
		builder.append("=");
		builder.append(this.getTopicName());
		
		return builder.toString();
	}
}
