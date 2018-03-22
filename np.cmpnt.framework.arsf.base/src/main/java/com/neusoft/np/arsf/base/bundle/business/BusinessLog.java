package com.neusoft.np.arsf.base.bundle.business;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class BusinessLog extends Log {

	public static void debug(int stacksLevel, Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().debug(getDomainName(), getLogMsg(stacks[stacksLevel], message));
	}

	public static void info(int stacksLevel, Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().info(getDomainName(), getLogMsg(stacks[stacksLevel], message));
	}

	public static void warn(int stacksLevel, Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().warn(getDomainName(), getLogMsg(stacks[stacksLevel], message));
	}

	public static void error(int stacksLevel, Object message, Throwable t) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().error(getDomainName(), getLogMsg(stacks[stacksLevel], message), t);
	}
}
