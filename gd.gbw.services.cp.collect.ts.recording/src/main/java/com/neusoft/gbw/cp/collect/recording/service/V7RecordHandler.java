package com.neusoft.gbw.cp.collect.recording.service;

import java.util.ArrayList;
import java.util.List;

import cn.com.pattek.octopus.OctoPusFileFace;
import cn.com.pattek.octopus.OctopusFile;
import cn.com.pattek.octopus.OctopusFileTS;

import com.neusoft.gbw.cp.collect.recording.constant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamRecord;
import com.neusoft.np.arsf.base.bundle.Log;

public class V7RecordHandler implements IRecord{

	private StreamParam info = null;
	private OctoPusFileFace of = null;
	private int handle = 0;
	private boolean isStop;
	private boolean isRealStoped = true;
	
	public boolean isRealStoped() {
		return isRealStoped;
	}

	@Override
	public void init(StreamParam info) {
		if(of==null) {
			String type = ConfigVariable.MANUFACTURERS_TYPE;
			switch(type) {
			case "TS":
				of = OctopusFileTS.getInstance();
				break;
			case "SSQ":
				of = OctopusFile.getInstance();
				break;
			default:
				of = OctopusFileTS.getInstance();
				break;
			}
		}
		this.info = info;
		isStop = false;
		isRealStoped = false;
	}

	@Override
	public StreamRecord start(){
		int countnums = 0;
		int recordLength = 0;
		int recordStatus = 0;
		List<byte[]> cutFileByte = new ArrayList<byte[]>();
		byte[] b = new byte[10250];
		try {
			isRealStoped = false;
			Log.info("[录音操作]启动录音线程, url=" + info.getUrl());
			//初始化句柄
			restHandler();
			of.fread(60000, handle);
			Log.info("[录音操作]接收机开始数据采集，录音长度："+info.getRecordLength());
			long recordDuration = info.getRecordLength() * 1000;
			long limit = System.currentTimeMillis() + recordDuration;
			while (System.currentTimeMillis() < limit ) {
				if(isStop){
					Log.debug("系统正在录音，将录音关闭，并休眠几秒，跳出录音循环");
					break;
				}
				b  = of.fread(10240, handle);
				if (b != null && b.length > 0) {
					cutFileByte.add(b);
					recordLength += b.length;
				} else {
					countnums++;
					if(countnums>60) {
					Log.warn("正在读取的流媒体出现音频流不连续情况:url=" + info.getUrl());
						break;
					}
				}
			}
			recordStatus = 1;
			Log.debug("[录音操作]接收机结束数据采集,param=" + info.toString() + ",recordLength=" + recordLength);
		} catch (Exception e) {
			recordStatus = 0;
			Log.error("[录音操作]接收机采集线程出现异常,停止采集音频，此音频视为漏采,param=" + info.toString(),e);
		}finally{
			closeHandle();
			isRealStoped = true;
		}
		StreamRecord record = createRecord(cutFileByte, recordLength, recordStatus);
		cutFileByte = null;
		b =null;
		return record;
	}

	private void restHandler() {
		if(of!=null) {
			closeHandle();
			Log.debug(this.getClass().getName()+"***********OctoPusFileFace  fopen start************");
			handle = of.fopen(info.getUrl(), "rb11k");
			Log.debug(this.getClass().getName()+"************OctoPusFileFace  fopen end*************");
			while(handle<=0 && isStop == false) {
				//log.error("不能打开站点{"+ConfigFileManager.getPropertiesValue(StaticStrings.S_ID)+"}的URL："+ConfigFileManager.getPropertiesValue(StaticStrings.M_S_S_U)+"失败，15秒后尝试重新打开!";);
				//dealDataDao.insertMsg(new RadioMessage(4,a,Integer.valueOf(ConfigFileManager.getPropertiesValue(StaticStrings.S_ID)),StaticDataPools.currentIp));
//				try {
//					Thread.sleep(15000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				Log.info("handle管道无法正常打开，进行关闭开启操作" + handle);
				closeHandle();
//				of.fclose(handle);
				Log.info("关闭handle：" + handle);
				handle = of.fopen(info.getUrl(), "rb11k");
				if(handle <= 0) 
					Log.info("启动失败，重新启动 ：handle= " + handle);
			}
			Log.info("打开handle："+handle);
		}
	}
	
	private StreamRecord createRecord(List<byte[]> cutFileByte, int recordLength, int status) {
		StreamRecord record = new StreamRecord();
		record.setRecordLength(recordLength);
		record.setRecordStatus(status);
		record.setStream(cutFileByte);
		return record;
	}
	
	
	private void closeHandle() {
		if(of != null && handle != 0) {
			of.fclose(handle);
			Log.info("关闭handle：" + handle);
		}
		handle = 0;
	}
	
	public void stop() {
		this.isStop = true;
	}
}
