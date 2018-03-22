package com.neusoft.gbw.cp.schedule.channel;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class PlanTaskChannel extends AbstractTaskChannel{
	private PlanTaskDisposeHandler handler = null;

	@Override
	public void init() {
		super.init();
	}
	
	public PlanTaskChannel(ChannelType type) {
		super(type);
	}

	@Override
	public void open() {
			handler = new PlanTaskDisposeHandler(this);
			handler.setServiceName(ChannelType.plan.name() + "_thread#");
			ARSFToolkit.getServiceCentre().addService(handler);
	}

	@Override
	public void close() {
		clear();
	}
}