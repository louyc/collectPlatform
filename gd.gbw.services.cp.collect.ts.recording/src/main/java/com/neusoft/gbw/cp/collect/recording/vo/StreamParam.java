package com.neusoft.gbw.cp.collect.recording.vo;

public class StreamParam {
	
	private String id;
	private String freq;			   //频率
	private String srcCode;			   //任务来源（来源于那个处理服务）
	private String monitorCode;		   //设备编码
	private String equCode;			   //接收机code
	private String url;				   //音频地址
	private String savePath;		   //存储路径
	private String fileName;		   //存储文件名称
	private int recordLength;		   //录音时长
	private int status;		   	       //是停止录音  0:start 1:stop
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMonitorCode() {
		return monitorCode;
	}
	public void setMonitorCode(String monitorCode) {
		this.monitorCode = monitorCode;
	}
	public int getRecordLength() {
		return recordLength;
	}
	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSrcCode() {
		return srcCode;
	}
	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "StreamParam [id=" + id + ", freq=" + freq + ", srcCode="
				+ srcCode + ", monitorCode=" + monitorCode + ", recordLength="
				+ recordLength + ", url=" + url + ", equCode=" + equCode
				+ ", status=" + status + "]";
	}
}
