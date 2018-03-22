package com.neusoft.gbw.cp.process.inspect.vo;
/**
 * 巡检项
 * @author jh
 *
 */
public enum InspectType {
	/**
	 * 接收机状态
	 */
	equ_status,
	/**
	 * 接收机日志状态（暂时未实现）
	 */
	equ_log_status,
	/**
	 * 版本查询
	 */
	program_status,
	/**
	 * 任务状态
	 */
	task_status
}
