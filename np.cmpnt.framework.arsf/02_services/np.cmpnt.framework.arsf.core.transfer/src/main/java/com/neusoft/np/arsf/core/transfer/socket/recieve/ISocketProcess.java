package com.neusoft.np.arsf.core.transfer.socket.recieve;

import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;



/**
 * 
 * 项目名称: LM采集处理平台组件化<br>
 * 模块名称: <br>
 * 功能描述: <br>
 * 创建日期: 2014-4-22 <br>
 * 版权信息: Copyright (c) 2014<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liu.bh@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2014-4-22       刘勃宏        创建
 * </pre>
 */
public interface ISocketProcess {

	/**
	 * 断开socket连接
	 */
	void disconnect();
	
	/**
	 * 发送数据，逐行字符串数据发送
	 * 
	 * @param data 待发送数据（字符串格式）
	 * @throws NMSSocketException 发送数据失败时抛出该异常，此方法封装了资源的释放
	 */
	public void send(String data) throws NMSocketException;
	
	/**
	 * 发送数据
	 * 
	 * @param data 待发送数据（字节数组格式）
	 * @throws NMSSocketException 发送数据失败时抛出该异常，此方法封装了资源的释放
	 */
	public void send(byte[] data) throws NMSocketException;

	/**
	 * 接收字符串数据，逐行字符串数据接收
	 * 此接收方法为阻塞接收方法
	 * 
	 * @return 接收的字符串
	 * @throws LMSocketException 
	 * @throws NMSSocketException 发送数据失败时抛出该异常，此方法封装了资源的释放
	 */
	//public String receiveStr() throws NMSocketException;
	
	/**
	 * 接收字节数组数据
	 * 此接收方法为非阻塞接收方法
	 * 
	 * @return 接收的字节数组
	 * @throws NMSSocketException 发送数据失败时抛出该异常，此方法封装了资源的释放
	 */
	//public byte[] receive() throws NMSocketException;
	
}
