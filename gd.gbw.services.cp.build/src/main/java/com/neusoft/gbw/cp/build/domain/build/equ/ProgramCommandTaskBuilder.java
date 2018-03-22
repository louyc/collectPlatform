package com.neusoft.gbw.cp.build.domain.build.equ;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AliveQuery;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.Command;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommand;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramInfoQuery;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ReviseTime;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;

public class ProgramCommandTaskBuilder {

	public static Query buildProgramCommand(String monitorId, String command, TaskPriority taskPriority)  throws BuildException {
		Query query = new Query();
		ProgramCommand pro = new ProgramCommand();
		
		query.setVersion(BuildConstants.XML_VERSION_8+"");
		Long msgID = DataUtils.getMsgID(pro);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(BuildConstants.MAX_PRIORITY+"");
		query.setQuery(pro);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("[构建服务]版本控制，获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		
		if(command == null)
			throw new BuildException("[构建服务]版本控制，获取版本控制操作标记信息为空,command=" + command);
		
		//1.重启、2.版本控制、3.授时、4.监控器状态、5.巡检操作（相当于2和4）
		int ptype = Integer.parseInt(command);
		switch(ptype) {
		case 1:
			Command c = new Command();
			c.setName("Reset");
			pro.addCommand(c);
			break;
		case 2:
			pro.addProgramInfoQuery(new ProgramInfoQuery());
			break;
		case 3:
			pro.addReviseTime(new ReviseTime());
			break;
		case 4:
			pro.addAliveQuery(new AliveQuery());
			break;
		case 5:
//			pro.addProgramInfoQuery(new ProgramInfoQuery());
			pro.addAliveQuery(new AliveQuery());
			break;
		}
		
		return query;
	}
	
	
	public static Query buildV5ProgramCommand(String monitorId, String command, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommand pro = new com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommand();
		
		query.setVersion(BuildConstants.XML_VERSION_5+"");
		Long msgID = DataUtils.getMsgID(pro);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(pro);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("[构建服务]版本控制，获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		
		if(command == null)
			throw new BuildException("[构建服务]版本控制，获取版本控制操作标记信息为空 command=" + command);
		
		//1.重启、2.版本控制
		int type = Integer.parseInt(command);
		switch(type) {
		case 1:
			com.neusoft.gbw.cp.conver.v5.protocol.vo.other.Command c = new com.neusoft.gbw.cp.conver.v5.protocol.vo.other.Command();
			c.setName("Reset");
			pro.addCommand(c);
			break;
		case 2:
			pro.addProgramInfoQuery(new com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramInfoQuery());
			break;
		case 5:
			pro.addProgramInfoQuery(new com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramInfoQuery());
			break;
		}
		
		return query;
	}
	
	public static Query buildV6ProgramCommand(String monitorId, String command) throws BuildException {
		Query query = new Query();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommand pro = new com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommand();
		
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		Long msgID = DataUtils.getMsgID(pro);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(BuildConstants.MAX_PRIORITY+"");
		query.setQuery(pro);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("[构建服务]版本控制，获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		
		if(command == null)
			throw new BuildException("[构建服务]版本控制，获取版本控制操作标记信息为空 command=" + command);
		
		//1.重启、2.版本控制
		int type = Integer.parseInt(command);
		switch(type) {
		case 1:
			com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Command c = new com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Command();
			c.setName("Reset");
			pro.addCommand(c);
			break;
		case 2:
			pro.addProgramInfoQuery(new com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramInfoQuery());
			break;
		case 5:
			pro.addProgramInfoQuery(new com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramInfoQuery());
			break;
		}
		
		return query;
	}
	
	public static Query buildV7ProgramCommand(String monitorId, String command, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommand pro = new com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommand();
		
		query.setVersion(BuildConstants.XML_VERSION_7+"");
		Long msgID = DataUtils.getMsgID(pro);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(pro);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("[构建服务]版本控制，获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		
		if(command == null)
			throw new BuildException("[构建服务]版本控制，获取版本控制操作标记信息为空 command=" + command);
		
		//1.重启、2.版本控制、3.授时
		int type = Integer.parseInt(command);
		switch(type) {
		case 1:
			com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Command c = new com.neusoft.gbw.cp.conver.v7.protocol.vo.other.Command();
			c.setName("Reset");
			pro.addCommand(c);
			break;
		case 2:
			pro.addProgramInfoQuery(new com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramInfoQuery());
			break;
		case 3:
			pro.addReviseTime(new com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ReviseTime());
			break;
		case 5:
			pro.addProgramInfoQuery(new com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramInfoQuery());
			break;
		}
		
		return query;
	}
}
