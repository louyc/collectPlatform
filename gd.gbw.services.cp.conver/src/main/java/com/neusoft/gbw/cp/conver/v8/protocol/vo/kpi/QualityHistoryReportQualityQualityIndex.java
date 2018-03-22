package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 6. QualityHistoryReport>Quality 上报的指标收测数据类型子元素 Type Desc Value MinValue MaxValue
 * @author Administrator
 *
 */
@XStreamAlias("QualityIndex")
public class QualityHistoryReportQualityQualityIndex {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
//	@NotNull(message = "Value不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("Value")
	private String value;
//	@NotNull(message = "MinValue不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("MinValue")
	private String minValue;
//	@NotNull(message = "MaxValue不能为空!")
	@XStreamAsAttribute
	@XStreamAlias("MaxValue")
	private String maxValue;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
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
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
