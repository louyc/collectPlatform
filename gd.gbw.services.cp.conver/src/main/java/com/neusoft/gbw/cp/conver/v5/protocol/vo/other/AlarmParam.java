package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;


import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 31.设备报警参数子元素
 * @author Administrator
 *
 */
@XStreamAlias("AlarmParam")
public class AlarmParam {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
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
