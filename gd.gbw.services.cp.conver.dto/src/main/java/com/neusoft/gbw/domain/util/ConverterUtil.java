package com.neusoft.gbw.domain.util;

import java.io.StringReader;

import com.neusoft.gbw.domain.exception.NXmlException;
import com.thoughtworks.xstream.XStream;

public class ConverterUtil {

	public static Object xmlToObj(String xmlName) throws NXmlException {
		XStream xstream = XStreamFactory.getXStream();
		if (xstream == null) {
			throw new NXmlException(xmlName + "不存在");
		}
		StringReader strReader2 = new StringReader(xmlName);
		Object obj = xstream.fromXML(strReader2);
		return obj;

	}

	public static String objToXml(Object obj) {
		XStream xstream = XStreamFactory.getXStream();
		String xmlstring = xstream.toXML(obj);
		return xmlstring;

	}
}
