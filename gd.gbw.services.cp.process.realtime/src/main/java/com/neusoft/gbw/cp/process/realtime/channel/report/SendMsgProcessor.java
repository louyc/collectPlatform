package com.neusoft.gbw.cp.process.realtime.channel.report;

import java.io.Serializable;
import java.util.Map;

import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.net.core.NetEventType;

public class SendMsgProcessor {

	public void sendMsg(Object syntObj) {
		if (syntObj instanceof JMSDTO) {
			ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, syntObj);
		} else if (syntObj instanceof String) {
			ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), (String)syntObj);
		} else {
			Log.warn("实时任务处理类型不存在，" + syntObj);
		}
	}
	
	public JMSDTO createJmsDto(Object obj, int type) {
		JMSDTO dto = new JMSDTO();
		dto.setTypeId(type);
		dto.setObj((Serializable)obj);
		return dto;
	}
	
	public StoreInfo buildStoreInfo(Object qData, String label) {
		Map<String, Object> map = null;
		StoreInfo info = new StoreInfo();
		try {
			map = NMBeanUtils.getObjectField(qData);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		return info;
	}
	
//	private String getCurrentTime() {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return df.format(new Date());
//	}

}
