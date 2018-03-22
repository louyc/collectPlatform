package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReportQuality;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.QualityDataStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class QualityReportTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	private final static String level = "taskLevel";
	private final static String amodulation = "taskAMModulation";
	private final static String attenuation = "taskAttenuation";
	private final static String bandwidth = "taskBandwidth";
	private final static String fmodulation = "taskFMModulation";
	private final static String ammodulationRate = "taskAMModulationRate";//V5版本调幅
	private final static String fmmodulationRate = "taskFMModulationRate";//V5调制度
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
		if(list.size()>4000){
			List<StoreInfo> listNew = new ArrayList<StoreInfo>();
			for(int i=0;i<list.size();i++){
				listNew.add(list.get(i));
				if(listNew.size() ==4000){
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
		Log.debug("[指标任务数据上报]处理指标任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		disposeTaskData(saveData(data));
	//	list.add(updateRecoverStatus(data));
		sendStore(list);
		return list;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		Log.debug("[指标任务数据上报]处理指标任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		disposeTaskData(saveV7Data(data));
	//	list.add(updateRecoverStatus(data));
		sendStore(list);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		Log.debug("[指标任务数据上报]处理指标任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		disposeTaskData(saveV6Data(data));
	//	list.add(updateRecoverStatus(data));
		sendStore(list);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		Log.debug("[指标任务数据上报]处理指标任务数据主动上报");
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		disposeTaskData(saveV5Data(data));
	//	list.add(updateRecoverStatus(data));
		sendStore(list);
		return null;
	}
	
	private List<StoreInfo> saveData(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV8QualityHistoryReport(data).getEquCode();
		List<QualityHistoryReportQuality> qaList = ((QualityHistoryReport)((Report)data.getData().getReportData()).getReport()).getQualityAlarm();
		if(qaList == null || qaList.size() <= 0)
			return list;
		for(QualityHistoryReportQuality quality : qaList) {
			for(QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(amodulation);
					qData.setQuality_code(amodulation);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				case 5:
					label = model.getQualLabel(fmodulation);
					qData.setQuality_code(fmodulation);
					qData.setMax_value(index.getMaxValue());
					qData.setMin_value(index.getMinValue());
					break;
				case 8:
					label = model.getQualLabel(bandwidth);
					qData.setQuality_code(bandwidth);
					qData.setQuality_value(index.getValue());
					break;
				}
				
				StoreInfo dInfo = buildStore(label.replaceFirst("insert", "delete"), qData);
				list.add(dInfo);
				StoreInfo iInfo = buildStore(label, qData);
				list.add(iInfo);
			}
		}
		qaList.clear();
		qaList = null;
		return list;
	}
	public StoreInfo buildStore(String label, QualityDataStore qData) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> map = null;
		try {
			map = NMBeanUtils.getObjectField(qData);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}finally {
			qData = null;
		}
		return info;
	}
	private MonitorDevice getMonitorDevice(String srcCode) {
		return CollectTaskModel.getInstance().getMonitorDevice(srcCode);
	}
	private List<StoreInfo> saveV7Data(CollectData data) {
		QualityDataStore qData = null;
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		String equCode = getV7QualityHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality> qaList = getV7QualityHistoryList(data);
		if(qaList == null || qaList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality quality : qaList) {
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setReceiver_code(equCode);
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(amodulation);
					qData.setQuality_code(amodulation);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				case 5:
					label = model.getQualLabel(fmodulation);
					qData.setQuality_code(fmodulation);
					qData.setMax_value(index.getMaxValue());
					qData.setMin_value(index.getMinValue());
					break;
				}
				
				StoreInfo info = new StoreInfo();
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
		String equCode = getV5QualityHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQuality> qaList = getV5QualityHistoryList(data);
		if(qaList == null || qaList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQuality quality : qaList) {
			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 2:
					label = model.getQualLabel(ammodulationRate);
					qData.setQuality_code(ammodulationRate);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(fmmodulationRate);
					qData.setQuality_code(fmmodulationRate);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;

				}
				
				StoreInfo info = new StoreInfo();
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
		String equCode = getV6QualityHistoryReport(data).getEquCode();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQuality> qaList = getV6QualityHistoryList(data);
		if(qaList == null || qaList.size() <= 0)
			return list;
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQuality quality : qaList) {
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQualityQualityIndex index : quality.getQualityHistoryReport_Quality_QualityIndexs()) {
				qData = fillQualityDataStore(data);
				qData.setBand(Integer.parseInt(quality.getBand()));
				qData.setCollect_time(quality.getCheckDateTime());
				qData.setReceiver_code(equCode);
				qData.setFrequency(quality.getFreq());
				qData.setStore_time(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				String label = null;
				int type = Integer.parseInt(index.getType());
				switch(type) {
				case 1:
					label = model.getQualLabel(level);
					qData.setQuality_code(level);
					qData.setQuality_value(index.getValue());
					break;
				case 3:
					label = model.getQualLabel(amodulation);
					qData.setQuality_code(amodulation);
					qData.setQuality_value(index.getValue());
					break;
				case 4:
					label = model.getQualLabel(attenuation);
					qData.setQuality_code(attenuation);
					qData.setQuality_value(index.getValue());
					break;
				case 5:
					label = model.getQualLabel(fmodulation);
					qData.setQuality_code(fmodulation);
					qData.setMax_value(index.getMaxValue());
					qData.setMin_value(index.getMinValue());
					break;
				case 8:
					label = model.getQualLabel(bandwidth);
					qData.setQuality_code(bandwidth);
					qData.setQuality_value(index.getValue());
					break;
				}
				
				StoreInfo info = new StoreInfo();
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
	
	private com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport getV8QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReportQuality> getV7QualityHistoryList(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport()).getQualityAlarm();
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport getV7QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReportQuality> getV5QualityHistoryList(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport()).getQualityAlarm();
	}
	
	private com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport getV5QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
	private List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReportQuality> getV6QualityHistoryList(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport()).getQualityAlarm();
	}
	
	private com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport getV6QualityHistoryReport(CollectData data) {
		return (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityHistoryReport)((Report)data.getData().getReportData()).getReport();
	}
	
}
