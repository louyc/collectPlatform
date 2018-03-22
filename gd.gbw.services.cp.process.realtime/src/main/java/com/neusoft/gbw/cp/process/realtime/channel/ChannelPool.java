package com.neusoft.gbw.cp.process.realtime.channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.process.realtime.channel.realtime.RealTimeTaskChannel;
import com.neusoft.gbw.cp.process.realtime.channel.report.ReportTaskChannel;

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
			//实时
			case realtime:
				channelPool.put(type, new RealTimeTaskChannel(type));
				break;
				//自动
			case auto:
//				channelPool.put(type, new AutoTaskChannel(type));
				break;
				//手动
			case manual:
//				channelPool.put(type, new ManualTaskChannel(type));
				break;
				//报告
			case report:
				channelPool.put(type, new ReportTaskChannel(type));
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
