package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 34.各个设备实时状态子元素
 * @author Administrator
 *
 */
@XStreamAlias("Equipment")
public class RealtimeStatusEquipment {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "EquStatus不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EquStatus-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquStatus")
	private String equStatus;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
	@Valid
	@XStreamImplicit
	private List<Parameter> parameters;
	
	public void addParameter(Parameter parameter) {
		if (this.parameters == null) {
			parameters = new ArrayList<Parameter>();
		}
		parameters.add(parameter);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	public String getEquStatus() {
		return equStatus;
	}

	public void setEquStatus(String equStatus) {
		this.equStatus = equStatus;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
