package com.neusoft.np.arsf.service.log;

/**
* 项目名称: 采集平台框架<br>
* 模块名称: 采集平台日志服务<br>
* 创建日期: 2012-6-11 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-11      马仲佳        创建
* </pre>
*/
public interface Log {

	/**
	 * debug级别日志输出
	 * 
	 * @param message 内容
	 */
	void debug(Object message);

	/**
	 * info级别日志输出
	 * 
	 * @param message 内容
	 */
	void info(Object message);

	/**
	 * warn级别日志输出
	 * 
	 * @param message 内容
	 */
	void warn(Object message);

	/**
	 * error级别日志输出
	 * 
	 * @param message 内容
	 * @param t 异常对象
	 */
	void error(Object message, Throwable t);

	/**
	 * debug级别日志输出
	 * 
	 * @param message 内容
	 * @param domain
	 * @param t 异常对象
	 */
	void debug(String domain, Object message);

	/**
	 * info级别日志输出
	 * 
	 * @param message 内容
	 * @param domain
	 * @param t 异常对象
	 */
	void info(String domain, Object message);

	/**
	 * warn级别日志输出
	 * 
	 * @param message 内容
	 * @param domain
	 * @param t 异常对象
	 */
	void warn(String domain, Object message);

	/**
	 * error级别日志输出
	 * 
	 * @param message 内容
	 * @param domain
	 * @param t 异常对象
	 */
	void error(String domain, Object message, Throwable t);

	//	/**
	//	 * debug级别日志输出，由于OSGi的原因，手动记录打印日志所在的位置和行数。
	//	 * 
	//	 * @param message
	//	 * @param className
	//	 * @param lineNum
	//	 */
	//	void debug(Object message, Class<?> className, int lineNum);
	//
	//	/**
	//	 * info级别日志输出，由于OSGi的原因，手动记录打印日志所在的位置和行数。
	//	 * 
	//	 * @param message
	//	 * @param className
	//	 * @param lineNum
	//	 */
	//	void info(Object message, Class<?> className, int lineNum);
	//
	//	/**
	//	 * warn级别日志输出，由于OSGi的原因，手动记录打印日志所在的位置和行数。
	//	 * 
	//	 * @param message
	//	 * @param className
	//	 * @param lineNum
	//	 */
	//	void warn(Object message, Class<?> className, int lineNum);
	//
	//	/**
	//	 * error级别日志输出，由于OSGi的原因，手动记录打印日志所在的位置和行数。
	//	 * 
	//	 * @param message
	//	 * @param className
	//	 * @param lineNum
	//	 * @param t
	//	 */
	//	void error(Object message, Class<?> className, int lineNum, Throwable t);

}
