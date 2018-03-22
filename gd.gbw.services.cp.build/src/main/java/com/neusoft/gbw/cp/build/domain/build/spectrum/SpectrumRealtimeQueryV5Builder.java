package com.neusoft.gbw.cp.build.domain.build.spectrum;

import com.neusoft.gbw.app.evaluation.intf.dto.SpectrumRealtimeQueryDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumRealtimeQuery;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;

public class SpectrumRealtimeQueryV5Builder {

	public static Object buildSpectrumRealtimeQuery(SpectrumRealtimeQueryDTO dto, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		long monitorID = Long.parseLong(dto.getMonitorID());
		SpectrumRealtimeQuery spectrum = new SpectrumRealtimeQuery();
		query.setVersion(BuildConstants.XML_VERSION_5 + "");
		query.setMsgID((DataUtils.getMsgID(spectrum)+""));
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(BuildConstants.MAX_PRIORITY+"");
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		query.setQuery(spectrum);
		
		spectrum.setEquCode(dto.getEquCode() == null ? "":dto.getEquCode());
		spectrum.setType(dto.getType());
		spectrum.setFreq(dto.getFreq());
		spectrum.setBand(dto.getBand()+"");
		spectrum.setStartFreq(dto.getStartFreq());
		spectrum.setEndFreq(dto.getEndFreq());
		spectrum.setStepFreq(dto.getStepFreq());
		spectrum.setReportInterval(getReportInterval(dto.getRepInterval()));
		spectrum.setExpireTime(getExpireTime(dto.getExpireTime()));
		spectrum.setAction(dto.getAction());
		
		return query;
	}
	
	private static String getReportInterval(String in){
		int interval = Integer.parseInt(in);
		String time = "" ;
		if (interval<60) {
			time ="00:00:"+unitFormat(interval);
		}else {
			int minute = interval/60;
			int second = interval%60;
			time = "00"+":"+unitFormat(minute)+":"+unitFormat(second);
		}
		
		
		return time;
	}
	private static String getExpireTime(String ex){
		int expire = Integer.parseInt(ex);
		String time = "" ;
		if (expire<60) {
			time = "00"+":"+unitFormat(expire)+":"+"00";
		}else {
			int hour = expire/60;
			int minute = expire%60;
			time = unitFormat(hour)+":"+unitFormat(minute)+":"+"00";
		}	
		
		return time;
	}

	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
}
