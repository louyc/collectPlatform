package com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 3.指标报警参数设置 参数 AlarmParam 子元素
 */
@XStreamAlias("Param")
public class Param {
	@NotNull(message = "Name不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Name-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;
	@NotNull(message = "Value不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Value-首尾字符不能为空")
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
