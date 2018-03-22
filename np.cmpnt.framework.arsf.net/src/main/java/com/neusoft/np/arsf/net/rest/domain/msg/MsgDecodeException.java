package com.neusoft.np.arsf.net.rest.domain.msg;

import com.neusoft.np.arsf.common.exception.NMException;

public class MsgDecodeException extends NMException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3732406034074601677L;

	public MsgDecodeException() {
		super();
	}

	public MsgDecodeException(String msg) {
		super(msg);
	}

	public MsgDecodeException(Throwable cause) {
		super(cause);
	}

	public MsgDecodeException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
