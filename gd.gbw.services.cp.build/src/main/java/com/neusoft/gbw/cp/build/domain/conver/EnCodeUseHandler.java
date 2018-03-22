package com.neusoft.gbw.cp.build.domain.conver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.site.MonitorMachine;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControlMode;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;

public class EnCodeUseHandler {
	
	private final static String QualityTaskSet = "1";
	private final static String StreamTaskSet = "2";
	private final static String SpectrumTaskSet = "3";
	private final static String OffsetTaskSet = "4";
//	private final static String QualityRealtimeQuery = "5";
//	private final static String SpectrumRealtimeQuery = "6";
//	private final static String StreamRealtimeQuery = "7";
	private final static String ALL = "-1";
	
	/**
	 * @param type  构建类型
	 * @param monitorID 站点Id
	 * @param firstEnCode 前台传过来的接收机
	 *  1	指标任务
	 *	2	录音任务
	 *	3	频谱任务
	 *	4	频偏任务
	 *	5	实时指标
	 *	6	实时频谱
	 *	7	实时音频
	 *	-1	全部
	 * @return
	 * @throws ConverException 
	 */
	public static String getFirstEnCode(BuildType type, String monitorID, String firstEnCode, int task_build_mode) throws ConverException {
		String enCode = null;
		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Long.parseLong(monitorID));
		if (device == null)
			throw new ConverException("[数据转换异常]获取设备信息为空 ,不存在该站点或该站点不在运行状态，monitorID=" + monitorID);
		//（1）收测单元录音选择默认接收机
		//（2）页面实时音频，实时频谱，和实时频偏使用前台传过来的接收机
		//（3）手动任务设置的接收机选用页面配置的接收机
		//（4）自动采集点任务选择支持该用途的接收机
		//（5）其他定制需求在这里开发
		BusinessTaskType taskType = type.getBusTaskType();
		switch(taskType) {
		case measure_manual_set://设置任务
			//设置任务分为0、自动任务，1、手动任务
			if(task_build_mode == 0) {
				//自动任务使用系统配置的接收机用途中支持操作的接收机
				//如果是自动任务是遥控站任务，按照自动选择走，如果是采集点任务，则按照接收机配置的用途走, 1：采集点，2：遥控站
				int siteType = device.getType_id();//站点类型，遥控站
				int manufacturer_id = device.getManufacturer_id(); //厂商ID,是2的是泰顺的站点
				if(1 == siteType && 2 == manufacturer_id) {//只有泰顺的站点，站点类型是遥控站的站点的任务才按照接收机用途的分配
					ProtocolType proType = type.getProType();
					if(proType.equals(ProtocolType.QualityTaskSet)) {
						enCode = getSetTaskEnCode(device, QualityTaskSet);
					}else if(proType.equals(ProtocolType.OffsetTaskSet)) {
						enCode = getSetTaskEnCode(device, OffsetTaskSet);
					}else if(proType.equals(ProtocolType.SpectrumTaskSet)) {
						enCode = getSetTaskEnCode(device, SpectrumTaskSet);
					}else if(proType.equals(ProtocolType.StreamTaskSet)) {
						enCode = getSetTaskEnCode(device, StreamTaskSet);
					}
				} else enCode = getDefaultEnCode(device);;//除了采集点，其他按照默认走
			}else {
				//手动任务使用前台选择的接收机
				enCode = firstEnCode == null || firstEnCode.equals("") ? "R1": firstEnCode;
			}
			break;
		case measure_auto://收测任务
			enCode = getDefaultEnCode(device);
			break;
		case measure_online://收测任务中的在线监听任务
			enCode = getDefaultEnCode(device);
			break;
		case measure_realtime://实时音频,实时指标，实时频谱任务
			//如果前台传过来的接收机选择自动接收机，如果是空的话就直接从默认接收机中选择
			enCode = firstEnCode == null || firstEnCode.equals("") ? getDefaultEnCode(device) : firstEnCode;
			break;
		case measure_real_record://实时录音任务
			enCode = firstEnCode == null || firstEnCode.equals("") ? getDefaultEnCode(device) : firstEnCode;
			break;
		case measure_manual_recover://回收任务
			enCode = getDefaultEnCode(device);
			break;
		default:
			enCode = firstEnCode != null ? firstEnCode : getDefaultEnCode(device);
			break;
		}
		return enCode;
	}
	
	
	/**
	 * 获取设置任务的接收机
	 * @param device
	 * @param taskType
	 * @return
	 * @throws ConverException
	 */
	private static String getSetTaskEnCode(MonitorDevice device, String taskType) throws ConverException {
		String firstCode = null;
		List<String> resultList = new ArrayList<String>();
		List<MonitorMachine> list = device.getMachineList();
		for(MonitorMachine machine : list) {
			//
			if(machine.getUsage_mode().equals(taskType)) {
				firstCode = machine.getMachine_code();
				return firstCode;
			}
			if (machine.getUsage_mode().equals(ALL)) 
				resultList.add(machine.getMachine_code());
		}
		if(resultList.isEmpty()) {
			throw new ConverException("[数据转换异常]获取设置任务指定接收机用途的接收机信息获取为空，monitorID=" + device.getMonitor_id());
		}
		Random ran = new Random();
		int index = ran.nextInt(resultList.size());
		firstCode = resultList.get(index);
		if(firstCode == null) {
			throw new ConverException("[数据转换异常]获取设置任务指定接收机用途的接收机信息获取为空 ，monitorID=" + device.getMonitor_id());
		}
		return firstCode;
	}
	
	/**
	 * 获取实时收测接收机，该接收机从默认接收机中进行选择
	 * @param device
	 * @param type
	 * @return
	 * @throws ConverException
	 */
	private static String getDefaultEnCode(MonitorDevice device) throws ConverException {
		String firstCode = null;
		List<String> resultList = new ArrayList<String>();
		List<MonitorMachine> list = device.getMachineList();
		for(MonitorMachine machine : list) {
			if (machine.getIs_default() == BuildConstants.RECEIVE_DEFAULT) {
				resultList.add(machine.getMachine_code());
			}
		}
		if(resultList.isEmpty()) {
			throw new ConverException("[数据转换异常]获取默认接收机信息获取为空，monitorID=" + device.getMonitor_id());
		}
		
		long monitorId = device.getMonitor_id();
		for(String code : resultList) {
			if(isFreeCode(monitorId, code)) {
				firstCode = code;
				break;
			}
		}
//		Random ran = new Random();
//		int index = ran.nextInt(resultList.size());
//		String firstCode = resultList.get(index);
//		if(firstCode == null) {
//			throw new ConverException("[数据转换异常]首选接收机信息获取为空 ，monitorID=" + device.getMonitor_id());
//		}
		return firstCode == null ? resultList.get(0) : firstCode;
	}
	
	private static boolean isFreeCode(long monitorId, String code) {
		if(EquStatusControlMode.getInstance().getStationControl(monitorId) == null)
			return true;
		return EquStatusControlMode.getInstance().getStationControl(monitorId).isFreeByEquCode(code+monitorId);
	}
	
	
//	/**
//	 * 获取站点配置的接收机，该设置主要用于非泰顺的站点使用，后续是否其他厂商需要使用再根据具体需求在定
//	 * @param device
//	 * @param type
//	 * @return
//	 * @throws ConverException
//	 */
//	private static String getSetEnCode(MonitorDevice device) throws ConverException {
//		
//		List<String> resultList = new ArrayList<String>();
//		List<MonitorMachine> list = device.getMachineList();
//		for(MonitorMachine machine : list) {
//			if (machine.getIs_default() == BuildConstants.RECEIVE_DEFAULT) {
//				resultList.add(machine.getMachine_code());
//			}
//		}
//		if(resultList.isEmpty()) {
//			throw new ConverException("[数据转换异常]获取默认接收机信息获取为空，monitorID=" + device.getMonitor_id());
//		}
//		Random ran = new Random();
//		int index = ran.nextInt(resultList.size());
//		String firstCode = resultList.get(index);
//		if(firstCode == null) {
//			throw new ConverException("[数据转换异常]首选接收机信息获取为空 ，monitorID=" + device.getMonitor_id());
//		}
//		return firstCode;
//	}
}
