package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

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
 * 35.设备初始化参数设备子元素
 * @author Administrator
 *(^\S*)(\S$) regexp="(^\S*)(\S$)"
 */
@XStreamAlias("Equipment")
public class InitEquipment {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
//	@NotNull(message = "EquCode不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@Valid
	@XStreamImplicit
	private List<Center> centers;
	@Valid
	@XStreamImplicit
	private List<LogInfo> logInfos;
	@Valid
	@XStreamImplicit
	private List<Param> params;

	//	@XStreamImplicit
	//	private List<Param> param2s;

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

	public List<Center> getCenters() {
		return centers;
	}

	public void setCenters(List<Center> centers) {
		this.centers = centers;
	}

	public List<LogInfo> getLogInfos() {
		return logInfos;
	}

	public void setLogInfos(List<LogInfo> logInfos) {
		this.logInfos = logInfos;
	}

	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
