package com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi;

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
 * 8.实时指标收测数据子元素
 * @author Administrator
 *
 */
@XStreamAlias("Quality")
public class QualityRealtimeReportQuality {
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@NotNull(message = "Freq不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Freq-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@NotNull(message = "CheckDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="CheckDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("CheckDateTime")
	private String checkDateTime;
	@Valid
	@XStreamImplicit
	private List<QualityRealtimeReportQualityQualityIndex> qualityRealtimeReport_Quality_QualityIndexs;

	public void addQualityRealtimeReport_Quality_QualityIndex(
			QualityRealtimeReportQualityQualityIndex QualityRealtimeReport_Quality_QualityIndex) {
		if (this.qualityRealtimeReport_Quality_QualityIndexs == null) {
			qualityRealtimeReport_Quality_QualityIndexs = new ArrayList<QualityRealtimeReportQualityQualityIndex>();
		}
		qualityRealtimeReport_Quality_QualityIndexs.add(QualityRealtimeReport_Quality_QualityIndex);
	}

	public List<QualityRealtimeReportQualityQualityIndex> getQualityRealtimeReport_Quality_QualityIndex() {
		return qualityRealtimeReport_Quality_QualityIndexs;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
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

	public String getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(String checkDateTime) {
		this.checkDateTime = checkDateTime;
	}

	public List<QualityRealtimeReportQualityQualityIndex> getQualityRealtimeReport_Quality_QualityIndexs() {
		return qualityRealtimeReport_Quality_QualityIndexs;
	}

	public void setQualityRealtimeReport_Quality_QualityIndexs(
			List<QualityRealtimeReportQualityQualityIndex> qualityRealtimeReport_Quality_QualityIndexs) {
		this.qualityRealtimeReport_Quality_QualityIndexs = qualityRealtimeReport_Quality_QualityIndexs;
	}

}
