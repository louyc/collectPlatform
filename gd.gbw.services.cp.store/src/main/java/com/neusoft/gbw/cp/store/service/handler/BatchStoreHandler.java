package com.neusoft.gbw.cp.store.service.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.store.constants.StoreVariable;
import com.neusoft.gbw.cp.store.service.Channel;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class BatchStoreHandler extends NMService {
	
	public Channel channel = null;
	
	private List<Map<String, Object>> datalist = null;
	
	public BatchStoreHandler(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		datalist = new ArrayList<Map<String, Object>>();
		StoreInfo info = null;
		while(isThreadRunning()) {
			info = channel.getQueue().poll();
			
			if (info == null) {
				commit();
				sleep();
				continue;
			}
			
			datalist.add(info.getDataMap());
			int size = datalist.size();
			if (size == StoreVariable.BATCH_RECORD_SAVE_NUM) {
				commit();
			}
			info = null;
		}
	}
	
	/**
	 * 批量提交
	 */
	private void commit() {
		if (datalist.isEmpty())
			return;
		
		channel.store(datalist);
		datalist.clear();
	}
	
	private void sleep() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
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
