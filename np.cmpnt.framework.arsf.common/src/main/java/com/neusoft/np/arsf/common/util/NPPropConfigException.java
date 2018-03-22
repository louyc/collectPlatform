package com.neusoft.np.arsf.common.util;

import com.neusoft.np.arsf.common.exception.NMException;

public class NPPropConfigException extends NMException {

	private static final long serialVersionUID = -117342866222964879L;

	public NPPropConfigException() {
		super();
	}

	public NPPropConfigException(final String msg) {
		super("[ARSF.Common]配置文件读取异常：" + msg);
	}

	public NPPropConfigException(final Throwable cause) {
		super("[ARSF.Common]配置文件读取异常：", cause);
	}

	public NPPropConfigException(final String msg, final Throwable cause) {
		super("[ARSF.Common]配置文件读取异常：" + msg, cause);
	}
}
