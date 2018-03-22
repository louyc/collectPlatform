package com.neusoft.np.arsf.core.transfer.service;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.service.config.Configuration;
import com.neusoft.np.arsf.service.log.Log;

public class Proxy {

	private Proxy() {
	}

	/**
	 * 获取log服务
	 */
	public static Log getLog() {
		return ARSFToolkit.getLog();
	}

	/**
	 * 获取配置服务
	 */
	public static Configuration getConfiguration() {
		return ARSFToolkit.getConfiguration();
	}

}
