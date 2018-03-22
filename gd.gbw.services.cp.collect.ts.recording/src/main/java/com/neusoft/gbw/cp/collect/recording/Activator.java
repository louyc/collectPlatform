package com.neusoft.gbw.cp.collect.recording;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.collect.recording.service.RecordService;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class Activator extends BaseActivator {
	
	private RecordService service = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		service = new RecordService();
		service.init();
		service.start();
	}

	@Override
	public void stop() {
		unbindCoreServices();
		Log.debug("someBody  调用了结束录音平台功能");
		if(service != null)
			service.stop();
	}
	
}
