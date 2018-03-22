package com.neusoft.np.arsf.core.transfer.socket.recieve;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;

/**
 * 
 * 项目名称: 处理平台<br>
 * 模块名称: 处理平台<br>
 * 功能描述: Socket服务端接收数据线程执行类<br>
 * 创建日期: 2012-12-12 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-12       刘勃宏        创建 
 * </pre>
 */
public class SocketServerHander extends NMService {

	/**
	 * Socket配置信息
	 */
	private SocketConfig config = null;

	/**
	 * Socket服务端端接口句柄
	 */
	private SocketServer server = null;
	
	private SocketChannel channel = null;

	public SocketServerHander(SocketChannel channel) {
		this.config = (SocketConfig) channel.getTransferConfig().getConfig();
		this.channel = channel;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																										
	}

	@Override
	public void run() {
		//首先建立连接
		startListener();
		
		

	}

	/**
	 * 创建服务端的连接
	 */
	private void startListener() {
		int count = 0;
		while (true) {
			if (Thread.currentThread().isInterrupted()) {
				Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "连接时被中断。");
				return;
			}
			// 连接失败不断重连
			if (connect()) {
				Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "创建连接属性" + config + "成功。");
				break;
			} else {
				Log.warn(TransferConstants.REVCIEVE_SERVICE_NAME + "根据连接属性:(" + config + ")建立" + (++count) + "次失败，准备重连");
				sleep(TransferConstants.SLEEP_TIME);
			}
		}
	}

	/**
	 * 建立连接
	 * 
	 * @return
	 */
	private boolean connect() {
		server = new SocketServer(channel);
		try {
			server.listen();

		} catch (NMSocketException e) {
			try {
				server.unlisten();
			} catch (NMSocketException e1) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "断开连接时异常，连接属性:" + config, e1);
			}
			return false;
		}

		return true;
	}

	/**
	 * 线程休眠设置
	 * 
	 * @param millis
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Log.error("InterruptedException", e);
		}
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
