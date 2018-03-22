package com.neusoft.gbw.cp.build.domain.build.quality;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.Param;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmParam;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmParamAlarmParam;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityAlarmParamSet;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.domain.monitor.intf.dto.KpiAlarmParamDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO;

public class QualityAlarmParamSetV6Builder {

	public static Object buildQualityAlarmParamSet(MoniSetingDTO dto, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		long monitorID = Long.parseLong(dto.getMonitorId());
		QualityAlarmParamSet paramSet = new QualityAlarmParamSet();
		query.setQuery(paramSet);
		query.setVersion(BuildConstants.XML_VERSION_6 + "");
		query.setMsgID(DataUtils.getMsgID(paramSet) + "");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority() + "");
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		
		List<KpiAlarmParamDTO> dtoList = dto.getKpiList();
		
		paramSet.setQualityAlarmParams(getQualityAlarmParam(dtoList));
		
		return query;
	}
	
	private static List<QualityAlarmParam> getQualityAlarmParam(List<KpiAlarmParamDTO> dtoList) {
		List<QualityAlarmParam> paramList = new ArrayList<QualityAlarmParam>();
		
		for(KpiAlarmParamDTO kpiDto: dtoList) {
			String[] enCodes = kpiDto.getMachineId().split(",");
			if(kpiDto.getMachineId().equals("-1")) {
				QualityAlarmParam alarmParam = new QualityAlarmParam();
				alarmParam.setEquCode(kpiDto.getMachineId().equals("-1") ? "" : kpiDto.getMachineId());
				alarmParam.setBand(kpiDto.getBandId());
				alarmParam.setFreq(kpiDto.getFreq());
				alarmParam.setAlarmParamSets(getAlarmParamList(kpiDto));
				paramList.add(alarmParam);
				continue;
			}
			
			for(int i=0 ; i< enCodes.length ; i++) {
				QualityAlarmParam alarmParam = new QualityAlarmParam();
				alarmParam.setEquCode(enCodes[i].equals("-1")? "" : enCodes[i]);
				alarmParam.setBand(kpiDto.getBandId());
				alarmParam.setFreq(kpiDto.getFreq());
				alarmParam.setAlarmParamSets(getAlarmParamList(kpiDto));
				paramList.add(alarmParam);
			}
		}
		return paramList;
		
	}
	
	private static List<QualityAlarmParamAlarmParam> getAlarmParamList(KpiAlarmParamDTO kpiDto) {
		List<QualityAlarmParamAlarmParam> list = new ArrayList<QualityAlarmParamAlarmParam>();
		list.add(getAlarmParam(BuildConstants.QualityAlarmParamSetTask.LEVEL_TYPE, kpiDto));
		String band = kpiDto.getBandId();
		if(band.equals(BuildConstants.CHANNEL_BAND_FREQ)) {
			list.add(getAlarmParam(BuildConstants.QualityAlarmParamSetTask.FM_MODULATION_TYPE, kpiDto));
		}else if(band.equals(BuildConstants.CHANNEL_BAND_CENTRE) || band.equals(BuildConstants.CHANNEL_BAND_SHORT)){
			list.add(getAlarmParam(BuildConstants.QualityAlarmParamSetTask.AM_MODULATION_TYPE, kpiDto));
		}else {
			list.add(getAlarmParam(BuildConstants.QualityAlarmParamSetTask.FM_MODULATION_TYPE, kpiDto));
			list.add(getAlarmParam(BuildConstants.QualityAlarmParamSetTask.AM_MODULATION_TYPE, kpiDto));
		}
		list.add(getAlarmParam(BuildConstants.QualityAlarmParamSetTask.ATTENUATION_TYPE, kpiDto));
		return list;
		
	}
	
	private static QualityAlarmParamAlarmParam getAlarmParam(String type,KpiAlarmParamDTO kpiDto) {
		QualityAlarmParamAlarmParam param = new QualityAlarmParamAlarmParam();
		if(type.equals(BuildConstants.QualityAlarmParamSetTask.LEVEL_TYPE)) {
			param.setDesc(BuildConstants.QualityAlarmParamSetTask.LEVEL_DESC);
			param.setType(type);
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.LEVELDOWNTHRESHOLD, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.LEVELABNORMITYLENGTH, kpiDto));
		}
		else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FM_MODULATION_TYPE)) {
			param.setDesc(BuildConstants.QualityAlarmParamSetTask.FM_MODULATION_DESC);
			param.setType(type);
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONSAMPLELENGTH, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONDOWNTHRESHOLD, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONUPTHRESHOLD, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONDOWNABNORMITYRATE, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONUPABNORMITYRATE, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONABNORMITYLENGTH, kpiDto));
		}
		else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AM_MODULATION_TYPE)) {
			param.setDesc(BuildConstants.QualityAlarmParamSetTask.AM_MODULATION_DESC);
			param.setType(type);
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONDOWNTHRESHOLD, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONSAMPLELENGTH, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONUPTHRESHOLD, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONDOWNABNORMITYRATE, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONUPABNORMITYRATE, kpiDto));
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONABNORMITYLENGTH, kpiDto));
		}
		else if(type.equals(BuildConstants.QualityAlarmParamSetTask.ATTENUATION_TYPE)) {
			param.setDesc(BuildConstants.QualityAlarmParamSetTask.ATTENUATION_DESC);
			param.setType(type);
			param.addParam(getParams(BuildConstants.QualityAlarmParamSetTask.ATTENUATION, kpiDto));
		}
		return param;
		
	}
	
	private static Param getParams(String type, KpiAlarmParamDTO kpiDto) {
		Param param = new Param();
		if(type.equals(BuildConstants.QualityAlarmParamSetTask.LEVELDOWNTHRESHOLD)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_DOWNTHRESHOLD);
			param.setValue(kpiDto.getLevelDownThreshold());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.LEVELABNORMITYLENGTH)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_ABNORMITYLENGTH);
			param.setValue(kpiDto.getLevelAbnormityLength());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONSAMPLELENGTH)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_SAMPLELENGTH);
			param.setValue(kpiDto.getFmModulationSampleLength());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONDOWNTHRESHOLD)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_DOWNTHRESHOLD);
			param.setValue(kpiDto.getFmModulationDownThreshold());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONUPTHRESHOLD)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_UPTHRESHOLD);
			param.setValue(kpiDto.getFmModulationUpThreshold());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONABNORMITYLENGTH)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_ABNORMITYLENGTH);
			param.setValue(kpiDto.getFmModulationAbnormityLength());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONDOWNABNORMITYRATE)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_DOWNABNORMITYRATE);
			param.setValue(kpiDto.getFmModulationDownAbnormityRate());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.FMMODULATIONUPABNORMITYRATE)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_UPABNORMITYRATE);
			param.setValue(kpiDto.getFmModulationUpAbnormityRate());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONDOWNTHRESHOLD)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_DOWNTHRESHOLD);
			param.setValue(kpiDto.getAmModulationDownThreshold());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONSAMPLELENGTH)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_SAMPLELENGTH);
			param.setValue(kpiDto.getAmModulationSampleLength());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONUPTHRESHOLD)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_UPTHRESHOLD);
			param.setValue(kpiDto.getAmModulationUpThreshold());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONDOWNABNORMITYRATE)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_DOWNABNORMITYRATE);
			param.setValue(kpiDto.getAmModulationDownAbnormityRate());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONUPABNORMITYRATE)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_UPABNORMITYRATE);
			param.setValue(kpiDto.getAmModulationUpAbnormityRate());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.AMMODULATIONABNORMITYLENGTH)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.BASE_ABNORMITYLENGTH);
			param.setValue(kpiDto.getAmModulationAbnormityLength());
		}else if(type.equals(BuildConstants.QualityAlarmParamSetTask.ATTENUATION)) {
			param.setName(BuildConstants.QualityAlarmParamSetTask.ATTENUATION);
			param.setValue(kpiDto.getAttenuation());
		}
		return param;
	}
}
