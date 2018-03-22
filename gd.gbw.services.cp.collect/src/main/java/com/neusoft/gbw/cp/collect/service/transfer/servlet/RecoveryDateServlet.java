package com.neusoft.gbw.cp.collect.service.transfer.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.service.transfer.TransferUpMgr;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecoveryDateServlet extends HttpServlet {
	
	private TransferUpMgr transferUpMgr;

	public RecoveryDateServlet(TransferUpMgr transferUpMgr) {
		this.transferUpMgr = transferUpMgr;
	}

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,HttpServletResponse response){  
		response.setContentType("text/html");  
        PrintWriter out = null;  
        try{  
        	out = response.getWriter();
            out.println("Recieve Service is running");  
            out.flush();  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }finally{  
            if(out != null){  
                out.close();  
            }  
        } 

    }
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{  
		String line = "";
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"GB2312"))  ;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String protcolXml =buffer.toString();
//			if(protcolXml.contains("QualityAlarmHistoryReport")&&protcolXml.contains("报警解除")){
//				Log.debug("报警历史查询   暂不支持    过滤掉");
//				Thread.sleep(100);
//				return;
//			}else{
			if(null!=protcolXml && protcolXml.length()>0 && protcolXml.contains("SrcCode") && protcolXml.contains("DstCode")){
				String monitorCode = protcolXml.substring(protcolXml.indexOf("SrcCode")+9,protcolXml.indexOf("DstCode")-2);
				Log.info("接收数据：" + protcolXml+"    /n"+monitorCode);
				if(0==CollectTaskContext.getModel().getCodeConfMap(monitorCode)){
					Log.info("采集平台没有此站点");
					return;
				}
			}
			transferUpMgr.recieveRecovertDate(protcolXml);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		catch (InterruptedException e) {
//		}
		finally{
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.error("servlet接收数据创建的流关闭失败", e);
				}
			}
		} 
		
		
    }
	
	public static void main(String[] args) {
		String ss = "Msg Version='8' MsgID='2' Type='RadioUp' DateTime='2002-08-17 15:30:00' SrcCode='R61D01' DstCode='CBT01'";
		System.out.println(ss.substring(ss.indexOf("SrcCode")+9,ss.indexOf("DstCode")-2));
	}
}
