package com.neusoft.gbw.cp.station.service;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.neusoft.gbw.cp.conver.IProtocolConver;
import com.neusoft.gbw.cp.conver.ProtocolConverFactory;
import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.station.service.build.ReportBuilder;
import com.neusoft.gbw.cp.station.service.transfer.servlet.ServletClientHandler;

public class DataProcessHandler implements Runnable {
	
	private BlockingQueue<String> queue;
	private IProtocolConver conver = null;
	private Dispatcher dispatch = null;
	private ServletClientHandler client = null;
	private ReportBuilder build = null;
	private AutoReportProcess autoProcess = null;
	
	public DataProcessHandler(BlockingQueue<String> queue) {
		this.queue = queue;
		dispatch = new Dispatcher();
		client = new ServletClientHandler();
		build = new ReportBuilder();
		build.init();
		autoProcess = new AutoReportProcess(queue,client,build);//主动上报
		autoProcess.start();
	}

	@Override
	public void run() {
		String xml = null;
		String report = null;
		while(true) {
			try {
				xml = queue.take();
				int version = getProtocolVersion(xml);
				conver = getConver(version);
				Query query = conver.decodeQuery(xml);
				ProtocolData data = dispatch.dispatch(query);
				
				switch(data.getType()) {
				case quality_realtime_query:
					report = build.builder(data);
					client.dispachDate(report);
					autoProcess.startQualityRrealQuery();
					break;
				default:
				   report = build.builder(data);
				   client.dispachDate(report);
				   break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			} catch (NXmlException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	private IProtocolConver getConver(int version) {
		switch(version) {
		case 5:
			conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV5();
			break;
		case 6:
			conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV6();
			break;
		case 7:
			conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV7();
			break;
		case 8:
			conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV8();
			break;
		}
		return conver;
	}
	
	private int getProtocolVersion(String protcolXML) {
		int versionNum = 8;
		String regex = "Version=.{3}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(protcolXML);
		m.find();
		String src = m.group();
		versionNum = Integer.parseInt(src.substring(9, 10));
		return versionNum;
	}
}
