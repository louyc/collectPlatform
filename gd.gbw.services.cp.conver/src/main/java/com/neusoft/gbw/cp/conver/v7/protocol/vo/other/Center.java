package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

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
 * 35.设备端上报中心系统参数子元素
 * @author Administrator
 *
 */
@XStreamAlias("Center")
public class Center {
	@NotNull(message = "SrcCode不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="SrcCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SrcCode")
	private String srcCode;
	@Valid
	@XStreamImplicit
	private List<Param> params;

	public String getSrcCode() {
		return srcCode;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
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
