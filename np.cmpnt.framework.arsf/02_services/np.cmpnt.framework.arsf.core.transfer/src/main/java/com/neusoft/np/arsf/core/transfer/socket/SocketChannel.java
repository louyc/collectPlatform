package com.neusoft.np.arsf.core.transfer.socket;

import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.service.AbstractChannel;
import com.neusoft.np.arsf.core.transfer.socket.recieve.SocketClientHander;
import com.neusoft.np.arsf.core.transfer.socket.recieve.SocketServerHander;
import com.neusoft.np.arsf.core.transfer.socket.send.SocketClientKeepSendTask;
import com.neusoft.np.arsf.core.transfer.socket.send.SocketClientShortSendTask;
import com.neusoft.np.arsf.core.transfer.socket.send.SocketServerSendTask;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: socket管道封装类<br>
* 创建日期: 2012-6-29 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-29下午12:00:48      马仲佳       创建
* </pre>
 */
public class SocketChannel extends AbstractChannel {

	public SocketChannel(TransferConfig config) {
		super(config);
	}

	@Override
	protected NMService createTask() {
		TransferType type = this.getTransferConfig().getType();
		SocketConfig sconfig = (SocketConfig) this.getTransferConfig().getConfig();
		NMService service = null;
		
		if (sconfig.getSocketType() == TransferConstants.SOCKET_TYPE_SERVER) {
			//作为Socket服务端的数据发送
			switch(type) {
			case SEND:
				service = new SocketServerSendTask(this);
				
				break;
			case RECEIVE:
				service = new SocketServerHander(this);
				break;
			}
		}
		if (sconfig.getSocketType() == TransferConstants.SOCKET_TYPE_CLIENT) {
			if (sconfig.getConnectType() == TransferConstants.SOCKET_CLIENT_CONNECT_TYPE_KEEP) {
				//作为Socket客户端长连接的数据发送
				switch(type) {
				case SEND:
					service = new SocketClientKeepSendTask(this);
					break;
				case RECEIVE:
					service = new SocketClientHander(this);
					break;
				}
			} else if (sconfig.getConnectType() == TransferConstants.SOCKET_CLIENT_CONNECT_TYPE_SHORT) {
				//作为Socket客户端短连接的数据发送
				service = new SocketClientShortSendTask(this);
			}
		}
		
		return service;
	}
}
