package com.neusoft.np.arsf.laucher;

import java.util.Map;

public class NPLauncherServiceImpl implements NPLauncherService {

	@Override
	public void startServices(Map<String, Integer> levelMap) throws Exception {
		StartLevelProcess.startBundlesByService(levelMap);
	}
}
