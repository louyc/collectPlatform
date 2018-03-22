package com.neusoft.gbw.cp.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.collect.event.handler.CollectInitHandler;
import com.neusoft.gbw.cp.collect.event.handler.CollectTaskHandler;
import com.neusoft.gbw.cp.collect.event.handler.MonitorAlarmHandler;
import com.neusoft.gbw.cp.collect.service.CollectServiceMgr;
import com.neusoft.gbw.cp.collect.service.transfer.TransferDownMgr;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private CollectServiceMgr serviceMgr = null;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		TransferDownMgr transferDownMgr  = serviceMgr.getTransferDownMgr();
		//获取总线信息需要获取的配置，注册订阅
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new CollectTaskHandler(transferDownMgr));
		list.add(new CollectInitHandler());
		list.add(new MonitorAlarmHandler());
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		//启动采集平台
		if (serviceMgr == null) {
			serviceMgr = new CollectServiceMgr();
			serviceMgr.start();
		}
		
	}
	
	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();
		if (serviceMgr != null)
			serviceMgr.stop();
	}
	
	
//	public void initDate() {
//		Thread thread = new Thread(new CreateCollect());
//		thread.start();
//	}
//	
	
//	protected void test() {
//		//标记log信息
//		Log.info("");
//		Log.info("服务名", "");
//		//获取配置文件，配置文件所在位置com.neusoft.np.arsf.core.config工程下的config包配置文件，自己新建配置文件，然后再fileListConfig文件中将配置文件写入
//		Map<String, String> configMap = ARSFToolkit.getConfiguration().getAllBusinessProperty("aedis_collect");
//		//创建队列
//		BlockingQueue<Object> queue = ARSFToolkit.getBlockingQueue("", 10000);
//		//上报总线
//		ARSFToolkit.sendEvent("TOPIC", null);
//		
//		//线程操作
//		TestHandler handle = new TestHandler();
//		Thread thread = new Thread(handle);
//		//添加一个线程
//		ARSFToolkit.getServiceCentre().addService(handle);
//		//删除一个线程
//		ARSFToolkit.getServiceCentre().removeServiceByName(handle.getServiceName());
//		
//		handle.stopThreadRunning();
//		thread.interrupt();	
//		//停止所有线程
//		ARSFToolkit.getServiceCentre().stopAllThreads();
//		//注册一个线程池
//		ARSFToolkit.getServiceCentre().startServicePool("abc", TestHandler.class.getClass(), 10);
//		//删除制定线程池
//		ARSFToolkit.getServiceCentre().removeServicePoolByName("abc");
//		//删除所有线程池
//	threadPool.addService(service);
//		ARSFToolkit.getServiceCentre().stopAllServicePool();
//	}
}

