package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 40.	接收机控制上报
 * @author Administrator
 *
 */
@XStreamAlias("ReceiverControlReport")
public class ReceiverControlReport implements IReport{
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "URL不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="URL-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("URL")
	private String URL;
	@Valid
	@XStreamAsAttribute
	@XStreamAlias("Param")
	private ReceiverParam param;
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public ReceiverParam getParam() {
		return param;
	}
	public void setParam(ReceiverParam param) {
		this.param = param;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
