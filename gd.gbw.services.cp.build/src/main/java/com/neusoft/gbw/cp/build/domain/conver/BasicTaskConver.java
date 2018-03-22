package com.neusoft.gbw.cp.build.domain.conver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;

public class BasicTaskConver {
	
	/**
	 * 获取不同任务提取到的接收机
	 * @param type
	 * @param monitorID
	 * @param firstEnCode
	 * @param task_build_mode
	 * @return
	 * @throws ConverException
	 */
	protected String getFirstEnCode(BuildType type, String monitorID, String firstEnCode, int task_build_mode) throws ConverException {
		return EnCodeUseHandler.getFirstEnCode(type, monitorID, firstEnCode, task_build_mode);
	}
	
	protected BuildInfo buildInfo(BuildType type, String monitorID, String firstEnCode, Object obj, TaskType taskType) throws ConverException {
		BuildInfo info = new BuildInfo();
		info.setTaskType(taskType);
		info.setType(type);
		info.setDevice(getMonitorDevice(monitorID, firstEnCode, type));
		info.setBuisness(obj);
		info.setUseFtp(false);
		return info;
	}
	/**
	 * 效果类 任务修改  lyc  站点+接收机 多个
	 * @param type
	 * @param map
	 * @param obj
	 * @param taskType
	 * @return
	 * @throws ConverException
	 */
	protected BuildInfo buildInfo(BuildType type, Map<String,String> map, Object obj, TaskType taskType) throws ConverException {
		BuildInfo info = new BuildInfo();
		List<MonitorDevice> lmd = new ArrayList<MonitorDevice>();
		info.setTaskType(taskType);
		info.setType(type);
		for(String monitor: map.keySet()){
			lmd.add(getMonitorDevice(monitor, map.get(monitor), type));
		}
		info.setDeviceList(lmd);
		info.setDevice(lmd.get(0));
		info.setBuisness(obj);
		return info;
	}

	protected BuildInfo buildInfo(ProtocolType pType, BusinessTaskType bType, ProtocolType oriType, String monitorID, String firstEnCode, Object obj, TaskType taskType) throws ConverException {
		return buildInfo(new BuildType(pType, bType, oriType), monitorID, firstEnCode, obj, taskType);
	}
	
	private MonitorDevice getMonitorDevice(String monitorID, String firstEnCode, BuildType type) throws ConverException {
		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Long.parseLong(monitorID));
		device.setFirstEnCode(firstEnCode);
		return device;
	}
	
	/**
	 * 原来逻辑
	 * @param monitorID
	 * @param firstEnCode
	 * @param type
	 * @return
	 * @throws ConverException
	 */
//	private MonitorDevice getMonitorDevice(String monitorID, String firstEnCode, BuildType type) throws ConverException {
//		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Integer.parseInt(monitorID));
//		if (device == null)
//			throw new ConverException("[数据转换异常]获取设备信息为空 ,不存在该站点或该站点不在运行状态，monitorID=" + monitorID);
//		
//		if(firstEnCode != null && !firstEnCode.equals(""))
//			device.setFirstEnCode(firstEnCode.trim());
//		else 
//			device.setFirstEnCode(getfirstEnCode(device, type));
//		
//		return device;
//	}
	
//	/**
//	 * 首选接收机，确定某任务选择那个接收机
//	 * @param device
//	 * @param type
//	 * @return
//	 * @throws ConverException
//	 */
//	private String getfirstEnCode(MonitorDevice device, BuildType type) throws ConverException {
//		//判断站点的默认接收机只适用于实时音频的任务，其他任务暂时不能应用默认接收机
//		if (!ProtocolType.StreamRealtimeQuery.equals(type.getProType())) {
//			Log.debug("[接收机选择]当前任务没有选择接收机，不属于实时音频不分配默认接收机。" + type.toString());
//			return "";
//		}
//		
//		List<String> resultList = new ArrayList<String>();
//		List<MonitorMachine> list = device.getMachineList();
//		for(MonitorMachine machine : list) {
//			if (machine.getIs_default() == BuildConstants.RECEIVE_DEFAULT) {
//				resultList.add(machine.getMachine_code());
//			}
//		}
//		if(resultList.isEmpty()) {
//			throw new ConverException("[数据转换异常]获取默认接收机信息获取为空 ，monitorID=" + device.getMonitor_id());
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
