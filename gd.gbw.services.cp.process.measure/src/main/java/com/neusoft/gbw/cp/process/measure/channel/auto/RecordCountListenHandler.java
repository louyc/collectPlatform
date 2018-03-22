//package com.neusoft.gbw.cp.process.measure.channel.auto;
//
//import java.util.Set;
//
//import com.neusoft.gbw.cp.process.measure.channel.ChannelType;
//import com.neusoft.gbw.cp.process.measure.channel.MeaUnitChannel;
//import com.neusoft.gbw.cp.process.measure.constants.ConfigVariable;
//import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
//import com.neusoft.gbw.cp.process.measure.context.MesureRecordModel;
//import com.neusoft.gbw.cp.process.measure.util.FileUtil;
//import com.neusoft.np.arsf.base.bundle.Log;
//import com.neusoft.np.arsf.common.util.NMService;
//
//public class RecordCountListenHandler extends NMService{
//
//	private MeaUnitChannel channel = null;
//	private MesureRecordModel model = null;
//	
//	public RecordCountListenHandler(MeaUnitChannel channel) {
//		this.channel = channel;
//		model = MesureRecordModel.getInstance();
//	}
//
//	@Override
//	public void run() {
//		boolean isOver = false; 
//		String autoType = getAutoType();
//		while(isThreadRunning()) {
//			if (model.isEmpty()) {
//				sleep(50);
//				continue;
//			}
//			
//			Set<String> set = model.getKey();
//			for(String key : set) {
//				if (key.startsWith(autoType)) {
//					//获取计数对象，实时判断录音文件是否都已全部采集
//					isOver = MesureRecordModel.getInstance().isOverTask(key);
//					if(!isOver) {
//						continue;
//					}
//					Log.info("本次收测单元录音完成,key=" + key);
//					sleep(ProcessConstants.STORE_MESURE_UNIT_WAIT_TIME);
//					int orgType =0;
//					orgType = getRunplanType(key);
//					//通知前台消息
//					channel.sendWebMsg(Integer.parseInt(autoType),orgType);
//					//记录收测单元完成状态
//					updateUnitStatus(getUnitBeginTime(key),key);
//					model.remove(key);
//					break;
//				}
//			}
//			sleep(ProcessConstants.TASK_STATUS_NOT_OVER_WAIT_TIME);
//		}
//	}
//	
//	private String getAutoType() {
//		ChannelType type = channel.getType();
//		int index = 0;
//		switch(type) {
//		case measure_unit_auto:
//			index = 0;
//			break;
//		case measure_unit_manual:
//			index = 1;
//			break;
//		default:
//			break;
//		}
//		return index+"";
//	}
//	
//	private int getRunplanType(String key) {
//		return key != null ? Integer.parseInt(key.substring(key.length()-1)): 0;
//	}
//	
//	private String getUnitBeginTime(String key) {
//		return key.split("_").length <= 3 ? "" : key.split("_")[2];
//	}
//	
//	private void updateUnitStatus(String beginTime,String unitKey) {
//		boolean isFinished = MesureRecordModel.getInstance().isFinishedStatus(beginTime, unitKey);
//		if(isFinished)
//			FileUtil.writerFile(ConfigVariable.UNIT_STATUS_FILE_PATH,"true");
//	}
//	
//	private void sleep(long million) {
//		try {
//			Thread.sleep(million);
//		} catch (InterruptedException e) {
//		}
//	}
//	
//	@Override
//	public String getServiceName() {
//		return this.serviceName;
//	}
//
//	@Override
//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}
//	
//	public static void main(String[] args) {
//		String key = "0_60_2015-05-26 11:11:11_3";
//		System.out.println(key.split("_").length);
//		String time = key.split("_").length <= 3 ? "" : key.split("_")[2];
//		System.out.println(time);
//	}
//
//}
