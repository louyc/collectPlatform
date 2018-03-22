package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.StreamTaskMode;
import com.neusoft.gbw.cp.process.realtime.vo.TaskTimeOutInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.Log;

public class RealStreamTaskHandler extends AbstractStreamTaskProcess{

	@Override
	public void disposeForceStart(CollectTask task) {
		//判断是泰顺运营商,推送当前url占用信息
		RealTimeStreamDTO streamDTO = getDto(task);
		String url = streamDTO.getUrl();
		String freq = streamDTO.getFreq();
		if(url != null && !url.equals("")) {
			if(isV8TS(task)) {
				streamDTO.setReturnType(ProcessConstants.URL_OCCUPY);
				streamDTO.setReturnDesc(ProcessConstants.URL_OCCUPY_DESC);
				//停止录音功能
				String key = freq + "_" + url;
				boolean isRecordRuning = StreamTaskMode.getInstance().isRecordRuning(key);
				if(isRecordRuning) {
					TaskTimeOutInfo info = StreamTaskMode.getInstance().getStreamTimeOut(key);
					CollectTask streamTask = info.getTask();
					stopRecord(streamTask);
				}
			}else {//判断不是泰顺,推送当前dto数据
				//发送前台消息
				streamDTO.setReturnDesc(ProcessConstants.REPORT_SUCCESS_DESC);
				streamDTO.setReturnType(ProcessConstants.REPORT_SUCCESS);
			}
			send(GBWMsgConstant.C_JMS_REAL_RECORD_RESPONSE_MSG,streamDTO);
		}else {
			//开始 强制
			StreamAssistant.sendForceMsg(task,null,ProcessConstants.START_COMMAND);
		}
		//记录一个通信超时
		sycnTaskTimeOut(task);
				
	}

	@Override
	public void disposeForceStop(CollectTask task) {
		//关闭stop
		String stopMarkId = StreamAssistant.getStopMarkId(task);
		StreamAssistant.sendForceMsg(task,stopMarkId,ProcessConstants.STOP_COMMAND);
		//取消播音音超时
		RealTimeStreamDTO dto = getDto(task);
		String url = dto.getLastUrl();
		String freq = dto.getFreq();
		StreamTaskMode.getInstance().cancelStreamOut(url);
		StreamTaskMode.getInstance().removeStreamTask(url);
		//停止录音录音
		String key = freq + "_" + url;
		boolean isRecordRuning = StreamTaskMode.getInstance().isRecordRuning(key);
		Log.debug("[实时播音录音]判断是否录音， isRecordRuning=" + isRecordRuning);
		if(isRecordRuning) {
			Log.info("[实时播音录音]取消音频超时， " + getLogContent(task));
			StreamTaskMode.getInstance().stopRecord(key);
		}
//		StreamAssistant.syntStreamStop(task, url);
	}

	@Override
	public void disposeStart(CollectTask task) {
		//判断是泰顺运营商,推送当前url占用信息
		RealTimeStreamDTO streamDTO = getDto(task);
		String url = streamDTO.getUrl();
		if(url != null && !url.equals("")) {
			//是否为泰顺
			if(isV8TS(task)) {
				streamDTO.setReturnType(ProcessConstants.URL_OCCUPY);
				streamDTO.setReturnDesc(ProcessConstants.URL_OCCUPY_DESC);
			}else {//判断不是泰顺,推送当前dto数据
				streamDTO.setReturnType(ProcessConstants.REPORT_SUCCESS);
				streamDTO.setReturnDesc(ProcessConstants.REPORT_SUCCESS_DESC);
				streamDTO.setStopMarkId(StreamAssistant.getStopMarkId(task));
				//记录播放超时
				sycnStreamTimeOut(task);
			}
			send(GBWMsgConstant.C_JMS_REAL_STREAM_RESPONSE_MSG,streamDTO);
		}else {
			//记录一个通信超时
			sycnTaskTimeOut(task);
			
		}
	}

	@Override
	public void disposeStop(CollectTask task) {
		//取消播音音超时
		RealTimeStreamDTO dto = getDto(task);
		
		String url = dto.getLastUrl();
		String freq = dto.getFreq();
		Log.debug("[实时播音录音]取消音频播放超时， freq=" + freq + ",url=" + url);
		StreamTaskMode.getInstance().cancelStreamOut(url);
		StreamTaskMode.getInstance().removeStreamTask(url);
		//停止录音录音
		String key = freq + "_" + url;
		boolean isRecordRuning = StreamTaskMode.getInstance().isRecordRuning(key);
		Log.debug("[实时播音录音]判断是否录音， isRecordRuning=" + isRecordRuning);
		if(isRecordRuning) {
			Log.info("[实时播音录音]停止录音， " + getLogContent(task));
			StreamTaskMode.getInstance().stopRecord(key);
		}
	}
	
	private String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
//		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq()+ ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id()+ ",");
		buffer.append("EquCode=" + task.getAttrInfo().getFirstEquCode());
		return buffer.toString();
	}
}
