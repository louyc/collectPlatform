package com.neusoft.gbw.cp.collect.stream.event;

import com.neusoft.gbw.cp.collect.stream.context.ServiceContext;
import com.neusoft.gbw.cp.collect.stream.vo.StreamChannel;
import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectStreamHandler implements BaseEventHandler{
	
	private static String V8_MONITOR_CHANNEL_CODE = "V8Monitor";
	
	public CollectStreamHandler() {
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_STREAM_REQUEST_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		//获取主题对象
		if (arg0 instanceof StreamParam) {
			String monitorCode = null;
			StreamParam para = (StreamParam) arg0;
			
			int version = para.getMonVersion();
			switch(version) {
			case 7:
				monitorCode = para.getMonitorCode();
				break;
			case 8: //V8站点采用通用管道类型
				monitorCode = V8_MONITOR_CHANNEL_CODE;
				break;
			}
			
			StreamChannel channel = ServiceContext.getInstance().getStreamChannel(monitorCode);
			
			if (channel == null) {
				Log.warn("音频采集不存在此设备的连接，MonitorCode=" + monitorCode + ",version=" + version);
			} else {
				try {
					channel.put(para);
					ServiceContext.getInstance().put(para.getId(), para);
				} catch (InterruptedException e) {
					Log.debug(this.getClass().getName()+"队列存储报错",e);

				}
			}
		}
		return true;
	}
}
