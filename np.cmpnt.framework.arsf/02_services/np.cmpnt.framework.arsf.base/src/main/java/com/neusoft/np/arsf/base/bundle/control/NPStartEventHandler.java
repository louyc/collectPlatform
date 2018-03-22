package com.neusoft.np.arsf.base.bundle.control;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseContext;
import com.neusoft.np.arsf.base.bundle.BaseContextImpl;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;
import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;

public class NPStartEventHandler implements BaseEventHandler {

	private BaseContext context = BaseContextImpl.getInstance();

	@Override
	public String getTopicName() {
		return NPBaseConstant.EventTopic.START_EVENT_TOPIC;
	}

	@Override
	public boolean processEvent(Object eventData) {
		if (eventData == null) {
			return false;
		}
		try {
			return processEvent(String.valueOf(eventData));
		} catch (XMLStreamException e) {
			Log.error("", e);
		} catch (NMException e) {
			Log.error("", e);
		} catch (Exception e) {
			Log.error("", e);
		}
		return false;
	}

	public boolean processEvent(String eventData) throws NMException, XMLStreamException {
		String bundleSymbolicName = context.getContext().getBundle().getSymbolicName();
		Log.debug("收到ANSWER消息(" + bundleSymbolicName + ")：" + eventData);
		List<NPMessage> messages = NPXmlStream.unmarshal(eventData);
		if (messages == null || messages.size() == 0) {
			return false;
		}
		// 目前NPStartEventHandler只处理单消息
		processEvent(messages.get(0).getData());
		return true;
	}

	/**
	 * 询问：BUNDLE_LIST
	 * 返回：BUNDLE_ANSWER
	 */
	public boolean processEvent(Map<String, String> eventData) throws NMException, XMLStreamException {
		String bundleSymbolicName = context.getContext().getBundle().getSymbolicName();
		String bundleList = eventData.get(NPBaseConstant.Control.BUNDLE_LIST);
		String[] bundles = bundleList.split(NPBaseConstant.Control.BUNDLE_SPLIT);
		if (bundles == null || bundles.length == 0) {
			return false;
		}
		for (String bundleName : bundles) {
			if (bundleName.equals(bundleSymbolicName)) {
				NPMessage message = new NPMessage();
				message.put(NPBaseConstant.Control.BUNDLE_ANSWER, bundleSymbolicName);
				String answer = NPXmlStream.marshal(message);
				Log.info("启动应答：" + bundleSymbolicName + "，消息内容：" + answer);
				ARSFToolkit.sendEvent(NPBaseConstant.EventTopic.START_SERVER_TOPIC, answer);
				return true;
			}
		}
		return false;
	}

}
