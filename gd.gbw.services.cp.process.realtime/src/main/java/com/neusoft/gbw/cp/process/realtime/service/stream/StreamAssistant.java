package com.neusoft.gbw.cp.process.realtime.service.stream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.neusoft.gbw.cp.collect.vo.SiteForceMsg;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.context.StreamTaskMode;
import com.neusoft.gbw.cp.process.realtime.vo.TaskTimeOutInfo;
import com.neusoft.gbw.cp.record.vo.ManufacturersType;
import com.neusoft.gbw.cp.record.vo.OperationType;
import com.neusoft.gbw.cp.record.vo.RecordInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class StreamAssistant {

//	public static boolean saveRecordToDisk(RecordInfo info,String subFilePath,String fileName) {
//		int totalLength = info.getRecordLength();
//		byte[] cutFileByte = info.getCutFileByte();
//		File f = new File(getSaveFilePath(subFilePath, fileName));
//		if (f.exists()) 
//			f.delete();
//		
//		try {
//			f.createNewFile();
//			OutputStream fos = new FileOutputStream(f);
//			OutputStream writer = new BufferedOutputStream(fos);
//			if(info.getPtype().equals("7")) {//如果为V7版本，则增加wav头
//				ByteBuffer bb = ByteBuffer.allocate(44);
//				initWaveHeader(bb, totalLength);
//				writer.write(bb.array());
//			}
//			writer.write(cutFileByte);
//			writer.flush();
//			fos.close();
//			fos = null;
//			writer.close();
//			writer = null;
//		} catch (IOException e) {
//			Log.error("写入文件出现异常：", e);
//			return false;
//		} finally {
//			cutFileByte = null;
//		}
//		return true;
//	}
	
//	 private static void initWaveHeader(ByteBuffer bb,int length) {
//		  	bb.order(ByteOrder.LITTLE_ENDIAN); 
//	    	bb.put((byte)'R');
//	    	bb.put((byte)'I');
//	    	bb.put((byte)'F');
//	    	bb.put((byte)'F');
//	    	bb.putInt(length+32);			// chank length
//	    	bb.put((byte)'W');
//	    	bb.put((byte)'A');
//	    	bb.put((byte)'V');
//	    	bb.put((byte)'E');
//	    	bb.put((byte)'f');
//	    	bb.put((byte)'m');
//	    	bb.put((byte)'t');
//	    	bb.put((byte)' ');
//	    	bb.putInt(16);
//	    	bb.putShort((short)1);
//	    	bb.putShort((short)1);	// channels
//	    	bb.putInt(11025);		// sample rate
//	    	bb.putInt(11025*2);		// avg bytes per second
//	    	bb.putShort((short)2);	// block align
//	    	bb.putShort((short)16);	// bit per sample
//	    	bb.put((byte)'d');
//	    	bb.put((byte)'a');
//	    	bb.put((byte)'t');
//	    	bb.put((byte)'a');
//	    	bb.putInt(length);			// chank2 length
//	    }

//	public static boolean createFilePath(String subFilePath,String fileName) {
//		boolean flag = true;
//		File f = new File(getSaveFilePath(subFilePath,fileName));
//		if (!f.exists()) {
//			flag = f.mkdirs();
//		}
//		return flag;
//	}
  
	public static String getHttpPath(String subFilePath,String recordFileName) {
		//录音储存路径
		String http_path = getBasePlayRecordPath() + subFilePath + "/" + recordFileName;
		return http_path;
	}

	public static String getSaveFilePath(String subFilePath,String recordFileName) {
		//录音储存路径
		String sound_path = getBaseSaveRecordPath() + subFilePath + "\\" + recordFileName;
		return sound_path;
	}
	
	public static String getpath() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		return dateFormat.format(new Date());
	}
	
	public static String getRecordFileName(CollectTask task) {
		String monitorCode = task.getBusTask().getMonitor_code();
		String freq = task.getBusTask().getFreq();
		String file_name = monitorCode + "_" + freq
				+ "_" + getCurrentTime() + ".wav";
		Log.debug("[在线监听]生成监听录音文件路径，file_name=" + file_name);
		return file_name;
	}
	
	public static void startRecording(CollectTask task,String url) {
		RecordInfo info = createRecordInfo(task,url);
		RecordStreamHandler handler = new RecordStreamHandler(task, info);
		Thread thread = new Thread(handler);
		thread.start();
		//缓存录音线程
		String key = task.getBusTask().getFreq() + "_" + url;
		StreamTaskMode.getInstance().putProcess(key, handler);
	}
	
	public static void syntStreamStop(CollectTask task, String url) {
		updateDTO(task);
		task.addExpandObj(ExpandConstants.STREAM_STOP_CONTROL_KEY, url);
		ARSFToolkit.sendEvent(EventServiceTopic.STREAM_TASK_OVER_TOPIC, task);
	}
	
	/**
	 * 创建通信超时定时器对象
	 * @return
	 */
	public static Timer createTransferTimer(final CollectTask task) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				String msgId = ((Query)task.getData().getQuery()).getMsgID() + "";
				TaskTimeOutInfo info = StreamTaskMode.getInstance().getTransferTimeOut(msgId);
				//将采集对象设置超时
				if(info != null) {
					Log.debug("发送通信超时");
					info.setTimeOut(true);
					timeOutDispose(info);
					StreamTaskMode.getInstance().removeTranTimeOut(msgId);
				}
			}
		}, ProcessConstants.TRANSFER_TIME_OUT_TIME);
		return timer;
	}
	
	/**
	 * 创建播音超时定时器对象
	 * @return
	 */
	public static Timer createStreamTimer(final CollectTask task) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				RealTimeStreamDTO dto = getDto(task);
				String url = dto.getUrl();
				TaskTimeOutInfo info = StreamTaskMode.getInstance().getStreamTimeOut(url);
				//将采集对象设置超时
				if(info != null) {
					info.setTimeOut(true);
					streamTimeOutDispose(info);
					StreamTaskMode.getInstance().removeStreamTask(url);
				}
			}
		}, getTimeOut(task));
		return timer;
	}
	
	public static void sendForceMsg(CollectTask task,String stopMarkId, String command) {
 		SiteForceMsg msg = new SiteForceMsg();
		RealTimeStreamDTO dto = getDto(task);
		String equCode = dto.getEquCode();
		String monitorId = dto.getMonitorId();
		msg.setCommand(command);
		msg.setEquCode(equCode);
		msg.setMonitorId(monitorId);
		msg.setStopMarkId(stopMarkId);
		Log.debug("[实时播音录音]发送强制通道请求， monitorId=" + monitorId + ",equCode=" + equCode + ",command=" + command);
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_FORCE_MGS_TOPIC, msg);
	}
	
	public static String getStopMarkId(CollectTask task) {
		String markId = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY) + "";
		return markId;
	}
	
	private static long getTimeOut(CollectTask task) {
		RealTimeStreamDTO dto = getDto(task);
		long time = Long.parseLong(dto.getTimeOut())*1000;
		return time;
	}
	
	private static void streamTimeOutDispose(TaskTimeOutInfo info) {
		CollectTask task = info.getTask();
		RealTimeStreamDTO dto = getDto(task);
		Log.debug("[音频超时]音频超时消息，并发送停止音频操作 ," + getLogContent(task));
		dto.setReturnDesc(ProcessConstants.STREAM_TIME_OUT_DESC);
		dto.setReturnType(ProcessConstants.STREAM_TIME_OUT);
		send(GBWMsgConstant.C_JMS_REAL_STREAM_RESPONSE_MSG,dto);
		//停止录音
		String key = dto.getFreq() + "_" + dto.getUrl();
		boolean isRecordRuning = StreamTaskMode.getInstance().isRecordRuning(key);
		Log.debug("[实时播音录音]判断是否录音， isRecordRuning=" + isRecordRuning);
		if(isRecordRuning) {
			Log.debug("[实时播音录音]停止录音， " + getLogContent(task));
			StreamTaskMode.getInstance().stopRecord(key);
		}else //发送停止音频
			syntStreamStop(task,dto.getUrl());
		//关闭强制通道
		if(dto.getForce().equals(ProcessConstants.IS_FORCE))
			sendForceMsg(task,null, ProcessConstants.STOP_COMMAND);
	}
	
	private static void timeOutDispose(TaskTimeOutInfo info) {
		CollectTask task = info.getTask();
		RealTimeStreamDTO dto = getDto(task);
		dto.setReturnDesc(ProcessConstants.TRANSFER_TIME_OUT_DESC);
		dto.setReturnType(ProcessConstants.TRANSFER_TIME_OUT);
		if(dto.getType().equals(ProcessConstants.STREAM_TYPE)) {
			Log.debug("[通信超时]实时播音任务通信超时 ," + getLogContent(info.getTask()));
			send(GBWMsgConstant.C_JMS_REAL_STREAM_RESPONSE_MSG,dto);
		}else {
			Log.debug("[通信超时]实时录音任务通信超时 ," + getLogContent(info.getTask()));
			send(GBWMsgConstant.C_JMS_REAL_RECORD_RESPONSE_MSG,dto);
		}
		//关闭强制通道
		if(dto.getForce().equals(ProcessConstants.IS_FORCE))
			sendForceMsg(task,null, ProcessConstants.STOP_COMMAND);
	}
	
	private static void updateDTO(CollectTask task) {
		 ((RealTimeStreamDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY)).setCommand(ProcessConstants.STOP_COMMAND);
	}
	
	private static RecordInfo createRecordInfo(CollectTask task,String url) {
		RecordInfo info = new RecordInfo();
		info.setEquCode(task.getAttrInfo().getFirstEquCode());
		int manufacturer_id = task.getBusTask().getManufacturer_id();
		if(manufacturer_id == 2) 
			info.setMantype(ManufacturersType.TS);
		else
			info.setMantype(ManufacturersType.Other);
		info.setMonitorId(task.getBusTask().getMonitor_id() + "");
		info.setOperType(OperationType.mausure);
		info.setPtype(task.getData().getProVersion() + "");
		info.setRecordDuration(getRecordTime(task));
		info.setUrl(url);
		return info;
	}
	
	private static int getRecordTime(CollectTask task) {
		RealTimeStreamDTO dto = getDto(task);
		int time = Integer.parseInt(dto.getTimeOut());
		return time;
	}
	
	private static RealTimeStreamDTO getDto(CollectTask task) {
		RealTimeStreamDTO dto = (RealTimeStreamDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		return dto;
	}
	
	private static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}
	
	private static String getBaseSaveRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_STORE_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.MANUAL_SUBPATH);
		return rootPath + subPath + "\\";
	}
	
	private static String getBasePlayRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_PLAY_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.MANUAL_SUBPATH);
		return rootPath + subPath + "/";
	}
	
	private static void send(int type,RealTimeStreamDTO streamDTO) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(type);
		jms.setObj(streamDTO);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
	}
	
	private static String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
//		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq()+ ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id()+ ",");
		buffer.append("EquCode=" + task.getAttrInfo().getFirstEquCode());
		return buffer.toString();
	}
}
