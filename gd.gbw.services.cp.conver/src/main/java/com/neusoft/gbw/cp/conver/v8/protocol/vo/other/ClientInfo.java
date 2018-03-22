package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 53.连接音频流的客户端子元素
 * @author Administrator
 *
 */
@XStreamAlias("ClientInfo")
public class ClientInfo {
	@NotNull(message = "IP不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="IP-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("IP")
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
