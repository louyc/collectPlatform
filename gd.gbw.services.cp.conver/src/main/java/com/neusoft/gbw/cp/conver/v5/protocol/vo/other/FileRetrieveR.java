package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 48.获取文件返回
 * @author Administrator
 *
 */
@XStreamAlias("FileRetrieveR")
public class FileRetrieveR implements IReport{
	@Valid
	@XStreamImplicit
	private List<FileRetrieveR_File> fileRetrieveR_Files;

	public void addFileRetrieveR_File(FileRetrieveR_File FileRetrieveR_File) {
		if (this.fileRetrieveR_Files == null) {
			fileRetrieveR_Files = new ArrayList<FileRetrieveR_File>();
		}
		fileRetrieveR_Files.add(FileRetrieveR_File);
	}

	public List<FileRetrieveR_File> getFileRetrieveR_File() {
		return fileRetrieveR_Files;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
