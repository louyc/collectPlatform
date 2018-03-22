package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

import java.util.ArrayList;
import java.util.List;









import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 3.指标报警参数设置 参数 QualityAlarmParamSet子元素
 */
@XStreamAlias("QualityAlarmParam")
public class QualityAlarmParam {

	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@Valid
	@XStreamImplicit
	private List<QualityAlarmParamAlarmParam> alarmParamSets;

	public void addAlarmParamSet(QualityAlarmParamAlarmParam alarmParamSet) {
		if (this.alarmParamSets == null) {
			alarmParamSets = new ArrayList<QualityAlarmParamAlarmParam>();
		}
		alarmParamSets.add(alarmParamSet);
	}

	public List<QualityAlarmParamAlarmParam> getAlarmParamSet() {
		return alarmParamSets;
	}
	
	public String getEquCode() {
		return equCode;
	}
	public List<QualityAlarmParamAlarmParam> getAlarmParamSets() {
		return alarmParamSets;
	}
	public void setAlarmParamSets(List<QualityAlarmParamAlarmParam> alarmParamSets) {
		this.alarmParamSets = alarmParamSets;
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
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
