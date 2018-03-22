package com.neusoft.np.arsf.net.rest.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.neusoft.np.arsf.net.rest.domain.resc.SyntResource;

public class SyntApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<Class<?>>();
		resources.add(SyntResource.class);
		return resources;
	}
}
