package com.neusoft.gbw.cp.schedule.context;

import java.util.HashMap;

import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;


public class TimeMsgContext {
	private static class TimeRemindMsgBuilderHolder {
		private static final TimeMsgContext INSTANCE = new TimeMsgContext();
	}
	
	private TimeMsgContext() {
		timeMap = new HashMap<String, TimeSetMsg>();
	}
	
	public static TimeMsgContext getInstance() {
		return TimeRemindMsgBuilderHolder.INSTANCE;
	}
	
	private HashMap<String, TimeSetMsg> timeMap = null;

	public void put(TimeSetMsg msg) {
		String id = msg.getRemindTopic();
		timeMap.put(id, msg);
	}
	
	public TimeSetMsg getTimeSetMsg(String msg_id) {
		if(constansKey(msg_id)) {
			return timeMap.get(msg_id);
		}
		return null;
	}
	
	private boolean constansKey(String msg_id) {
		if(timeMap.containsKey(msg_id)) {
			return true;	
		}
		return false;
	}
}
