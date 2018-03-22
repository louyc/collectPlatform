package com.neusoft.np.arsf.core.schedule.util;

import com.neusoft.np.arsf.common.exception.NMException;

public class FileLoadException extends NMException {

	private static final long serialVersionUID = -5592632232657086298L;

	public FileLoadException() {
		super();
	}

	public FileLoadException(String msg) {
		super(msg);
	}

	public FileLoadException(Throwable cause) {
		super(cause);
	}

	public FileLoadException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
