package com.neusoft.np.arsf.common.exception;

/**
 * Base class for exceptions thrown by the NMS Collection Platform
 */
public class NMException extends Exception {

	private static final long serialVersionUID = 1517554801450445074L;

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * Constructors.
	 * 
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	
	public NMException() {
		super();
	}

	public NMException(String msg) {
		super(msg);
	}

	public NMException(Throwable cause) {
		super(cause);
	}

	public NMException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * Interface.
	 * 
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	/**
	 * <p>
	 * Return the exception that is the underlying cause of this exception.
	 * </p>
	 * 
	 * <p>
	 * This may be used to find more detail about the cause of the error.
	 * </p>
	 * 
	 * @return the underlying exception, or <code>null</code> if there is not
	 *         one.
	 */
	public Throwable getUnderlyingException() {
		return super.getCause();
	}

	public String toString() {
		Throwable cause = getUnderlyingException();
		if (cause == null || cause == this) {
			return super.toString();
		} else {
			return super.toString() + " [See NMException: " + cause + "]";
		}
	}
}
