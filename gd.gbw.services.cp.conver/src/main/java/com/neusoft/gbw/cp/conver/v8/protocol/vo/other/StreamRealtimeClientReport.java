package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 53.客户端连接查询返回
 * @author Administrator
 *
 */
@XStreamAlias("StreamRealtimeClientReport")
public class StreamRealtimeClientReport implements IReport{
	@Valid
	@XStreamImplicit
	private List<StreamRealtime> streamRealtimes;

	public void addStreamRealtime(StreamRealtime StreamRealtime) {
		if (this.streamRealtimes == null) {
			streamRealtimes = new ArrayList<StreamRealtime>();
		}
		streamRealtimes.add(StreamRealtime);
	}

	public List<StreamRealtime> getStreamRealtime() {
		return streamRealtimes;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

	public List<StreamRealtime> getStreamRealtimes() {
		return streamRealtimes;
	}

	public void setStreamRealtimes(List<StreamRealtime> streamRealtimes) {
		this.streamRealtimes = streamRealtimes;
	}


	

}
