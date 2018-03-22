package com.neusoft.np.arsf.service.avro;

public class AvroNetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6506766028457405292L;

	public AvroNetException() {
		super();
	}

	public AvroNetException(String msg) {
		super(msg);
	}

	public AvroNetException(Throwable cause) {
		super(cause);
	}

	public AvroNetException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public Throwable getUnderlyingException() {
		return super.getCause();
	}

	public String toString() {
		Throwable cause = getUnderlyingException();
		if (cause == null || cause == this) {
			return super.toString();
		} else {
			return super.toString() + " [Avro Net : " + cause + "]";
		}
	}
}
