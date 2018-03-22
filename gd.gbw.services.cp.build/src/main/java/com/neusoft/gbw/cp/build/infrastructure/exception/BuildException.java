package com.neusoft.gbw.cp.build.infrastructure.exception;

public class BuildException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BuildException() {
		super();
	}

	public BuildException(String msg) {
		super(msg);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
