package com.neusoft.np.arsf.core.transfer.comm;

import java.util.concurrent.BlockingQueue;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;

public abstract class NMTransferService extends NMService {

	private String topic;

	protected BlockingQueue<Object> queue;

	public NMTransferService(BlockingQueue<Object> queue) {
		this.queue = queue;
	}

	public abstract void send(Object result);

	@Override
	public void run() {
		Object result = null;
		while (true) {
			if (!isThreadRunning()) {
				Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "[数据接收服务]" + serviceName + "退出");
				break;
			}
			try {
				result = queue.take();
				// Log.info(Constants.SERVICE_NAME + "接收到数据：" + result, this.getClass(), 36);
				send(result);
			} catch (InterruptedException e) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "[数据接收服务]发送信息中断异常...", e);
				continue;
			}
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

	/**
	 * 停止当前线程。
	 */
	public void stopThreadRunning() {
		this.isThreadRunning = false;
		Thread.currentThread().interrupt();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
