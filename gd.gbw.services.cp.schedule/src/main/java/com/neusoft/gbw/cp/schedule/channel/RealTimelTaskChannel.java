package com.neusoft.gbw.cp.schedule.channel;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class RealTimelTaskChannel extends AbstractTaskChannel{
	private RealTimeTaskDisposeHandler handler;
	
	@Override
	public void init() {
		super.init();
	}

	public RealTimelTaskChannel(ChannelType type) {
		super(type);
	}

	@Override
	public void open() {
		init();
		handler = new RealTimeTaskDisposeHandler(this);
		handler.setServiceName(ChannelType.realtime.name()+"_thread#");
		ARSFToolkit.getServiceCentre().addService(handler);
	}

	@Override
	public void close() {
		clear();
	}

}
