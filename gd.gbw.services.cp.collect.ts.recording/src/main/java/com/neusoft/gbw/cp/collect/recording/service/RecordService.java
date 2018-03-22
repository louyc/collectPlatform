package com.neusoft.gbw.cp.collect.recording.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.collect.recording.constant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.service.control.RecordControlMgr;
import com.neusoft.gbw.cp.collect.recording.service.transfer.RecordListenerMgr;
import com.neusoft.gbw.cp.collect.recording.utils.RecordUtils;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordService {

	private RecordListenerMgr listenMgr = null;
	private RecordControlMgr controlMgr = null;

	public void init() {
		//加载配置文件
		new ConfigLoader().loadConfig();
	}

	public void start() {
		try {
			deleteThread();
		} catch (Exception e) {
			Log.debug("删除txt文件中线程id报错",e);
		}
		listenMgr = new RecordListenerMgr();
		listenMgr.openRecordingListener();//启动音频采集数据任务接收线程

		controlMgr = new RecordControlMgr();
		controlMgr.init();//初始化v7录音处理Handler
		controlMgr.open();//创建录音控制线程
		RecordUtils.writeStatus();//写入录音线程记录
	}

	/**
	 * 根据文件里的进程号 杀掉进程
	 * @param 
	 * @throws IOException
	 */
	public static void deleteThread() throws IOException{
		Log.debug("********写文件前关闭老进程start*********");
		String path = ConfigVariable.RECORD_STATUS_FILE_PATH;
		String command =null;
		String command1 = null;
		String deleteMonitorCode =null;
		int i=0;
		Map<String,String> map = new HashMap<String,String>();
		try{
			String value = RecordUtils.readFile(path);
			deleteMonitorCode = path.substring(path.lastIndexOf("\\")+1, path.lastIndexOf("."));
			String[] args = value.split("#");
			map.put(deleteMonitorCode, args[1]);
		}catch(Exception e){
			Log.debug("**************取文件信息报错********",e);
		}
		if(isThreadDead(map.get(deleteMonitorCode))){
			Log.error("******之前老线程已关闭******"+deleteMonitorCode+"    "+map.entrySet(),null);
			return;
		}
		Log.debug("********要关闭的站点id*********"+deleteMonitorCode+"    "+map.entrySet());
		if(null!=map){
			while(i<3 && !isThreadDead(map.get(deleteMonitorCode))){
				try{
					Log.debug("********线程存在并且线程号：*********"+map.get(deleteMonitorCode));
					command = "cmd.exe /c taskkill /F /T /PID " + map.get(deleteMonitorCode);
					command1 = "cmd.exe /c taskkill /F /T /FI \"WINDOWTITLE eq " + map.get(deleteMonitorCode) + "*\"";
					Runtime.getRuntime().exec(command);
					Runtime.getRuntime().exec(command1);
				}catch(Exception e){
					Log.error("关闭线程报错",e);
				}
				i++;
			}
			if(!isThreadDead(map.get(deleteMonitorCode))){
				Log.error("******当前线程"+map.get(deleteMonitorCode)+"没有关闭成功FAIL******",null);
			}else{
				Log.error("******当前线程"+map.get(deleteMonitorCode)+"关闭成功SUCCESS******",null);
			}
		}
		Log.debug("********写文件前关闭老进程END*********");
	}
	private static boolean isThreadDead(String id) {
		boolean flag = true;
		Process proc = null;
		String value = null;
		String temp = null;
		Runtime rt = Runtime.getRuntime();
		try{ 
			proc = rt.exec("cmd.exe /c taskList | findstr " + id);
			InputStream in = proc.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			while((temp = reader.readLine()) != null) {
				value = value + temp; 
			}
			if(value != null)
				flag = false;
			proc.waitFor();
		}catch(Exception e) {
			Log.error("", e);
		}
		return flag;
	}
	public void stop() {
		if(listenMgr != null){
			Log.debug("结束%%%%%%%%音频采集数据任务接收+发送线程%%%%%%%%%%%");
			listenMgr.closeRecordingListener();
		}
		if(controlMgr != null){
			Log.debug("结束%%%%%%%%录音线程%%%%%%%%%%%");
			controlMgr.close();
		}
	}

}
