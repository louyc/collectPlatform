package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 47.文件获取
 * @author Administrator
 *
 */
@XStreamAlias("FileRetrieve")
public class FileRetrieve implements IQuery {
	@Valid
	@XStreamImplicit
	private List<FileRetrieve_File> fileRetrieve_Files;

	public void addFileRetrieve_File(FileRetrieve_File FileRetrieve_File) {
		if (this.fileRetrieve_Files == null) {
			fileRetrieve_Files = new ArrayList<FileRetrieve_File>();
		}
		fileRetrieve_Files.add(FileRetrieve_File);
	}

	public List<FileRetrieve_File> getFileRetrieve_File() {
		return fileRetrieve_Files;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
