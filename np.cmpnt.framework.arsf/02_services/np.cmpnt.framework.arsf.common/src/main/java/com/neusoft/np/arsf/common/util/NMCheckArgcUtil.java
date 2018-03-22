package com.neusoft.np.arsf.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * 项目名称: IT监管处理平台<br>
 * 模块名称: 处理平台服务开发<br>
 * 功能描述: 字符串参数验证工具类<br>
 * 创建日期: 2012-12-19 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-19       黄守凯        创建
 * </pre>
 */
public class NMCheckArgcUtil {

	/**
	 * 字符串验证，针对key验证；
	 */
	public static boolean checkString(String str) {
		if (str == null) {
			return false;
		}
		if ("".equals(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证多个String类型字符串是否为null或者""，不带异常，有返回值
	 */
	public static boolean checkParameter(String... argument) {
		for (String elem : argument) {
			if (elem == null || "".equals(elem)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证多个String类型字符串是否为null或者""，带异常，无返回值
	 * 
	 * @param argument
	 * @throws NMFormateException
	 */
	public static void checkParameterErr(String... argument) throws NMFormateException {
		for (String elem : argument) {
			if (elem == null || "".equals(elem)) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("value:");
				for (String item : argument) {
					buffer.append(item);
					buffer.append(";");
				}
				throw new NMFormateException("checkParameterErr校验错误 : " + buffer.toString() + "，存在空值");
			}
		}
	}

	/**
	 * 验证容器是否为null或者个数为0，不带异常，有返回值
	 * 
	 * @param groups 待采集指标值
	 * @return 是否存在（为null或者个数为0返回false）
	 */
	public static <E> boolean verifyCollection(Collection<E> groups) {
		if (groups == null) {
			return false;
		}
		if (groups.size() <= 0) {
			return false;
		}
		return true;
	}

	/**
	 *  验证容器是否为null或者个数为0，带异常，无返回值
	 * 
	 * @param groups
	 * @throws NMFormateException
	 */
	public static <E> void verifyCollectionErr(Collection<E> groups) throws NMFormateException {
		if (groups == null) {
			throw new NMFormateException("参数为null.");
		}
		if (groups.size() <= 0) {
			throw new NMFormateException("参数个数为0");
		}
	}

	/**
	 * 验证待采集的多个指标值是否存在。
	 * 
	 * @param groups 待采集指标值
	 * @return 是否存在（为null或者个数为0返回false）
	 * @throws NMFormateException 
	 */
	public static <E> void verifyMapErr(Map<E, E> groups) throws NMFormateException {
		if (groups == null) {
			throw new NMFormateException("参数为null.");
		}
		if (groups.size() <= 0) {
			throw new NMFormateException("参数个数为0");
		}
	}

}
