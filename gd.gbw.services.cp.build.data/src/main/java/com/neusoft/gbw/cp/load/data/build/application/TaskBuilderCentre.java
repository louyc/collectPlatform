package com.neusoft.gbw.cp.load.data.build.application;

import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.model.LoadContext;
import com.neusoft.gbw.cp.load.data.build.domain.services.ConfigLoader;
import com.neusoft.gbw.cp.load.data.build.infrastructure.constants.BuildConstants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskBuilderCentre {
	
	private boolean init() {
		try {
			new ConfigLoader().loadConfig();
			DataMgrCentreModel.getInstance().synt();
			LoadContext.getInstance();
		} catch (Exception e) {
			Log.error("服务初始化失败", e);
			return false;
		}
		
		return true;
	}
	
	public void start() {
		if (!init())
			return;
	}
	
	public void stop() {
		ARSFToolkit.getServiceCentre().removeServiceByName(BuildConstants.REQUEST_THREAD_NAME);
	}
}
