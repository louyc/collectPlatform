package com.neusoft.gbw.cp.process.measure.channel.condition;

import com.neusoft.np.arsf.common.exception.NMException;

public class ConditionPoolException extends NMException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5057254451445168492L;

	public ConditionPoolException() {
		super();
	}

	public ConditionPoolException(String msg) {
		super(msg);
	}

	public ConditionPoolException(Throwable cause) {
		super(cause);
	}

	public ConditionPoolException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
