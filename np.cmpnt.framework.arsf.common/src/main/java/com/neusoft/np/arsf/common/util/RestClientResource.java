package com.neusoft.np.arsf.common.util;

import java.io.IOException;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.neusoft.np.arsf.common.exception.RestClientResourceException;

public class RestClientResource {

	public static String restGetJson(String url) throws RestClientResourceException {
		ClientResource client = new ClientResource(url);
		try {
			return client.get().getText();
		} catch (ResourceException e) {
			throw new RestClientResourceException(e);
		} catch (IOException e) {
			throw new RestClientResourceException(e);
		} catch (Exception e) {
			throw new RestClientResourceException(e);
		}
	}

	/**
	 * String url = "http://localhost:8080/NMS/restlet/remote-get/?category=dicswitch&key=topo.addins";
	 * SwitchDataDTO dto = RestClientResource.restGetObj(url, SwitchDataDTO.class);
	 */
	public static <T> T restGetObj(String url, Class<T> clazz) throws RestClientResourceException {
		String jsonStr = restGetJson(url);
		return NPJsonUtil.jsonToObject(jsonStr, clazz);
	}

	/**
	 * RestClientPara para = new RestClientPara("dicswitch",this.getClass());
	 * para.put("key", "topo.addins");
	 * SwitchDataDTO dto = RestClientResource.restGetObj(para, SwitchDataDTO.class);
	 */
	public static <T> T restGetObj(RestClientPara restClientPara, Class<T> clazz) throws RestClientResourceException {
		try {
			return restGetObj(restClientPara.toURL(), clazz);
		} catch (NMFormateException e) {
			throw new RestClientResourceException(e);
		}
	}

	//	public static <T> List<T> restGetListObj(String url, Class<T> clazz) throws RestClientResourceException {
	//		throw new IllegalArgumentException("UnSupport");
	//	}
}
