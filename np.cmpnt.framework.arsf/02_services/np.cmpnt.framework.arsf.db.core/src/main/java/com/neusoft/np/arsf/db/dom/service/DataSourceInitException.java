package com.neusoft.np.arsf.db.dom.service;

import com.neusoft.np.arsf.common.exception.NMException;

public class DataSourceInitException extends NMException {
	private static final long serialVersionUID = 6817913554332605250L;

	public DataSourceInitException() {
		super();
	}

	public DataSourceInitException(String msg) {
		super(msg);
	}

	public DataSourceInitException(Throwable cause) {
		super(cause);
	}

	public DataSourceInitException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
