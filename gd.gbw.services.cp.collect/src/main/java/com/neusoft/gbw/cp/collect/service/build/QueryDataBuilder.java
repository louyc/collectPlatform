package com.neusoft.gbw.cp.collect.service.build;

import com.neusoft.gbw.cp.conver.IProtocolConver;
import com.neusoft.gbw.cp.conver.ProtocolConverFactory;
import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.CollectTask;

public class QueryDataBuilder {

	public static String buildQueryDataV8(CollectTask taskInfo) throws NXmlException {
		IProtocolConver conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV8();
		String xml = conver.encodeQuery((Query)taskInfo.getData().getQuery());
		return xml;
//		return buildHttpPack(taskInfo, xml);
	}
	
	public static String buildQueryDataV7(CollectTask taskInfo) throws NXmlException {
		IProtocolConver conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV7();
		String xml = conver.encodeQuery((Query)taskInfo.getData().getQuery());
		return xml;
//		return buildHttpPack(taskInfo, xml);
	}
	
	public static String buildQueryDataV6(CollectTask taskInfo) throws NXmlException {
		IProtocolConver conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV6();
		String xml = conver.encodeQuery((Query)taskInfo.getData().getQuery());
		return xml;
//		return buildHttpPack(taskInfo, xml);
	}
	
	public static String buildQueryDataV5(CollectTask taskInfo) throws NXmlException {
		IProtocolConver conver = ProtocolConverFactory.getInstance().newProtocolConverServiceV5();
		String xml = conver.encodeQuery((Query)taskInfo.getData().getQuery());
		return xml;
//		return buildHttpPack(taskInfo, xml);
	}
	
//	/**
//	 * 创建通信头信息
//	 * @param strContent
//	 * @return
//	 */
//	private static String buildHttpPack(CollectTask taskInfo, String strContent) {
//		TransferType type = taskInfo.getAttrInfo().getTransferType();
//		if (!type.equals(TransferType.SOCKET)) {
//			return strContent;
//		}
//		SocketTransferAttr socket = (SocketTransferAttr)taskInfo.getAttrInfo().getTransferAttr();
//		String ip = socket.getIpAddress();
//		int port = socket.getPort();
//		
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("POST " + " HTTP/1.1\r\n");
//		buffer.append("Referer: " +  ip  + "\r\n");
//		buffer.append("User-Agent: Mozila\r\n");
//		buffer.append("Connection: close\r\n");
//		buffer.append("Host: " +  ip + ":" + port + "\r\n");
////		buffer.append("POST " + " HTTP/1.1\r\n");
//		try {
//			buffer.append("Content-Length: " + strContent.getBytes("GB2312").length + "\r\n\r\n");
//			buffer.append(strContent);
//		} catch (UnsupportedEncodingException ex) {
//		     Log.error("创建http头信息失败", ex);
//		}
//
//		return strContent;
//	}
		    
}
