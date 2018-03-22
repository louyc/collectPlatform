package com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 *  13.	频谱实时查询
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumRealtimeQuery")
public class SpectrumRealtimeQuery implements IQuery{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	
	@XStreamAsAttribute
	@XStreamAlias("StartFreq")
	private String startFreq;
	
	@XStreamAsAttribute
	@XStreamAlias("EndFreq")
	private String endFreq;
	
	@XStreamAsAttribute
	@XStreamAlias("StepFreq")
	private String stepFreq;
	@NotNull(message = "ReportInterval不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("ReportInterval")
	private String reportInterval;
	@NotNull(message = "ExpireTime不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("ExpireTime")
	private String expireTime;
	@NotNull(message = "Action不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("Action")
	private String action;
	
	
	
		
	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getStartFreq() {
		return startFreq;
	}

	public void setStartFreq(String startFreq) {
		this.startFreq = startFreq;
	}

	public String getEndFreq() {
		return endFreq;
	}

	public void setEndFreq(String endFreq) {
		this.endFreq = endFreq;
	}

	public String getStepFreq() {
		return stepFreq;
	}

	public void setStepFreq(String stepFreq) {
		this.stepFreq = stepFreq;
	}



	public String getReportInterval() {
		return reportInterval;
	}

	public void setReportInterval(String reportInterval) {
		this.reportInterval = reportInterval;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
