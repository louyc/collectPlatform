package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 34.设备具体参数子元素
 * @author Administrator
 *
 */
@XStreamAlias("Parameter")
public class Parameter {
	@NotNull(message = "ParammeterType不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="ParammeterType-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ParammeterType")
	private String parammeterType;
	@NotNull(message = "Value不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Value-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Value")
	private String value;

	public String getParammeterType() {
		return parammeterType;
	}
	public void setParammeterType(String parammeterType) {
		this.parammeterType = parammeterType;
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
