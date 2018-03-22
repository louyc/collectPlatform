package com.neusoft.np.arsf.core.transfer.jms;

import com.neusoft.nms.common.net.jms.NMConnection;
import com.neusoft.nms.common.net.jms.NMConnectionFactory;
import com.neusoft.nms.common.net.jms.NMQueueSession;
import com.neusoft.nms.common.net.jms.NMReceiver;
import com.neusoft.nms.common.net.jms.NMSender;
import com.neusoft.nms.common.net.jms.NMTopicSession;
import com.neusoft.nms.common.net.jms.exception.NMMQException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.JMSConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatusType;

public abstract class AbstractJMSService extends NMService{

	/**
	 * JMS配置信息
	 */
	private JMSConfig config = null;

	/**
	 * jms组件Conncetion对象
	 */
	private NMConnection connection;

	/**
	 * jms组件点对点session对象
	 */
	private NMQueueSession qsession;

	/**
	 * jms组件发布订阅session对象
	 */
	private NMTopicSession tsession;

	/**
	 * jms组件接收对象
	 */
	private NMReceiver receiver;
	
	/**
	 * jms组件发送对象
	 */
	private NMSender sender;
	
	public NMReceiver getReceiver() {
		return receiver;
	}

	public NMSender getSender() {
		return sender;
	}

	private JMSChannel channel;

	public AbstractJMSService(JMSChannel channel) {
		this.config = (JMSConfig) channel.getTransferConfig().getConfig();
		this.channel = channel;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "任务" + serviceName + "所在线程因中断而退出。");
					return;
				}
				try {
					// 首先建立连接
					createConnect();
					while (true) {
						if (Thread.currentThread().isInterrupted()) {
							Log.info(TransferConstants.REVCIEVE_SERVICE_NAME  + "[数据接收服务]任务" + serviceName + "所在线程因中断而退出。");
							return;
						}
						// 通道必须在每次准备通讯数据前可用，如不可用，继续重连
						if (!channel.getCurrentStatus().equals(TransferStatusType.STATUS_CONNECT)) {
							break;
						}
						
						if (!transfer(this)) {
							// 进行重现连接
							break;
						}
					}
				} catch (InterruptedException e) {
					Log.warn(TransferConstants.REVCIEVE_SERVICE_NAME  + "任务" + serviceName + "所在线程因中断而退出。");
					return;
				} catch (Exception e) {
					Log.error("", e);
				} catch (Error e) {
					Log.error("", e);
				}finally {
					disconnect();
				}
			}
		} finally {
			Log.info(TransferConstants.REVCIEVE_SERVICE_NAME  + "任务:" + serviceName + "退出。");
		}
	}

	protected abstract boolean transfer(AbstractJMSService abjms) throws InterruptedException;

	/**
	 * 创建连接
	 * 
	 * @throws InterruptedException
	 */
	private void createConnect() throws InterruptedException {
		Log.info(TransferConstants.REVCIEVE_SERVICE_NAME  + "开始连接，连接属性为：" + config);
		long count = 0;
		while (true) {
			if (Thread.currentThread().isInterrupted()) {
				Log.info(TransferConstants.REVCIEVE_SERVICE_NAME  + "连接时被中断。");
				Thread.currentThread().interrupt();
				return;
			}
			// 连接失败不断重连
			if (connect()) {
				// 更新管道状态为:可用状态
				updateChannelState(TransferStatusType.STATUS_CONNECT);
				break;
			} else {
				// 更新管道状态为:可用状态
				updateChannelState(TransferStatusType.STATUS_CONNECT);
				Log.warn(TransferConstants.REVCIEVE_SERVICE_NAME  + "根据连接属性:(" + config + ")建立" + (++count) + "次失败，准备重连");
				sleep(TransferConstants.RECONNECT_TIME);
			}
		}
	}
	
	/**
	 * 更新管道状态
	 * @param stateUse
	 */
	private void updateChannelState(TransferStatusType status) {
		channel.updateTransferStatus(status);
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	private boolean connect() {
		int configType = config.getConfigType();
		String username = config.getUsername();
		String password = config.getPassword();
		String url = config.getUrl();
		String ip = config.getIp();
		int port = config.getPort();
		String clientID = serviceName + System.currentTimeMillis();
		try {
			if(configType == 0)
				this.connection = NMConnectionFactory.createConnection(username, password, ip, port, clientID);
			else
				this.connection = NMConnectionFactory.createConnection(username, password, url, clientID);
			
			if (config.getJmsType() == TransferConstants.JMS_TYPE_P2P) {
				this.qsession = connection.createQueueSession(config.getSessionName());
				this.qsession.setAsyncDispatch(this.config.isAsyncDispatch());

				switch(channel.getTransferType()) {
				case RECEIVE:
					this.receiver = qsession.createReceiver();
					break;
				case SEND:
					this.sender = qsession.createSender();
					this.sender.setDeliveryMode(this.config.isDeliveryMode());
					//无超时时间
					this.sender.setTimeToLive(0L);
					break;
				}
			} else {
				this.tsession = connection.createTopicSession(config.getSessionName());
				this.tsession.setAsyncDispatch(this.config.isAsyncDispatch());
				switch(channel.getTransferType()) {
				case RECEIVE:
					this.receiver = tsession.createReceiver(config.getTopicName());
					break;
				case SEND:
					this.sender = tsession.createSender(config.getTopicName());
					this.sender.setDeliveryMode(this.config.isDeliveryMode());
					//无超时时间
					this.sender.setTimeToLive(0L);
					break;
				}
			}
			return true;
		} catch (NMMQException e) {
			System.out.println(e.getMessage());
			Log.warn(TransferConstants.REVCIEVE_SERVICE_NAME  + "连接失败，连接属性:" + config);
			disconnect();
			return false;
		}
	}

	/**
	 * 断开连接
	 */
	private void disconnect() {
		try {
			if (this.connection != null) {
				connection.disconnect();
				connection = null;
			}
			qsession = null;
			tsession = null;
			receiver = null;
			sender = null;
		} catch (Exception e) {
			Log.error(TransferConstants.REVCIEVE_SERVICE_NAME  + "断开连接时异常，连接属性:" + config, e);
		}finally {
			// 更新管道状态为:可用状态
			updateChannelState(TransferStatusType.STATUS_DISCONNECT);
		}
	}
	
	public JMSChannel getChannel() {
		return channel;
	}

	protected TransferDataType getDataType() {
		return config.getDataType();
	}

	/**
	 * 设置休眠时间
	 * 
	 * @param millis
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
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
