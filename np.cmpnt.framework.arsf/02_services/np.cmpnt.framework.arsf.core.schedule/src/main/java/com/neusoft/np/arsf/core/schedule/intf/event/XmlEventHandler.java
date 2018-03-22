package com.neusoft.np.arsf.core.schedule.intf.event;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 调度任务事件监听EventHandler基类<br>
 * 创建日期: 2013年11月8日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年11月8日       黄守凯        创建
 * </pre>
 */
public abstract class XmlEventHandler implements BaseEventHandler {

	@Override
	public boolean processEvent(Object eventData) {
		if (eventData == null) {
			return false;
		}
		try {
			return processEvent(String.valueOf(eventData));
		} catch (Exception e) {
			Log.error("事件接收，数据解析错误。接收到的内容为：" + eventData, e);
		}
		return false;
	}

	public boolean processEvent(String eventData) throws NMException, XMLStreamException {
		List<NPMessage> messages = NPXmlStream.unmarshal(eventData);
		for (NPMessage message : messages) {
			Log.info("schedule receive :" + message);
			processEvent(message.getData());
		}
		return true;
	}

	public abstract void processEvent(Map<String, String> eventData) throws NMException;

}
