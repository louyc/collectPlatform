package com.neusoft.np.arsf.core.event;

import com.neusoft.np.arsf.common.exception.NMException;

public class NMServerException extends NMException {

	private static final long serialVersionUID = 5309795380272124006L;

	public NMServerException() {
		super();
	}

	public NMServerException(String msg) {
		super("[NMServerException]服务引用异常:" + msg);
	}

	public NMServerException(String msg, Throwable cause) {
		super("[NMServerException]服务引用异常:" + msg, cause);
	}
}
