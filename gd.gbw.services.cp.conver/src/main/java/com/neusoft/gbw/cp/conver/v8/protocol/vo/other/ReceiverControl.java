package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 39.	接收机控制
 * @author Administrator
 *
 */
@XStreamAlias("ReceiverControl")
public class ReceiverControl implements IQuery {
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "Action不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Action-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Action")
	private String action;
	@NotNull(message = "ExpireTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="ExpireTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ExpireTime")
	private String expireTime;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
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
