package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 27.	视频实时查询
 * @author Administrator
 *
 */
@XStreamAlias("VideoRealtimeQuery")
public class VideoRealtimeQuery implements IQuery{
	@Valid
	@XStreamImplicit
	private List<VideoRealtimeStream> videoRealtimeStreams;
	
	public void addVideoRealtimeStream(VideoRealtimeStream videoRealtimeStream) {
		if (this.videoRealtimeStreams == null) {
			videoRealtimeStreams = new ArrayList<VideoRealtimeStream>();
		}
		videoRealtimeStreams.add(videoRealtimeStream);
	}

	
	public List<VideoRealtimeStream> getVideoRealtimeStreams() {
		return videoRealtimeStreams;
	}


	public void setVideoRealtimeStreams(
			List<VideoRealtimeStream> videoRealtimeStreams) {
		this.videoRealtimeStreams = videoRealtimeStreams;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
