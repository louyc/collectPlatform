package com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 7. QualityRealtimeQuery 实时指标收测数据类型子元素     Type Desc SampleNumber
 * @author Administrator
 *
 */
@XStreamAlias("QualityIndex")
public class QualityRealtimeQueryQualityIndex {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
	@NotNull(message = "SampleNumber不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="SampleNumber-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SampleNumber")
	private String sampleNumber;
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
	public String getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
