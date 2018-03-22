package com.neusoft.gbw.cp.conver.v5.protocol.vo.offset;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 频偏收测数据子元素
 * @author Administrator
 *
 */
@XStreamAlias("OffsetIndex")
public class OffsetIndex {
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
	@NotNull(message = "Offset不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Offset-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Offset")
	private String offset;
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
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
