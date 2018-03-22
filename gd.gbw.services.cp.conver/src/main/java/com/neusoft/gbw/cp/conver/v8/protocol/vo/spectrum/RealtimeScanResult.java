package com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 频谱实时扫描结果子元素
 * @author Administrator
 *
 */
@XStreamAlias("RealtimeScanResult")
public class RealtimeScanResult {
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
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
