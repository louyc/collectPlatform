package com.neusoft.gbw.cp.core.collect;

import com.neusoft.gbw.cp.core.report.ReportData;


/**
 * 上报总线对象
 * @author jh
 *
 */
public class CollectData {
	
	private CollectTask collectTask;
	
	private ReportData data;
	
	private ReportStatus status;
	
	private String collectTime;
	
	public CollectTask getCollectTask() {
		return collectTask;
	}

	public void setCollectTask(CollectTask collectTask) {
		this.collectTask = collectTask;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public ReportData getData() {
		return data;
	}

	public void setData(ReportData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CollectData [collectTask=" + collectTask + ", data=" + data
				+ ", status=" + status + ", collectTime=" + collectTime + "]";
	}
}
