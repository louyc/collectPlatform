package com.neusoft.np.arsf.net.rest.infra.context;

import com.neusoft.np.arsf.base.bundle.BaseContext;
import com.neusoft.np.arsf.net.rest.domain.vo.SyntVO;
import com.neusoft.np.arsf.net.rest.infra.condition.ConditionPool;
import com.neusoft.np.arsf.net.rest.infra.pool.TimeoutDataPool;

public class ClassContext {

	protected static class ClassContextHolder {
		private static final ClassContext INSTANCE = new ClassContext();
	}

	private ClassContext() {
	}

	public static ClassContext getContext() {
		return ClassContextHolder.INSTANCE;
	}

	private BaseContext baseContext;

	private ConditionPool ctp;

	private TimeoutDataPool<SyntVO> tsp;

	public void init() {
		ctp = new ConditionPool();
		ctp.init();
		tsp = new TimeoutDataPool<SyntVO>();
		tsp.init();
		tsp.start();
	}

	public void stop() {
		ctp.stop();
		tsp.stop();
	}

	public BaseContext getBaseContext() {
		return baseContext;
	}

	public void setBaseContext(BaseContext baseContext) {
		this.baseContext = baseContext;
	}

	public ConditionPool getCtp() {
		return ctp;
	}

	public void setCtp(ConditionPool ctp) {
		this.ctp = ctp;
	}

	public TimeoutDataPool<SyntVO> getTsp() {
		return tsp;
	}

	public void setTsp(TimeoutDataPool<SyntVO> tsp) {
		this.tsp = tsp;
	}
}
