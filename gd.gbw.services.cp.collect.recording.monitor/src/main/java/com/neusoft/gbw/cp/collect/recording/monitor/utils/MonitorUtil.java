package com.neusoft.gbw.cp.collect.recording.monitor.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.neusoft.gbw.cp.collect.recording.monitor.vo.MonitorStatus;
import com.neusoft.np.arsf.base.bundle.Log;

public class MonitorUtil {

	public synchronized static void reSetThread(String commander, MonitorStatus status, String batPath) {
		Log.info("重启操作来源：" + commander);
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try{
			//如果录音平台启动不超过30秒，则不重新启动
			MonitorStatus statusLast = FileUtil.readMonitorStatus(status.getMonitorCode());
//			String[] ta = statusLast.getMonitorTime().split("[ |\\:|\\-]");
//			java.util.Calendar cal = Calendar.getInstance();
//			cal.set(Integer.parseInt(ta[0]), Integer.parseInt(ta[1]), Integer.parseInt(ta[2]), Integer.parseInt(ta[3]), Integer.parseInt(ta[4]), Integer.parseInt(ta[5]));
//			long startTime = cal.getTimeInMillis();
			java.util.Calendar cal = Calendar.getInstance();
			try {
				Date da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(statusLast.getMonitorTime());
				cal.setTime(da);
			} catch (ParseException e) {
				Log.error("时间格式转换出错", e);
			}
			long startTime = cal.getTimeInMillis();
			if (System.currentTimeMillis() - startTime < 30000) {
				Log.info("录音平台启动时间不超过30秒，忽略本次重启操作，code=" + status.getMonitorCode() +",curpid="+statusLast.getThreadId());
				return;
			}
			
			//杀掉原有录音平台进程
			Log.debug("kill录音平台进程，id=" + status.getThreadId()+"   "+status.getMonitorCode());
			proc = rt.exec("cmd.exe /c taskkill /F /T /FI \"WINDOWTITLE eq \" + Java(TM) Platform SE binary + \"*\"");
			proc = rt.exec("cmd.exe /c taskkill /F /T /PID " + status.getThreadId());
			proc = rt.exec("cmd.exe /c taskkill /F /T /FI \"WINDOWTITLE eq " + status.getMonitorCode() + "*\"");
			Thread.sleep(2000);
			
			//启动新的录音平台进程
			Log.debug("start录音平台进程，batPath=" + batPath);
			proc = rt.exec("cmd.exe /c start " + batPath);
			proc.waitFor();
			int pstatus = proc.exitValue();
			if(pstatus == 0) {
				Log.debug("启动录音平台成功，code=" + status.getMonitorCode());
			}else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				String line = null;
				String errorMsg = "";
				while ((line = reader.readLine()) != null) {
					errorMsg += line;
				}
				Log.error("启动录音平台失败，code=" + status.getMonitorCode() + ", ERROR=" + errorMsg, null);
			}
			//等待录音平台启动完成（进程txt文件进程号被更新）
			boolean isPerfect = false;
			for(int i=0;i<20;i++){
				MonitorStatus statusNew = FileUtil.readMonitorStatus(status.getMonitorCode());
				if (statusNew.getThreadId().equals(status.getThreadId()) == false) {
					isPerfect = true;
					Log.debug("启动录音平台成功，进程txt文件已更新，code=" + status.getMonitorCode() +",newpid="+statusNew.getThreadId());
					break;
				}
				try{
					Thread.sleep(1000);
				} catch (InterruptedException e){
				}
			}
			if (isPerfect == false) {
				Log.warn("启动录音平台成功，但是进程txt文件未更新，code=" + status.getMonitorCode() +",oldpid="+status.getThreadId());
			}
		}catch(Exception e) {
			Log.error("kill进程失败", e);
		}
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
}
