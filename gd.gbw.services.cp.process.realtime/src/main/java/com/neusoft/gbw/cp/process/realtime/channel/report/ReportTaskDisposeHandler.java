package com.neusoft.gbw.cp.process.realtime.channel.report;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.report.ReportData;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.channel.ReportIdentityTypeExecutor;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.ReportType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class ReportTaskDisposeHandler extends NMService {
	
	private ReportTaskChannel channel = null;
	
	public ReportTaskDisposeHandler(ReportTaskChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		CollectData data = null;
		while(isThreadRunning()) {
			try {
				data = (CollectData)channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			ReportData reportData = data.getData();
			
			if(reportData == null) {
				Log.warn("实时上报任务处理通道没有指定的上报数据。");
				continue;
			}
			
			Report report = (Report)data.getData().getReportData();
			
			int version = Integer.parseInt(report.getVersion());
			if (version == 0) {
				Log.warn("任务处理通道数据没有对应的版本类型，可能是上报数据");
				continue;
			}
			
			ReportType type = getRepoetType(report,version);
			if (type==null) {
				Log.warn("实时上报任务处理通道没有指定名称的处理类。type=" + type);
				continue;
			}
			ITaskProcess executor = channel.getRealtimeDispose(type.name());
			if (executor == null) {
				Log.warn("实时上报任务处理通道没有指定名称的处理类。Name=" + type.name());
				continue;
			}
//			int version = Integer.parseInt(report.getVersion());
			
//			CollectTask task = data.getCollectTask();
//			ReportType type = getRepoetType(task);
//			ITaskProcess executor = channel.getRealtimeDispose(type.name());
//			if (executor == null) {
//				Log.warn("实时任务处理通道没有指定名称的处理类。Name=" + type.name());
//			}
//			
//			int version = data.getCollectTask().getData().getProVersion();
			try {
				switch(version) {
				case 8:
					executor.disposeV8(data);
					break;
				case 7:
					executor.disposeV7(data);
					break;
				case 6:
					executor.disposeV6(data);
					break;
				case 5:
					executor.disposeV5(data);
					break;
				}
			} catch (RealtimeDisposeException e) {
				Log.error("", e);
			}
//			Object syntObj = executor.dispose(data);
//			if (syntObj == null) {
//				Log.warn("处理数据为空，处理服务名称=" + type.name());
//			}
		}
	}
	
	private ReportType getRepoetType(Report report, int version) {
		ReportType type = null;
		switch(version) {
		case 8:
			type = ReportIdentityTypeExecutor.identityReportType(report);
			break;
		case 7:
			type = ReportIdentityTypeExecutor.identityReportTypeV7(report);
			break;
		case 6:
			type = ReportIdentityTypeExecutor.identityReportTypeV6(report);
			break;
		case 5:
			type = ReportIdentityTypeExecutor.identityReportTypeV5(report);
			break;
		default:
			break;
		}
		return type;
	}
	
//	private ReportType getRepoetType(CollectTask task) {
//		return IdentityTypeExecutor.identityReportType(task);
//	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}

}
