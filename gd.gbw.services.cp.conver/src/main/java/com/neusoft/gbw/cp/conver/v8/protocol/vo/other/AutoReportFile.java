package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 51.自动上报文件 上行消息
 * @author Administrator
 *
 */
@XStreamAlias("AutoReportFile")
public class AutoReportFile implements IReport {
	@NotNull(message = "URL不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="URL-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("URL")
	private String URL;

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
