package com.neusoft.gbw.cp.collect.stream.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.neusoft.gbw.cp.collect.stream.service.transfer.CollectStreamResquestHandler;
import com.neusoft.gbw.cp.collect.stream.service.transfer.CollectVoiceStreamHandler;
import com.neusoft.gbw.cp.collect.stream.vo.StreamChannel;
import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.np.arsf.base.bundle.Log;

public class ServiceContext {

	private static class ServiceContextHolder {
		private static final ServiceContext INSTANCE = new ServiceContext();
	}

	private ServiceContext() {
		channelMap = new ConcurrentHashMap<String, StreamChannel>();
		dataMap = new ConcurrentHashMap<String, StreamParam>();
		streamRequestMap = new HashMap<String, CollectStreamResquestHandler>();
		streamResposeMap = new HashMap<String, CollectVoiceStreamHandler>();
		streamMonitorMap = new HashMap<String, String>();
	}

	public static ServiceContext getInstance() {
		return ServiceContextHolder.INSTANCE;
	}
	
	private ConcurrentHashMap<String, StreamChannel> channelMap = null;
	private ConcurrentHashMap<String, StreamParam> dataMap = null;
	private Map<String, CollectStreamResquestHandler> streamRequestMap = null;
	private Map<String, CollectVoiceStreamHandler> streamResposeMap = null;
	//key:monitorCode;value:socketKey
	private Map<String, String> streamMonitorMap = null;
	
	public void putMonitor(String monitorCode, String socketKey) {
		streamMonitorMap.put(monitorCode, socketKey);
	}
	
	public String getSocketKey(String socketKey) {
		return streamMonitorMap.get(socketKey);
	}
	
	public boolean containMonitor(String socketKey) {
		return streamMonitorMap.containsKey(socketKey);
	}
	
	public void putStreamReq(String key, CollectStreamResquestHandler res) {
		streamRequestMap.put(key, res);
	}
	
	public void stopStreamReq(String key) {
		if(streamRequestMap.containsKey(key)) {
			streamRequestMap.get(key).stopThreadRunning();
			streamRequestMap.remove(key);
		}
	}
	
	public void putStreamRes(String key, CollectVoiceStreamHandler res) {
		streamResposeMap.put(key, res);
	}
	
	public void stopStreamRes(String key) {
		if(streamResposeMap.containsKey(key)) {
			streamResposeMap.get(key).stopThreadRunning();
			streamResposeMap.remove(key);
		}
	}
	
	public StreamParam getStreamParam(String key) {
		if (dataMap.containsKey(key)) {
			return this.dataMap.get(key);
		}
		return null;
	}
	
	public void put(String key, StreamParam para) {
		this.dataMap.put(key, para);
	}
	
	public void removeData(String key) {
		this.dataMap.remove(key);
	} 
	
	public StreamChannel getStreamChannel(String key) {
		try{
			if (channelMap.containsKey(key)) {
				return this.channelMap.get(key);
			}
		}catch(Exception e){
			return null;
		}
		return null;
	}
	
	public void put(String key, StreamChannel channel) {
		this.channelMap.put(key, channel);
	}
	
	public void removeChannel(String key) {
		this.channelMap.remove(key);
	}
}
