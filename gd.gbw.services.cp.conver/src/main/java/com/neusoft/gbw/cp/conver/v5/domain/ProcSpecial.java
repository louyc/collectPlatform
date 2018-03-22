package com.neusoft.gbw.cp.conver.v5.domain;

import java.util.HashSet;
import java.util.Set;

import org.dom4j.Element;

import com.neusoft.gbw.cp.conver.vo.IQuery;

public class ProcSpecial {

	static Set<String> specialMsg = new HashSet<String>();

	static {
		// specialMsg.add()
	}

	protected static boolean isSpecial(String name) {
		return specialMsg.contains(name);
	}

	protected static IQuery processSpecialQuery(String name, Element element) {
		if ("A".equals(name)) {
			return processSpecialA(element);
		}
		return null;
	}

	protected static IQuery processSpecialA(Element element) {
		return null;
	}

}
