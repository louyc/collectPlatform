package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 38.设备日志子元素
 * @author Administrator
 *
 */
@XStreamAlias("LogItem")
public class LogItem {
	@NotNull(message = "DateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="DateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("DateTime")
	private String dateTime;
	@NotNull(message = "Name不能为空!")
//	@Pattern(regexp="^[^\\s]+$",message="Name-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;

	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
