package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import com.neusoft.gbw.cp.process.realtime.channel.AbstractTaskChannel;
import com.neusoft.gbw.cp.process.realtime.channel.ChannelType;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.TaskProcessContext;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class RealTimeTaskChannel extends AbstractTaskChannel {
	
	private NMServiceCentre servicePool = null;
	private RealtimeTaskDisposeHandler handler = null;

	@Override
	public void init() {
		super.init();
		this.servicePool = new NMServiceCentre();
	}
	
	public RealTimeTaskChannel(ChannelType type) {
		super(type);
	}

	@Override
	public void open() {
		for(int i=0;i<ProcessConstants.REAL_TIME_THREAD_NUM;i++){
			try{
				handler = new RealtimeTaskDisposeHandler(this);
				handler.setServiceName(ChannelType.realtime.name() + "_thread#" + i);
				servicePool.addService(handler);
			}catch(Exception e){
				Log.debug(this.getClass().getName()+"线程启动报错", e);
			}
		}
	}

	@Override
	public void close() {
		servicePool.stopAllThreads();
		clear();
	}
	/**
	 * 获取处理器
	 * @param name
	 * @return
	 */
	public ITaskProcess getRealtimeDispose(String name) {
		return TaskProcessContext.getInstance().getTaskProcessor(name);
	}
}
