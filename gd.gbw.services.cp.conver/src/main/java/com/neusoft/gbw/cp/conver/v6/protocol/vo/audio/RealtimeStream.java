package com.neusoft.gbw.cp.conver.v6.protocol.vo.audio;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 19.	音频实时查询 子元素
 * @author Administrator
 *
 */
@XStreamAlias("RealtimeStream")
public class RealtimeStream {
	@NotNull(message = "Index不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Index-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Index")
	private String index;
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("LastURL")
	private String lastURL;
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
	@NotNull(message = "Bps不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Bps-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Bps")
	private String bps;
	@XStreamAsAttribute
	@XStreamAlias("Encode")
	private String encode;
	@NotNull(message = "Action不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Action-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Action")
	private String action;
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	public String getLastURL() {
		return lastURL;
	}

	public void setLastURL(String lastURL) {
		this.lastURL = lastURL;
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

	public String getBps() {
		return bps;
	}

	public void setBps(String bps) {
		this.bps = bps;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
