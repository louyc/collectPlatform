package com.neusoft.np.arsf.base.bundle.event;

/**
 * 
 * 项目名称: IT监管处理平台<br>
 * 模块名称: 处理平台服务开发<br>
 * 功能描述: 事件消息封装，服务内部使用。本类属于事件处理机制公共代码。<br>
 * 创建日期: 2012-12-19 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br>
 * 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * 
 *          <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-19       黄守凯        创建
 * </pre>
 */
public class NPEventInfo {

	/**
	 * 消息类别
	 */
	private String topic;

	/**
	 * 消息体，应该为xml数据
	 */
	private Object dataInfo;

	public NPEventInfo() {
	}

	public NPEventInfo(String topic, Object dataInfo) {
		this.topic = topic;
		this.dataInfo = dataInfo;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Object getDataInfo() {
		return dataInfo;
	}

	public void setDataInfo(Object dataInfo) {
		this.dataInfo = dataInfo;
	}

	@Override
	public String toString() {
		return "NPEventInfo [topic=" + topic + ", dataInfo=" + dataInfo + "]";
	}

}
