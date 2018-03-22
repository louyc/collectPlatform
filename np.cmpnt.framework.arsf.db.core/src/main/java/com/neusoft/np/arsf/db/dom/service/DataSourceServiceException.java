package com.neusoft.np.arsf.db.dom.service;

import com.neusoft.np.arsf.common.exception.NMException;

public class DataSourceServiceException extends NMException {

	private static final long serialVersionUID = -2207324927505865073L;

	public DataSourceServiceException() {
		super();
	}

	public DataSourceServiceException(String msg) {
		super(msg);
	}

	public DataSourceServiceException(Throwable cause) {
		super(cause);
	}

	public DataSourceServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
