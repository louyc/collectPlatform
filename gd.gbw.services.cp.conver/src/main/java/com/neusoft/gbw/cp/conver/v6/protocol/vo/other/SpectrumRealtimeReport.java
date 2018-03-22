package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

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
 *  14.	频谱实时查询返回
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumRealtimeReport")
public class SpectrumRealtimeReport implements IReport {
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@Valid
	@XStreamImplicit
	private List<SpectrumRealtimeReportSpectrumScan> spectrumScans;

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

	public List<SpectrumRealtimeReportSpectrumScan> getSpectrumScans() {
		return spectrumScans;
	}

	public void setSpectrumScans(List<SpectrumRealtimeReportSpectrumScan> spectrumScans) {
		this.spectrumScans = spectrumScans;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
