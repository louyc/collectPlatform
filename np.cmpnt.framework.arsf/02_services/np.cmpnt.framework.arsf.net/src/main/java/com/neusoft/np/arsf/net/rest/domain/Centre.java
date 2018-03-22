package com.neusoft.np.arsf.net.rest.domain;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.rest.domain.server.RestJaxRsServer;
import com.neusoft.np.arsf.net.rest.infra.context.ClassContext;

public class Centre {

	private RestJaxRsServer server;

	public void init() {
		ClassContext.getContext().init();
		server = new RestJaxRsServer();
		try {
			server.start();
		} catch (Exception e) {
			Log.error("REST 服务启动失败。", e);
		}
	}

	public void stop() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				Log.error("REST 服务停止失败。", e);
			}
		}
		ClassContext.getContext().stop();
	}

}
