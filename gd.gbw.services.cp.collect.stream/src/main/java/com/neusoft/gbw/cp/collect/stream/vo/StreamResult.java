package com.neusoft.gbw.cp.collect.stream.vo;

public class StreamResult {
	
	private String id;				   //任务返回的标识ID
	private String freq;			   //频率
	private String srcCode;			   //任务来源（来源于那个处理服务）
	private String monitorCode;		   //设备编码
	private String equCode;			   //接收机code
	
	private int collectStatus;      //采集状态，1：采集成功,2：获取音频流失败，3：采集文件生成失败
	private int recordLength;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getSrcCode() {
		return srcCode;
	}
	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	public String getMonitorCode() {
		return monitorCode;
	}
	public void setMonitorCode(String monitorCode) {
		this.monitorCode = monitorCode;
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public int getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(int collectStatus) {
		this.collectStatus = collectStatus;
	}
	public int getRecordLength() {
		return recordLength;
	}
	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}
	@Override
	public String toString() {
		return "StreamResult [id=" + id + ", freq=" + freq + ", srcCode="
				+ srcCode + ", monitorCode=" + monitorCode + ", equCode="
				+ equCode + ", collectStatus=" + collectStatus
				+ ", recordLength=" + recordLength + "]";
	}
}
