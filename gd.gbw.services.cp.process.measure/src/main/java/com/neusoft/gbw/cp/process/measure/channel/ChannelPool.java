package com.neusoft.gbw.cp.process.measure.channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.process.measure.channel.auto.AutoTaskChannel;
import com.neusoft.gbw.cp.process.measure.channel.manual.ManualTaskChannel;
import com.neusoft.gbw.cp.process.measure.channel.online.OnlineTaskChannel;

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
			case measure_unit_auto:
				channelPool.put(type, new AutoTaskChannel(type));
				break;
			case measure_unit_manual:
				channelPool.put(type, new AutoTaskChannel(type));
				break;
			case measure_online_auto:
				channelPool.put(type, new OnlineTaskChannel(type));
				break;
			case manual_set_recover:
				channelPool.put(type, new ManualTaskChannel(type));
				break;
			}
		}
	}
	
	public void open() {
		for(ChannelType type : ChannelType.values()) {
			if (null != channelPool.get(type)) {
				channelPool.get(type).init();
				channelPool.get(type).open();
				
			}
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
