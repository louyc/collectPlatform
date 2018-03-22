package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;

public class UploadStreamFileTaskProcessor implements ITaskProcess {
	
//	private final static String END_TIME = "END_TIME";
//	private final static String RECORD_BAND = "BAND";
//	private final static String EQU_CODE = "EQU_CODE";
//	private final static String UNIT_STORE = "UNIT_STORE";
//	private CollectTaskModel model = CollectTaskModel.getInstance();

	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
//		sleep(5000);
//		uploadFileV8(data);
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
//		sleep(5000);
//		uploadFileV7(data);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
//		sleep(5000);
//		uploadFileV6(data);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
//		sleep(5000);
//		uploadFileV5(data);
		return null;
	}
	
//	private void uploadFileV8(CollectData data) {
//		Report report = (Report)data.getData().getReportData();
//		FileRetrieveR retrieve = (FileRetrieveR) report.getReport();
//		if (retrieve == null) {
//			return;
//		}
//		
//		NPFtpUtil ftpUtil = new NPFtpUtil();
//		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
//		List<FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
//		if (list == null || list.size()  <= 0) 
//			return;
//			
//		for(FileRetrieveR_File file : list) {
//			String url = file.getFileURL();
//			FileVO vo = getFileVOByUrl(url);
//			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
//			if (value == null) {
//				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
//				continue;
//			}
//			String[] array = value.split("#SEP#");
//			try {
//				ftpUtil.connect(vo.ip, array[0], array[1]);
//			} catch (Exception e) {
//				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
//				ftpUtil.disconnect();
//				continue;
//			}
//			
//			String localPath = getAndCreatLocalPath();
//			Log.debug("ftp录音文件下载开始，path = " + vo.remotePath + vo.zipFileName);
//			ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
//			Log.debug("ftp录音文件下载结束，path = " + vo.remotePath + vo.zipFileName);
//			//如果文件以zip结尾，则进行解压缩
//			if(vo.zipFileName.endsWith("zip")) 
//				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
//			else vo.fileName = vo.zipFileName;
//			Log.debug("ftp录音文件解压，fileName = " + vo.fileName);
//			//存储入指标数据表中
//			QualityDataStore qData = buildQualityDataStore(data, localPath + vo.fileName);
////			if(isExistData(qData))
////				continue;
//			String label = CollectTaskModel.getInstance().getQualLabel("taskStream");
//			infoList.add( buildStoreInfo(label, qData));
//			
//			//存储入手动打分表中
//			StoreInfo info = (StoreInfo) getStearmTask(data).getExpandObject(UNIT_STORE);
//			info.getDataMap().put("eval_url", localPath + vo.fileName);
//			infoList.add(info);
//		}
//		sendStore(infoList);
//	}
//	
//	private void uploadFileV7(CollectData data) {
//		Report report = (Report)data.getData().getReportData();
//		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR retrieve = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR) report.getReport();
//		if (retrieve == null) {
//			return ;
//		}
//		
//		NPFtpUtil ftpUtil = new NPFtpUtil();
//		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
//		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
//		if (list == null || list.size()  <= 0) 
//			return;
//		
//		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.FileRetrieveR_File file : list) {
//			String url = file.getFileURL();
//			FileVO vo = getFileVOByUrl(url);
//			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
//			if (value == null) {
//				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
//				continue;
//			}
//			String[] array = value.split("#SEP#");
//			try {
//				ftpUtil.connect(vo.ip, array[0], array[1]);
//			} catch (Exception e) {
//				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
//				ftpUtil.disconnect();
//				continue;
//			}
//			
//			String localPath = getAndCreatLocalPath();
//			ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
//			//如果文件以zip结尾，则进行解压缩
//			if(vo.zipFileName.endsWith("zip")) 
//				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
//			else vo.fileName = vo.zipFileName;
//			//存储入指标数据表中
//			QualityDataStore qData = buildQualityDataStore(data, localPath + vo.fileName);
////			if(isExistData(qData))
////				continue;
//			String label = CollectTaskModel.getInstance().getQualLabel("taskStream");
//			infoList.add( buildStoreInfo(label, qData));
//			
//			//存储入手动打分表中
//			StoreInfo info = (StoreInfo) getStearmTask(data).getExpandObject(UNIT_STORE);
//			info.getDataMap().put("eval_url", localPath + vo.fileName);
//			infoList.add(info);
//		}
//		sendStore(infoList);
//	}
//	
//	private void uploadFileV5(CollectData data) {
//		Report report = (Report)data.getData().getReportData();
//		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieveR retrieve = (com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieveR) report.getReport();
//		if (retrieve == null) {
//			return ;
//		}
//		
//		NPFtpUtil ftpUtil = new NPFtpUtil();
//		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
//		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
//		if (list == null || list.size()  <= 0) 
//			return;
//		
//		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.FileRetrieveR_File file : list) {
//			String url = file.getFileURL();
//			FileVO vo = getFileVOByUrl(url);
//			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
//			if (value == null) {
//				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
//				continue;
//			}
//			String[] array = value.split("#SEP#");
//			try {
//				ftpUtil.connect(vo.ip, array[0], array[1]);
//			} catch (Exception e) {
//				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
//				ftpUtil.disconnect();
//				continue;
//			}
//			
//			String localPath = getAndCreatLocalPath();
//			ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
//			//如果文件以zip结尾，则进行解压缩
//			if(vo.zipFileName.endsWith("zip")) 
//				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
//			else vo.fileName = vo.zipFileName;
//			//存储入指标数据表中
//			QualityDataStore qData = buildQualityDataStore(data, localPath + vo.fileName);
////			if(isExistData(qData))
////				continue;
//			String label = CollectTaskModel.getInstance().getQualLabel("taskStream");
//			infoList.add( buildStoreInfo(label, qData));
//			
//			//存储入手动打分表中
//			StoreInfo info = (StoreInfo) getStearmTask(data).getExpandObject(UNIT_STORE);
//			info.getDataMap().put("eval_url", localPath + vo.fileName);
//			infoList.add(info);
//		}
//		sendStore(infoList);
//	}
//	
//	private void uploadFileV6(CollectData data) {
//		Report report = (Report)data.getData().getReportData();
//		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR retrieve = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR) report.getReport();
//		if (retrieve == null) {
//			return ;
//		}
//		
//		NPFtpUtil ftpUtil = new NPFtpUtil();
//		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
//		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR_File> list = retrieve.getFileRetrieveR_File();
//		if (list == null || list.size()  <= 0) 
//			return;
//		
//		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieveR_File file : list) {
//			String url = file.getFileURL();
//			FileVO vo = getFileVOByUrl(url);
//			String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
//			if (value == null) {
//				Log.warn("[Ftp音频下载]下载的音频文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
//				continue;
//			}
//			String[] array = value.split("#SEP#");
//			try {
//				ftpUtil.connect(vo.ip, array[0], array[1]);
//			} catch (Exception e) {
//				Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
//				ftpUtil.disconnect();
//				continue;
//			}
//			
//			String localPath = getAndCreatLocalPath();
//			ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
//			//如果文件以zip结尾，则进行解压缩
//			if(vo.zipFileName.endsWith("zip")) 
//				vo.fileName = unZipFile(localPath + vo.zipFileName, localPath);
//			else vo.fileName = vo.zipFileName;
//			//存储入指标数据表中
//			QualityDataStore qData = buildQualityDataStore(data, localPath + vo.fileName);
////			if(isExistData(qData))
////				continue;
//			String label = CollectTaskModel.getInstance().getQualLabel("taskStream");
//			infoList.add( buildStoreInfo(label, qData));
//			
//			//存储入手动打分表中
//			StoreInfo info = (StoreInfo) getStearmTask(data).getExpandObject(UNIT_STORE);
//			info.getDataMap().put("eval_url", localPath + vo.fileName);
//			infoList.add(info);
//		}
//		sendStore(infoList);
//	}
//	
//	private StoreInfo buildStoreInfo(String label, Object qData) {
//		Map<String, Object> map = null;
//		StoreInfo info = new StoreInfo();
//		try {
//			map = NMBeanUtils.getObjectField(qData);
//			info.setDataMap(map);
//			info.setLabel(label);
//		} catch (NMBeanUtilsException e) {
//			Log.error("", e);
//		}
//		return info;
//	}
//	
//	private void sendStore(List<StoreInfo> infoList) {
//		if(infoList != null && !infoList.isEmpty()) {
//			for(StoreInfo info : infoList) {
//				ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
//			}
//		}
//	}
//	
//	
//	private QualityDataStore buildQualityDataStore(CollectData data, String file) {
//		QualityDataStore qData = new QualityDataStore();
//		qData.setBand(Integer.parseInt((String)getStearmTask(data).getExpandObject(RECORD_BAND)));
//		qData.setCollect_time((String)getStearmTask(data).getExpandObject(END_TIME));
//		qData.setFrequency(data.getCollectTask().getBusTask().getFreq());
//		qData.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
//		qData.setQuality_code("taskStream");
//		qData.setQuality_value(file);
//		qData.setReceiver_code((String)getStearmTask(data).getExpandObject(EQU_CODE));
//		qData.setTask_id(data.getCollectTask().getBusTask().getTask_id());
//		qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
//		return qData;
//	}
//	
//	private CollectTask getStearmTask(CollectData data) {
//		return (CollectTask)data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
//	}
//	
//	private String getAndCreatLocalPath() {
//		String path = getBaseSaveRecordPath() + getYMdTime() + "\\";
//		File file = new File(path);
//		if (file.exists()) {
//			return path;
//		}
//		
//		file.mkdirs();
//		return path;
//	}
//	
//	private static String getBaseSaveRecordPath() {
//		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_STORE_ADDRESS);
//		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.COLLECT_SUBPATH);
//		return rootPath + subPath + "\\";
//	}
//	
//	private String getYMdTime() {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		return df.format(new Date()).toString();
//	}
//	
//	private FileVO getFileVOByUrl(String url) {
//		String tmp = url.replaceFirst("ftp://", "");
//		String[] array = tmp.split("/");
//		FileVO vo = new FileVO();
//		vo.ip = array[0].split(":").length >= 2 ? array[0].split(":")[0] : array[0];
//		vo.zipFileName = array[array.length - 1];
//		vo.remotePath = tmp.replaceAll(array[0], "").replaceAll(array[array.length - 1], "");
//		
//		return vo;
//	}
//	
//	@SuppressWarnings("resource")
//	private String unZipFile(String sourceFile,String localPath) {
//		String newFileName = null;
//		File file = new File(sourceFile);
//		ZipFile zipFile;
//		try {
//			zipFile = new ZipFile(file);
//		    ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file)); 
//	        ZipEntry zipEntry = null; 
//	        while ((zipEntry = zipInputStream.getNextEntry()) != null) { 
//	        	newFileName = zipEntry.getName(); 
//	            File temp = new File(localPath + newFileName); 
//	            if (! temp.getParentFile().exists()) 
//	                temp.getParentFile().mkdirs(); 
//	            OutputStream os = new FileOutputStream(temp); 
//	            InputStream is = zipFile.getInputStream(zipEntry); 
//	
//	            int len = 0; 
//	            byte[] bt = new byte[10240];
//	            while ((len = is.read(bt)) != -1) 
//		            os.write(bt,0,len); 
//		            os.close(); 
//		            is.close(); 
//	        } 
//	        zipInputStream.close();
//		} catch (ZipException e) {
//			return newFileName; 
//		} catch (IOException e) {
//			return newFileName;
//		}
//		return newFileName; 
//	}
//	
//	private class FileVO {
//		private String ip;
//		private String remotePath;
//		private String zipFileName;
//		private String fileName;
//	}
//	
////	private boolean isExistData(QualityDataStore qData) {
////		String uniKey = getUniKey(qData);
////		return model.getRecoverDataMap(uniKey);
////	}
////	
////	private String getUniKey(QualityDataStore qData) {
////		return qData.getTask_id() + "_" + qData.getMonitor_id() + "_" + qData.getFrequency() + "_" + qData.getQuality_code() + "_" + qData.getCollect_time();
////	}
////	
//	private void sleep(long millis) {
//		try {
//			Thread.sleep(millis);
//		} catch (InterruptedException e) {
//		}
//	}
//	
//	public static void main(String[] args) {
//		UploadStreamFileTaskProcessor process = new UploadStreamFileTaskProcessor();
//		System.out.println(System.currentTimeMillis());
//		process.unZipFile("Z:\\collect\\2015-06-16\\SmP05_780381783_20150616105829_20150616105929_549_R2.zip","Z:\\collect\\2015-06-16\\");
//		System.out.println(System.currentTimeMillis());
//	}
}
