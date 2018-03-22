package com.neusoft.gbw.cp.core.collect;

public class SocketTransferAttr implements TransferAttr {

	private String ipAddress;			//Socket ip地址
	private int port;					//Socket port端口
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
