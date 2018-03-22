package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.StreamTaskMode;
import com.neusoft.gbw.cp.process.realtime.vo.TaskTimeOutInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;

public class RealRecordTaskHandler extends AbstractStreamTaskProcess{
	
	@Override
	public void disposeForceStart(CollectTask task) {
		//判断是泰顺运营商,推送当前url占用信息
		RealTimeStreamDTO streamDTO = getDto(task);
		if(!checkUrl(streamDTO)) 
			return;
		
		String url = streamDTO.getUrl();
		if(url != null && !url.equals("")) {
			if(isV8TS(task)) {
				streamDTO.setReturnType(ProcessConstants.URL_OCCUPY);
				streamDTO.setReturnDesc(ProcessConstants.URL_OCCUPY_DESC);
	
			}else {//判断不是泰顺,推送当前dto数据
				//开始录音线程
				StreamAssistant.startRecording(task,url);
				//发送前台消息
				streamDTO.setReturnDesc(ProcessConstants.REPORT_SUCCESS_DESC);
				streamDTO.setReturnType(ProcessConstants.REPORT_SUCCESS);
			}
			send(GBWMsgConstant.C_JMS_REAL_RECORD_RESPONSE_MSG,streamDTO);
		}else {
			//获取播音的同步key
			String stopMarkId = null;
			TaskTimeOutInfo info = StreamTaskMode.getInstance().getStreamTimeOut(url);
			if(info != null) {
				CollectTask colTask = info.getTask();
				stopMarkId = StreamAssistant.getStopMarkId(colTask);
			}
			//开始 强制
			StreamAssistant.sendForceMsg(task, stopMarkId, ProcessConstants.START_COMMAND);
			//取消播音超时
			StreamTaskMode.getInstance().cancelStreamOut(url);
			StreamTaskMode.getInstance().removeStreamTask(url);
		}
		
		//记录一个通信超时
		sycnTaskTimeOut(task);
		
	}
	
	@Override
	public void disposeForceStop(CollectTask task) {
		//关闭stop
		String stopMarkId = StreamAssistant.getStopMarkId(task);
		StreamAssistant.sendForceMsg(task,stopMarkId,ProcessConstants.STOP_COMMAND);
		//停止录音功能
		stopRecord(task);
//		StreamAssistant.syntStreamStop(task, url);
	}

	@Override
	public void disposeStart(CollectTask task) {
		//判断是泰顺运营商,推送当前url占用信息
		RealTimeStreamDTO streamDTO = getDto(task);
		if(!checkUrl(streamDTO)) 
			return;
		
		String url = streamDTO.getUrl();
		if(url != null && !url.equals("")) {
			if(isV8TS(task)) {
				streamDTO.setReturnType(ProcessConstants.URL_OCCUPY);
				streamDTO.setReturnDesc(ProcessConstants.URL_OCCUPY_DESC);
			}else {//判断不是泰顺,推送当前dto数据
				//开始录音线程
				StreamAssistant.startRecording(task,url);
				//发送前台消息
				streamDTO.setReturnDesc(ProcessConstants.REPORT_SUCCESS_DESC);
				streamDTO.setReturnType(ProcessConstants.REPORT_SUCCESS);
			}
			send(GBWMsgConstant.C_JMS_REAL_RECORD_RESPONSE_MSG,streamDTO);
		}else {
			//记录一个通信超时
			sycnTaskTimeOut(task);
		}
	}

	@Override
	public void disposeStop(CollectTask task) {
		//停止录音功能
		stopRecord(task);
	}
	
	/**
	 * 目前临时添加这个校验，因为前台可能在点击录音的时候把音频路径传错了
	 * @param streamDTO
	 * @return
	 */
	private boolean checkUrl(RealTimeStreamDTO streamDTO) {
		boolean isFlag = true;
		if(streamDTO != null && streamDTO.getUrl().endsWith(".wav")) {
			//发送前台消息
			streamDTO.setReturnDesc(ProcessConstants.OTHER_EXCEPTION_DESC + ",url=" + streamDTO.getUrl());
			streamDTO.setReturnType(ProcessConstants.OTHER_EXCEPTION);
			send(GBWMsgConstant.C_JMS_REAL_RECORD_RESPONSE_MSG,streamDTO);
			isFlag = false;
		}
		return isFlag;
	}
}
