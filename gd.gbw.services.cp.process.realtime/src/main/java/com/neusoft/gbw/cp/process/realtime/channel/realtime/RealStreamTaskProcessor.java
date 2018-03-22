package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.util.List;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.RealtimeStream;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.StreamTaskMode;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.service.stream.StreamAssistant;
import com.neusoft.gbw.cp.process.realtime.vo.TaskTimeOutInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.Log;

public class RealStreamTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
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
		case date_collect_occupy:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时音频]实时音频任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
		Log.debug("[实时音频]实时音频任务处理消息发送至前台，msg=" + obj);
		if(obj != null)
			sendTask(obj);
	}
	
	private Object dispose(CollectData data) {
		Object obj = null;
		String url = null;
		RealTimeStreamDTO streamDTO = getDto(data);
		//如果是stop关闭音频数据
		String command = streamDTO.getCommand();
		//音频操作类型   0：音频、1：录音
		String operType = streamDTO.getType();
		ReportStatus status = data.getStatus();
		streamDTO.setEquCode(getEquCode(data));
		streamDTO.setCurrentTime(data.getCollectTask().getBusTask().getCurrentTime());
		switch(status) {
		case date_collect_success:
			Report report = (Report)  data.getData().getReportData();
			Return info = report.getReportReturn();
			int type = Integer.parseInt(info.getValue());
			switch(type) {
			case 0://成功
				streamDTO.setReturnDesc(ProcessConstants.REPORT_SUCCESS_DESC);
				streamDTO.setReturnType(ProcessConstants.REPORT_SUCCESS);
				streamDTO.setStopMarkId(getStopMarkId(data));
				if(ProcessConstants.START_COMMAND.equals(command)) {
					 url = getStartUrl(data);
				}else {
					url = getStopUrl(data);
				}
				streamDTO.setUrl(url);
				//如果是stop关闭音频超时数据，如果为start则开启音频超时
				if(ProcessConstants.STOP_COMMAND.equals(command)) {
					if(operType.equals(ProcessConstants.IS_RECORD_TYPE)) 
						streamDTO.setCommand(ProcessConstants.STOP_COMMAND);
					StreamTaskMode.getInstance().cancelStreamOut(url);
					StreamTaskMode.getInstance().removeStreamTask(url);
				}else if(ProcessConstants.START_COMMAND.equals(command) && operType.equals(ProcessConstants.IS_STREAM_TYPE))
					syntStreamTimeOut(data);
				 else if(ProcessConstants.START_COMMAND.equals(command) && operType.equals(ProcessConstants.IS_RECORD_TYPE))
					StreamAssistant.startRecording(data.getCollectTask(),url);
				break;
			default://失败
				streamDTO.setStopMarkId(getStopMarkId(data));
				streamDTO.setReturnDesc(info.getDesc());
//				streamDTO.setCommand(getAction(data.getCollectTask()));
				streamDTO.setReturnType(ProcessConstants.OTHER_EXCEPTION);
				if(ProcessConstants.START_COMMAND.equals(command)) 
					StreamAssistant.syntStreamStop(data.getCollectTask(),"");
				
				break;
			}
			break;
		case date_collect_occupy:
			streamDTO.setEquCode(getEquCode(data));
			streamDTO.setReturnType(ProcessConstants.OTHER_OCCUPY);
			streamDTO.setReturnDesc(ProcessConstants.OTHER_OCCUPY_DESC);
			break;
		default:
			break;
		}
		//如果没有超时，并且是start任务，则通知前台
		boolean is_tranOut = StreamTaskMode.getInstance().isTransferTimeOut(getMsgId(data));
		if(is_tranOut || ProcessConstants.STOP_COMMAND.equals(command)) 
			return null;
		//取消通信超时
		StreamTaskMode.getInstance().cancelTransferOut(getMsgId(data));
		obj = createJmsDto(streamDTO);
		return obj;
	}
	
	private Object disposeError(CollectData data) {
		Object obj = null;
		RealTimeStreamDTO streamDTO = getDto(data);
		//如果是stop关闭音频数据
		String command = streamDTO.getCommand();
		
		streamDTO.setReturnDesc(ProcessConstants.TASK_SEND_ERROR_DESC);
		streamDTO.setReturnType(ProcessConstants.OTHER_EXCEPTION);
		//实时音频播放stop  下发失败后  不再下发  （如果站点连通不好  一直上行 下发任务）
//		StreamAssistant.syntStreamStop(data.getCollectTask(),"");

		boolean is_tranOut = StreamTaskMode.getInstance().isTransferTimeOut(getMsgId(data));
		if(is_tranOut || ProcessConstants.STOP_COMMAND.equals(command)) 
			return null;
		//取消通信超时,如果通信超时
		StreamTaskMode.getInstance().cancelTransferOut(getMsgId(data));
		obj = createJmsDto(streamDTO);
		return obj;
	}
	
	private String getStopMarkId(CollectData data) {
		String markId = data.getCollectTask().getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY) + "";
		return markId;
	}
	
	private RealTimeStreamDTO getDto(CollectData data) {
		RealTimeStreamDTO dto = (RealTimeStreamDTO)data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		return dto;
	}
	
	private JMSDTO createJmsDto(RealTimeStreamDTO realDTO) {
		JMSDTO dto = new JMSDTO();
		dto.setTypeId(GBWMsgConstant.C_JMS_REAL_STREAM_RESPONSE_MSG);
		dto.setObj(realDTO);
		return dto;
	}
	
	private String getStartUrl(CollectData data) {
		String url = null;
		int version = data.getData().getVersionId();
		switch(version) {
		case 8:
			url = getV8URL(data);
			break;
		case 7:
			url = getV7URL(data);
			break;
		case 6:
			url = getV6URL(data);
			break;
		case 5:
			url = getV5URL(data);
			break;
		}
		return url;
	}
	
	private String getStopUrl(CollectData data) {
		String url = null;
		int version = data.getData().getVersionId();
		switch(version) {
		case 8:
			url = getStopV8URL(data);
			break;
		case 7:
			url = getStopV7URL(data);
			break;
		case 6:
			url = getStopV6URL(data);
			break;
		case 5:
			url = getStopV5URL(data);
			break;
		}
		return url;
	}
	
	private String getV8URL(CollectData data) {
		String url = null;
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR  streamRealtimeQueryR = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR)report.getReport();
		if(streamRealtimeQueryR == null) {
			 return url;
		}
		Object obj = streamRealtimeQueryR.getMediaStreams();
		if(obj == null) {
			 return url;
		}
		List<MediaStream> msList = (List<MediaStream>) streamRealtimeQueryR.getMediaStreams();
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getURL();
		}
		return url;
	}
	
	private String getStopV8URL(CollectData data) {
		String url = null;
		Query query = (Query) data.getCollectTask().getData().getQuery();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery  streamRealtimeQuery = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
		if(streamRealtimeQuery == null) {
			 return url;
		}
		Object obj = streamRealtimeQuery.getRealtimeStreams();
		if(obj == null) {
			 return url;
		}
		List<RealtimeStream> msList = (List<RealtimeStream>) (streamRealtimeQuery.getRealtimeStreams());
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getLastURL();
		}
		return url;
	}
	
	private String getV7URL(CollectData data) {
		String url = null;
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR  streamRealtimeQueryR = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR)report.getReport();
		if(streamRealtimeQueryR == null) {
			 return url;
		}
		Object obj = streamRealtimeQueryR.getMediaStreams();
		if(obj == null) {
			 return url;
		}
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream>)streamRealtimeQueryR.getMediaStreams();
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getURL();
		}
		return url;
	}
	
	private String getStopV7URL(CollectData data) {
		String url = null;
		Query query = (Query) data.getCollectTask().getData().getQuery();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery  streamRealtimeQuery = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
		if(streamRealtimeQuery == null) {
			 return url;
		}
		Object obj = streamRealtimeQuery.getRealtimeStreams();
		if(obj == null) {
			 return url;
		}
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.RealtimeStream> msList = (List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.RealtimeStream>) (streamRealtimeQuery.getRealtimeStreams());
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getLastURL();
		}
		return url;
	}
	
	private String getV5URL(CollectData data) {
		String url = null;
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQueryR  streamRealtimeQueryR = (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQueryR)report.getReport();
		if(streamRealtimeQueryR == null) {
			 return url;
		}
		Object obj = streamRealtimeQueryR.getMediaStreams();
		if(obj == null) {
			 return url;
		}
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.MediaStream>)streamRealtimeQueryR.getMediaStreams();
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getURL();
		}
		return url;
	}
	
	private String getStopV5URL(CollectData data) {
		String url = null;
		Query query = (Query) data.getCollectTask().getData().getQuery();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQuery  streamRealtimeQuery = (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
		if(streamRealtimeQuery == null) {
			 return url;
		}
		Object obj = streamRealtimeQuery.getRealtimeStreams();
		if(obj == null) {
			 return url;
		}
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.RealtimeStream> msList = (List<com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.RealtimeStream>) (streamRealtimeQuery.getRealtimeStreams());
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getLastURL();
		}
		return url;
	}
	
	private String getStopV6URL(CollectData data) {
		String url = null;
		Query query = (Query) data.getCollectTask().getData().getQuery();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQuery  streamRealtimeQuery = (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
		if(streamRealtimeQuery == null) {
			 return url;
		}
		Object obj = streamRealtimeQuery.getRealtimeStreams();
		if(obj == null) {
			 return url;
		}
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.RealtimeStream> msList = (List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.RealtimeStream>) (streamRealtimeQuery.getRealtimeStreams());
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getLastURL();
		}
		return url;
	}
	
	@SuppressWarnings("unchecked")
	private String getV6URL(CollectData data) {
		String url = null;
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQueryR  streamRealtimeQueryR = (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQueryR)report.getReport();
		if(streamRealtimeQueryR == null) {
			 return url;
		}
		Object obj = streamRealtimeQueryR.getMediaStreams();
		if(obj == null) {
			 return url;
		}
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.MediaStream>)obj;
		if(msList != null  && !msList.isEmpty()) {
			 url = msList.get(0).getURL();
		}
		return url;
	}
	
	private String getEquCode(CollectData data) {
		String encode = data.getCollectTask().getAttrInfo().getFirstEquCode();
		return encode;
	}
	
	private String getMsgId(CollectData data) {
		String msgId = ((Query)data.getCollectTask().getData().getQuery()).getMsgID() + "";
		return msgId;
	}
	
	private void syntStreamTimeOut(CollectData data) {
		
		RealTimeStreamDTO dto = getDto(data);
		String url = dto.getUrl();
		StreamTaskMode.getInstance().cancelStreamOut(url);
		StreamTaskMode.getInstance().removeStreamTask(url);
		TaskTimeOutInfo info = new TaskTimeOutInfo();
		info.setTask(data.getCollectTask());
		info.setTimer(StreamAssistant.createStreamTimer(data.getCollectTask()));
		StreamTaskMode.getInstance().putStream(url, info);
		Log.debug("[实时播音录音]创建实时播音定时器，url=" + url);
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}
	
}
