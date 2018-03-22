package com.neusoft.gbw.cp.store.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelPool {

	private static class ChannelPoolHolder{
		private static final ChannelPool INSTNCE = new ChannelPool();
	}
	private ChannelPool(){
		channelPoolMap = new ConcurrentHashMap<String, Channel>();
	}
	
	public static ChannelPool getInstance() {
		return ChannelPoolHolder.INSTNCE;
	} 
	
	private Map<String, Channel> channelPoolMap = null;
	
	
	public Channel getChannel(String key) {
		return channelPoolMap.get(key);
	}
	
	
	public void putChannel(String label,Channel channel) {
		channelPoolMap.put(label, channel);
	}
	
	public boolean containKey(String key) {
		if(channelPoolMap.containsKey(key)) {
			return true;
		}
		return false;
	}
	
	public void clear() {
		channelPoolMap.clear();
	}
}
