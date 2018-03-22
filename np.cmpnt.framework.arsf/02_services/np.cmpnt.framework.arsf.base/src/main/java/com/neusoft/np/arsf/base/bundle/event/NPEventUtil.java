package com.neusoft.np.arsf.base.bundle.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;
import com.neusoft.np.arsf.service.event.NMSEvent;

/**
 * 
 * 项目名称: IT监管处理平台<br>
 * 模块名称: 处理平台服务开发<br>
 * 功能描述: 事件处理相关工具类<br>
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
public class NPEventUtil {

	public static final String EVENT_TOPIC = "event.topics";

	private NPEventUtil() {
	}

	protected static List<NPEventInfo> getEventInfo(NMSEvent event) throws NMFormateException {
		String[] events = event.getPropertyNames();
		if (events.length <= 0) {
			throw new NMFormateException("the event property is empty");
		}
		List<NPEventInfo> list = new ArrayList<NPEventInfo>();
		for (String eventKey : events) {
			if (EVENT_TOPIC.equals(eventKey)) {
				continue;
			}
			Object dataInfo = getDataInfo(event.getProperty(eventKey));
			list.add(new NPEventInfo(eventKey, dataInfo));
		}
		return list;
	}

//	protected static String getDataInfo(Object o) throws NMFormateException {
//		if (o == null) {
//			throw new NMFormateException("");
//		}
//		if (o instanceof String) {
//			return String.valueOf(o);
//		}
//		throw new NMFormateException("");
//	}
	
	protected static Object getDataInfo(Object o) throws NMFormateException {
		if (o == null) {
			throw new NMFormateException("event data 不能为null");
		}
		return o;
	}

	public static String buildXml(NPMessage message) {
		try {
			return NPXmlStream.marshal(message);
		} catch (XMLStreamException e) {
			Log.error("ARSF INNER 构建异常", e);
			return null;
		}
	}
}
