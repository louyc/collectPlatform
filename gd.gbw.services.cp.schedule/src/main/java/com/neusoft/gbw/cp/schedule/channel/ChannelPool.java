package com.neusoft.gbw.cp.schedule.channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 管道池
 * @author yanghao
 *
 */
public class ChannelPool {
	
	private static class ChannelPoolHolder {
		private static final ChannelPool INSTANCE = new ChannelPool();
	}
	
	private ChannelPool(){
		channelPool = new HashMap<ChannelType, Channel>();
		init();
	}
	
	public static ChannelPool getInstance() {
		return ChannelPoolHolder.INSTANCE;
	}

	private Map<ChannelType, Channel> channelPool = null;
	
	public void init() {
		for(ChannelType type : ChannelType.values()) {
			switch(type) {
			//计划任务
			case plan:
				channelPool.put(type, new PlanTaskChannel(type));
				break;
				//周期任务
			case period:
				channelPool.put(type, new PeriodTaskChannel(type));
				break;
				//实时任务
			case realtime:
				channelPool.put(type, new RealTimelTaskChannel(type));
				break;
			}
		}
	}
	
	
	public void open() {
		for(ChannelType type : ChannelType.values()) {
			channelPool.get(type).init();
			channelPool.get(type).open();
		}
	}
	
	
	public void close() {
		for(ChannelType type : ChannelType.values()) {
			channelPool.get(type).close();
		}
	}
	
	public Channel getChannel(ChannelType type) {
		return channelPool.get(type);
	}
	
	public Collection<Channel> getChannelValues() {
		return channelPool.values();
	}
}
