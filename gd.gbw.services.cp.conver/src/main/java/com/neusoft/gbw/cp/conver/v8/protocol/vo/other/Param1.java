package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 当Type属性为System时存在，表示设备端系统参数子元素
 * @author Administrator
 *
 */
@XStreamAlias("Param")
public class Param1 {
	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;
	@XStreamAsAttribute
	@XStreamAlias("Value")
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
