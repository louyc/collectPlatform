package com.neusoft.gbw.cp.conver.v7.protocol.vo.audio;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 20.	音频实时查询返回
 * @author Administrator
 *
 */
@XStreamAlias("StreamRealtimeQueryR")
public class StreamRealtimeQueryR implements IReport{
	@Valid
	@XStreamImplicit
	private List<MediaStream> mediaStreams;
	
	public void addMediaStream(MediaStream mediaStream) {
		if (this.mediaStreams == null) {
			mediaStreams = new ArrayList<MediaStream>();
		}
		mediaStreams.add(mediaStream);
	}

	public List<MediaStream> getMediaStreams() {
		return mediaStreams;
	}

	public void setMediaStreams(List<MediaStream> mediaStreams) {
		this.mediaStreams = mediaStreams;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
