package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetIndex;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.QualityDataStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class OffsetReportTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String offset = "taskFreqOffset";
	private CollectTaskModel model = CollectTaskModel.getInstance();

	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		Log.debug("[频偏任务数据主动上报]处理频偏任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveDataV8(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveDataV8(data));
		sendStore(list);
		return list;
	}
	public void sleep(long millin) {
		try {
			Thread.sleep(millin);
		} catch (InterruptedException e) {
		}
	}
	private void insertData(List<StoreInfo> list) {
		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
		for(StoreInfo info : list) {
			if (info.getLabel().startsWith("insert"))
				resultList.add(info);
		}
		sendStore(resultList);
	}
	public void disposeTaskData(List<StoreInfo> list) {
		//100  条批量插入删除
		delData(list);
		if(list.size()>100){
			List<StoreInfo> listNew = new ArrayList<StoreInfo>();
			for(int i=0;i<list.size();i++){
				listNew.add(list.get(i));
				if(listNew.size() ==100){
					//在构建插入数据，并执行
					insertData(listNew);
					listNew = new ArrayList<StoreInfo>();
					sleep(1000);
				}
				if(i+1 == list.size()){
					insertData(listNew);
				}
			}
		}else{
			delData(list);
			//在构建插入数据，并执行
			insertData(list);
		}
	}
	private void delData(List<StoreInfo> list) {
		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
		for(StoreInfo info : list) {
			if (info.getLabel().startsWith("delete"))
				resultList.add(info);
		}
		sendStore(resultList);
	}
	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		Log.debug("[频偏任务数据主动上报]处理频偏任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveV7Data(data));
	//  list.add(updateRecoverStatus(data));
		disposeTaskData(saveV7Data(data));
		sendStore(list);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		Log.debug("[频偏任务数据主动上报]处理频偏任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveV5Data(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveV5Data(data));
		sendStore(list);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		Log.debug("[频偏任务数据主动上报]处理频偏任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveV5Data(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveV5Data(data));
		sendStore(list);
		return null;
	}
	
	private List<StoreInfo> saveDataV8(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV8OffsetHistoryReport(data).getEquCode();
		List<OffsetIndex> setList = ((OffsetHistoryReport)((Report) data.getData().getReportData()).getReport()).getOffsetIndexs();
		if(setList == null || setList.size() <= 0)
			return list;
		for(OffsetIndex index : setList) {
			StoreInfo info = new StoreInfo();
			qData = fillQualityDataStore(data);
			qData.setBand(Integer.parseInt(index.getBand()));
			qData.setCollect_time(index.getCheckDateTime());
			qData.setReceiver_code(equCode);
			qData.setFrequency(index.getFreq());
			qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
			
			String label = model.getQualLabel(offset);
			qData.setQuality_code(offset);
			qData.setQuality_value(String.valueOf(index.getOffset()));
			
			Map<String, Object> map = null;
			try {
				map = NMBeanUtils.getObjectField(qData);
				info.setDataMap(map);
				info.setLabel(label);
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		
		return list;
	}
	
	private List<StoreInfo> saveV7Data(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV7OffsetHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetIndex> setList = getV7OffsetIndex(data);
		if(setList == null || setList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetIndex index : setList) {
			StoreInfo info = new StoreInfo();
			qData = fillQualityDataStore(data);
			qData.setBand(Integer.parseInt(index.getBand()));
			qData.setCollect_time(index.getCheckDateTime());
			qData.setReceiver_code(equCode);
			qData.setFrequency(index.getFreq());
			qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
			
			String label = model.getQualLabel(offset);
			qData.setQuality_code(offset);
			qData.setQuality_value(String.valueOf(index.getOffset()));
			
			Map<String, Object> map = null;
			try {
				map = NMBeanUtils.getObjectField(qData);
				info.setDataMap(map);
				info.setLabel(label);
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		
		return list;
	}
	
	private List<StoreInfo> saveV5Data(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV5OffsetHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetIndex> setList = getV5OffsetIndex(data);
		if(setList == null || setList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetIndex index : setList) {
			StoreInfo info = new StoreInfo();
			qData = fillQualityDataStore(data);
			qData.setBand(Integer.parseInt(index.getBand()));
			qData.setCollect_time(index.getCheckDateTime());
			qData.setReceiver_code(equCode);
			qData.setFrequency(index.getFreq());
			qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
			
			String label = model.getQualLabel(offset);
			qData.setQuality_code(offset);
			qData.setQuality_value(String.valueOf(index.getOffset()));
			
			Map<String, Object> map = null;
			try {
				map = NMBeanUtils.getObjectField(qData);
				info.setDataMap(map);
				info.setLabel(label);
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		
		return list;
	}
	
	
	
	private List<com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetIndex> getV7OffsetIndex(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport)((Report) data.getData().getReportData()).getReport()).getOffsetIndexs();
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport getV7OffsetHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetHistoryReport)((Report) data.getData().getReportData()).getReport();
	}
	
	private List<com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetIndex> getV5OffsetIndex(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport)((Report) data.getData().getReportData()).getReport()).getOffsetIndexs();
	}
	
	private com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport getV5OffsetHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetHistoryReport)((Report) data.getData().getReportData()).getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport getV8OffsetHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryReport)((Report) data.getData().getReportData()).getReport();
	}
}
