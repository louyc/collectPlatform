package com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 15.多个包内引用，具体内容待定
 * @author Administrator
 *
 */
@XStreamAlias("Channel")
public class Channel {
	@NotNull(message = "Freq不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Freq-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	
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
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
