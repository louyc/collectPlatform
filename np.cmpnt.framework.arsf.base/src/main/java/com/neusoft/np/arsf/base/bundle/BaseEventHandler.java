package com.neusoft.np.arsf.base.bundle;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: Event事件处理接口<br>
 * 创建日期: 2013年8月29日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年8月29日       黄守凯        创建
 * </pre>
 */
public interface BaseEventHandler {

	String getTopicName();

	/**
	 * 事件处理接口
	 * 
	 * @param topic 主题名称
	 * @param eventData 传输数据
	 */
	boolean processEvent(Object eventData);

}
