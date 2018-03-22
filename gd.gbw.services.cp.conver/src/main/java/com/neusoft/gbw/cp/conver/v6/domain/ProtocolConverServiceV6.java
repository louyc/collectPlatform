package com.neusoft.gbw.cp.conver.v6.domain;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.neusoft.gbw.cp.conver.IProtocolConver;
import com.neusoft.gbw.cp.conver.ProtocolConverFactory;
import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.conver.util.StandaloneWriter;
import com.thoughtworks.xstream.XStream;

public class ProtocolConverServiceV6 implements IProtocolConver {
	public static void main(String[] args) throws NXmlException {
		IProtocolConver iConver = ProtocolConverFactory.getInstance()
				.newProtocolConverServiceV7();
		String recoveryXMLDate = "<?xml version='1.0' encoding='GB2312' standalone='yes'?><Msg Version='6' MsgID='141216140806243' Type='RadioUp' DateTime='2014-12-16 14:08:06' SrcCode='SJZXCJD' DstCode='CBT01' ReplyID='13030779'><Return Type='EquipmentInitParamSet' Value='0' Desc='成功'></Return></Msg>";
		System.out.println(recoveryXMLDate);
		Report r = iConver.decodeReport(recoveryXMLDate);
		System.out.println(r);
	}

	@Override
	public Query decodeQuery(String xml) throws NXmlException {
		try {
			return innerDecodeQuery(xml);
		} catch (DocumentException e) {
			throw new NXmlException(e);
		}
	}

	public static Query innerDecodeQuery(String xml) throws DocumentException,
			NXmlException {
		Query queryInfo = new Query();
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		queryInfo.setVersion(root.attribute("Version")
				.getValue());
		queryInfo.setMsgID(root.attribute("MsgID").getValue());
		queryInfo.setType(root.attribute("Type").getValue());
		queryInfo.setDateTime(root.attribute("DateTime").getValue());
		queryInfo.setSrcCode(root.attribute("SrcCode").getValue());
		queryInfo.setDstCode(root.attribute("DstCode").getValue());
		
			queryInfo.setPriority(root.attribute("Priority")
					.getValue());
		
		@SuppressWarnings("rawtypes")
		Iterator rootIter = root.elementIterator();
		Element sub = (Element) rootIter.next();
		XStream xstream = XStreamFactory.getXStream(sub.getName());
		if (xstream == null) {
			throw new NXmlException(sub.getName() + "不存在");
		}
		String subXml = sub.asXML();
		StringReader strReader = new StringReader(subXml);
		Object obj = xstream.fromXML(strReader);
		queryInfo.setQuery((IQuery) obj);
		return queryInfo;
	}

	@Override
	public Report decodeReport(String xml) throws NXmlException {
		try {
			return innerDecodeReport(xml);
		} catch (DocumentException e) {
			throw new NXmlException(e);
		}
	}

	public static Report innerDecodeReport(String xml)
			throws DocumentException, NXmlException {
		Document document = DocumentHelper.parseText(xml);
		// 获取文档的根节点
		Element root = document.getRootElement();
		Report reportInfo = new Report();
		reportInfo.setVersion(root.attribute("Version")
				.getValue());
		reportInfo.setMsgID(root.attribute("MsgID").getValue());
		reportInfo.setType(root.attribute("Type").getValue());
		reportInfo.setDateTime(root.attribute("DateTime").getValue());
		reportInfo.setSrcCode(root.attribute("SrcCode").getValue());
		reportInfo.setDstCode(root.attribute("DstCode").getValue());
		reportInfo.setReplyID(root.attribute("ReplyID").getValue());
		// 获取根节点下所有子节点
		@SuppressWarnings("rawtypes")
		Iterator rootIter = root.elementIterator();

		// 循环遍历
		while (rootIter.hasNext()) {

			Element returnElement = (Element) rootIter.next();
			// 常量写前面防止空指针异常
			if (("Return").equals(returnElement.getName())) {
				Return ret = processReturn(returnElement);
				reportInfo.setReportReturn(ret);
				// if (rootIter.hasNext()) {
				//
				// Element reportElement = (Element) rootIter.next();
				// String subXml = reportElement.asXML();
				// XStream xstream = XStreamFactory.getXStream(name);
				// // 将字符串转化为java对象
				// StringReader strReader2 = new StringReader(subXml);
				// Object obj = xstream.fromXML(strReader2);
				// reportInfo.setReport((IReport) obj);
				// }
				// return reportInfo;
			} else {
				String subXml = returnElement.asXML();
				String name = returnElement.getName();
				XStream xstream = XStreamFactory.getXStream(name);
				if (xstream == null) {
					throw new NXmlException(name + "不存在");
				}
				// 将字符串转化为java对象
				StringReader strReader2 = new StringReader(subXml);
				Object obj = xstream.fromXML(strReader2);
				reportInfo.setReport((IReport) obj);
				// return reportInfo;
			}
		}
		return reportInfo;
	}

	public static Return processReturn(Element element) {
		XStream xstream = XStreamFactory.getReturnXStream();
		String subXml = element.asXML();
		StringReader strReader = new StringReader(subXml);
		return (Return) xstream.fromXML(strReader);
	}

	@Override
	public String encodeQuery(Query queryInfo) throws NXmlException {
		try {
			return innerEncodeQuery(queryInfo);
		} catch (IOException e) {
			throw new NXmlException(e);
		} catch (DocumentException e) {
			throw new NXmlException(e);
		}
	}

	public static String innerEncodeQuery(Query queryInfo) throws IOException,
			DocumentException {
		Document document = DocumentHelper.createDocument();
		Element msgElement = document.addElement("Msg");
		msgElement.addAttribute("Version",
				String.valueOf(queryInfo.getVersion()));
		msgElement.addAttribute("MsgID", String.valueOf(queryInfo.getMsgID()));
		msgElement.addAttribute("Type", queryInfo.getType());
		msgElement.addAttribute("DateTime", queryInfo.getDateTime());
		msgElement.addAttribute("SrcCode", queryInfo.getSrcCode());
		msgElement.addAttribute("DstCode", queryInfo.getDstCode());
		msgElement.addAttribute("Priority",
				String.valueOf(queryInfo.getPriority()));

		String name = queryInfo.getQuery().getClass().getSimpleName();
		XStream xstream = XStreamFactory.getXStream(name);
		String queryInfoStr = xstream.toXML(queryInfo.getQuery());
		Document queryDocument = DocumentHelper.parseText(queryInfoStr);
		Element queryElement = queryDocument.getRootElement();
		msgElement.add(queryElement);
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GB2312");
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = new StandaloneWriter(stringWriter, format);
		writer.write(document);
		writer.flush();
		writer.close();
		return stringWriter.toString();
	}

	@Override
	public String encodeReport(Report reportInfo) throws NXmlException {
		try {
			return innerEncodeReport(reportInfo);
		} catch (IOException e) {
			throw new NXmlException(e);
		} catch (DocumentException e) {
			throw new NXmlException(e);
		}
	}

	public static String innerEncodeReport(Report reportInfo)
			throws IOException, DocumentException {
		Document document = DocumentHelper.createDocument();
		Element msgElement = document.addElement("Msg");
		msgElement.addAttribute("Version",
				String.valueOf(reportInfo.getVersion()));
		msgElement.addAttribute("MsgID", String.valueOf(reportInfo.getMsgID()));
		msgElement.addAttribute("Type", reportInfo.getType());
		msgElement.addAttribute("DateTime", reportInfo.getDateTime());
		msgElement.addAttribute("SrcCode", reportInfo.getSrcCode());
		msgElement.addAttribute("DstCode", reportInfo.getDstCode());
		msgElement.addAttribute("ReplyID",
				String.valueOf(reportInfo.getReplyID()));
		if (!("-1").equals(reportInfo.getReplyID())) {
			String reportReturn = XStreamFactory.getReturnXStream().toXML(
					reportInfo.getReportReturn());
			Document documentReturn = DocumentHelper.parseText(reportReturn);
			Element returnElement = documentReturn.getRootElement();
			msgElement.add(returnElement);
			if (reportInfo.getReport() != null) {
				String nameReport = reportInfo.getReport().getClass()
						.getSimpleName();
				XStream xstream = XStreamFactory.getXStream(nameReport);
				String report = xstream.toXML(reportInfo.getReport());
				Document documentReport = DocumentHelper.parseText(report);
				Element reportElement = documentReport.getRootElement();
				msgElement.add(reportElement);
			}
		} else {
			String nameReport = reportInfo.getReport().getClass().getSimpleName();
			XStream xstream = XStreamFactory.getXStream(nameReport);
			String report = xstream.toXML(reportInfo.getReport());
			Document documentReport = DocumentHelper.parseText(report);
			Element reportElement = documentReport.getRootElement();
			msgElement.add(reportElement);
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GB2312");
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = new StandaloneWriter(stringWriter, format);
		writer.write(document);
		writer.flush();
		writer.close();
		return stringWriter.toString();
	}

}
