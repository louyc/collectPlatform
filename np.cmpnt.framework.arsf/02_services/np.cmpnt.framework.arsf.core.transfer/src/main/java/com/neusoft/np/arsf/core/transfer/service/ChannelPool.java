package com.neusoft.np.arsf.core.transfer.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 数据发送服务<br>
* 功能描述: 管道池，该类线程安全<br>
* 说明:该类实例支持并发访问，不需要客户端加锁<br>
* 创建日期: 2012-6-26 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-26       马仲佳       创建
* </pre>
 */
public class ChannelPool {
	
	private static class ChannelPoollHolder {
		private static final ChannelPool INSTANCE = new ChannelPool();
	}
	
	private ChannelPool(){
		pool = new ConcurrentHashMap<String, Channel>();
		eventMap = new ConcurrentHashMap<String, BaseEventHandler>();
	}
	
	public static ChannelPool getInstance() {
		return ChannelPoollHolder.INSTANCE;
	}

	private Map<String, Channel> pool = null;
	private Map<String, BaseEventHandler> eventMap = null;
	
	public void addChannel(Channel channel) {
		String topic = channel.getTopicName();
		if (pool.containsKey(topic)) {
			Log.warn("存在相同主题的传输通道,覆盖原有的通道信息");
			pool.get(topic).close();
			pool.remove(topic);
		}
		pool.put(topic, channel);
	}
	
	public void put(String topic, Object obj) {
		try {
			AbstractChannel channel = (AbstractChannel)getChannel(topic);
			channel.put(obj);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		for(Channel channel : pool.values())
			channel.close();
		clear();
	}

	/**
	 * 根据指定的管道唯一ID获取管道对象
	 * 
	 * @param sid
	 * @return
	 */
	public Channel getChannel(String topic) {
		return pool.get(topic);
	}

	/**
	 * 获取当前所有通道对象
	 * 
	 * @return
	 */
	public Collection<Channel> getChannelList() {
		return pool.values();
	}
	
	public void clear() {
		pool.clear();
		eventMap.clear();
	}
}
