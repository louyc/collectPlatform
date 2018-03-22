package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 8.指标实时查询主动上报
 * @author Administrator
 *
 */
@XStreamAlias("QualityRealtimeReport")
public class QualityRealtimeReport implements IReport{
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@Valid
	@XStreamImplicit
	private List<QualityRealtimeReportQuality> qualityRealtimeReport_Qualitys;

	public void addQualityRealtimeReport_Quality(QualityRealtimeReportQuality QualityRealtimeReport_Quality) {
		if (this.qualityRealtimeReport_Qualitys == null) {
			qualityRealtimeReport_Qualitys = new ArrayList<QualityRealtimeReportQuality>();
		}
		qualityRealtimeReport_Qualitys.add(QualityRealtimeReport_Quality);
	}

	public List<QualityRealtimeReportQuality> getQualityRealtimeReport_Quality() {
		return qualityRealtimeReport_Qualitys;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	public List<QualityRealtimeReportQuality> getQualityRealtimeReport_Qualitys() {
		return qualityRealtimeReport_Qualitys;
	}

	public void setQualityRealtimeReport_Qualitys(
			List<QualityRealtimeReportQuality> qualityRealtimeReport_Qualitys) {
		this.qualityRealtimeReport_Qualitys = qualityRealtimeReport_Qualitys;
	}
	
}
