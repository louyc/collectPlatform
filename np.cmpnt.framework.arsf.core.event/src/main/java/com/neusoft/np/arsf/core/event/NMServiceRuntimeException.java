package com.neusoft.np.arsf.core.event;

public class NMServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8176894055641167016L;

	public NMServiceRuntimeException() {
		super();
	}

	public NMServiceRuntimeException(final String msg) {
		super("[NMServiceRuntimeException]服务启动异常，框架出现启动或运行问题：" + msg);
	}

	public NMServiceRuntimeException(final String msg, final Throwable cause) {
		super("[NMServiceRuntimeException]服务引用异常，框架出现启动或运行问题：" + msg, cause);
	}
}
