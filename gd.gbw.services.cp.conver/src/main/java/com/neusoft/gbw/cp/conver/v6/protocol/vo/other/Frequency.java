package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 45.查询的录音文件的频率子元素
 * @author Administrator
 *
 */
@XStreamAlias("Frequency")
public class Frequency {
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String Freq;
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public String getFreq() {
		return Freq;
	}
	public void setFreq(String freq) {
		Freq = freq;
	}
	
}
