package com.neusoft.np.arsf.net.rest.domain.msg;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.common.util.NPMessage;
import com.neusoft.np.arsf.common.util.NPXmlStream;
import com.neusoft.np.arsf.net.rest.domain.vo.SyntVO;

public class SyntMsgDecode {

	public static SyntVO decode(String task) throws MsgDecodeException {
		try {
			Map<String, String> attrs = NPJsonUtil.jsonToMap(task);
			return mapToVo(attrs);
		} catch (NMFormateException e) {
			throw new MsgDecodeException("JSON处理格式异常：" + task, e);
		}
	}

	public static SyntVO mapToVo(Map<String, String> attrs) throws MsgDecodeException {
		try {
			SyntVO vo = new SyntVO();
			NMBeanUtils.createObject(vo, attrs);
			return vo;
		} catch (NMBeanUtilsException e) {
			throw new MsgDecodeException("解析对象处理格式异常：" + attrs, e);
		}
	}

	public static String voToXml(SyntVO vo) throws MsgDecodeException {
		try {
			Map<String, String> attrs = NMBeanUtils.getObjectFieldStr(vo);
			NPMessage msg = new NPMessage(attrs);
			return NPXmlStream.marshal(msg);
		} catch (NMBeanUtilsException e) {
			throw new MsgDecodeException("封装xml处理格式异常：" + vo);
		} catch (XMLStreamException e) {
			throw new MsgDecodeException("封装xml处理格式异常：" + vo);
		}
	}

}
