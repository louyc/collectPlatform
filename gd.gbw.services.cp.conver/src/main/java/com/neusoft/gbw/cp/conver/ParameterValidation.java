package com.neusoft.gbw.cp.conver;

import java.util.List;

import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;
/**
 * 对象属性参数验证
 */
public interface ParameterValidation {
	/**
	 * 验证下行消息
	 * @param query
	 * @return
	 */
	public boolean checkQuery(Query query);
	/**
	 * 验证上行消息
	 * @param report
	 * @return
	 */
	public boolean checkReport(Report report);
	/**
	 * 提取错误信息
	 * @return
	 */
	public List<String> getErrorMessage();
}
