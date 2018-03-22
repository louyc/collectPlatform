package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;

public class StreamDispatchProcess {
	
	public void dispose(CollectTask task) {
		IStreamTaskProcess process = null;
		String force = null;
		String command = null;
		String type = null;
		RealTimeStreamDTO streamDTO =  getDto(task);
		force = streamDTO.getForce();
		command = streamDTO.getCommand();
		type = streamDTO.getType();
		
		if(ProcessConstants.STREAM_TYPE.equalsIgnoreCase(type)) {
			process = new RealStreamTaskHandler();
			if(ProcessConstants.IS_FORCE.equalsIgnoreCase(force) && ProcessConstants.START_COMMAND.equalsIgnoreCase(command)) {
				process.disposeForceStart(task);
			}else if(ProcessConstants.IS_FORCE.equalsIgnoreCase(force) && ProcessConstants.STOP_COMMAND.equalsIgnoreCase(command)) {
				process.disposeForceStop(task);
			}else if(ProcessConstants.NO_FORCE.equalsIgnoreCase(force) && ProcessConstants.START_COMMAND.equalsIgnoreCase(command)) {
				process.disposeStart(task);
			}else if(ProcessConstants.NO_FORCE.equalsIgnoreCase(force) && ProcessConstants.STOP_COMMAND.equalsIgnoreCase(command)) {
				process.disposeStop(task);
			}
		}
		else {
			process = new RealRecordTaskHandler();
			if(ProcessConstants.IS_FORCE.equalsIgnoreCase(force) && ProcessConstants.START_COMMAND.equalsIgnoreCase(command)) {
				process.disposeForceStart(task);
			}else if(ProcessConstants.IS_FORCE.equalsIgnoreCase(force) && ProcessConstants.STOP_COMMAND.equalsIgnoreCase(command)) {
				process.disposeForceStop(task);
			}else if(ProcessConstants.NO_FORCE.equalsIgnoreCase(force) && ProcessConstants.START_COMMAND.equalsIgnoreCase(command)) {
				process.disposeStart(task);
			}else if(ProcessConstants.NO_FORCE.equalsIgnoreCase(force) && ProcessConstants.STOP_COMMAND.equalsIgnoreCase(command)) {
				process.disposeStop(task);
			}
		}
	}
	
	private RealTimeStreamDTO getDto(CollectTask task) {
		RealTimeStreamDTO dto = (RealTimeStreamDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		return dto;
	}

}
