package com.neusoft.np.arsf.base.bundle.handler;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;

public abstract class XmlMsgHandler extends StringHandler {

	public boolean processStrEvent(String eventData) throws NMException {
		try {
			if (!eventData.startsWith("<")) {
				Log.info("消息为非xml格式，选用其他通道解析");
				return true;
			}
			List<NPMessage> messages = NPXmlStream.unmarshal(eventData);
			for (NPMessage message : messages) {
				processMapEvent(message.getData());
			}
			batchUpdata();
			return true;
		} catch (XMLStreamException e) {
			throw new NMException(e);
		}
	}

	public abstract boolean processMapEvent(Map<String, String> eventData);

}
