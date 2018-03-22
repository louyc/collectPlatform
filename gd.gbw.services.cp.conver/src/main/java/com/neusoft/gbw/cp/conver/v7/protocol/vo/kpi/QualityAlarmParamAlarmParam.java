package com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi;

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
 * 3.指标报警参数设置 参数  QualityAlarmParam 子元素
 */
@XStreamAlias("AlarmParam")
public class QualityAlarmParamAlarmParam {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
	@Valid
	@XStreamImplicit
	private List<Param> params;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void addParam(Param param) {
		if (this.params == null) {
			params = new ArrayList<Param>();
		}
		params.add(param);
	}

	public List<Param> getParam() {
		return params;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
