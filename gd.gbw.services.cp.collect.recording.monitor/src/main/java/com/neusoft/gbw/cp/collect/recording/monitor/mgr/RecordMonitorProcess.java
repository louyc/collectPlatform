package com.neusoft.gbw.cp.collect.recording.monitor.mgr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.neusoft.gbw.cp.collect.recording.monitor.mode.MonitorMode;
import com.neusoft.gbw.cp.collect.recording.monitor.utils.MonitorUtil;
import com.neusoft.gbw.cp.collect.recording.monitor.vo.MonitorStatus;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordMonitorProcess extends NMService{

	private MonitorInit init = null;
	private MonitorMode mode = MonitorMode.getInstance();

	@Override
	public void run() {
		Map<String, String> batMap = null;
		Map<String, MonitorStatus> statusMap = null;
		MonitorStatus status = null;
		String batPath = null;
		initData();
		batMap = mode.getBatMap();
		statusMap = mode.getStatusMap();
		//监测站点状态，是否大于15分钟(配置文件中配置)，如果大于则需要重启
		if(statusMap.isEmpty() || batMap.isEmpty()) {
			Log.warn("未发现要监控的录音平台程序");
			return;
		}
		for(String code : statusMap.keySet()) {
			status = statusMap.get(code);
			//			if(!isThreadDead(status.getThreadId()) && !isTimeOut(status.getMonitorTime())) {
			if(!isThreadDead(status.getThreadId())) {
				Log.debug("录音平台code=" + status.getMonitorCode() + "运行正常, time=" + status.getMonitorTime()
						+ ", threadId=" + status.getThreadId());		
				continue;
			}
			Log.debug("当前站点线程已死掉，请重启*************"+status.getMonitorCode());
			Log.debug("录音平台code=" + status.getMonitorCode() + "运行异常, time=" + status.getMonitorTime()
					+ ", threadId=" + status.getThreadId());
			batPath = batMap.get(code);
			//重启首先杀掉进程，然后再启动进程
			MonitorUtil.reSetThread("录音监控平台的定时轮询检查", status, batPath);
		}
		/*if(null !=map && map.size()>0){
			Log.debug("开始等待20s********************");
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.debug("运行异常的站点等待20s后进行再次判断"+map.entrySet());
			initData();
			batMap = mode.getBatMap();
			statusMap = mode.getStatusMap();
			for(String code : statusMap.keySet()) {
				for(String code1 : map.keySet()){
					if(map.get(code1).equals(((MonitorStatus)statusMap.get(code)).getThreadId())){
						batPath = batMap.get(code1);
						Log.debug("运行异常的站点要进行重启"+"，老进程号"+map.get(code1)+"，新进程号"+((MonitorStatus)statusMap.get(code)).getThreadId());
						MonitorUtil.reSetThread(statusMap.get(code1), batPath);
						Log.debug("异常关闭结束");
					}
				}
			}
		}*/
	}
	
	private boolean isThreadDead(String id) {
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

	//	private boolean isTimeOut(String time) {
	//		boolean flag = true;
	//		try {
	//			long now = System.currentTimeMillis();
	//			long ctime = getTimeToMilliSecond(time);
	//			long timeIntervel = ConfigVariable.RECORD_RESET_TIME * 60 * 1000;
	//			if(now - ctime > timeIntervel) {
	//				return flag;
	//			}
	//		} catch (Exception e) {
	//			Log.error("", e);
	//		}
	//		return false;
	//	}



	private void initData() {
		init = new MonitorInit();
		init.init();
	}

	//	private long getTimeToMilliSecond(String time) throws Exception {
	//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//		return df.parse(time).getTime();
	//	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/*public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try{ 
			//			proc = rt.exec("cmd.exe /c taskkill /F /PID \"1496\"");
			//			proc.waitFor();
			String[] cmd = new String[]{"wscript","D:\\backrunbat.vbs"};
			proc = rt.exec(cmd);
			proc.waitFor();
			int i = proc.exitValue();
			System.out.println(",,,," + i);

			//			String s = null;
			//			proc = rt.exec("cmd.exe /c taskList | findstr 1088");
			//			InputStream in = proc.getInputStream();
			//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			//			while((s = reader.readLine()) != null) {
			//				System.out.println(new String(s.getBytes("gbk")));
			//			}
			//			proc.waitFor();
		}catch(Exception e) {
			Log.error("", e);
		}
		//		
		//		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		//		String name = runtime.getName();

	}*/

}
