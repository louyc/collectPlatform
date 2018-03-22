package com.neusoft.np.arsf.core.transfer.jms;

import com.neusoft.nms.common.net.jms.exception.NMMQException;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;

public class JMSReceiveHandler extends AbstractJMSService {

	public JMSReceiveHandler(JMSChannel channel) {
		super(channel);
	}

	@Override
	public boolean transfer(AbstractJMSService abjms) {
		Object result = null;
		TransferDataType dataType = abjms.getDataType();
		try {
			switch(dataType) {
			case BYTE:
				result = abjms.getReceiver().receiveByte();
				break;
			case STRING:
				result = abjms.getReceiver().receiveStr();
				break;
			case OBJECT:
				result = abjms.getReceiver().receiveObj();
				break;
			default:
				break;
			}
			
			if (result == null) {
				Thread.sleep(10);
			} else {
				Log.info("接收到数据：" + result);
//				abjms.getChannel().put(result);
				ARSFToolkit.sendEvent(abjms.getChannel().getTransferConfig().getTopicName(), result);
			}
		} catch (InterruptedException e) {
			Log.warn(TransferConstants.REVCIEVE_SERVICE_NAME  + "任务" + serviceName + "所在线程因中断而退出。");
			return false;
		} catch (NMMQException e) {
			Log.error(TransferConstants.REVCIEVE_SERVICE_NAME  + "数据接收失败。", e);
			return false;
		}

		return true;
	}

	
	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
