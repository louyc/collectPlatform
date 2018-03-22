package com.neusoft.np.arsf.core.transfer.vo.status;

import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;

public class TransferStatus {
	//发送配置对象
	private TransferConfig config;
	//通信状态
	private TransferStatusType status;
	
	public TransferConfig getConfig() {
		return config;
	}
	public void setConfig(TransferConfig config) {
		this.config = config;
	}
	public TransferStatusType getStatus() {
		return status;
	}
	public void setStatus(TransferStatusType status) {
		this.status = status;
	}
}
