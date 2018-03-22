package com.neusoft.np.arsf.db.pool;

import com.neusoft.np.arsf.common.exception.NMException;

public class NPPooledDataSourceException extends NMException {

	private static final long serialVersionUID = -3593970262627588681L;

	public NPPooledDataSourceException() {
		super();
	}

	public NPPooledDataSourceException(String msg) {
		super(msg);
	}

	public NPPooledDataSourceException(Throwable cause) {
		super(cause);
	}

	public NPPooledDataSourceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
