package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.SpectrumRealtimeReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.SpectrumRealtimeReportSpectrumScan;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ScanResult;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.duties.quota.vo.QuotaItemValueVo;
import com.neusoft.gbw.duties.quota.vo.QuotaTypeVo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPAmfConverUtil;

public class SpectrumRealtimeReportProcessor implements ITaskProcess {
	
	private final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		return dispose(data);
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		return disposeV7Data(data);
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		return disposeV6Data(data);
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
//		return disposeV5Data(data);
		
		return null;
	}
	
	private Object dispose(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		QuotaTypeVo quota = null;
		QuotaItemValueVo quotaItem = null;
		List<QuotaItemValueVo> itemList = null;
		List<SpectrumRealtimeReportSpectrumScan> spectrumList = null;
		if(report.getReport() instanceof SpectrumRealtimeReport) {
			SpectrumRealtimeReport spectrumRealtimeReport = (SpectrumRealtimeReport)report.getReport();
			spectrumList = spectrumRealtimeReport.getSpectrumScans();
		}
		if(spectrumList == null || spectrumList.size() == 0)
			return null;
		for (SpectrumRealtimeReportSpectrumScan spectrum : spectrumList) {
			String scanDateTime = spectrum.getScanDateTime();
			for(ScanResult spectrumIndex : spectrum.getScanResults()){
				quota = new QuotaTypeVo();
				itemList = new ArrayList<QuotaItemValueVo>();
				
				quota.setQuotaType("9");
				String freq = spectrumIndex.getFreq();
				String level = spectrumIndex.getLevel();
				quotaItem = new QuotaItemValueVo();
				double lFreq = Double.valueOf(freq);
				BigDecimal b = new BigDecimal(freq);
				if(lFreq>=531 && lFreq<=1602){
				}else{
					b =b.movePointLeft(3);
				}
				freq = String.valueOf(b);
				quotaItem.setAchieveValue(freq);
				quotaItem.setAchieveMAXValue(level);
				quotaItem.setAchieveTime(getTime(scanDateTime));
				itemList.add(quotaItem);
				quota.setQuotaItemValueArray(itemList);
				sendQualityQuery(quota);
			}
		}
		return null;
	}
	
	private Object disposeV7Data(CollectData data) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport qualityReport = getV7QualityRealtimeReport(data);
		QuotaTypeVo quota = null;
		QuotaItemValueVo quotaItem = null;
		List<QuotaItemValueVo> itemList = null;
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReportSpectrumScan> qualityList = qualityReport.getSpectrumScans();
		if(qualityList == null || qualityList.size() == 0)
			return null;
		for (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReportSpectrumScan quality : qualityList) {
			String checkDate = quality.getScanDateTime();
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ScanResult qualityIndex : quality.getScanResults()) {
				quota = new QuotaTypeVo();
				itemList = new ArrayList<QuotaItemValueVo>();
				quota.setQuotaType("9");
				quota.setQuotaItemValueArray(itemList);
				String freq = qualityIndex.getFreq();
				String level = qualityIndex.getLevel();
				quotaItem = new QuotaItemValueVo();
				double lFreq = Double.valueOf(freq);
				BigDecimal b = new BigDecimal(freq);
				if(lFreq>=531 && lFreq<=1602){
				}else{
					b =b.movePointLeft(3);
				}
				freq = String.valueOf(b);
				quotaItem.setAchieveValue(freq);
				quotaItem.setAchieveMAXValue(level);
				quotaItem.setAchieveTime(getTime(checkDate));
				itemList.add(quotaItem);
				sendQualityQuery(quota);
			}
		}
		return null;
	}
	
	private Object disposeV6Data(CollectData data) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport qualityReport = getV6QualityRealtimeReport(data);
		QuotaTypeVo quota = null;
		QuotaItemValueVo quotaItem = null;
		List<QuotaItemValueVo> itemList = null;
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReportSpectrumScan> qualityList = qualityReport.getSpectrumScans();
		if(qualityList == null || qualityList.size() == 0)
			return null;
		for (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReportSpectrumScan quality : qualityList) {
			String checkDate = quality.getScanDateTime();
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ScanResult qualityIndex : quality.getScanResults()) {
				quota = new QuotaTypeVo();
				itemList = new ArrayList<QuotaItemValueVo>();
				quota.setQuotaType("9");
				quota.setQuotaItemValueArray(itemList);
				String freq = qualityIndex.getFreq();
				String level = qualityIndex.getLevel();
				quotaItem = new QuotaItemValueVo();
				double lFreq = Double.valueOf(freq);
				BigDecimal b = new BigDecimal(freq);
				if(lFreq>=531 && lFreq<=1602){
				}else{
					b =b.movePointLeft(3);
				}
				freq = String.valueOf(b);
				quotaItem.setAchieveValue(freq);
				quotaItem.setAchieveMAXValue(String.valueOf(Math.abs(Double.parseDouble(level))));
				quotaItem.setAchieveTime(getTime(checkDate));
				itemList.add(quotaItem);
				sendQualityQuery(quota);
			}
		}
		return null;
	}
	
//	private Object disposeV5Data(CollectData data) {
//		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReport qualityReport = getV5QualityRealtimeReport(data);
//		QuotaTypeVo quota = null;
//		QuotaItemValueVo quotaItem = null;
//		List<QuotaItemValueVo> itemList = null;
//		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReportQuality> qualityList = qualityReport.getQualityRealtimeReport_Quality();
//		for (com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReportQuality quality : qualityList) {
//			String checkDate = quality.getCheckDateTime();
//			for(com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityRealtimeReportQualityQualityIndex qualityIndex : quality.getQualityRealtimeReport_Quality_QualityIndex()) {
//				quota = new QuotaTypeVo();
//				itemList = new ArrayList<QuotaItemValueVo>();
//				
//				String type = qualityIndex.getType();
//				quota.setQuotaType(type);
//				quota.setQuotaItemValueArray(itemList);
//				if (type.equals(FM_MODULATION_MAX)) {
//					String[] valueMaxArray = qualityIndex.getMaxValue().split(",");
//					String[] valueMinArray = qualityIndex.getMinValue().split(",");
//					int unit = getUnit(valueMaxArray);
//					for(int i = 0; i < valueMaxArray.length; i++) {
//						quotaItem = new QuotaItemValueVo();
//						quotaItem.setAchieveValue(valueMinArray[i]);
//						quotaItem.setAchieveMAXValue(valueMaxArray[i]);
//						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
//						itemList.add(quotaItem);
//					}
//				} else {
//					String[] valueArray = qualityIndex.getValue().split(",");
//					int unit = getUnit(valueArray);
//					for(int i = 0; i < valueArray.length; i++) {
//						quotaItem = new QuotaItemValueVo();
//						quotaItem.setAchieveValue(valueArray[i]);
//						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
//						itemList.add(quotaItem);
//					}
//				}
//				
//				sendQualityQuery(quota);
//			}
//		}
//		return null;
//	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport getV7QualityRealtimeReport(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.SpectrumRealtimeReport)report.getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport getV6QualityRealtimeReport(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		return (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.SpectrumRealtimeReport)report.getReport();
	}
	
	private String getTime(String checkDate) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		Date date = DateUtil.dateString(checkDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return format.format(c.getTime());
	}
	
	private void sendQualityQuery(QuotaTypeVo quota) {
		byte[] result = null;  
		Map<String, Object> quality = new HashMap<String, Object>();
		quality.put("status", 0);
		quality.put("message", "data");
		quality.put("data", quota);
		try {
			result = NPAmfConverUtil.getInstance().convert(quality);
			Log.debug("[实时频谱主动上报]发送实时频谱上报数据至前台，quality=" + quality);
			ARSFToolkit.sendEvent(EventServiceTopic.SOCKET_SERVICE_TOPIC, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
