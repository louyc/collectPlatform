package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.HistoryScanResult;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumScan;
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

public class SpectrumReportTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String spectrum = "taskSpectrum";
	private CollectTaskModel model = CollectTaskModel.getInstance();

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
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveData(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveData(data));
		sendStore(list);
		return list;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveV7Data(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveV7Data(data));
		sendStore(list);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveV6Data(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveV6Data(data));
		sendStore(list);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
	//	list.addAll(saveV5Data(data));
	//	list.add(updateRecoverStatus(data));
		disposeTaskData(saveV5Data(data));
		sendStore(list);
		return null;
	}
	
	private List<StoreInfo> saveData(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV8SpectrumHistoryReport(data).getEquCode();
		List<SpectrumScan> ssList = ((SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport()).getSpectrumScan();
		if(ssList == null || ssList.size() <= 0)
			return list;
		for(SpectrumScan quality : ssList) {
			for(HistoryScanResult result : quality.getScanResults()) {
				StoreInfo info = new StoreInfo();
				
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setCollect_time(quality.getScanDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
				
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
		}

		return list;
	}
	
	private List<StoreInfo> saveV7Data(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV7SpectrumHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan> ssList = getV7SpectrumScanList(data);
		if(ssList == null || ssList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan quality : ssList) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.HistoryScanResult result : quality.getScanResults()) {
				StoreInfo info = new StoreInfo();
				
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getScanDateTime());
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
				
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
		}
		return list;
	}
	
	private List<StoreInfo> saveV6Data(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV6SpectrumHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumScan> ssList = getV6SpectrumScanList(data);
		if(ssList == null || ssList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumScan quality : ssList) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.HistoryScanResult result : quality.getScanResults()) {
				StoreInfo info = new StoreInfo();
				
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getScanDateTime());
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
				
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
		}
		return list;
	}
	
	private List<StoreInfo> saveV5Data(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV5SpectrumHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumScan> ssList = getV5SpectrumScanList(data);
		if(ssList == null || ssList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumScan quality : ssList) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.HistoryScanResult result : quality.getScanResults()) {
				StoreInfo info = new StoreInfo();
				
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(result.getBand()));
				qData.setCollect_time(quality.getScanDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(result.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				
				String label = model.getQualLabel(spectrum);
				qData.setQuality_code(spectrum);
				qData.setQuality_value(String.valueOf(result.getLevel()));
				
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
		}
		return list;
	}
	
	private List<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumScan> getV7SpectrumScanList(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport()).getSpectrumScan();
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport getV7SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
	
	private List<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumScan> getV6SpectrumScanList(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport()).getSpectrumScan();
	}
	
	private com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport getV6SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
	
	private List<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumScan> getV5SpectrumScanList(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport()).getSpectrumScan();
	}
	
	private com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport getV5SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
	
	private com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport getV8SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryReport)((Report)data.getData().getReportData()).getReport());
	}
}
