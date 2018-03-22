package com.neusoft.np.arsf.core.transfer.service;

import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;


/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: 管道接口<br>
* 说明:该接口的所有子类实例支持并发访问，不需要客户端加锁<br>
* 创建日期: 2012-6-28 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-28下午12:54:03      马仲佳       创建
* </pre>
 */
public interface Channel {

	/**
	 * 获取管道唯一主题信息
	 * 
	 * @return
	 */
	public String getTopicName();
	
	/**
	 * 
	 * @return
	 */
	public TransferType getTransferType();
	
	/**
	 * 打开管道
	 */
	public void open();
	
	/**
	 * 关闭管道
	 * @throws InterruptedException 
	 */
	public void close();
	
//	/**
//	 * 向通道中发送数据
//	 */
//	public void send(Object obj);
//	
//	/**
//	 * 向通道中发送一组数据
//	 */
//	public void send(List<Object> dataList);
//	
//	/**
//	 * 从管道中提取一个数据对象
//	 * 
//	 * @return
//	 * @throws InterruptedException 
//	 */
//	public Object take() throws InterruptedException;
//	
//	/**
//	 * 从管道中添加一个数据对象
//	 * 
//	 * @throws InterruptedException
//	 */
//	public void put(Object object) throws InterruptedException;
	
}
