package com.neusoft.np.arsf.base.bundle.util;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;

import javax.management.ObjectName;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseContextImpl;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;
import com.neusoft.np.arsf.base.bundle.event.NPEventUtil;
import com.neusoft.np.arsf.common.util.NPMessage;

public class NPBlockingQueue<T> extends ArrayBlockingQueue<T> implements NPBlockingQueueMBean {

	private static final long serialVersionUID = -1598844077568990336L;

	private static final int DEFAULT_RATE = 95;

	private static final String RATE = "QUEUE_THRESHOLD_RATE";

	private String queueName;

	private int capacity;

	private int threshold;

	private int rate;

	private volatile boolean isAlarmSend = false;

	public NPBlockingQueue(String name, int capacity) {
		super(capacity);
		this.capacity = capacity;
		this.queueName = name;
		this.rate = DEFAULT_RATE;
		String rateStr = ARSFToolkit.getConfigurationFrameProperty(RATE);
		if (rateStr != null && !"".equals(rateStr)) {
			this.rate = Integer.valueOf(rateStr);
		} else {
			Log.info("队列：" + queueName + " 初始化，容量:" + capacity + "，阈值比例:" + this.rate);
		}
		BigDecimal t = new BigDecimal(capacity * this.rate / 100);
		this.threshold = t.intValue();
		// Log.info("队列：" + name + " 初始化，容量:" + capacity + "，阈值限定:" + this.threshold);
	}

	@Override
	public void put(T e) throws InterruptedException {
		processThreshold();
		super.put(e);
	}

	@Override
	public boolean add(T e) {
		processThreshold();
		return super.add(e);
	}

	private synchronized void processThreshold() {
		if (super.size() > threshold) {
			if (!isAlarmSend) {
				NPMessage message = new NPMessage();
				String date = getQueueFormate();
				message.put(NPBaseConstant.Util.CAPACITY, String.valueOf(capacity));
				message.put(NPBaseConstant.Util.DATE, date);
				message.put(NPBaseConstant.Util.NAME, queueName);
				message.put(NPBaseConstant.Util.RATE, rate + "%");
				message.put(NPBaseConstant.Util.BUNDLE_NAME, BaseContextImpl.getInstance().getBundleName());
				message.put(NPBaseConstant.Util.SIZE, String.valueOf(super.size()));
				Log.warn("队列异常：" + date);
				ARSFToolkit.sendEvent(NPBaseConstant.EventTopic.QUEUE_THRESHOLD_TOPIC, NPEventUtil.buildXml(message));
				isAlarmSend = true;
			}
		} else {
			//TODO 已经恢复，是否需要发送清除告警
			isAlarmSend = false;
		}
	}

	private String getQueueFormate() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("队列(" + queueName + ")目前队列大小为：" + super.size() + "，最大容量：" + capacity + "，已经超过阈值" + rate + "%");
		return buffer.toString();
	}

	public void destroyQueue() {
		try {
			String name = "com.neusoft.np.arsf.base.bundle.util:type=" + BaseContextImpl.getInstance().getBundleName()
					+ "|" + queueName;
			ObjectName objectName = new ObjectName(name);
			ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectName);
		} catch (Exception e) {
			Log.error("JMX监控开启错误。", e);
		}
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public boolean isAlarmSend() {
		return isAlarmSend;
	}

	public void setAlarmSend(boolean isAlarmSend) {
		this.isAlarmSend = isAlarmSend;
	}

	@Override
	public String getName() {
		return getQueueName();
	}

	@Override
	public int getCacheSize() {
		return super.size();
	}

}
