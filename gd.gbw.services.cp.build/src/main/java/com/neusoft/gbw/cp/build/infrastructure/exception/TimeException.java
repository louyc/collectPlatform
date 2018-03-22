package com.neusoft.gbw.cp.build.infrastructure.exception;

public class TimeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TimeException() {
		super();
	}

	public TimeException(String msg) {
		super(msg);
	}

	public TimeException(Throwable cause) {
		super(cause);
	}

	public TimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
}