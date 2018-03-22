package com.neusoft.np.arsf.common.util;

public class NMServiceExecuteException extends Exception {

	private static final long serialVersionUID = 5731757441014938311L;

	public NMServiceExecuteException() {
		super();
	}

	public NMServiceExecuteException(final String msg) {
		super("[ARSF.Common]配置文件内容到类对象转换异常：" + msg);
	}

	public NMServiceExecuteException(final String msg, final Throwable cause) {
		super("[ARSF.Common]配置文件内容到类对象转换异常：" + msg, cause);
	}
}
