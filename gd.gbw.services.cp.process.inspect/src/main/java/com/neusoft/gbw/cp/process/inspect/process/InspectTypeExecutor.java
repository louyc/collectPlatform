package com.neusoft.gbw.cp.process.inspect.process;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.inspect.vo.InspectType;
import com.neusoft.np.arsf.base.bundle.Log;

public class InspectTypeExecutor {
   //巡检任务类型   设备状态  任务状态  版本查询
	public static InspectType getInspectType(CollectTask task) {
		InspectType type = null;
		ProtocolData data = task.getData();
		if(data == null){ 
			Log.warn("[实时处理服务]未找到实时任务中的协议数据，protocolData=" + data);
			return type;
		}
		ProtocolType proType = data.getType();
		switch(proType) {
		case EquStatusRealtimeQuery:
			type = InspectType.equ_status;
			break;
		case TaskStatusQuery:
			type = InspectType.task_status;
			break;
		case ProgramCommand:
			type = InspectType.program_status;
			break;
		default:
			break;
		}
		return type;
	}
}
