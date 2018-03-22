package com.neusoft.gbw.cp.conver.v6.protocol.vo.audio;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 20.实时音频流子元素      28.视频实时查询返回子元素
 * @author Administrator
 *
 */
@XStreamAlias("MediaStream")
public class MediaStream {
	@NotNull(message = "Index不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Index-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Index")
	private String index;
	@NotNull(message = "URL不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="URL-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("URL")
	private String URL;

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
	
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
