package com.neusoft.np.arsf.base.bundle;

import com.neusoft.np.arsf.common.exception.NMException;

public class ServiceHandlerException extends NMException {

	private static final long serialVersionUID = 5309795380272124006L;

	public ServiceHandlerException() {
		super();
	}

	public ServiceHandlerException(String msg) {
		super("[NMServerException]服务引用异常:" + msg);
	}

	public ServiceHandlerException(String msg, Throwable cause) {
		super("[NMServerException]服务引用异常:" + msg, cause);
	}
}
