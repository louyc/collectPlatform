package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReportQuality;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityRealtimeReportQualityQualityIndex;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.special.QualityRealTimeReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.duties.quota.vo.QuotaItemValueVo;
import com.neusoft.gbw.duties.quota.vo.QuotaTypeVo;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPAmfConverUtil;
import com.neusoft.np.arsf.net.core.NetEventType;

public class QualityRealtimeReportProcessor implements ITaskProcess {
	
	private final String FM_MODULATION_MAX = "5";
	private final String OFFSET_LISHAYU = "7";
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
		List<QualityRealtimeReportQuality> qualityList = null;
		if(report.getReport() instanceof QualityRealtimeReport) {
			QualityRealtimeReport qualitytimeReport = (QualityRealtimeReport)report.getReport();
			qualityList = qualitytimeReport.getQualityRealtimeReport_Quality();
		}else if(report.getReport() instanceof QualityRealTimeReport) {
	    	QualityRealTimeReport qualityTimeReport = (QualityRealTimeReport)report.getReport();
	    	qualityList = qualityTimeReport.getQualityRealtimeReport_Quality();
		}
		if(qualityList == null || qualityList.size() == 0)
			return null;
		for (QualityRealtimeReportQuality quality : qualityList) {
			String checkDate = quality.getCheckDateTime();
			for(QualityRealtimeReportQualityQualityIndex qualityIndex : quality.getQualityRealtimeReport_Quality_QualityIndex()) {
				quota = new QuotaTypeVo();
				itemList = new ArrayList<QuotaItemValueVo>();
				
				String type = qualityIndex.getType();
				quota.setQuotaType(type);
				quota.setQuotaItemValueArray(itemList);
				if (type.equals(FM_MODULATION_MAX)) {
					String[] valueMaxArray = qualityIndex.getMaxValue().split(",");
					String[] valueMinArray = qualityIndex.getMinValue().split(",");
					int unit = getUnit(valueMaxArray);
					for(int i = 0; i < valueMaxArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueMinArray[i]);
						quotaItem.setAchieveMAXValue(valueMaxArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueMinArray));
//					quotaItem.setAchieveMAXValue(getAverage(valueMaxArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				} else {
					String[] valueArray = qualityIndex.getValue().split(",");
					int unit = getUnit(valueArray);
					for(int i = 0; i < valueArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				}
				sendQualityQuery(quota);
			}
		}
		return null;
	}
	
	private Object disposeV7Data(CollectData data) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport qualityReport = getV7QualityRealtimeReport(data);
		QuotaTypeVo quota = null;
		QuotaItemValueVo quotaItem = null;
		List<QuotaItemValueVo> itemList = null;
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReportQuality> qualityList = qualityReport.getQualityRealtimeReport_Quality();
		if(qualityList == null || qualityList.size() == 0)
			return null;
		for (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReportQuality quality : qualityList) {
			String checkDate = quality.getCheckDateTime();
			for(com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReportQualityQualityIndex qualityIndex : quality.getQualityRealtimeReport_Quality_QualityIndex()) {
				quota = new QuotaTypeVo();
				itemList = new ArrayList<QuotaItemValueVo>();
//				
				String type = qualityIndex.getType();
				quota.setQuotaType(type);
				quota.setQuotaItemValueArray(itemList);
				if (type.equals(FM_MODULATION_MAX)) {
					String[] valueMaxArray = qualityIndex.getMaxValue().split(",");
					String[] valueMinArray = qualityIndex.getMinValue().split(",");
					int unit = getUnit(valueMaxArray);
					for(int i = 0; i < valueMaxArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueMinArray[i]);
						quotaItem.setAchieveMAXValue(valueMaxArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueMinArray));
//					quotaItem.setAchieveMAXValue(getAverage(valueMaxArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				} else {
					String[] valueArray = qualityIndex.getValue().split(",");
					int unit = getUnit(valueArray);
					for(int i = 0; i < valueArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				}
				sendQualityQuery(quota);
			}
		}
		return null;
	}
	
	private Object disposeV6Data(CollectData data) {
		
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport qualityReport = getV6QualityRealtimeReport(data);
		QuotaTypeVo quota = null;
		QuotaItemValueVo quotaItem = null;
		List<QuotaItemValueVo> itemList = null;
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReportQuality> qualityList = qualityReport.getQualityRealtimeReport_Quality();
		if(qualityList == null || qualityList.size() == 0)
			return null;
		for (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReportQuality quality : qualityList) {
			String checkDate = quality.getCheckDateTime();
			if(null ==quality.getQualityRealtimeReport_Quality_QualityIndex() || quality.getQualityRealtimeReport_Quality_QualityIndex().size()<1){
				return null;
			}
			for(com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReportQualityQualityIndex qualityIndex : quality.getQualityRealtimeReport_Quality_QualityIndex()) {
				quota = new QuotaTypeVo();
				itemList = new ArrayList<QuotaItemValueVo>();
				
				String type = qualityIndex.getType();
				quota.setQuotaType(type);
				quota.setQuotaItemValueArray(itemList);
				if (type.equals(FM_MODULATION_MAX)) {
					String[] valueMaxArray = qualityIndex.getMaxValue().split(",");
					String[] valueMinArray = qualityIndex.getMinValue().split(",");
					int unit = getUnit(valueMaxArray);
					for(int i = 0; i < valueMaxArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueMinArray[i]);
						quotaItem.setAchieveMAXValue(valueMaxArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueMinArray));
//					quotaItem.setAchieveMAXValue(getAverage(valueMaxArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				}else if(type.equals(OFFSET_LISHAYU)) { 
					String[] valueMaxArray = qualityIndex.getxValue().split(",");
					String[] valueMinArray = qualityIndex.getyValue().split(",");
					int unit = getUnit(valueMaxArray);
					for(int i = 0; i < valueMaxArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueMinArray[i]);
						quotaItem.setAchieveMAXValue(valueMaxArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueMinArray));
//					quotaItem.setAchieveMAXValue(getAverage(valueMaxArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				}else {
					String[] valueArray = qualityIndex.getValue().split(",");
					int unit = getUnit(valueArray);
					for(int i = 0; i < valueArray.length; i++) {
						quotaItem = new QuotaItemValueVo();
						quotaItem.setAchieveValue(valueArray[i]);
						quotaItem.setAchieveTime(getTime(checkDate, unit, i));
						itemList.add(quotaItem);
					}
//					quota = new QuotaTypeVo();
//					itemList = new ArrayList<QuotaItemValueVo>();
//					quota.setQuotaType(type);
//					quota.setQuotaItemValueArray(itemList);
//					quotaItem = new QuotaItemValueVo();
//					quotaItem.setAchieveValue(getAverage(valueArray));
//					quotaItem.setAchieveTime(checkDate);
//					itemList.add(quotaItem);
//					sendQualityQuery(quota);
				}
				sendQualityQuery(quota);
			}
		}
		return null;
	}
	
	private com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport getV7QualityRealtimeReport(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		return (com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityRealtimeReport)report.getReport();
	}
	
	private com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport getV6QualityRealtimeReport(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		return (com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityRealtimeReport)report.getReport();
	}
	
	private int getUnit(String[] valueArray) {
		int size = valueArray.length;
		return 1000 / size ;
	}
	
	private String getTime(String checkDate, int unit, int index) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		Date date = DateUtil.dateString(checkDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MILLISECOND, unit*index);
		
		return format.format(c.getTime());
	}
	/**
	 * 取平均值
	 * @param valueArray
	 * @return
	 */
	private String getAverage(String[] valueArray){
		String average ="";
		double sum=0;
		if(valueArray.length>1){
			for(String value: valueArray){
				sum+=Double.valueOf(value);
			}
			average = String.valueOf(sum/valueArray.length);
		}else{
			average = valueArray[0];
		}
		return average;
	}
	
	private void sendQualityQuery(QuotaTypeVo quota) {
		byte[] result = null;
		Map<String, Object> quality = new HashMap<String, Object>();
		quality.put("status", 0);
		quality.put("message", "data");
		quality.put("data", quota);
		try {
			result = NPAmfConverUtil.getInstance().convert(quality);
			Log.debug("[实时指标主动上报]发送实时指标上报数据至前台，quality=" + quality);
			ARSFToolkit.sendEvent(EventServiceTopic.SOCKET_SERVICE_TOPIC, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			result = null;
//			quality = null;
//		}
	}
	
	
	private void disposeError() {
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessDicDesc("站点返回指标数据为空");
		recover.setSuccessType(ProcessConstants.TASK_SEND_ERROR_TYPE);
		JMSDTO jms = createDTO(recover);
		sendTask(jms);
	}
	
	private JMSDTO createDTO(RecoveryMessageDTO recover) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(GBWMsgConstant.C_JMS_REAL_KPI_RESPONSE_MSG);
		jms.setObj(recover);
		return jms;
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
}
