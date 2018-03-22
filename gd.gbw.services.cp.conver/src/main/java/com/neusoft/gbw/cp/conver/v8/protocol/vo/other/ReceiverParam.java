package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 39.接收机返回数据子元素
 * 40.接收机控制命令子元素
 * @author Administrator
 *
 */
@XStreamAlias("Param")
public class ReceiverParam {
	@XStreamAsAttribute
	@XStreamAlias("Value")
	private String Value;

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
