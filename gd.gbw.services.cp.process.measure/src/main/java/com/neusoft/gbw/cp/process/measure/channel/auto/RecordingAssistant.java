package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;

public class RecordingAssistant {
  
	public static String getURL(CollectTask task,String recordFileName) {
		String http_path =null;
		String monitorPath = task.getBusTask().getMonitor_code();
		if(null!=task.getBusTask().getUnit_begin()){
			String startTime = task.getBusTask().getUnit_begin();
			String endTime = task.getBusTask().getUnit_end();
			String fatherPath = getFpath(string2Date(startTime));
			String subPath = getSubPath(startTime,endTime);
			//录音储存路径
			http_path = getBasePlayRecordPath() + fatherPath + "/" + subPath + "/" + monitorPath + "/" + recordFileName;
		}else{
			http_path = getBasePlayRecordPath() +getFpath(new Date())+"/" + monitorPath + "/" + recordFileName;
		}
		return http_path;
	}

	public static String getSaveFilePath(CollectTask task,String recordFileName) {
		String sound_path =null;
		if(null!=task.getBusTask().getUnit_begin()){
			String startTime = task.getBusTask().getUnit_begin();
			String endTime = task.getBusTask().getUnit_end();
			String fatherPath = getFpath(string2Date(startTime));
			String subPath = getSubPath(startTime,endTime);
			String monitorPath = task.getBusTask().getMonitor_code();
			//录音储存路径
			sound_path = getBaseSaveRecordPath() + fatherPath + "\\" + subPath + "\\" + monitorPath + "\\" + recordFileName;
		}else{
			String monitorPath = task.getBusTask().getMonitor_code();
			//录音储存路径
			sound_path = getBaseSaveRecordPath() +getFpath(new Date())+"\\"+ monitorPath + "\\" + recordFileName;
			
		}
		return sound_path;
	}
	
	private static String getBaseSaveRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_STORE_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.MEASURE_SUBPATH);
		return rootPath + subPath + "\\";
	}
	
	private static String getBasePlayRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_PLAY_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.MEASURE_SUBPATH);
		return rootPath + subPath + "/";
	}
	
	public static String getFpath(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		return dateFormat.format(date);
	}
	
	public static String getSubPath(String unit_begin,String unit_end) {
		String startTime = getSubPath(string2Date(unit_begin));
		String endTime = getSubPath(string2Date(unit_end));
		return startTime + "-" + endTime;
	}
	
	public static String getSubPath(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
		dateFormat.setLenient(true);
		return dateFormat.format(date);
	}
	
	public static Date string2Date(String date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//指定日期/时间解析是否不严格 lenient - 为 true 时，解析过程是不严格的
			dateFormat.setLenient(true);
			return dateFormat.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
}
