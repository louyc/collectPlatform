package com.neusoft.np.arsf.common.util;

import com.neusoft.np.arsf.common.exception.NMException;

public class NPConstructorException extends NMException {

	private static final long serialVersionUID = -1059464223851002308L;

	public NPConstructorException() {
		super();
	}

	public NPConstructorException(final String msg) {
		super("[ARSF.Common]构造器工具方法异常：" + msg);
	}

	public NPConstructorException(final String msg, final Throwable cause) {
		super("[ARSF.Common]构造器工具方法异常：" + msg, cause);
	}
}
