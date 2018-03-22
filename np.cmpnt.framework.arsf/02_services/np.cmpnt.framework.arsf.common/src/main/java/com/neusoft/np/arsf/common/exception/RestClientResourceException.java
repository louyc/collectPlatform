package com.neusoft.np.arsf.common.exception;

public class RestClientResourceException extends NMException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4430087102310295925L;

	public RestClientResourceException() {
		super();
	}

	public RestClientResourceException(String msg) {
		super(msg);
	}

	public RestClientResourceException(Throwable cause) {
		super(cause);
	}

	public RestClientResourceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
