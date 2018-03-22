package com.neusoft.gbw.cp.process.inspect.service;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.inspect.exception.InspectDisposeException;
import com.neusoft.gbw.cp.process.inspect.process.ITaskProcess;
import com.neusoft.gbw.cp.process.inspect.process.InspectTypeExecutor;
import com.neusoft.gbw.cp.process.inspect.vo.InspectType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class InspectDataDisposeHandler extends NMService{
	
	private InspectDataDisposeMgr channel = null;
	
	

	public InspectDataDisposeHandler(InspectDataDisposeMgr channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		CollectData data = null;
		while(isThreadRunning()) {
			try {
				data = channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			
			CollectTask task =  data.getCollectTask();
			if(task == null)
				continue;
			
			BusinessTaskType btype = data.getCollectTask().getBusTask().getType();
			if(!isInpectTask(btype))
				continue;
			//协议数据
			ProtocolData pdata = data.getCollectTask().getData();
			if(pdata == null){
				Log.warn("巡检服务为找到该任务的协议数据类型，" + getLogContent(data));
				continue;
			}
			//协议类型
		    ProtocolType type = pdata.getType();
		    if(type == null) {
		    	Log.warn("巡检服务为找到该任务的协议类型，" + getLogContent(data)); 
		    	continue;
		    }
		    
		    InspectType inType =  getInspectType(data.getCollectTask());
		    if(inType == null) {
		    	Log.warn("未找到巡检任务对应的巡检处理类型，" + getLogContent(data)); 
		    	continue;
		    }
		    
		    ITaskProcess executor = channel.getInspectDispose(inType.name());
			if (executor == null) {
				Log.warn("巡检任务处理通道没有指定名称的处理类。Name=" + inType.name());
				continue;
			}
			
			int version = data.getCollectTask().getData().getProVersion();
			if (version == 0) {
				Log.warn("任务处理通道数据没有对应的版本类型，可能是上报数据");
				continue;
			}
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
			} catch (InspectDisposeException e) {
				Log.error(this.getClass().getName()+"报错 版本解析", e);
			}
		}
	}
	/**
	 * 判断巡检类型 是 true ，否 false
	 * @param btype
	 * @return
	 */
	private boolean isInpectTask(BusinessTaskType btype) {
		return BusinessTaskType.measure_inspect.equals(btype);
	}
	
	private InspectType getInspectType(CollectTask task) {
		return InspectTypeExecutor.getInspectType(task);
	}
	
	private String getLogContent(CollectData date) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TaskID=" + date.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + date.getCollectTask().getBusTask().getFreq() + ",");
		buffer.append("MonitorID=" + date.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + date.getCollectTask().getBusTask().getMonitor_code() + ",");
		buffer.append("problemDate=" + getReturnValue(date));
		return buffer.toString();
	}
	
	private String getReturnValue(CollectData data) {
		String type = null;
		String desc = null;
		if(data.getData() == null || data.getData().getReportData() == null) 
			return "type=" + type + ";desc=" + desc;
		type = ((Report) data.getData().getReportData()).getReportReturn().getType();
		desc = ((Report) data.getData().getReportData()).getReportReturn().getDesc();
		return "type=" + type + ";desc=" + desc;
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
}
