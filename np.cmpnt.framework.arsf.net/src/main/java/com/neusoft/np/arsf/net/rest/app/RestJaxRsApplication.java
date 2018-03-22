package com.neusoft.np.arsf.net.rest.app;

import org.restlet.Context;
import org.restlet.ext.jaxrs.JaxRsApplication;

public class RestJaxRsApplication extends JaxRsApplication {

	public RestJaxRsApplication(Context context) {
		super(context);
		this.add(new SyntApplication());
	}

}
