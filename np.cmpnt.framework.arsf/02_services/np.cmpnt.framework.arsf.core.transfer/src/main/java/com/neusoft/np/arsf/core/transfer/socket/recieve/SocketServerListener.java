package com.neusoft.np.arsf.core.transfer.socket.recieve;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NMServiceCentre;
import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatusType;

/**
 * 
 * 项目名称: 处理平台<br>
 * 模块名称: 数据接收服务<br>
 * 功能描述: 启动Socket服务端的监听<br>
 * 创建日期: 2012-12-12 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-12       刘勃宏        创建 
 * </pre>
 */
public class SocketServerListener extends NMService {

	private String serviceName = null;

	/**
	 * 服务端监听的端口
	 */
	private SocketConfig config = null;

	/**
	 * 服务端套接字句柄
	 */
	private ServerSocket server = null;

	/**
	 * 线程控制集合
	 */
	private NMServiceCentre servicePool = null;
	
	private SocketChannel channel = null;

	public SocketServerListener(SocketChannel channel) {
		this.config = (SocketConfig)channel.getTransferConfig().getConfig();
		this.channel = channel;
		servicePool = new NMServiceCentre();
	}

	/**
	 * 监听初始化，创建Socket服务端连接
	 * 
	 * @throws NMSocketException
	 */
	public void init() throws NMSocketException {
		try {
			server = new ServerSocket(config.getPort());
		} catch (BindException e) {
			close();
			throw new NMSocketException(e);
		} catch (IllegalArgumentException e) {
			close();
			throw new NMSocketException(e);
		} catch (IOException e) {
			close();
			throw new NMSocketException(e);
		}
	}

	@Override
	public void run() {
		int count = 0;
		while (true) {
			if (isThreadRunning()) {
				break;
			}
			try {
				Socket socket = server.accept();
				socket.setKeepAlive(true);
				if (!onConnect(socket)) {
					Log.warn("非法接入设备");
					continue;
				}
				Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "接收到设备：" + socket.getInetAddress().getHostAddress() + "的一个连接");
				addClientProcess(socket, count++);
			} catch (IOException e) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "Socket服务端接收连接异常", e);
			} catch (Exception e) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "出现未知异常", e);
			}
		}
	}
	
	/**
	 * 添加连接客户端的Socket处理
	 * 
	 * @param socket
	 * @throws NMSocketException 
	 * @throws IOException 
	 */
	private void addClientProcess(Socket socket, int count) throws NMSocketException {
		NMService process = new SocketClientProcess(socket, channel);
		process.setServiceName(channel.getTopicName() + "#接收线程" + count);
		Log.info("任务：" + serviceName + "启动线程：" + channel.getTopicName() + "#接收线程" + count);
		channel.updateTransferStatus(TransferStatusType.STATUS_CONNECT);
		servicePool.addService(process);
	}
	
	/**
	 * 判断是否为非法IP
	 * 
	 * @param socket
	 * @return
	 */
	private boolean onConnect(Socket socket) {
		return true;
	}

	/**
	 * 关闭服务端Socket
	 */
	public void close() {
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
			}
			server = null;
		}
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
