package com.neusoft.gbw.cp.station.service.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.neusoft.gbw.cp.conver.IProtocolConver;
import com.neusoft.gbw.cp.conver.ProtocolConverFactory;
import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityAlarmHistoryReport;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentAlarmHistoryReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.station.constants.ConfigVariable;
import com.neusoft.gbw.cp.station.service.ProtocolData;
import com.neusoft.gbw.cp.station.vo.ProtocolType;

public class ReportBuilder {

	private Map<String, String> xmlMap = null;
	private ProtocolConverFactory factory = ProtocolConverFactory.getInstance();
	// private String path = System.getProperty("user.dir") + "\\xml\\";
	// private String path =
	// "D:\\gbw_git\\gbw_40_Src\\30_app\\3001_platfrom\\com.neusoft.gbw.cp.station\\xml\\";
	// private String path = "D:\\software\\CollectPlatform\\bundles\\xml\\";
	private String path = "D:\\gbw_git\\gbw_40_Src\\30_app\\3002_platform4Mvn\\03_services\\gd.gbw.services.cp.station\\src\\main\\resources\\xml\\";

	public void init() {
		xmlMap = new HashMap<String, String>();
		for (File file : new File(path).listFiles()) {
			if (file.getName().equals(".svn")) {
				continue;
			}
			String fileName = file.getName().split("\\.")[0];
			String xml = readXmlFile(file);
			xmlMap.put(fileName, xml);
		}
	}

	@SuppressWarnings("null")
	private String readXmlFile(File file) {

		/**
		 * String line = ""; BufferedReader reader = null; StringBuffer buffer =
		 * new StringBuffer(); try { reader = new BufferedReader(new
		 * InputStreamReader(request.getInputStream(),"GB2312")) ; while((line =
		 * reader.readLine()) != null) { buffer.append(line); } String
		 * protcolXml =buffer.toString();
		 */
		String line = "";
		StringBuffer buffer = new StringBuffer();
		InputStream input = null;
		BufferedReader reader = null;
		try {
			input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					input, "GB2312"));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==reader&&null==input) {
				try {
					reader.close();
					input.close();
				} catch (IOException e) {
				}
				
			}
		}
		String xml = buffer.toString();

		return xml;

	}


	public String builder(ProtocolData data) {
		ProtocolType type = data.getType();
		if (!xmlMap.containsKey(type.name())) {
			return null;
		}

		String xml = xmlMap.get(type.name());

		try {
			Report report = ProtocolConverFactory.getInstance().newProtocolConverServiceV8().decodeReport(xml);
			report.setMsgID(System.currentTimeMillis() + "");
			if(!report.getReplyID().equals("-1"))
				report.setReplyID(data.getMsgID() + "");
			report.setSrcCode(data.getDstCode());
			report.setDstCode(data.getSrcCode());
			xml = ProtocolConverFactory.getInstance().newProtocolConverServiceV8().encodeReport(report);
			System.out.println("解析成功:" + xml);
		} catch (NXmlException e) {
			e.printStackTrace();
		}

		return xml;
	}
	
	public String builderQualityRrealQuery() {
		ProtocolType type = null;
		IProtocolConver conver = null;
		switch(ConfigVariable.REPORT_PROTOCOL_VERSION) {
		case 5:
			type = ProtocolType.v5_quality_realtime_query;
			conver = factory.newProtocolConverServiceV5();
			break;
		case 6:
			type = ProtocolType.v6_quality_realtime_query;
			conver = factory.newProtocolConverServiceV6();
			break;
		case 7:
			type = ProtocolType.v7_quality_realtime_query;
			conver = factory.newProtocolConverServiceV7();
			break;
		case 8:
			type = ProtocolType.quality_realtime_query;
			conver = factory.newProtocolConverServiceV8();
			break;
		}
		if (!xmlMap.containsKey(type.name())) {
			return null;
		}

		String xml = xmlMap.get(type.name());

		try {
			Report report = conver.decodeReport(xml);
			report.setMsgID(System.currentTimeMillis() + "");
			report.setReplyID("-1");
			xml = conver .encodeReport(report);
		} catch (NXmlException e) {
			e.printStackTrace();
		}

		return xml;
	}

	public Map<String, String> QualityAlarmBuilder() {
		ProtocolType type = null;
		IProtocolConver conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV8();
		switch(ConfigVariable.REPORT_PROTOCOL_VERSION) {
		case 5:
			type = ProtocolType.v5_quality_alarm_history_query;
			break;
		case 6:
			type = ProtocolType.v5_quality_alarm_history_query;
			break;
		case 7:
			type = ProtocolType.v7_quality_alarm_history_query;
			break;
		case 8:
			type = ProtocolType.quality_alarm_history_query;
			break;
		}
		if (!xmlMap.containsKey(type.name())) {
			return null;
		}
		String xml = xmlMap.get(type.name());
		Map<String, String> alarmMap = new TreeMap<String, String>();
		String[] alarmIDs = new String[ConfigVariable.REPORT_ALARM_NUM];
		// 创建报警数据
		for (int i = 0; i < ConfigVariable.REPORT_ALARM_NUM; i++) {
			String alarmID = getAlarmID();
			alarmMap.put(i + "", restructurnXml(xml, alarmID));
			alarmIDs[i] = alarmID;

			if (ConfigVariable.REPORT_ALARM_NUM - 1 == i) {
				String relieveAlarmID = alarmIDs[getRelieveAlarmId()];
				Report report;
				try {
					report = conver.decodeReport(xml);
					report.setReplyID("-1");
					((QualityAlarmHistoryReport) report.getReport()).getQualityAlarm().get(0).setAlarmID(relieveAlarmID);
					((QualityAlarmHistoryReport) report.getReport()).getQualityAlarm().get(0).setMode("1");
					((QualityAlarmHistoryReport) report.getReport()).getQualityAlarm().get(0).setCheckDateTime(getCurrentTime());
					((QualityAlarmHistoryReport) report.getReport()).getQualityAlarm().get(0).setDesc("电平低报警解除");
					((QualityAlarmHistoryReport) report.getReport()).getQualityAlarm().get(0).setReason("任务结束报警解除");
					alarmMap.put(i + "", ProtocolConverFactory.getInstance().newProtocolConverServiceV8().encodeReport(report));
				} catch (NXmlException e) {
					e.printStackTrace();
				}
			}
		}
		return alarmMap;

	}
	
	public Map<String, String> EquAlarmBuilder() {
		ProtocolType type = null;
		IProtocolConver conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV8();
		switch(ConfigVariable.REPORT_PROTOCOL_VERSION) {
		case 5:
			type = ProtocolType.v5_equipment_alarm_history_query;
			break;
		case 6:
			type = ProtocolType.v5_equipment_alarm_history_query;
			break;
		case 7:
			type = ProtocolType.v7_equipment_alarm_history_query;
			break;
		case 8:
			type = ProtocolType.equipment_alarm_history_query;
			break;
		}
		if (!xmlMap.containsKey(type.name())) {
			return null;
		}
		String xml = xmlMap.get(type.name());
		Map<String, String> alarmMap = new TreeMap<String, String>();
		String[] alarmIDs = new String[ConfigVariable.REPORT_ALARM_NUM];

		// 创建报警数据
		for (int i = 0; i < ConfigVariable.REPORT_ALARM_NUM; i++) {
			String alarmID = getAlarmID();
			alarmMap.put(i + "", restructurnXmlEqu(xml, alarmID,conver));
			alarmIDs[i] = alarmID;

			if (ConfigVariable.REPORT_ALARM_NUM - 1 == i) {
				String relieveAlarmID = alarmIDs[getRelieveAlarmId()];
				Report report;
				try {
					report = conver.decodeReport(xml);
					report.setReplyID("-1");
					((EquipmentAlarmHistoryReport) report.getReport()).getEquipmentAlarms().get(0).setAlarmID(relieveAlarmID);
					((EquipmentAlarmHistoryReport) report.getReport()).getEquipmentAlarms().get(0).setMode("1");
					((EquipmentAlarmHistoryReport) report.getReport()).getEquipmentAlarms().get(0).setCheckDateTime(getCurrentTime());
					((EquipmentAlarmHistoryReport) report.getReport()).getEquipmentAlarms().get(0).setDesc("电平低报警解除");
					((EquipmentAlarmHistoryReport) report.getReport()).getEquipmentAlarms().get(0).setReason("任务结束报警解除");
					alarmMap.put(i + "", ProtocolConverFactory.getInstance().newProtocolConverServiceV8().encodeReport(report));
				} catch (NXmlException e) {
					e.printStackTrace();
				}
			}
		}
		return alarmMap;

	}

	private String restructurnXml(String xml, String alarmID) {
		String new_xml = null;
		Report report;
		try {
			report = ProtocolConverFactory.getInstance().newProtocolConverServiceV8().decodeReport(xml);
			report.setReplyID("-1");
			((QualityAlarmHistoryReport) report.getReport()).getQualityAlarm()
					.get(0).setAlarmID(alarmID);
			new_xml = ProtocolConverFactory.getInstance().newProtocolConverServiceV8().encodeReport(report);
		} catch (NXmlException e) {
			e.printStackTrace();
		}
		return new_xml;
	}
	
	private String restructurnXmlEqu(String xml, String alarmID, IProtocolConver conver) {
		String new_xml = null;
		Report report;
		try {
			report = conver.decodeReport(xml);
			report.setReplyID("-1");
			((EquipmentAlarmHistoryReport) report.getReport()).getEquipmentAlarms()
					.get(0).setAlarmID(alarmID);
			new_xml = conver.encodeReport(report);
		} catch (NXmlException e) {
			e.printStackTrace();
		}
		return new_xml;
	}

	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date()).toString();
	}

	private String getAlarmID() {
		return String.valueOf(Math.random()).substring(4,10);
	}

	private int getRelieveAlarmId() {
		return (int) (Math.random() * (ConfigVariable.REPORT_ALARM_NUM - 2)) + 1;
	}
}
