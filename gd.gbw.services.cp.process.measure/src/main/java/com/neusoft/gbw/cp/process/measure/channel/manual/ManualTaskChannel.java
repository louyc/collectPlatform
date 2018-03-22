package com.neusoft.gbw.cp.process.measure.channel.manual;

import com.neusoft.gbw.cp.process.measure.channel.AbstractTaskChannel;
import com.neusoft.gbw.cp.process.measure.channel.ChannelType;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.TaskProcessContext;
import com.neusoft.gbw.cp.process.measure.vo.ManualType;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class ManualTaskChannel extends AbstractTaskChannel {
	
	private NMServiceCentre servicePool = null;
	private ManualTaskDisposeHandler handler = null;
	private ManualTaskListenHandler lisHandler = null;
	private RecordTaskListenHandler recordLisHandler = null;
	
	
	public ManualTaskChannel(ChannelType type) {
		super(type);
	}

	@Override
	public void init() {
		super.init();
		this.servicePool = new NMServiceCentre();
	}
	
	@Override
	public void open() {
		for(int i=0;i<ProcessConstants.SET_RECOVER_THREAD_NUM;i++){
			handler = new ManualTaskDisposeHandler(this);
			handler.setServiceName(ChannelType.manual_set_recover.name() + "_thread#" + i);
			servicePool.addService(handler);
		}
		//初始化回收任务完成监听线程
		lisHandler = new ManualTaskListenHandler();
		lisHandler.setServiceName(ChannelType.manual_set_recover.name() + "_manualTaskListen#");
		servicePool.addService(lisHandler);
		
		//初始化回收录音任务监听线程（如果任务下有录音任务，则需要对录音进行计数，并更改录音回收状态和回收计数）
		recordLisHandler = new RecordTaskListenHandler();
		recordLisHandler.setServiceName(ChannelType.manual_set_recover.name() + "_recordTaskListen#");
		servicePool.addService(recordLisHandler);
	}
	

	@Override
	public void close() {
		servicePool.stopAllThreads();
		clear();
	}
	
	public ITaskProcess getManualDispose(ManualType type) {
		return type == null ? null : TaskProcessContext.getInstance().getTaskProcessor(type.name());
	}
}
