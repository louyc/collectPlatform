package com.neusoft.np.arsf.service.transfer;

/**
 * 
 * 项目名称: 处理平台<br>
 * 模块名称: 处理平台核心服务<br>
 * 功能描述: 数据发送的接口<br>
 * 创建日期: 2012-12-20 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-20       刘勃宏        创建 
 * </pre>
 */
public interface ISend {

	/**
	 * 数据发送
	 * 
	 * @param sendInfo
	 */
	void send(SendInfo sendInfo);
}
