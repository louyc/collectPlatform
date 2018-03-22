package com.neusoft.gbw.cp.conver.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Return")
public class Return {
	@XStreamAsAttribute
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="type-首尾字符不能为空")
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@NotNull(message = "Value不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="value-首尾字符不能为空")
	@XStreamAlias("Value")
	private String value;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
