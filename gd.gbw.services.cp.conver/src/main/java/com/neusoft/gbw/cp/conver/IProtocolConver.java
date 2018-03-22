package com.neusoft.gbw.cp.conver;

import com.neusoft.gbw.cp.conver.exception.NXmlException;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;

public interface IProtocolConver {

	public Query decodeQuery(String xml) throws NXmlException;

	public Report decodeReport(String xml) throws NXmlException;

	public String encodeQuery(Query queryInfo) throws NXmlException;

	public String encodeReport(Report reportInfo) throws NXmlException;

}
 