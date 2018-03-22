package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

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
 * 2.指标报警历史查询返回  参数
 * @author Administrator
 *
 */
@XStreamAlias("QualityAlarm")
public class QualityAlarm {
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "AlarmID不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="AlarmID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("AlarmID")
	private String alarmID;
	@NotNull(message = "Mode不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Mode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Mode")
	private String mode;
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@NotNull(message = "Freq不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Freq-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
	@XStreamAsAttribute
	@XStreamAlias("Reason")
	private String reason;
	@NotNull(message = "CheckDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("CheckDateTime")
	private String checkDateTime;
	@Valid
	@XStreamImplicit
	private List<AlarmParam> alarmParams;

	public void addAlarmParam(AlarmParam alarmParam) {
		if (this.alarmParams == null) {
			alarmParams = new ArrayList<AlarmParam>();
		}
		alarmParams.add(alarmParam);
	}

	public List<AlarmParam> getAlarmParam() {
		return alarmParams;
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

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
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

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
