package com.neusoft.gbw.cp.conver.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;


public class Query {
	@NotNull(message = "version不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="version-首尾字符不能为空")
	private String version;
	@NotNull(message = "msgID不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="msgID-首尾字符不能为空")
	private String msgID;
	@NotNull(message = "type不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="type-首尾字符不能为空")
	private String type;
	private String dateTime;
	@NotNull(message = "srcCode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="srcCode-首尾字符不能为空")
	private String srcCode;
	@NotNull(message = "dstCode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="dstCode-首尾字符不能为空")
	private String dstCode;
	@NotNull(message = "priority不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="priority-首尾字符不能为空")
	private String priority;
	@Valid
	private IQuery query;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getSrcCode() {
		return srcCode;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}

	public String getDstCode() {
		return dstCode;
	}

	public void setDstCode(String dstCode) {
		this.dstCode = dstCode;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

	public IQuery getQuery() {
		return query;
	}

	public void setQuery(IQuery query) {
		this.query = query;
	}
}
