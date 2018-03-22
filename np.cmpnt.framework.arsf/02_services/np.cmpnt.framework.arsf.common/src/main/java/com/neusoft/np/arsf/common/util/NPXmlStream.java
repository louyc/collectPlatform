package com.neusoft.np.arsf.common.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: NP消息，XML处理工具类<br>
 * 创建日期: 2013年8月27日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年8月27日       黄守凯        创建
 * </pre>
 */
public final class NPXmlStream {

	private final static String SYNC_TYPE = "syncType";

	private final static String SERIAL = "serial";

	private final static QName keyQName = new QName("key");
	private final static QName valueQName = new QName("value");

	public static List<NPMessage> unmarshal(String messages) throws XMLStreamException {
		final StringReader stringReader = new StringReader(messages);
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		final XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(stringReader);
		List<NPMessage> messageList = new ArrayList<NPMessage>();

		XMLEvent event = null;
		NPMessage message = null;
		while (xmlEventReader.hasNext()) {
			event = xmlEventReader.nextEvent();
			if (event.isStartElement()) {
				final StartElement startElement = event.asStartElement();
				String lableName = startElement.getName().getLocalPart();
				if ("message".equals(lableName)) {
					message = new NPMessage();
				} else if ("property".equals(lableName)) {
					if (message == null) {
						throw new IllegalArgumentException("[NPXmlStream ERROR]消息格式错误，无法解析" + messages);
					}
					final String key = startElement.getAttributeByName(keyQName).getValue();
					final String value = startElement.getAttributeByName(valueQName).getValue();
					if (checkParameter(key, value)) {
						if (SYNC_TYPE.equals(key)) {
							message.setSyncType(value);
						} else if (SERIAL.equals(key)) {
							message.setSerial(value);
						} else {
							message.put(key, value);
						}
					}
				}
			} else if (event.isEndElement()) {
				final EndElement endElement = event.asEndElement();
				String lableName = endElement.getName().getLocalPart();
				if ("message".equals(lableName)) {
					messageList.add(message);
				}
			}
		}
		return messageList;
	}

	public static String marshal(NPMessage message) throws XMLStreamException {
		List<NPMessage> messages = new ArrayList<NPMessage>();
		messages.add(message);
		return marshal(messages);
	}

	public static String marshal(List<NPMessage> messages) throws XMLStreamException {
		if (!checkCollection(messages)) {
			throw new IllegalArgumentException("[NPXmlStream ERROR]无消息数据");
		}
		XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XMLStreamWriter xmlw = null;
		try {
			xmlw = xmlof.createXMLStreamWriter(outputStream);
			xmlw.writeStartDocument();
			xmlw.writeStartElement("messages");
			for (NPMessage message : messages) {
				marshalMessage(xmlw, message);
			}
			xmlw.writeEndElement();
			xmlw.writeEndDocument();
		} finally {
			if (xmlw != null) {
				xmlw.close();
			}
		}
		return outputStream.toString();
	}

	private static void marshalMessage(XMLStreamWriter xmlw, NPMessage message) throws XMLStreamException {
		xmlw.writeStartElement("message");
		writeProperty(xmlw, SYNC_TYPE, message.getSyncType());
		writeProperty(xmlw, SERIAL, message.getSerial());
		Iterator<Entry<String, String>> mapIter = message.iterator();
		while (mapIter.hasNext()) {
			Entry<String, String> entry = mapIter.next();
			writeProperty(xmlw, entry.getKey(), entry.getValue());
		}
		xmlw.writeEndElement();

	}

	private static void writeProperty(XMLStreamWriter xmlw, String key, String value) throws XMLStreamException {
		if (checkString(value)) {
			xmlw.writeStartElement("property");
			xmlw.writeAttribute("key", key);
			xmlw.writeAttribute("value", value);
			xmlw.writeEndElement();
		}
	}

	private static boolean checkCollection(Collection<?> collection) {
		if (collection == null || collection.size() == 0) {
			return false;
		}
		return true;
	}

	private static boolean checkString(String str) {
		if (str == null || "".equals(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证多个String类型字符串是否为null或者""，不带异常，有返回值
	 */
	private static boolean checkParameter(String... argument) {
		for (String elem : argument) {
			if (elem == null || "".equals(elem)) {
				return false;
			}
		}
		return true;
	}
}
