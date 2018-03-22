package com.neusoft.np.arsf.net.rest.domain.server;

import org.restlet.Component;
import org.restlet.data.Protocol;

import com.neusoft.np.arsf.net.rest.app.RestJaxRsApplication;

public class RestJaxRsServer {

	private Component component;

	public void start() throws Exception {
		component = new Component();
		component.getServers().add(Protocol.HTTP, 8088);
		component.getDefaultHost().attach(new RestJaxRsApplication(null));
		component.start();
	}

	public void stop() throws Exception {
		if (component != null) {
			component.stop();
		}
	}

	public static void main(String[] args) {
		RestJaxRsServer r = new RestJaxRsServer();
		try {
			r.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
