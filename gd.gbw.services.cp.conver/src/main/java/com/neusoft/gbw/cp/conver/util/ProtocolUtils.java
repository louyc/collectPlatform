package com.neusoft.gbw.cp.conver.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolUtils {

	public static int getProtocolVersion(String protcolXML) {
		int versionNum = 8;
		String regex = "Version=.{3}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(protcolXML);
		m.find();
		String src = m.group();
		versionNum = Integer.parseInt(src.substring(9, 10));
		return versionNum;
	}
}
