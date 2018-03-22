package com.neusoft.np.arsf.core.transfer.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NMServiceCentre;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatusType;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: 管道抽象类<br>
* 创建日期: 2012-6-28 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-28下午12:41:50      马仲佳       创建
* </pre>
 */
public abstract class AbstractChannel implements Channel {

	/**
	 * 管道内主题
	 */
	protected TransferConfig tConfig;

	/**
	 * 管道内队列
	 */
	private BlockingQueue<Object> queue = null;

	/**
	 * 通道内部线程池
	 */
	private NMServiceCentre servicePool = null;
	
	/**
	 * 通道内通信状态监控对象
	 */
	private TransferMonitorMgr tMonitorMgr = null;
	
	public AbstractChannel(TransferConfig config) {
		this.tConfig = config;
	}
	/**
	 * 初始化
	 */
	private void init() {
		String topicName = tConfig.getTopicName();
		queue = ARSFToolkit.getBlockingQueue(topicName+"_queue", TransferConstants.DEFAULT_QUEUE_SIZE);
		tMonitorMgr = new TransferMonitorMgr(tConfig);
		servicePool = new NMServiceCentre();
	}

	public Object take() throws InterruptedException {
		return queue.take();
	}

	public void put(Object object) throws InterruptedException {
		queue.put(object);
	}

	@Override
	public synchronized void open() {
		//初始化
		init();
		tMonitorMgr.open();
		//启动管道内数据发送任务
		startAllTask();
	}

	@Override
	public synchronized void close() {
		tMonitorMgr.close();
		//停止管道内数据发送任务
		stopAllTask();
		//清理
		clear();
	}

	public TransferStatusType getCurrentStatus() {
		return tMonitorMgr.getCurrentStatus();
	}
	
	public void updateTransferStatus(TransferStatusType statusType) {
		tMonitorMgr.updateTransferStatus(statusType);
	}

	/**
	 * 启动管道内发送任务
	 */
	private void startAllTask() {
		// 下面开辟管道内线程
		int threadNum = tConfig.getConfig().getThreadNum();
		for (int i = 0; i < threadNum; i++) {
			String taskName = getTaskName(i);
			NMService task = createTask();
			task.setServiceName(taskName);
			servicePool.addService(task);
		}
	}
	
	private String getTaskName(int i) {
		return getTopicName() + TransferConstants.TASK_SEPARATOR + getTransferType() + i;
	}

	/**
	 * 清理
	 */
	private void clear() {
		this.queue.clear();
	}

	/**
	 * 停止通道内任务
	 */
	private void stopAllTask() {
		servicePool.stopAllThreads();
	}

	@Override
	public String getTopicName() {
		return tConfig.getTopicName();
	}
	
	@Override
	public TransferType getTransferType() {
		return tConfig.getType();
	}
	
	public TransferConfig getTransferConfig() {
		return tConfig;
	}
	/**
	 * 创建通信任务
	 * @return
	 */
	protected abstract NMService createTask();
}
