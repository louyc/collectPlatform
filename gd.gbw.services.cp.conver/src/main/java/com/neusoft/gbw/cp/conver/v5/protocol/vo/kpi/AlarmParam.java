package com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 2.指标报警历史查询返回  参数
 */
@XStreamAlias("AlarmParam")
public class AlarmParam {
	@NotNull(message = "Name不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Name-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;
	@NotNull(message = "Value不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Value-首尾字符不能为空")
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
