package com.neusoft.gbw.cp.process.filter.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class DataDispatcherHandler extends NMService{
	
	private BlockingQueue<CollectData> queue = null;
	private DataFilterProcess process = null;
	
	public DataDispatcherHandler(BlockingQueue<CollectData> queue) {
		this.queue = queue;
		process = new DataFilterProcess();
	}

	@Override
	public void run() {
		CollectData collectData = null;
		while(isThreadRunning()) {
			try {
				collectData = queue.take();
			} catch (InterruptedException e) {
				Log.error("DataDispatcherHandler分发线程获取队列信息失败", e);
			}
			//暂时只是对于采集数据缺陷的过滤到，将来加入站点相关信息后再添加站点状态切换的过滤
			if (!process.dispose(collectData)) {
				Log.debug("[数据过滤]此类型数据不进行处理 ,data=" + collectData.getData() + ",collectStatus=" + collectData.getStatus());
				continue;
			}
			ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_DATA_PROCESS_TOPIC, collectData);
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
