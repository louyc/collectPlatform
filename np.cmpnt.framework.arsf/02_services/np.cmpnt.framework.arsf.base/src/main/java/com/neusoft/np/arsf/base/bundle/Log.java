package com.neusoft.np.arsf.base.bundle;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: 日志代理类<br>
 * 创建日期: 2013年9月26日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年9月26日       黄守凯        创建
 * </pre>
 */
public class Log {

	protected static String getDomainName() {
		String name = BaseContextImpl.getInstance().getBundleName();
		if (name.startsWith("Bundle")) {
			return BaseContextImpl.getInstance().getStortName();
		} else {
			return name;
		}
		// return BaseContextImpl.getInstance().getBundleName();
	}

	/**
	 * debug级别日志输出
	 * 
	 * @param message 内容
	 */
	public static void debug(Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().debug(getDomainName(), getLogMsg(stacks[1], message));
	}

	public static void debug(String domain, Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().debug(getDomainName() + "." + domain, getLogMsg(stacks[1], message));
	}

	/**
	 * info级别日志输出
	 * 
	 * @param message 内容
	 */
	public static void info(Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().info(getDomainName(), getLogMsg(stacks[1], message));
	}

	public static void info(String domain, Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().info(getDomainName() + "." + domain, getLogMsg(stacks[1], message));
	}

	/**
	 * warn级别日志输出
	 * 
	 * @param message 内容
	 */
	public static void warn(Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().warn(getDomainName(), getLogMsg(stacks[1], message));
	}

	public static void warn(String domain, Object message) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().warn(getDomainName() + "." + domain, getLogMsg(stacks[1], message));
	}

	/**
	 * error级别日志输出
	 * 
	 * @param message 内容
	 * @param t 异常对象
	 */
	public static void error(Object message, Throwable t) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().error(getDomainName(), getLogMsg(stacks[1], message), t);
	}

	public static void error(String domain, Object message, Throwable t) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		ARSFToolkit.getLog().error(getDomainName() + "." + domain, getLogMsg(stacks[1], message), t);
	}

	private static String getBundleInfo() {
		return BaseContextImpl.getInstance().getBundleName();
	}

	protected static String getLogMsg(StackTraceElement stack, Object message) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(getBundleInfo() + " - ");
		buffer.append(stack.getFileName());
		buffer.append(":").append(stack.getLineNumber()).append("]");
		buffer.append(message);
		return buffer.toString();
	}
}
