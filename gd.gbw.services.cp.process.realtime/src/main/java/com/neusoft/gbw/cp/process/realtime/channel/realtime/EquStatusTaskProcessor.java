package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquStatusTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}
	
	private void dispatchData(CollectData data) {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]设备状态查询任务处理异常，，任务执行状态：" + status + "," + getLogMsg(data));
			break;
		}
		sendTask(obj);
	}

	private Object dispose(CollectData data) {
		if(!checkReport(data)) {
			return null;
		}
		return null;
	}
	
	private boolean checkReport(CollectData data) {
		Object report =  data.getData().getReportData();
		if(report == null) {
			Log.debug("设备状态DTO对象构建流程# 返回数据report为空");
			return false;
		}

		Object is_sucess =  ((Report) report).getReportReturn().getValue();
		if(is_sucess == null || !is_sucess.toString().equals("0")) {
			Log.debug("设备状态DTO对象构建流程# 返回数据状态错误： 状态value=" + is_sucess + "," + getLogMsg(data));
			return false;
		}
		return true;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}
}
