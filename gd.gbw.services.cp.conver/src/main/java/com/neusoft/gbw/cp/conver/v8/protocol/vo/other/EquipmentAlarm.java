package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

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
 * 30.设备报警条目子元素
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentAlarm")
public class EquipmentAlarm {
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "AlarmID不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="AlarmID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("AlarmID")
	private String alarmID;
	@NotNull(message = "Mode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Mode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Mode")
	private String mode;
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
	@NotNull(message = "Reason不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Reason-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Reason")
	private String reason;
	@NotNull(message = "CheckDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="CheckDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("CheckDateTime")
	private String checkDateTime;
	@Valid
	@XStreamImplicit
	private List<Param> params;
	
	public void addParam(Param param) {
		if (this.params == null) {
			params = new ArrayList<Param>();
		}
		 params.add(param);
	}

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	public String getAlarmID() {
		return alarmID;
	}

	public void setAlarmID(String alarmID) {
		this.alarmID = alarmID;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(String checkDateTime) {
		this.checkDateTime = checkDateTime;
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
