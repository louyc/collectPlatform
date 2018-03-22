package com.neusoft.gbw.cp.process.inspect.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.inspect.Activator;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.vo.InspectMonStat;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MonitorDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.nms.net.ping.FpingDetectImpl;
import com.neusoft.nms.net.ping.PingDetect;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class InspectUtils {

	public static boolean isConnect(String ip) {
		boolean isConn = false;
		int iTimeOut = 5;
		try {
			isConn = InetAddress.getByName(ip).isReachable(iTimeOut*1000);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(!isConn){
			//TODO ping组件占用内存高废弃
			PingDetect ping = new FpingDetectImpl();
			//获取项目路径
			String path = getFpingPath(Activator.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			try {
				isConn = ping.getConnectByIp(ip,path);
			} catch (Exception e) {
			}
		}
		return isConn;
	}
	public static void main(String[] args) {
		boolean isConn = false;
		int iTimeOut = 2;
		try {
			isConn = InetAddress.getByName("10.14.112.122").isReachable(iTimeOut*1000);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(isConn);
	}
	
    private static String getFpingPath(String fpingPath) {

	  StringBuffer buffer = new StringBuffer();
	  String[] name = fpingPath.split("/");
	  for(String tmp : name){
	      if (tmp.indexOf(".jar") != -1){
	          break;
	      }
	      
	      if (tmp.indexOf("bin") != -1){
	          break;
	      }
	      
	      buffer.append(tmp + "/");
	  }
	  String fping = buffer.toString();
	  return fping;
	}
    
	public static void sendWeb(MonitorDTO dto) {
		JMSDTO jms = new JMSDTO();
		jms.setObj(dto);
		jms.setTypeId(GBWMsgConstant.C_JMS_MONITOR_ONLINE_STATE_MSG);
		ARSFToolkit.sendEvent(InspectConstants.JMS_SEND_MSG_TOPIC, jms);
//		Log.debug("站点软件存活状态发送至前台，monitorCode=" + dto.getMonitorCode() + ",status=" + dto.getOnlineState());
	}
	
	public static void sendStore(InspectMonStat monistat) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> map = null;
		try {
			map = NMBeanUtils.getObjectField(monistat);
			info.setDataMap(map);
			info.setLabel(InspectConstants.UPDATE_MONI_TRANSFER_RESULT_STORE);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	/**
	 * 针对  ping 站点更新状态
	 * @param monistat
	 */
	public static void sendMonitStore(InspectMonStat monistat) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> map = null;
		try {
			map = NMBeanUtils.getObjectField(monistat);
			info.setDataMap(map);
			info.setLabel(InspectConstants.UPDATE_MONI_STATUS_STORE);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	
	public static int converAlarmType(int status) {
		//monitorStatus 1、连通性正常 2、软件异常  3、连通性异常 4、软件正常
		int alarmType = -1;
		switch(status) {
		case 1:
			alarmType = 101;
			break;
		case 2:
			alarmType = 102;
			break;
		case 3:
			alarmType = 101;
			break;
		case 4:
			alarmType = 102;
			break;
		}
		return alarmType;
	}
}
