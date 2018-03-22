package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 7.指标实时查询
 * @author Administrator
 * 
 */
@XStreamAlias("QualityRealtimeQuery")
public class QualityRealtimeQuery implements IQuery {
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String EquCode;
	@NotNull(message = "Freq不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Freq-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@NotNull(message = "ReportInterval不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="ReportInterval-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ReportInterval")
	private String reportInterval;
	@NotNull(message = "ExpireTime不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="ExpireTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ExpireTime")
	private String expireTime;
	@NotNull(message = "Action不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Action-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Action")
	private String action;
	@Valid
	@XStreamImplicit
	private List<QualityRealtimeQueryQualityIndex> qualityRealtimeQueryQualityIndexs;

	public void addQualityRealtimeQuery_QualityIndex(QualityRealtimeQueryQualityIndex QualityRealtimeQuery_QualityIndex) {
		if (this.qualityRealtimeQueryQualityIndexs == null) {
			qualityRealtimeQueryQualityIndexs = new ArrayList<QualityRealtimeQueryQualityIndex>();
		}
		qualityRealtimeQueryQualityIndexs.add(QualityRealtimeQuery_QualityIndex);
	}

	public List<QualityRealtimeQueryQualityIndex> getQualityRealtimeQueryQualityIndex() {
		return qualityRealtimeQueryQualityIndexs;
	}
	
	

	public String getEquCode() {
		return EquCode;
	}

	public void setEquCode(String equCode) {
		EquCode = equCode;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
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

	public List<QualityRealtimeQueryQualityIndex> getqualityRealtimeQueryQualityIndexs() {
		return qualityRealtimeQueryQualityIndexs;
	}

	public void setqualityRealtimeQueryQualityIndexs(List<QualityRealtimeQueryQualityIndex> qualityRealtimeQueryQualityIndexs) {
		this.qualityRealtimeQueryQualityIndexs = qualityRealtimeQueryQualityIndexs;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
