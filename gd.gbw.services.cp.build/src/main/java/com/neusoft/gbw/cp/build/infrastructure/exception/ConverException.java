package com.neusoft.gbw.cp.build.infrastructure.exception;

public class ConverException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ConverException() {
		super();
	}

	public ConverException(String msg) {
		super(msg);
	}

	public ConverException(Throwable cause) {
		super(cause);
	}

	public ConverException(String msg, Throwable cause) {
		super(msg, cause);
	}
}