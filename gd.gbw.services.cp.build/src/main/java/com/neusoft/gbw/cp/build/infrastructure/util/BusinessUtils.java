package com.neusoft.gbw.cp.build.infrastructure.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.net.ftp.FTPFile;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.domain.services.SyntInitOtherService;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.LeakageCollect;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPFtpUtil;

public class BusinessUtils {

	public static Map<String, String> getUnfinishedUnit() {
		Map<String, String> newUnUnit = new HashMap<String, String>();
		LeakageCollect leakageCollect = BuildContext.getInstance().getLeakageCollect();
		int leakCollectStatus = leakageCollect.getLeakCollectStatus(); //补采状态，1：重启补采，2：前台触发补采,0:不进行补采
		int leakCollectType = leakageCollect.getLeakCollectType();   //补采类型，0：全部，3，广播，4实验
		Map<String, String> unfinishedUnit = DataMgrCentreModel.getInstance().getUnfinishedUnit();
		Iterator<Map.Entry<String, String>> it =  unfinishedUnit.entrySet().iterator();
		String key = null;
		while(it.hasNext()) {
			Map.Entry<String, String> data = it.next();
			key = data.getKey();
			if(leakCollectType != 0 && !key.split("_")[3].equals(leakCollectType + ""))
				continue;
			//如果为前台触发补采，则只采集漏采的
			if(leakCollectStatus == 2 && key.endsWith("3")) 
				continue;
			newUnUnit.put(getUniqueKey(key), data.getValue());
		}
		return newUnUnit;
	}

	public static Map<String, String> getFinishedUnit() {
		Map<String, String> newUnit = new HashMap<String, String>();
		LeakageCollect leakageCollect = BuildContext.getInstance().getLeakageCollect();
		int leakCollectStatus = leakageCollect.getLeakCollectStatus(); //补采状态，1：重启补采，2：前台触发补采,0:不进行补采
		Map<String, String> finishedUnit = DataMgrCentreModel.getInstance().getFinishedUnit();
		Iterator<Map.Entry<String, String>> it =  finishedUnit.entrySet().iterator();
		if(leakCollectStatus == 0) 
			return newUnit;

		String key = null;
		while(it.hasNext()) {
			Map.Entry<String, String> data = it.next();
			key = data.getKey();
			newUnit.put(getUniqueKey(key), data.getValue());
		}
		return newUnit;
	}

	public static void delUnFinishedUnitProcess(Map<String, String> unUnitMap) {
		Iterator<Map.Entry<String, String>> it =  unUnitMap.entrySet().iterator();
		List<Map<String, Object>> manualList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> measureList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> dataMap = null;
		while(it.hasNext()) {
			Map.Entry<String, String> data = it.next();
			dataMap = new HashMap<String, Object>();
			dataMap.put("id", Integer.parseInt(data.getValue()));
			if(data.getKey().startsWith("0"))
				measureList.add(dataMap);
			else
				manualList.add(dataMap);
		}
		if(!manualList.isEmpty())
			DataMgrCentreModel.getInstance().deleteManualUnit(manualList);
		if(!measureList.isEmpty())
			DataMgrCentreModel.getInstance().deleteMeasureUnit(measureList);
	}

	public static String getTimeKey(Task task) {
		StringBuffer key = new StringBuffer();
		key.append(task.getMeasureTask().getTask_id());
		key.append("_");
		key.append(task.getTaskFreq().getTaskfreq_id());
		key.append("_");
		key.append(task.getTaskMonitor().getMonitor_id());
		key.append("_");
		ProtocolType ptype = task.getBuildType().getProType();
		if(ptype.name().startsWith("Quality"))
			key.append("QualityTask");
		else if(ptype.name().startsWith("Spectrum"))
			key.append("SpectrumTask");
		else if(ptype.name().startsWith("Stream"))
			key.append("StreamTask");
		else if(ptype.name().startsWith("Offset"))
			key.append("OffsetTask");
		return key.toString();
	}

	/**
	 * 任务巡检信息，拆分成小任务巡检信息
	 * @param task
	 * @return
	 */
	public static String getInspectTaskInfo(Task task) {
		String taskName = null;
		String taskType = null;
		//		String taskTime = null;
		taskName = task.getMeasureTask().getTask_name();
		ProtocolType type = task.getBuildType().getProType();
		if(type.name().startsWith("Quality"))
			taskType = "指标任务";
		if(type.name().startsWith("Spectrum"))
			taskType = "频谱任务";
		if(type.name().startsWith("Stream"))
			taskType = "录音任务";
		if(type.name().startsWith("Offset"))
			taskType = "频偏任务";

		//		String start = task.getTaskSchedule().getStarttime();
		//		String end = task.getTaskSchedule().getEndtime();
		//		taskTime = start + "~" + end;
		String freq = task.getTaskFreq().getFreq();

		return taskName + ":" + taskType + "(" + freq + ")";
	}

	public static Map<String,String> getInspectTotalTaskInfo(Map<String, Object> task) {
		String taskStatusDesc = "";
		Map<String,String> map = new HashMap<String,String>();
		if(task.isEmpty()){
			map.put("taskStreamState", "失败");
			map.put("taskQualityState", "失败");
			return map;
		}
		for(String name : task.keySet()){
			taskStatusDesc = taskStatusDesc + "任务：" + name + ",状态：" + getTaskStatusDesc(String.valueOf(task.get(name))) + ";";
			if(name.equals("StreamTask"))  map.put("taskStreamState", "成功");
			if(name.equals("QualityTask")) map.put("taskQualityState", "成功");
		}
		return map;
	}

	private static String getTaskStatusDesc(String status_id) {
		String desc = null;
		switch(status_id) {
		case "0":
			desc = "下发失败";
			break;
		case "1":
			desc = "已下发";
			break;
		case "2":
			desc = "未下发";
			break;
		case "3":
			desc = "下发中";
			break;
		}
		return desc;
	}

	/**
	 * 提取唯一key
	 * @param key
	 * @return
	 */
	private static String getUniqueKey(String key) {
		return key.substring(0, key.length()-2);
	}

	/**
	 * 判断当前站点是否能在当前服务器上执行采集任务
	 * @param monitorId
	 * @return
	 */
	public static boolean judePartformIsDone(String monitorId){
		String hostName="";
		Map<String,Object> ret = new HashMap<String,Object>();
		boolean b =false;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
			if(hostName.length()>0){
				InetAddress[] addres = InetAddress.getAllByName(hostName);
				if(addres.length>0){
					for(int i=0;i<addres.length;i++){
						if(!addres[i].getHostAddress().contains(":"))
							ret.put(addres[i].getHostAddress(),"1");
					}
				}
			}
		} catch (UnknownHostException e) {
			Log.debug("获取本地服务器报错"+e);
		}
		if(null!=ret && ret.size()>0){
			Map<String, String> mp = DataMgrCentreModel.getInstance().getMonPartformMap();
			if(null!=mp.get(monitorId) && null!=ret.get(mp.get(monitorId)) &&ret.get(mp.get(monitorId)).equals("1")){  //有对应关系
				b = true;
			}
		}
		return b;
	}
	/**
	 * 录音文件下载
	 * @param url
	 * @return
	 */
	public static boolean ftpDownDataFile(String ip,String username,String password,String path,String filename
			,CollectTask colTask) {
		//		Log.debug(ip+"  "+username+"  "+password+"  "+path+"  "+filename);
		//		NPFtpUtil ftpFrom = new NPFtpUtil();
		//		try {
		//			ftpFrom.connect(ip, username, password);
		//		} catch (Exception e) {
		//			Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + ip + ", user=" + username + ", password=" + password, e);
		//			ftpFrom.closePut();
		//			return false;
		//		}
		Map<String,String> map = new SyntInitOtherService().getAddrMap();
		String localPath = map.get("recording_store_address") + map.get("collect_subpath")+"/"+getYMdTime() + "/";
		//		String localPath ="D:"+"/"+getYMdTime() + "/";
		path = getYMdTime().replace("-","");
		Log.debug("ftp数据文件下载开始，path = " +path+"   "+localPath +"   "+filename);
		boolean isOver = downFile(path, filename, localPath,ip,username,password);
		if(isOver){
			Log.debug("ftp数据文件下载成功，path = " + localPath + filename);
			if(filename.equals("zip")){
				try {
					readZipFile(localPath + filename);
				} catch (Exception e) {
					return false;
				}
			}
			updateStreamState(filename, localPath + filename,colTask,"1");
			return true;
		}else{
			Log.debug("ftp数据文件下载失败，path = " + path + filename);
			return false;
		}
	}

	private static void updateStreamState(String filename,String url,CollectTask colTask,String state){
		Map<String, Object> map = new HashMap<String, Object>();
		//		map.put("monitor_id",data.getCollectTask().getBusTask().getMonitor_id());
		map.put("store_time",DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		map.put("recover_url",url);
		map.put("recover_status",state);
		map.put("id",Integer.parseInt((String)((Map<String, String>)colTask.getExpandObject(ExpandConstants.RECORD_FILE_RECOVER_KEY)).get(filename)));
		StoreInfo info = new StoreInfo();
		try {
			info.setDataMap(map);
			info.setLabel("updateRecordFileUrl");
		} catch (Exception e) {
			Log.error("", e);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	private static String getYMdTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date()).toString();
	}
	/**
	 * 解压文件
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	private static String readZipFile(String file) throws Exception { 
		StringBuffer xml = new StringBuffer();
		ZipFile zf = new ZipFile(file);  
		InputStream in = new BufferedInputStream(new FileInputStream(file));  
		ZipInputStream zin = new ZipInputStream(in); 
		ZipEntry ze;  
		while ((ze = zin.getNextEntry()) != null) {  
			if (ze.isDirectory()) {
			} else {  
				String zeName = new String(ze.getName().getBytes("iso-8859-1"),"utf-8");
				System.out.println("获取文件名称："+zeName);
				long size = ze.getSize();  
				if (size > 0) {  
					BufferedReader br = new BufferedReader(  
							new InputStreamReader(zf.getInputStream(ze))); 
					String line;  
					while ((line = br.readLine()) != null) {  
						xml.append(line);
					}  
					br.close();  
				}  
			}  
		}  
		zin.closeEntry();  
		return xml.toString();
	}  

	public static boolean downFile(String remotePath, String fileName, String localPath,String ip,String user,String password) {
		try {
			String url = "ftp://"+user+":"+password+"@"+ip+"/"+remotePath+"/"+fileName;
			URL fileUrl = new URL(url);
			InputStream stream = fileUrl.openStream();
			try {
				File newFile = new File(localPath);
				if (!newFile.exists()) {
					newFile.mkdirs();
				}
				File localFile = new File(localPath + "/" + fileName);
				OutputStream out = new FileOutputStream(localFile);
				byte[] b = new byte[10240];
				int num = 0;
				while((num = stream.read(b)) != -1) {
					out.write(b,0,num);
				}
				stream.close();
				out.close();
			} catch (Exception e) {
				Log.debug("bug = " +  e);
				return false;
			}
		} catch (Exception e) {
			Log.debug("bug = " +  e);
			return false;
		}
		return true;
	}
	/**
	 * 复制文件
	 * @param oldpath
	 * @param newpath
	 * @return
	 */
	public static boolean copyZip(String filepath,String fileName,CollectTask colTask){
		Log.debug("****************start***");
		Map<String,String> map = new SyntInitOtherService().getAddrMap();
		String httpAddress = map.get("recording_http_address") + map.get("collect_subpath")+"/"+getYMdTime() + "/";
		String localPath = map.get("recording_store_address") + map.get("collect_subpath")+"/"+getYMdTime() + "/";
		String path = filepath+"/"+getYMdTime().replace("-","");
		try {
			File localFilepath = new File(localPath);
			if (!localFilepath.exists()) {
				localFilepath.mkdirs();
			}
			File streamFile = new File(path);
			File[] fs = streamFile.listFiles();
			for(File ff : fs) {
				if (ff.getName().trim().equals(fileName.trim())) {
					File newFile = new File(localPath+fileName);
					InputStream in = new FileInputStream(path+"/"+fileName);
					FileOutputStream fo = new  FileOutputStream(newFile);
					byte[] buff = new byte[1024000];
					int i =0;
					while((i = in.read(buff))!=-1){
						fo.write(buff, 0, i);
					}
					in.close();
					fo.close();
				}
			}
		} catch (Exception e) {
			Log.debug(e);
			Log.debug("ftp数据文件下载失败，filepath = " + filepath + fileName);
			return false;
		}
		Log.debug("ftp数据文件下载成功，path = " + localPath + fileName);
		if(fileName.equals("zip")){
			try {
				readZipFile(localPath + fileName);
			} catch (Exception e) {
				Log.debug("ftp数据文件解压失败，localPath = " + localPath + fileName);
				return false;
			}
		}
		updateStreamState(fileName, httpAddress + fileName,colTask,"1");
		return true;
	}


	public static void main(String[] args) {
		//		boolean b = ftpDownDataFile("10.13.2.6", "user", "user", "临河(SmP02)/record/20170419", "SmP02_100324225_20170419192831_20170419192932_1458_R1.wav", null);
		//		boolean b = ftpDownDataFile("10.13.2.6", "user", "user", "库伦(SmP38)/record", "SmP38_100325345_20170423132411_20170423132512_549_R1.wav", null);
		//		String url = "ftp://user:user@10.13.2.6/";
		boolean b = copyZip("X:\\满洲里(SmP45)\\record","SmP45_100325475_20170425000209_20170425000310_675_R1.wav",null);
		System.out.println(b);
	}

}
