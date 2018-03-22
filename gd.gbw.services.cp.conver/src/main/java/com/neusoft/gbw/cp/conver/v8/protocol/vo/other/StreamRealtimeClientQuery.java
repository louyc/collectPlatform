package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 52.客户端连接查询
 * @author Administrator
 *
 */
@XStreamAlias("StreamRealtimeClientQuery")
public class StreamRealtimeClientQuery implements IQuery{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
