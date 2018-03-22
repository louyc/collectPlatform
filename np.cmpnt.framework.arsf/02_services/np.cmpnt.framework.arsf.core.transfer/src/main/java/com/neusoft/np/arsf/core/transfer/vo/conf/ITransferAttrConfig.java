package com.neusoft.np.arsf.core.transfer.vo.conf;

public interface ITransferAttrConfig {
	
	String getIp();

	int getPort();

	/**
	 * 获取配置线程数
	 * 
	 * @return
	 */
	public int getThreadNum();

//	/**
//	 * 获取接收类型
//	 * 
//	 * @return
//	 */
//	public String getReceiveType();

	/**
	 * 验证配置信息
	 * 
	 * @return
	 */
	public boolean validate();
}
