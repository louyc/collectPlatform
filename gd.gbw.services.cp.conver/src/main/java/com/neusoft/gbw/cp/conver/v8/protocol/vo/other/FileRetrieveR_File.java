package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 48.获取的文件子元素
 * @author Administrator
 *
 */
@XStreamAlias("File")
public class FileRetrieveR_File {
	@NotNull(message = "FileURL不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="FileURL-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("FileURL")
	private String fileURL;

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	
}
