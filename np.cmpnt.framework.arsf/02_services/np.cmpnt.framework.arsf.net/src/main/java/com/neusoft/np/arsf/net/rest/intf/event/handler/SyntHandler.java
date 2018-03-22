package com.neusoft.np.arsf.net.rest.intf.event.handler;

import java.util.Map;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.net.core.NetEventType;
import com.neusoft.np.arsf.net.rest.domain.msg.MsgDecodeException;
import com.neusoft.np.arsf.net.rest.domain.msg.SyntMsgDecode;
import com.neusoft.np.arsf.net.rest.domain.vo.SyntVO;
import com.neusoft.np.arsf.net.rest.infra.condition.ConditionPoolException;
import com.neusoft.np.arsf.net.rest.infra.context.ClassContext;
import com.neusoft.np.arsf.net.rest.intf.event.JSonMsgHandler;

public class SyntHandler extends JSonMsgHandler {

	private static ClassContext context = ClassContext.getContext();

	@Override
	public String getTopicName() {
		return NetEventType.REST_SYNT.name();
	}

	@Override
	public boolean processMapEvent(Map<String, String> eventData) {
		try {
			SyntVO revo = SyntMsgDecode.mapToVo(eventData);
			String taskId = revo.getTaskId();
			if(null!=context.getTsp().get(taskId)){
				Log.debug("REST_SYNT****************************"+taskId);
				SyntVO save = context.getTsp().getT(taskId);
				context.getTsp().update(taskId, merge(save, revo));
				context.getCtp().signal(taskId);
			}
		} catch (ConditionPoolException e) {
			Log.error("", e);
		} catch (MsgDecodeException e) {
			Log.error("", e);
		}
		return true;
	}

	private SyntVO merge(SyntVO fir, SyntVO sec) {
		fir.setResponse(sec.getResponse());
		return fir;
	}

	@Override
	public void initHandler() {
	}

	@Override
	public void clearHandler() {
	}

	@Override
	public void batchUpdata() throws NMException {
	}

}
