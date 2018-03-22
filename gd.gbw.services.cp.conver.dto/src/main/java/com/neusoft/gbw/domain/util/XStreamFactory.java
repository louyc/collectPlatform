package com.neusoft.gbw.domain.util;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamFactory {
	static Map<String, XStream> streamMap = new HashMap<String, XStream>();
	static XStream defaultStream;
	static {
		try {
			init();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	static void init() throws ClassNotFoundException {
		defaultStream = newXStream();
		defaultStream.processAnnotations(com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.app.monitorMgr.intf.dto.MoniControlDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDLBaseDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.app.monitorMgr.intf.dto.MoniLinkDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.app.monitorMgr.intf.dto.MoniStatusDTO.class);
		
		defaultStream.processAnnotations(com.neusoft.gbw.domain.abnormal.intf.dto.AbnormalEventDTO.class);
		
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO.class);
		
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.mgr.intf.dto.TaskConfDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.mgr.intf.dto.TaskFreqDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.mgr.intf.dto.TaskMonitorDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.mgr.intf.dto.TaskScheduleDTO.class);
		
		defaultStream.processAnnotations(com.neusoft.gbw.domain.monitor.intf.dto.KpiAlarmParamDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.monitor.intf.dto.MoniDeviceDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.monitor.intf.dto.MoniReportDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.domain.task.mgr.intf.dto.DownLoadFileDTO.class);
		
		defaultStream.processAnnotations(com.neusoft.gbw.infrastructure.sys.intf.dto.PlayerRecoveryDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO.class);
		defaultStream.processAnnotations(com.neusoft.gbw.duties.quota.vo.QuotaItemValueVo.class);
		defaultStream.processAnnotations(com.neusoft.gbw.duties.quota.vo.QuotaTypeVo.class);
		
		defaultStream.processAnnotations(com.neusoft.gbw.domain.evaluation.intf.dto.EvaluationGradeDTO.class);
	}
	
	private static XStream newXStream() {
		return new XStream(new DomDriver());
	}

	protected static XStream getXStream() {
		return defaultStream;
	}

}
