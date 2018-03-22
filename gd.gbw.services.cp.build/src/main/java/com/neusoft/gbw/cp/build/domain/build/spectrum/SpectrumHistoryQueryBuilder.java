package com.neusoft.gbw.cp.build.domain.build.spectrum;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class SpectrumHistoryQueryBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryQuery buildSpectrumHistoryQuery(Task spTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryQuery spQuery = new com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumHistoryQuery();
		spQuery.setTaskID(DataUtils.getTaskID(spTask)+"");
		spQuery.setEquCode(spTask.getTaskFreq().getReceiver_code()==null?"":spTask.getTaskFreq().getReceiver_code());
		spQuery.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		spQuery.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
//		spQuery.setStartDateTime(spTask.getMeasureTask().getValidity_b_time());//modify by jiahao 不需要转换时间
//		spQuery.setEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spQuery.setStartDateTime(spTask.getMeasureTask().getRecover_b_time());
		spQuery.setEndDateTime(spTask.getMeasureTask().getRecover_e_time());
		
		return spQuery;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryQuery buildSpectrumHistoryQueryV5(Task spTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryQuery spQuery = new com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumHistoryQuery();
		spQuery.setTaskID(DataUtils.getTaskID(spTask)+"");
		spQuery.setEquCode(spTask.getTaskFreq().getReceiver_code()==null?"":spTask.getTaskFreq().getReceiver_code());
		spQuery.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
//		spQuery.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
//		spQuery.setStartDateTime(spTask.getMeasureTask().getValidity_b_time());//modify by jiahao 不需要转换时间
//		spQuery.setEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spQuery.setStartDateTime(spTask.getMeasureTask().getRecover_b_time());
		spQuery.setEndDateTime(spTask.getMeasureTask().getRecover_e_time());
		
		return spQuery;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryQuery buildSpectrumHistoryQueryV6(Task spTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryQuery spQuery = new com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumHistoryQuery();
		spQuery.setTaskID(DataUtils.getTaskID(spTask)+"");
		spQuery.setEquCode(spTask.getTaskFreq().getReceiver_code()==null?"":spTask.getTaskFreq().getReceiver_code());
		spQuery.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		spQuery.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
//		spQuery.setStartDateTime(spTask.getMeasureTask().getValidity_b_time());//modify by jiahao 不需要转换时间
//		spQuery.setEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spQuery.setStartDateTime(spTask.getMeasureTask().getRecover_b_time());
		spQuery.setEndDateTime(spTask.getMeasureTask().getRecover_e_time());
		
		return spQuery;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryQuery buildSpectrumHistoryQueryV7(Task spTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryQuery spQuery = new com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumHistoryQuery();
		spQuery.setTaskID(DataUtils.getTaskID(spTask)+"");
		spQuery.setEquCode(spTask.getTaskFreq().getReceiver_code()==null?"":spTask.getTaskFreq().getReceiver_code());
		spQuery.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		spQuery.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
//		spQuery.setStartDateTime(spTask.getMeasureTask().getValidity_b_time());//modify by jiahao 不需要转换时间
//		spQuery.setEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spQuery.setStartDateTime(spTask.getMeasureTask().getRecover_b_time());
		spQuery.setEndDateTime(spTask.getMeasureTask().getRecover_e_time());
		
		return spQuery;
	}
	
//	private static Date getStartDateTime(Task sTask) {
//		try {
//			return NMDateUtil.convertStringToDate(sTask.getMeasureTask().getValidity_b_time());
//		} catch (ParseException e) {
//			return null;
//		}
//	}
//	
//	private static Date getEndDateTime(Task sTask) {
//		try {
//			return NMDateUtil.convertStringToDate(sTask.getMeasureTask().getValidity_b_time());
//		} catch (ParseException e) {
//			return null;
//		}
//	}
}
