package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 54.中断客户端音频连接
 * @author Administrator
 *
 */
@XStreamAlias("StreamRealtimeClientStop")
public class StreamRealtimeClientStop implements IQuery{
	@Valid
	@XStreamImplicit
	private List<StreamRealtimeClientStopClientInfo> streamRealtimeClientStop_ClientInfos;

	public void addStreamRealtimeClientStop_ClientInfo(StreamRealtimeClientStopClientInfo StreamRealtimeClientStop_ClientInfo) {
		if (this.streamRealtimeClientStop_ClientInfos == null) {
			streamRealtimeClientStop_ClientInfos = new ArrayList<StreamRealtimeClientStopClientInfo>();
		}
		streamRealtimeClientStop_ClientInfos.add(StreamRealtimeClientStop_ClientInfo);
	}

	public List<StreamRealtimeClientStopClientInfo> getStreamRealtimeClientStop_ClientInfo() {
		return streamRealtimeClientStop_ClientInfos;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
