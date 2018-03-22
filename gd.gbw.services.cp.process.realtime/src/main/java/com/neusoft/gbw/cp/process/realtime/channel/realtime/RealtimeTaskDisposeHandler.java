package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.channel.RealIdentityTypeExecutor;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.RealtimeType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RealtimeTaskDisposeHandler extends NMService {
	
	private RealTimeTaskChannel channel = null;
	public RealtimeTaskDisposeHandler(RealTimeTaskChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		CollectData data = null;
		while(isThreadRunning()) {
			try {
				data = (CollectData)channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			CollectTask task = data.getCollectTask();
			RealtimeType type = getRealtimeType(task);
			if(type == null) {
				Log.warn("采集数据无法找到对应实时任务处理类型。"+ getLogContent(data));
				continue;
			}
			ITaskProcess executor = channel.getRealtimeDispose(type.name());
			if (executor == null) {
				Log.warn("实时任务处理通道没有指定名称的处理类。Name=" + type.name());
			}
			int version = data.getCollectTask().getData().getProVersion();
			if (version == 0) {
				Log.warn("任务处理通道数据没有对应的版本类型，可能是上报数据");
				continue;
			}
			try {
				switch(version) {
				case 8:
					executor.disposeV8(data);
					break;
				case 7:
					executor.disposeV7(data);
					break;
				case 6:
					executor.disposeV6(data);
					break;
				case 5:
					executor.disposeV5(data);
					break;
				}
			} catch (RealtimeDisposeException e) {
				Log.error(this.getClass().getName()+"解析失败", e);
			}
		}
	}
	
	private String getLogContent(CollectData date) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TaskID=" + date.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + date.getCollectTask().getBusTask().getFreq() + ",");
		buffer.append("MonitorID=" + date.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("problemDate=" + getReturnValue(date));
		return buffer.toString();
	}
	
	private String getReturnValue(CollectData data) {
		String type = null;
		String desc = null;
		if(data.getData() == null || data.getData().getReportData() == null) 
			return "type=" + type + ";desc=" + desc;
		type = ((Report) data.getData().getReportData()).getReportReturn().getType();
		desc = ((Report) data.getData().getReportData()).getReportReturn().getDesc();
		return "type=" + type + ";desc=" + desc;
	}
	
	private RealtimeType getRealtimeType(CollectTask task) {
		return RealIdentityTypeExecutor.identityRealtimeType(task);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}

}
