package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.util.List;

import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordDispatchTaskHandler extends NMService {

	private AutoTaskChannel channel = null;
	
	public RecordDispatchTaskHandler(AutoTaskChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		CollectData data = null;
		ReportStatus status = null;
		int measureStatus = 0;
		String statusDesc = null;
		while (isThreadRunning()) {
			try {
				data = channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			
			//过滤stop音频任务
			if(isFilterStopStream(data)) {
				Log.info("[录音功能]该音频为停止音频任务，不做操作, " + getLogMsg(data));
				//清除缓存
				clear(data);
				continue;
			}
			
			//获取采集数据状态，确定状态类型，将失败原因写入数据库，并将任务发送到采集平台进行补采
			status = getReportStatus(data);
			measureStatus = getStatus(data);
			Log.debug(status+"采集状态status：：    "+measureStatus+"收测状态measureStatus：："
					+"   "+data.getCollectTask().getBusTask().getTask_id()
					+"  "+data.getCollectTask().getBusTask().getMonitor_id()+"   "+
					data.getCollectTask().getBusTask().getMonitor_code());
			//失败 true 成功 false
			if(isFailureStat(status)) {
				//判断漏采为第一次漏采，还是补采，漏采则发送给采集进行补采，如果是补采则将漏采原因封装入库
				if(measureStatus == 1) {
//					Log.debug("[录音功能]收测单元补采，" + getLogMsg(data));
					Log.debug("[录音功能]收测单元漏采补采，" + getLogMsg(data));
					sendTask(data.getCollectTask());
				}
				else {
					statusDesc = getStatDesc(status);
					Log.debug("二次补采"+data.getCollectTask().getBusTask().getTask_id()
							+"  "+data.getCollectTask().getBusTask().getMonitor_id()+"   "+
							data.getCollectTask().getBusTask().getMonitor_code()
							+"状态：：：："+statusDesc);
					channel.storeLeakageReason(data, statusDesc);
//					channel.storeSafeAssessData(data);
//					syntStreamStop(data,""); //默认如果返回结果失败，则发送stop释放采集状态
					clear(data);
				}
				//清除缓存
				continue;
			}
			
			//校验返回数据标签（url是否完成） 
			if(!isStartStream(data)) {
				//记为漏采数据
				//如果是第一次采集，则发回采集服务进行第二次采集，如果第二次采集还是漏采，则确定为音频漏采
				if(measureStatus == 1) {
					sendTask(data.getCollectTask());
					continue;
				}
				
				channel.storeLeakageReason(data, ProcessConstants.LeakageReason.COLLECT_URL_ANALYTICAL_FAILURE);
//				channel.storeSafeAssessData(data);
				syntStreamStop(data,""); //默认如果返回结果失败，则发送stop释放采集状态
				continue;
			}
			//创建录音线程
			channel.createRecordHandler(data);
		}
	}
	
	private boolean isFilterStopStream(CollectData collectData) {
		boolean flag = false;
		int version  = collectData.getCollectTask().getData().getProVersion();
		switch (version) {
		case 8:
			flag = isStopStreamV8(collectData);
			break;
		case 7:
			flag = isStopStreamV7(collectData);
			break;
		case 6:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV6(collectData);
			flag = false;
			break;
		case 5:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV5(collectData);
			flag = false;
			break;
		}
		return flag;
	}
	
	

	private boolean isStartStream(CollectData collectData) {
		boolean flag = true;
		int version  = collectData.getCollectTask().getData().getProVersion();
		switch (version) {
		case 8:
			flag = isStreamV8(collectData);
			break;
		case 7:
			flag = isStreamV7(collectData);
			break;
		case 6:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV6(collectData);
			flag = false;
			break;
		case 5:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV5(collectData);
			flag = false;
			break;
		}
		return flag;
	}

	private boolean isStreamV8(CollectData collectData) {
		boolean flag = true;
		try{
			if ((com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport() == null) {
				String value = ((Report) collectData.getData().getReportData()).getReportReturn().getValue();
				String desc = ((Report)  collectData.getData().getReportData()).getReportReturn().getDesc();
				Log.info("[录音功能]返回路径异常," + getLogMsg(collectData)+" 失败原因:" +desc + " 失败代码:"+value);
				flag = false;
			}else{
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream>) ((com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport()).getMediaStreams();
				if(msList != null  && !msList.isEmpty()) {
					String url = msList.get(0).getURL();
					if(url == null) {
						Log.info("[录音功能]未找到音频路径," + getLogMsg(collectData));
						flag = false;
					}
				}
			}
		}catch(Exception e) {
			Log.info("*************解析协议音频url失败**********"+e);
			flag = false;
		}
		return flag;
	}
	
	private boolean isStreamV7(CollectData collectData) {
		boolean flag = true;
		try{
			if ((com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport() == null) {
				String value = ((Report) collectData.getData().getReportData()).getReportReturn().getValue();
				String desc = ((Report)  collectData.getData().getReportData()).getReportReturn().getDesc();
				Log.info("[录音功能]返回路径异常," + getLogMsg(collectData)+" 失败原因:" +desc + " 失败代码:"+value);
				flag = false;
			}else{
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream>) ((com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport()).getMediaStreams();
				if(msList != null  && !msList.isEmpty()) {
					String url = msList.get(0).getURL();
					if(url == null) {
						Log.info("[录音功能]未找到音频路径 ," + getLogMsg(collectData));
						flag = false;
					}
				}
			}
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private boolean isStopStreamV8(CollectData collectData) {
		boolean flag = false;
		try{
			Query query = (Query)collectData.getCollectTask().getData().getQuery();
			com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery stream = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			String command = stream.getRealtimeStreams().get(0).getAction();
			if(command.equalsIgnoreCase("stop"))
				flag = true;
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private boolean isStopStreamV7(CollectData collectData) {
		boolean flag = false;
		try{
			Query query = (Query)collectData.getCollectTask().getData().getQuery();
			com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery stream = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			String command = stream.getRealtimeStreams().get(0).getAction();
			
			if(command.equalsIgnoreCase("stop")) 
				flag = true;
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private boolean isFailureStat(ReportStatus status) {
		boolean isStatus = false;
		switch(status) {
		case date_collect_success:
			break;
		default:
			isStatus = true;
			break;
		}
		return isStatus;
	}
	
	private ReportStatus getReportStatus(CollectData data) {
		return data.getStatus();
	}
	
	private String getStatDesc(ReportStatus status) {
		String desc = null;
		switch(status) {
		case date_collect_success:
			desc = ProcessConstants.LeakageReason.DATE_COLLECT_SUCCESS;
			break;
		case date_collect_time_out:
			desc = ProcessConstants.LeakageReason.DATE_COLLECT_TIME_OUT;
			break;
		case date_collect_error:
			desc = ProcessConstants.LeakageReason.DATE_COLLECT_ERROR;
			break;
		case data_collect_null:
			desc = ProcessConstants.LeakageReason.DATE_COLLECT_ERROR;
			break;
		case date_collect_active_report:
			desc = ProcessConstants.LeakageReason.DATE_COLLECT_ACTIVE_REPORT;
			break;
		case date_collect_no_found_task:
			desc = ProcessConstants.LeakageReason.DATE_COLLECT_NO_FOUND_TASK;
			break;
//		case date_collect_occupy:
//			desc = ProcessConstants.LeakageReason.DATE_COLLECT_TIME_OUT;
//			break;
		case collect_task_analytical_failure:
			desc = ProcessConstants.LeakageReason.TRANSFER_BASE_INFO_VALIDATE_FAILURE;
			break;
		case collect_task_send_failure:
			desc = ProcessConstants.LeakageReason.COLLECT_TASK_SEND_FAILURE;
			break;
		case transfer_base_info_validate_failure:
			desc = ProcessConstants.LeakageReason.TRANSFER_BASE_INFO_VALIDATE_FAILURE;
			break;
		default:
			break;
		}
		return desc;
	}
	
	private void syntStreamStop(CollectData collectData, String url) {
		CollectTask task = collectData.getCollectTask();
		task.addExpandObj(ExpandConstants.STREAM_STOP_CONTROL_KEY, url);
		ARSFToolkit.sendEvent(EventServiceTopic.STREAM_TASK_OVER_TOPIC, task);
		Log.debug("[录音服务]重定向启动关闭当前录音的URL任务，URL=" + url + "," + getLogMsg(collectData));
	}
	
	private void sendTask(CollectTask task){
		task.getBusTask().setMeasure_status(2);//修改补采状态
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("taskId=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("monitorId=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + data.getCollectTask().getBusTask().getMonitor_code()+ ",");
		buffer.append("Freq=" + data.getCollectTask().getBusTask().getFreq() + ",");
		return buffer.toString();
	}
	
	private void clear(CollectData data) {
		if (data == null) 
			return;
		CollectTask task = data.getCollectTask();
		Log.debug("本次收测单元的CollectTask回收内存。CollectTaskID=" + task.getCollectTaskID());
		task = null;
		data = null;
	}
	
	private int getStatus(CollectData data) {
		return data.getCollectTask().getBusTask().getMeasure_status();
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
