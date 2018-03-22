package com.neusoft.np.arsf.common.util;

import com.neusoft.np.arsf.common.exception.NMException;

public class NMBeanUtilsException extends NMException {

	private static final long serialVersionUID = 7675557573689522835L;

	public NMBeanUtilsException() {
		super();
	}

	public NMBeanUtilsException(final String msg) {
		super("[ARSF.Common]配置文件内容到类对象转换异常：" + msg);
	}

	public NMBeanUtilsException(final String msg, final Throwable cause) {
		super("[ARSF.Common]配置文件内容到类对象转换异常：" + msg, cause);
	}
}