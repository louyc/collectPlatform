package com.neusoft.np.arsf.core.transfer.jms;

import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.service.AbstractChannel;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: jms管道封装类<br>
* 说明:通道实例对象的生命周期分为创建、准备、运行、结束，一旦处于结束状态，则不运行继续使用，需要重新创建管道对象<br>
* 创建日期: 2012-6-25 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-25       马仲佳       创建
* </pre>
 */
public class JMSChannel extends AbstractChannel {
	
	public JMSChannel(TransferConfig config) {
		super(config);
	}

	@Override
	protected NMService createTask() {
		TransferType type = this.getTransferType();
		NMService service = null;
		switch(type) {
		case SEND:
			service = new JMSSendHandler(this);
			break;
		case RECEIVE:
			service = new JMSReceiveHandler(this);
			break;
		}
		return service;
	}
}
