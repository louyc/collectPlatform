package com.neusoft.gbw.cp.collect.stream.service;

import java.util.Map;

import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;

public class ConverUtils {

	public static String converString(StreamParam stream) throws NMBeanUtilsException {
		Map<String, String> attrs = NMBeanUtils.getObjectFieldStr(stream);
		return NPJsonUtil.mapToJson(attrs);
	}
}
