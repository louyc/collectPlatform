package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 54.中断客户端或接收机子元素
 * @author Administrator
 *
 */
@XStreamAlias("ClientInfo")
public class StreamRealtimeClientStopClientInfo {
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("ClientIP")
	private String clientIP;
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
