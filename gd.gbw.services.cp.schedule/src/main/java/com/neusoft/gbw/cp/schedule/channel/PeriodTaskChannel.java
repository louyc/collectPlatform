package com.neusoft.gbw.cp.schedule.channel;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class PeriodTaskChannel extends AbstractTaskChannel{
	private PeriodTaskDisposeHandler handler = null;

	@Override
	public void init() {
		super.init();
	}
	
	public PeriodTaskChannel(ChannelType type) {
		super(type);
	}

	@Override
	public void open() {
		handler = new PeriodTaskDisposeHandler(this);
		handler.setServiceName(ChannelType.period.name() + "_thread");
		ARSFToolkit.getServiceCentre().addService(handler);
	}

	@Override
	public void close() {
		clear();
	}
}