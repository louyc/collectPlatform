package com.neusoft.gbw.cp.process.measure.channel.online;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;

public class RecordingAssistant {
	
 	public static String getURL(String recordFileName) {
		String fatherPath = getpath();
		String http_path = getBasePlayRecordPath() + fatherPath  + "/" + recordFileName;
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
}
