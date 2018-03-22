package com.neusoft.gbw.cp.collect.service.build;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.core.report.ReportData;

public class ReportDataBuilder {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 构建采集回收数据
	 * @param task 任务对象
	 * @param recoveryReport 回收数据对象
	 * @param status  返回数据状态
	 */
	public static CollectData buildRecoveryData(CollectTask task, Report recoveryReport, ReportStatus status) {
		//构建返回数据对象
		CollectData collectReport = new CollectData();
		collectReport.setCollectTask(task);;
		ReportData data = new ReportData();
		if (null != recoveryReport) {
			data.setReportData(recoveryReport);
			data.setVersionId(Integer.parseInt(((Report)recoveryReport).getVersion()));
		}
		collectReport.setData(data);
		collectReport.setStatus(status);
		collectReport.setCollectTime(sdf.format(new Date()));
		return collectReport;
	}
	
	/**
	 * 构建发送失败数据
	 * @param task 任务数据
	 * @param recoveryDateList  返回数据
	 */
	public static CollectData buildTaskFailureData(CollectTask task, ReportStatus status) {
		//构建返回数据对象
		CollectData collectReport = new CollectData();
		collectReport.setCollectTask(task);
		collectReport.setData(null);
		collectReport.setStatus(status);
		collectReport.setCollectTime(sdf.format(new Date()));
		return collectReport;
	}

}
