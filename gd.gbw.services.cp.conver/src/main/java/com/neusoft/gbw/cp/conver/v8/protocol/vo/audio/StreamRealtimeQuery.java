package com.neusoft.gbw.cp.conver.v8.protocol.vo.audio;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 19.	音频实时查询
 * @author Administrator
 *
 */
@XStreamAlias("StreamRealtimeQuery")
public class StreamRealtimeQuery implements IQuery{
	@Valid
	@XStreamImplicit
	private List<RealtimeStream> realtimeStreams;
	
	public void addRealtimeStream(RealtimeStream realtimeStream) {
		if (this.realtimeStreams == null) {
			realtimeStreams = new ArrayList<RealtimeStream>();
		}
		realtimeStreams.add(realtimeStream);
	}
	
	public List<RealtimeStream> getRealtimeStreams() {
		return realtimeStreams;
	}


	public void setRealtimeStreams(List<RealtimeStream> realtimeStreams) {
		this.realtimeStreams = realtimeStreams;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
