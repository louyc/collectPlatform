package com.neusoft.np.arsf.core.transfer.socket.send;

import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.client.NMSSocketClient;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatusType;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: socket主动短连接数据发送任务<br>
* 创建日期: 2012-6-29 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-29下午1:30:28      马仲佳       创建
* </pre>
 */
public class SocketClientShortSendTask extends NMService {

	/**
	 * Socket配置信息
	 */
	private final SocketConfig config;

	/**
	 * 所服务的Socket数据管道
	 */
	private final SocketChannel channel;

	/**
	 * Socket组件主动短连接套接字
	 */
	private NMSSocketClient clientShort;

	public SocketClientShortSendTask(SocketChannel channel) {
		this.channel = channel;
		this.config = (SocketConfig) channel.getTransferConfig().getConfig();
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					Log.info(TransferConstants.SEND_SERVICE_NAME + "任务" + serviceName + "所在线程因中断而退出。");
					return;
				}
				try {
					while (true) {
						//初始化
						init();
						if (Thread.currentThread().isInterrupted()) {
							Log.info(TransferConstants.SEND_SERVICE_NAME + "任务" + serviceName + "所在线程因中断而退出。");
							return;
						}
						// 通道必须在每次准备发送数据前可用，如不可用，继续重连
						if (!channel.getCurrentStatus().equals(TransferStatusType.STATUS_CONNECT)) {
							break;
						}
						Object object = channel.take();
						if(object == null)
							continue;
						
						boolean result = send(object);
						if (result) {
							break;
						}
					}
				} catch (InterruptedException e) {
					Log.warn(TransferConstants.SEND_SERVICE_NAME + "任务" + serviceName + "所在线程因中断而退出。");
					return;
				}
			}
		} finally {
			Log.info(TransferConstants.SEND_SERVICE_NAME + "任务:" + serviceName + "退出。");
		}
	}

	/**
	 *  数据发送
	 *  
	 * @param sendable
	 * @throws InterruptedException 
	 */
	private boolean send(Object object) throws InterruptedException {
		boolean sendResult = true;
		TransferDataType dataType = config.getDataType();
		try {
			switch(dataType) {
			case BYTE:
				clientShort.send((byte[])object);
				break;
			case STRING:
				clientShort.send((String)object);
				break;
			case OBJECT:
//				sender.send((Serializable)object);
				Log.info("Socket通信暂时不支持对象类型的传输");
				break;
			default:
				break;
			}
		} catch (NMSSocketException e) {
			Log.error("数据发送失败,准备重新连接,相关通信信息=" + config.toString(), e);
			sendResult = false;
		}
		
		return sendResult;
	}

	/**
	 * 初始化
	 * 
	 * @throws InterruptedException
	 */
	private void init() throws InterruptedException {
		Log.info(TransferConstants.SEND_SERVICE_NAME + "开始连接，连接属性为：" + config);
		long count = 0;
		while (true) {
			if (Thread.currentThread().isInterrupted()) {
				Log.info(TransferConstants.SEND_SERVICE_NAME + "连接时被中断。");
				Thread.currentThread().interrupt();
				return;
			}
			// 连接失败不断重连
			if (connect()) {
				// 更新管道状态为:可用状态
				updateChannelState(TransferStatusType.STATUS_CONNECT);
				Log.info(TransferConstants.SEND_SERVICE_NAME + "创建连接属性" + config + "成功。");
				break;
			} else {
				// 更新管道状态为:不可用状态
				updateChannelState(TransferStatusType.STATUS_DISCONNECT);
				Log.warn(TransferConstants.SEND_SERVICE_NAME + "根据连接属性:(" + config + ")建立" + (++count) + "次失败，准备重连");
				Thread.sleep(TransferConstants.RECONNECT_TIME);
			}
		}
	}

	/**
	 * 更新管道状态
	 * @param statusType
	 */
	private void updateChannelState(TransferStatusType statusType) {
		channel.updateTransferStatus(statusType);
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	private boolean connect() {
		this.clientShort = NMSSocketFactory.createSocketClient(config.getIp(), config.getPort());
		try {
			this.clientShort.connect();
		} catch (NMSSocketException e) {
			this.clientShort.disconnect();
			return false;
		}
		return true;
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
