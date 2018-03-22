package com.neusoft.gbw.cp.schedule.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.schedule.constants.ScheduleConstants;
import com.neusoft.gbw.cp.schedule.service.handler.TaskDispatchorHandler;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskDispatchorService {
	private BlockingQueue<CollectTask> dataDispatchorQueue;

	private TaskDispatchorHandler handler;

	private void init() {
		dataDispatchorQueue = ARSFToolkit.getBlockingQueue(CollectTask.class,"collectTaskDispatchorQueue",
				ScheduleConstants.COLLECT_TASK_DISPATCHOR_QUEUE_SIZE);

	}

	public void recieveDate(CollectTask obj) {
		try {
			dataDispatchorQueue.put(obj);
		} catch (InterruptedException e) {
			Log.error("collectTaskDispatchorQueue队列接收数据失败", e);
		}
		//添加  捕捉异常
		catch(ClassCastException  e){
			Log.debug("ClassCastException    类型转换错误       TaskDispatchorService ");
		} catch(NullPointerException e){
			Log.debug("NullPointerException   空指针异常        TaskDispatchorService ");
		}catch(IllegalArgumentException  e){
			Log.debug("IllegalArgumentException  不合法参数异常      TaskDispatchorService");
		}
	}

	public void start() {
		init();
		handler = new TaskDispatchorHandler(dataDispatchorQueue);
		handler.setServiceName("collectTaskDispatchor数据分发线程");
		ARSFToolkit.getServiceCentre().addService(handler);
	}

	public void stop() {
		clear();
		ARSFToolkit.getServiceCentre().removeServiceByName(
				handler.getServiceName());
	}

	public void clear() {
		try{
		dataDispatchorQueue.clear();
		}catch(UnsupportedOperationException e){
			Log.debug("UnsupportedOperationException 不支持操作 不支持功能异常     TaskDispatchorService ");
		}
	}
}
