package com.neusoft.np.arsf.base.bundle.control;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;
import com.neusoft.np.arsf.base.bundle.NPStartControler;
import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;

public class NPStartCtrlHandler implements BaseEventHandler {

	@Override
	public String getTopicName() {
		return NPBaseConstant.EventTopic.START_SERVER_TOPIC;
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
		List<NPMessage> messages = NPXmlStream.unmarshal(eventData);
		if (messages == null || messages.size() == 0) {
			return false;
		}
		// 目前NPStartEventHandler只处理单消息
		processEvent(messages.get(0).getData());
		return true;
	}

	public boolean processEvent(Map<String, String> eventData) throws NMException, XMLStreamException {
		String bundleName = eventData.get(NPBaseConstant.Control.BUNDLE_ANSWER);
		if (bundleName == null || "".equals(bundleName)) {
			return false;
		}
		NPStartControler.getInstance().addAnswerNeuBundle(bundleName);
		return true;
	}
}
