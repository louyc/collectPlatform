package com.neusoft.np.arsf.base.bundle.persistence;

import com.neusoft.np.arsf.base.bundle.Log;

public class MybatisLogProxy implements org.apache.ibatis.logging.Log {

	public MybatisLogProxy() {
	}

	public MybatisLogProxy(String mapper) {
		Log.info("mybatis log mapper : " + mapper);
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void error(String s, Throwable e) {
		Log.error(s, e);
	}

	@Override
	public void error(String s) {
		Log.warn(s);
	}

	@Override
	public void debug(String s) {
		Log.debug(s);
	}

	@Override
	public void trace(String s) {
		Log.debug(s);
	}

	@Override
	public void warn(String s) {
		Log.warn(s);
	}

}
