package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 *  14.频谱扫描收测结果子元素
 * @author Administrator
 *
 */
@XStreamAlias("ScanResult")
public class ScanResult {
	@NotNull(message = "Freq不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Freq-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@NotNull(message = "Level不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Level-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Level")
	private String level;
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
