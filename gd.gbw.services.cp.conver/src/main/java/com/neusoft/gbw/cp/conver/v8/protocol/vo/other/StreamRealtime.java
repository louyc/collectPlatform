package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 53 音频流子元素
 * @author Administrator
 *
 */
@XStreamAlias("StreamRealtime")
public class StreamRealtime {
	@NotNull(message = "EquCode不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EquCode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "URL不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="URL-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("URL")
	private String uRL;
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
	@Valid
	@XStreamImplicit
	private List<ClientInfo> clientInfos;

	public void addClientInfo(ClientInfo ClientInfo) {
		if (this.clientInfos == null) {
			clientInfos = new ArrayList<ClientInfo>();
		}
		clientInfos.add(ClientInfo);
	}

	public List<ClientInfo> getClientInfo() {
		return clientInfos;
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

	public String getuRL() {
		return uRL;
	}

	public void setuRL(String uRL) {
		this.uRL = uRL;
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

	public List<ClientInfo> getClientInfos() {
		return clientInfos;
	}

	
	
}
