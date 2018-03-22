package com.neusoft.np.arsf.core.transfer.vo.conf;



/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: Socket发送配置信息包装类<br>
* 创建日期: 2012-6-29 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-29下午1:35:12      马仲佳       创建
*    2	  2014-12-20	刘勃宏	重新构建结构
* </pre>
 */
public class SocketConfig implements ITransferAttrConfig {

	/**
	 * ip地址
	 */
	private String ip;
	
	/**
	 * 端口
	 */
	private int port;
	
	/**
	 * 连接类型，0:长连接/1:短连接
	 */
	private int connectType;
	
	/**
	 * socket类型，0:服务端/1:客户端
	 */
	private int socketType;
	
	/**
	 * 该配置的处理线程数，可以为空
	 */
	private int threadNum;
	
	/**
	 * 传输数据类型，可以为空，默认根据实际对象判断
	 */
	private TransferDataType dataType;
	
	@Override
	public boolean validate() {
		if(ip == null || ip.equals("")){
			return false;
		}
		if(port <=0){
			return false;
		}
		if(connectType != 0 && connectType != 1){
			return false;
		}
		if(socketType != 0 && socketType != 1){
			return false;
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
		builder.append("ip");
		builder.append("=");
		builder.append(this.getIp());
		builder.append(",");
		
		builder.append("port");
		builder.append("=");
		builder.append(this.getPort());
		builder.append(",");
		
		builder.append("connectType");
		builder.append("=");
		builder.append(this.getConnectType());
		builder.append(",");
		
		builder.append("socketType");
		builder.append("=");
		builder.append(this.getSocketType());
		
		return builder.toString();
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
	public int getConnectType() {
		return connectType;
	}
	public void setConnectType(int connectType) {
		this.connectType = connectType;
	}
	public int getSocketType() {
		return socketType;
	}
	public void setSocketType(int socketType) {
		this.socketType = socketType;
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
}
