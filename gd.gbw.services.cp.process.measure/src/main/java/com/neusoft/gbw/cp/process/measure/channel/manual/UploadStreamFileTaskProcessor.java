package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieveR;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieveR_File;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.process.measure.vo.manual.QualityDataStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NPFtpUtil;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;
import com.neusoft.np.arsf.net.core.NetEventType;

public class UploadStreamFileTaskProcessor extends RecoverTaskProcessor implements ITaskProcess{
	
	private CollectTaskModel model = CollectTaskModel.getInstance();

	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		ReportStatus status = data.getStatus();
		boolean recoverStatus = true;
		switch(status) {
		case date_collect_success:
			break;
		case date_collect_active_report:
//			sleep(2500);
			recoverStatus = uploadFileV8(data);
			model.updataRecordSize(data, recoverStatus);
//			//更新任务回收时间
//			updateRecoverTime(data, TaskType.StreamTask);
//			//更新任务回收计数
//			updateRecoverCount(data);
			break;
		default:
			Log.warn("[实时处理服务]FTP录音文件回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
//			disposeError(data);
			sendMsg(data.getCollectTask(), ProcessConstants.TASK_RECOVER_FAUIL, ProcessConstants.LeakageReason.DATE_COLLECT_FAUIL); 
			break;
		}
		return null;
	}

	private Object sendMsg(CollectTask task, String status, String msg) {
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessType(status);
		recover.setSuccessDicDesc(msg);
		Object syntStr = getRestStr(recover, task);
		Log.debug("本批次录音文件接收完成,向前台发送回收消息，syntStr=" + syntStr);
		sendTask(syntStr);
		return syntStr;
	}
	private String getRestStr(RecoveryMessageDTO recover, CollectTask task) {
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String tmpStr = ConverterUtil.objToXml(recover);
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
		return NPJsonUtil.jsonValueString(syntMap);
	}
	public void sendTask(Object syntObj) {
		if (syntObj instanceof JMSDTO) {
			ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, syntObj);
		} else if (syntObj instanceof String) {
			ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), (String)syntObj);
		} else {
			Log.warn("实时任务处理类型不存在，" + syntObj);
		}
	}
	
	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		ReportStatus status = data.getStatus();
		boolean recoverStatus = true;
		switch(status) {
		case date_collect_success:
			break;
		case date_collect_active_report:
//			sleep(2500);
			recoverStatus = uploadFileV7(data);
			model.updataRecordSize(data, recoverStatus);
//			//更新任务回收时间
//			updateRecoverTime(data, TaskType.StreamTask);
//			//更新任务回收计数
//			updateRecoverCount(data);
			break;
		default:
			Log.warn("[实时处理服务]FTP录音文件回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
//			disposeError(data);
			sendMsg(data.getCollectTask(), ProcessConstants.TASK_RECOVER_FAUIL, ProcessConstants.LeakageReason.DATE_COLLECT_FAUIL); 
			break;
		}
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		boolean recoverStatus = true;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			break;
		case date_collect_active_report:
//			sleep(2500);
			recoverStatus = uploadFileV6(data);
			model.updataRecordSize(data, recoverStatus);
//			//更新任务回收时间 
//			updateRecoverTime(data, TaskType.StreamTask);
//			//更新任务回收计数
//			updateRecoverCount(data);
			break;
		default:
			Log.warn("[实时处理服务]FTP录音文件回收处理异常，任务执行状态：" + status + "," + getLogMsg(data));
//			disposeError(data);
			sendMsg(data.getCollectTask(), ProcessConstants.TASK_RECOVER_FAUIL, ProcessConstants.LeakageReason.DATE_COLLECT_FAUIL); 
			break;
		}
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		//没有V5的回收功能
//		sleep(15000);
//		uploadFileV5(data);
//		//更新任务回收时间
//		updateRecoverTime(data, TaskType.StreamTask);
//		//更新任务回收计数
//		updateRecoverCount(data);
		return null;
	}
	
	private boolean uploadFileV8(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		FileRetrieveR retrieve = (FileRetrieveR) report.getReport();
		if (retrieve == null) {
			return true;
		}
		
		NPFtpUtil ftpUtil = new NPFtpUtil();
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		List<FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
		if (list == null || list.size()  <= 0) 
			return true;
			
		for(FileRetrieveR_File file : list) {
			String url = file.getFileURL();
			FileVO vo = getFileVOByUrl(url);
			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
			if (value == null) {
				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
				continue;
			}
			String[] array = value.split("#SEP#");
			try {
				ftpUtil.connect(vo.ip, array[0], array[1]);
			} catch (Exception e) {
				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
				ftpUtil.disconnect();
				continue;
			}
			
			String localPath = getAndCreatLocalPath();
			String playPath = getAndPlayPath();
			Log.debug("ftp录音文件下载开始，path = " + vo.remotePath + vo.zipFileName);
//			boolean isOver = ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
			boolean isOver = copyZip("Z:"+vo.remotePath + vo.zipFileName,localPath+vo.zipFileName);
			if(!isOver) {
				Log.debug("ftp录音文件下载失败，path = " + vo.remotePath + vo.zipFileName);
				return false;
			}	
			Log.debug("ftp录音文件下载成功，path = " + vo.remotePath + vo.zipFileName);
			//如果文件以zip结尾，则进行解压缩
			if(vo.zipFileName.endsWith("zip")) 
				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
			else vo.fileName = vo.zipFileName;
			Log.debug("ftp录音文件解压，fileName = " + vo.fileName);
			//更新录音数据的文件夹，变成已回收
			QualityDataStore qData = buildQualityDataStore(data, playPath + vo.fileName, vo.zipFileName);
//			if(isExistData(qData))
//				continue;
			infoList.add(buildStoreInfo(ProcessConstants.StoreTopic.UPDATE_RECORD_FILE_URL, qData));
			
		}
		sendStore(infoList);
		return true;
	}
	
	private boolean uploadFileV7(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR retrieve = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR) report.getReport();
		if (retrieve == null) {
			return true;
		}
		
		NPFtpUtil ftpUtil = new NPFtpUtil();
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
		if (list == null || list.size()  <= 0) 
			return true;
		
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR_File file : list) {
			String url = file.getFileURL();
			FileVO vo = getFileVOByUrl(url);
			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
			if (value == null) {
				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
				continue;
			}
			String[] array = value.split("#SEP#");
			try {
				ftpUtil.connect(vo.ip, array[0], array[1]);
			} catch (Exception e) {
				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
				ftpUtil.disconnect();
				continue;
			}
			
			String localPath = getAndCreatLocalPath();
			String playPath = getAndPlayPath();
			Log.debug("ftp录音文件下载开始，path = " + vo.remotePath + vo.zipFileName);
//			boolean isOver = ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
			boolean isOver = copyZip("Z:"+vo.remotePath + vo.zipFileName,localPath+vo.zipFileName);
			if(!isOver) {
				Log.debug("ftp录音文件下载失败，path = " + vo.remotePath + vo.zipFileName);
				return false;
			}	
			Log.debug("ftp录音文件下载成功，path = " + vo.remotePath + vo.zipFileName);
			//如果文件以zip结尾，则进行解压缩
			if(vo.zipFileName.endsWith("zip")) 
				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
			else vo.fileName = vo.zipFileName;
			Log.debug("ftp录音文件解压，fileName = " + vo.fileName);
			//更新录音数据的文件夹，变成已回收
			QualityDataStore qData = buildQualityDataStore(data, playPath + vo.fileName, vo.zipFileName);
//			if(isExistData(qData))
//				continue;
			infoList.add(buildStoreInfo(ProcessConstants.StoreTopic.UPDATE_RECORD_FILE_URL, qData));
		}
		sendStore(infoList);
		return true;
	}
	
	private boolean uploadFileV6(CollectData data) {
		Report report = (Report)data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR retrieve = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR) report.getReport();
		if (retrieve == null) {
			return true;
		}
		
		NPFtpUtil ftpUtil = new NPFtpUtil();
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
		if (list == null || list.size()  <= 0) 
			return true;
		
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR_File file : list) {
			String url = file.getFileURL();
			FileVO vo = getFileVOByUrl(url);
			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
			if (value == null) {
				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
				continue;
			}
			String[] array = value.split("#SEP#");
			try {
				ftpUtil.connect(vo.ip, array[0], array[1]);
			} catch (Exception e) {
				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
				ftpUtil.disconnect();
				continue;
			}
			
			String localPath = getAndCreatLocalPath();
			String playPath = getAndPlayPath();
			Log.debug("[Ftp音频下载]开始下载  remotePath=" + vo.remotePath + ",fileName=" + vo.zipFileName + ",localPath=" + localPath);
//			boolean isOver = ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
			//
			boolean isOver = copyZip("Z:"+vo.remotePath + vo.zipFileName,localPath+vo.zipFileName);
			if(!isOver) {
				Log.debug("ftp录音文件下载失败，path = " + vo.remotePath + vo.zipFileName);
				return false;
			}	
			Log.debug("ftp录音文件下载成功，path = " + vo.remotePath + vo.zipFileName);
			//如果文件以zip结尾，则进行解压缩
			if(vo.zipFileName.endsWith("zip")) 
				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
			else vo.fileName = vo.zipFileName;
			Log.debug("ftp录音文件解压，fileName = " + vo.fileName);
			//更新录音数据的文件夹，变成已回收
			QualityDataStore qData = buildQualityDataStore(data, playPath + vo.fileName, vo.zipFileName);
//			if(isExistData(qData))
//				continue;
			infoList.add(buildStoreInfo(ProcessConstants.StoreTopic.UPDATE_RECORD_FILE_URL, qData));
		}
		sendStore(infoList);
		return true;
	}
	
	private StoreInfo buildStoreInfo(String label, Object qData) {
		Map<String, Object> map = null;
		StoreInfo info = new StoreInfo();
		try {
			map = NMBeanUtils.getObjectField(qData);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		return info;
	}
	
	private QualityDataStore buildQualityDataStore(CollectData data, String playURL, String zipFileName) {
		QualityDataStore qData = new QualityDataStore();
		qData.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		qData.setFile_name(zipFileName);
		qData.setRecover_url(playURL);
		qData.setRecover_status(1);
		qData.setId(Integer.parseInt((String)getStearmRecord(data).get(zipFileName)));
		return qData;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getStearmRecord(CollectData data) {
		Map<String, String> dataMap = (Map<String, String>) data.getCollectTask().getExpandObject(ExpandConstants.RECORD_FILE_RECOVER_KEY);
		return dataMap;
	}
	
	private String getAndCreatLocalPath() {
		String path = getBaseSaveRecordPath() + getYMdTime() + "\\";
		File file = new File(path);
		if (file.exists()) {
			return path;
		}
		
		file.mkdirs();
		return path;
	}
	
	private String getAndPlayPath() {
		return getBaseHttpRecordPath() + getYMdTime() + "/";
	}
	
	private static String getBaseSaveRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_STORE_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.COLLECT_SUBPATH);
		return rootPath + subPath + "\\";
	}

	private static String getBaseHttpRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_HTTP_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.COLLECT_SUBPATH);
		return rootPath + subPath + "/";
	}
	
	private String getYMdTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date()).toString();
	}
	
	private FileVO getFileVOByUrl(String url) {
		String tmp = url.replaceFirst("ftp://", "");
		String[] array = tmp.split("/");
		FileVO vo = new FileVO();
		vo.ip = array[0].split(":").length >= 2 ? array[0].split(":")[0] : array[0];
		vo.zipFileName = array[array.length - 1];
		vo.remotePath = tmp.replaceAll(array[0], "").replaceAll(array[array.length - 1], "");
		
		return vo;
	}
	
	@SuppressWarnings("resource")
	private String unZipFile(String sourceFile,String localPath) {
		String newFileName = null;
		File file = new File(sourceFile);
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(file);
		    ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file)); 
	        ZipEntry zipEntry = null; 
	        while ((zipEntry = zipInputStream.getNextEntry()) != null) { 
	        	newFileName = zipEntry.getName(); 
	            File temp = new File(localPath + newFileName); 
	            if (! temp.getParentFile().exists()) 
	                temp.getParentFile().mkdirs(); 
	            OutputStream os = new FileOutputStream(temp); 
	            InputStream is = zipFile.getInputStream(zipEntry); 
	
	            int len = 0; 
	            byte[] bt = new byte[10240];
	            while ((len = is.read(bt)) != -1) 
		            os.write(bt,0,len); 
		            os.close(); 
		            is.close(); 
	        } 
	        zipInputStream.close();
		} catch (ZipException e) {
			return newFileName; 
		} catch (IOException e) {
			return newFileName;
		}
		return newFileName; 
	}
	
	private class FileVO {
		private String ip;
		private String remotePath;
		private String zipFileName;
		private String fileName;
		@Override
		public String toString() {
			return "FileVO [ip=" + ip + ", remotePath=" + remotePath
					+ ", zipFileName=" + zipFileName + ", fileName=" + fileName
					+ "]";
		}
	}
	
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code()+ ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
	
	private boolean copyZip(String oldpath,String newpath){
		try {
			File oldFile = new File(oldpath);
			File newFile = new File(newpath);
			InputStream in = new FileInputStream(oldFile);
			FileOutputStream fo = new  FileOutputStream(newFile);
			byte[] buff = new byte[102400];
			int i =0;
			while((i = in.read(buff))!=-1){
				fo.write(buff, 0, i);
			}
			in.close();
			fo.close();
		} catch (Exception e) {
			Log.debug(e);
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
//		UploadStreamFileTaskProcessor process = new UploadStreamFileTaskProcessor();
//		FileVO vo = process.getFileVOByUrl("ftp://10.13.2.201:21/upload/SmP05_168292473_20150719224447_20150719224549_576_R2.zip");
//		System.out.println(vo.toString());
		File f = new File("Z:\\upload\\V11170822_549_R1_144623_SmP39_20170403.mp3.zip");
		File f1 = new File("Z:\\collect\\V11170822_549_R1_144623_SmP39_20170403.mp3.zip");
		try {
			InputStream in = new FileInputStream(f);
			FileOutputStream fo = new  FileOutputStream(f1);
			byte[] buff = new byte[10240];
			int i =0;
			while((i = in.read(buff))!=-1){
				fo.write(buff, 0, i);
			}
			in.close();
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
