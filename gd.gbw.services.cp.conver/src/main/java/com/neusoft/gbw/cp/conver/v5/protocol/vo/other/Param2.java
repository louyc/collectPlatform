package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 当Type属性不为System时存在，表示设备端各个接收机、摄像头等设备的参数子元素
 * @author Administrator
 *
 */
@XStreamAlias("Param")
public class Param2 {
	@XStreamAsAttribute
	@XStreamAlias("Value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
