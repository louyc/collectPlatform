package com.neusoft.np.arsf.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: 数据模型格式化工具<br>
 * 创建日期: 2013年8月30日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年8月30日       黄守凯        创建
 * </pre>
 */
public class NPFormateUtil {

	private static String ALL_MARK = "ALL";

	public static String getKgStr(String kg) throws NMFormateException {
		if (ALL_MARK.equals(kg)) {
			return kg;
		}
		if (kg.length() != 10) {
			throw new NMFormateException("");
		}
		char[] kgChars = kg.toCharArray();
		return "" + kgChars[2] + kgChars[3] + "." + kgChars[5] + kgChars[6] + "." + kgChars[8] + kgChars[9];
	}

	public static String getKgInt(String kg) throws NMFormateException {
		if (ALL_MARK.equals(kg)) {
			return kg;
		}
		if (kg.length() != 8) {
			throw new NMFormateException("");
		}
		char[] kgChars = kg.toCharArray();
		return "10" + kgChars[0] + kgChars[1] + "0" + kgChars[3] + kgChars[4] + "0" + kgChars[6] + kgChars[7];
	}

	public static String getKpiStr(String kpi) throws NMFormateException {
		if (ALL_MARK.equals(kpi)) {
			return kpi;
		}
		if (kpi.length() != 13) {
			throw new NMFormateException("");
		}
		char[] kpiChars = kpi.toCharArray();
		return "" + kpiChars[2] + kpiChars[3] + "." + kpiChars[5] + kpiChars[6] + "." + kpiChars[8] + kpiChars[9] + "."
				+ kpiChars[11] + kpiChars[12];
	}

	public static String getKpiInt(String kpi) throws NMFormateException {
		if (ALL_MARK.equals(kpi)) {
			return kpi;
		}
		if (kpi.length() != 11) {
			throw new NMFormateException("");
		}
		char[] kpiChars = kpi.toCharArray();
		return "10" + kpiChars[0] + kpiChars[1] + "0" + kpiChars[3] + kpiChars[4] + "0" + kpiChars[6] + kpiChars[7]
				+ "0" + kpiChars[9] + kpiChars[10];
	}

	public static String getSevItemInt(String sevItem) throws NMFormateException {
		if (ALL_MARK.equals(sevItem)) {
			return sevItem;
		}
		if (sevItem.length() != 5) {
			throw new NMFormateException("");
		}
		char[] kpiChars = sevItem.toCharArray();
		return "10" + kpiChars[0] + kpiChars[1] + "0" + kpiChars[3] + kpiChars[4];
	}

	public static String getSevTypeInt(String sevType) throws NMFormateException {
		if (ALL_MARK.equals(sevType)) {
			return sevType;
		}
		if (sevType.length() != 2) {
			throw new NMFormateException("");
		}
		char[] kpiChars = sevType.toCharArray();
		return "10" + kpiChars[0] + kpiChars[1];
	}

	private static String KPI_FORMATE = "\\d\\d\\.\\d\\d\\.\\d\\d\\.\\d\\d";

	public static boolean isKpiStr(String text) {
		return regular(text, KPI_FORMATE);
	}

	/**
	 * 判断text是否符合表达式规则
	 * 
	 * @param text
	 * @param regular
	 * @return
	 */
	public static boolean regular(String text, String regular) {
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

	public static void main(String[] args) throws NMFormateException {
		System.out.println(getSevItemInt("01.02"));
		System.out.println(getSevTypeInt("01"));
	}

}
