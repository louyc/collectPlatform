package com.neusoft.gbw.cp.process.measure.event.handler;

import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveEvalMsgHandler implements BaseEventHandler {
	
	
	public ReceiveEvalMsgHandler() {
	}

	@Override
	public String getTopicName() {
		return ProcessConstants.JMS_RECEIVE_EVALUATION_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 instanceof JMSDTO) {
//			EvaluationGradeDTO art = (EvaluationGradeDTO)((JMSDTO)arg0).getObj();
//			System.out.print(art.getTableName());
		}
			
		return false;
	}
}

