package com.neusoft.np.arsf.core.transfer.jms;

import java.io.Serializable;

import com.neusoft.nms.common.net.jms.exception.NMMQException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;

/**
 * 项目名称: IT监管采集平台<br>
 * 模块名称: 发送服务<br>
 * 功能描述: JMS数据发送任务类<br>
 * 创建日期: 2012-6-29 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br>
 * 
 * @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
 * @version v1.0
 * 
 *          <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-6-29上午10:19:16      马仲佳       创建
 * </pre>
 */
public class JMSSendHandler extends AbstractJMSService {

	public JMSSendHandler(JMSChannel jmsChannel) {
		super(jmsChannel);
	}

	/**
	 * JMS发送
	 * 
	 * @param sendable
	 * @throws InterruptedException 
	 */
	protected boolean transfer(AbstractJMSService abjms) throws InterruptedException {
		// 发送结果
		boolean sendResult = true;
		TransferDataType dataType = abjms.getDataType();
		Object object =abjms.getChannel().take();
		try {
			switch(dataType) {
			case BYTE:
				abjms.getSender().send((byte[])object);
				break;
			case STRING:
				abjms.getSender().send((String)object);
				break;
			case OBJECT:
				abjms.getSender().send((Serializable)object);
				break;
			default:
				break;
			}
		} catch (NMMQException e) {
			Log.error("数据发送失败,准备重新连接,相关通信信息=" + abjms.getChannel().getTransferConfig(), e);
			sendResult = false;
		}
		
		return sendResult;
	}



	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}

}
