package com.neusoft.gbw.cp.process.realtime.channel.report;

import com.neusoft.gbw.cp.process.realtime.channel.AbstractTaskChannel;
import com.neusoft.gbw.cp.process.realtime.channel.ChannelType;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.context.TaskProcessContext;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class ReportTaskChannel extends AbstractTaskChannel{
	
	private ReportTaskDisposeHandler handler = null;
	private NMServiceCentre servicePool = null;

	public ReportTaskChannel(ChannelType type) {
		super(type);
	}
	
	@Override
	public void init() {
		super.init();
		this.servicePool = new NMServiceCentre();
	}

	@Override
	public void open() {
		handler = new ReportTaskDisposeHandler(this);
		handler.setServiceName(ChannelType.report.name() + "_thread");
		this.servicePool.addService(handler);
	}

	@Override
	public void close() {
		servicePool.stopAllThreads();
		clear();
	}
	
	public ITaskProcess getRealtimeDispose(String name) {
		return TaskProcessContext.getInstance().getTaskProcessor(name);
	}

}
