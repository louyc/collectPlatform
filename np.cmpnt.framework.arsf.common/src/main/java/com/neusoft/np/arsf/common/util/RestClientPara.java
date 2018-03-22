package com.neusoft.np.arsf.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.impl.Log4JLogger;
import org.eclipse.equinox.internal.util.ref.Log;

public class RestClientPara {

	private String mgrIp;

	private String port;

	private String category;

	private Map<String, String> parameters;

	public RestClientPara() {
	}

	public RestClientPara(String category, Object object) {
		this.category = category;
		init(object.getClass());
	}

	private void init(Class<?> clazz) {
		try {
			Class<?> arsftoolkit = clazz.getClassLoader().loadClass("com.neusoft.np.arsf.base.bundle.ARSFToolkit");
			Method method = arsftoolkit.getDeclaredMethod("getConfiguration");
			Object confObj = method.invoke(arsftoolkit);
			Class<?> confClass = clazz.getClassLoader().loadClass("com.neusoft.np.arsf.service.config.Configuration");
			Method confm = confClass.getDeclaredMethod("getFrameProperty", String.class);
			this.mgrIp = (String) confm.invoke(confObj, "mgrIp");
			this.port = (String) confm.invoke(confObj, "port");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RestClientPara(String mgrIp, String port, String category) {
		this.mgrIp = mgrIp;
		this.port = port;
		this.category = category;
	}

	public void put(String key, String value) {
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		parameters.put(key, value);
	}

	public String toURL() throws NMFormateException {
		if (!NMCheckArgcUtil.checkParameter(mgrIp, port)) {
			throw new NMFormateException("IP地址、Port端口获取失败");
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://");
		buffer.append(mgrIp);
		buffer.append(":");
		buffer.append(port);
		buffer.append("/NMS/restlet/remote-get/?category=");
		buffer.append(category);
		if (parameters == null) {
			return buffer.toString();
		}
		Iterator<Entry<String, String>> iter = parameters.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			buffer.append("&");
			buffer.append(entry.getKey());
			buffer.append("=");
			buffer.append(entry.getValue());
		}
		return buffer.toString();
	}

	public String getMgrIp() {
		return mgrIp;
	}

	public void setMgrIp(String mgrIp) {
		this.mgrIp = mgrIp;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
