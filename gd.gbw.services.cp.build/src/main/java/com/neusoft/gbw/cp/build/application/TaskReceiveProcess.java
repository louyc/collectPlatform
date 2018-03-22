package com.neusoft.gbw.cp.build.application;

import com.neusoft.gbw.cp.build.domain.services.TaskProcessCentre;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class TaskReceiveProcess extends NMService{

	private TaskReceiveMgr mgr = null;
	private TaskProcessCentre centre = null;
	
	public TaskReceiveProcess(TaskReceiveMgr mgr) {
		this.mgr = mgr; 
		centre = TaskProcessCentre.getInstance();
	}

	@Override
	public void run() {
		MessageTask task = null;
		while(isThreadRunning()) {
			try {
				Thread.sleep(500);//多任务一起下发时  延时逐条接收  
				task = mgr.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"TaskReceiveMgr.queue队列取值抛出错误    TaskReceiveProcess  ", e);
				break;
			}
			
			MessageType type = task.getType();
			
			if(type == null)
				Log.warn("接收消息未找到对应的消息类型， type=" + type);;
			
			switch(type) {
			case jms:
				centre.newJMSTaskProcess().taskProcess(task.getObject());
				break;
			case rest:
				centre.newRestTskProcess().taskProcess(task.getObject());
				break;
			case system:
				centre.newManualAndAutoTskProcess().taskProcess(task.getObject());
				break;
			}
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
