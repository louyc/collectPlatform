package com.neusoft.gbw.cp.build.domain.conver;

import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskAttribute;

public class DelTaskConverHandler extends AbstractTaskConverHandler{
	
	@Override
	BuildType buildBuildType(TaskAttribute taskAttr,String taskType, ProtocolType oriProType) {
		BuildType type = new BuildType(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del, oriProType);
		return type;
	}
}
