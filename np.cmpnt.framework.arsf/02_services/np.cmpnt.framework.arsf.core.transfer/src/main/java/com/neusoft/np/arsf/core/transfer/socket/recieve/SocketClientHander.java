package com.neusoft.np.arsf.core.transfer.socket.recieve;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;

public class SocketClientHander extends NMService {

	/**
	 * Socket配置信息
	 */
	private SocketConfig config = null;
	
	private SocketChannel channel = null;
	
	private SocketClient client = null;

	public SocketClientHander(SocketChannel channel) {
		this.config = (SocketConfig) channel.getTransferConfig().getConfig();
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
							Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "任务" + serviceName + "所在线程因中断而退出。");
							return;
						}

						if (!receive()) {
							//进行重现连接
							break;
						}
					}
				} finally {
					close();
				}
			}
		} finally {
			Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "任务:" + serviceName + "退出。");
		}
	}

	/**
	 * 建立Socket客户端的连接
	 */
	private void createConnect() {
		String ip = config.getIp();
		client = new SocketClient(config);
		int nloopcount = 0;
		while (true) {
			try {
				client.connect();
				break;
			} catch (NMSocketException e) {
				Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "设备：" + ip + "进行重连 " + nloopcount + "次");
			}
			nloopcount++;
			sleep(TransferConstants.RECONNECT_SLEEP_TIME);
		}
	}

	/**
	 * 数据接收
	 * 
	 * @return
	 * @throws NMFormateException
	 * @throws NMSocketException
	 */
	private boolean receive() {
		TransferDataType dataType = config.getDataType();
		Object data = null;
		try {
			switch(dataType) {
			case BYTE:
				data = client.receive();
				break;
			case STRING:
				data = client.receiveStr();
				break;
			case OBJECT:
//				data = receiver.receiveObj();
				break;
			default:
				break;
			}
			Log.info("接收数据： " + data);
			channel.put(data);
		
		} catch (InterruptedException e) {
			Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "出现中断异常", e);
			return false;
		} catch (NMSocketException e) {
			Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "出现Socket异常", e);
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
		}
	}

	/**
	 * 关闭Socket
	 */
	private void close() {
		client.disconnect();
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
