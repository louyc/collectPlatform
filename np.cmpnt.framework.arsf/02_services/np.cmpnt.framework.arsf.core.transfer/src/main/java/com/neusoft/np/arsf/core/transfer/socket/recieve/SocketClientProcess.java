package com.neusoft.np.arsf.core.transfer.socket.recieve;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatusType;

/**
 * 
 * 项目名称: LM采集处理平台组件化<br>
 * 模块名称: <br>
 * 功能描述: <br>
 * 创建日期: 2014-4-16 <br>
 * 版权信息: Copyright (c) 2014<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liu.bh@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2014-4-16       刘勃宏        创建
 * </pre>
 */
public class SocketClientProcess extends NMService implements ISocketProcess{

	/**
	 * 客户端套接字对象
	 */
	private Socket socket;
	
	/**
	 * 到客户端带缓冲区的输出流（字节）
	 */
	private BufferedOutputStream out;
	
	/**
	 * 到客户端带缓冲区的输入流（字节）
	 */
	private BufferedInputStream in;
	
	/**
	 * 到客户端带缓冲区的输出流（字符）
	 */
	private BufferedWriter writer;
	
	/**
	 * 到客户端带缓冲区的输入流（字符）
	 */
	private BufferedReader reader;
	
	private String charset = "UTF-8";
	
	/**
	 * Socket配置信息
	 */
	private SocketConfig config = null;
	
	private SocketChannel channel = null;
	
	public SocketClientProcess(Socket socket, SocketChannel channel) throws NMSocketException {
		this.socket = socket;
		this.config = (SocketConfig)channel.getTransferConfig().getConfig();
		this.channel = channel;
		createStream();
	}
	
	private void createStream() throws NMSocketException {
		try {
			this.out = new BufferedOutputStream(socket.getOutputStream(), 8192*1000);
			this.in = new BufferedInputStream(socket.getInputStream(), 8192*1000);
			this.writer = new BufferedWriter(new OutputStreamWriter(out, charset));
			this.reader = new BufferedReader(new InputStreamReader(in, charset));
		} catch (IOException e) {
			throw new NMSocketException(e);
		}
	}
	
	@Override
	public void run() {
		try {
			while(isThreadRunning()) {
				while (true) {
					if (Thread.currentThread().isInterrupted()) {
						Log.info("任务" + serviceName + "所在线程因中断而退出。");
						return;
					}
					
					if (!receiveData()){
						break;
					}
				}
			}
		} finally {
			//清空数据结构，即连接断开
			updateTransferStatus(TransferStatusType.STATUS_CLOSE);
			Log.info("任务:" + serviceName + "退出。");
		}
		
	}
	
	/**
	 * 更改连接状态
	 * 
	 * @param state
	 */
	private void updateTransferStatus(TransferStatusType statusType) {
		channel.updateTransferStatus(statusType);
	} 
	
	private boolean receiveData() {
		TransferDataType dataType = config.getDataType();
		Object data = null;
		try {
			switch(dataType) {
			case BYTE:
				data = receive();
				break;
			case STRING:
				data = receiveStr();
				break;
			case OBJECT:
//				data = receiver.receiveObj();
				break;
			default:
				break;
			}
			if(data != null) {
				channel.put(data);
			}
			
			
		} catch (InterruptedException e) {
			return false;
		} catch (NMSocketException e) {
			Log.error("出现Socket异常", e);
			//设置连接状态
			updateTransferStatus(TransferStatusType.STATUS_DISCONNECT);
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 本客户端包装器Socket client资源释放
	 */
	public synchronized void disconnect(){
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				Log.error("socket关闭连接出现问题。设备：" + config.getIp(), e);
			}
		}
		if (out != null) {
			try {
				out.close();
				out = null;
				writer = null;
			} catch (IOException e) {
				Log.error("socket关闭输出流出现问题。设备：" + config.getIp(), e);
			}
		}
		if (in != null) {
			try {
				in.close();
				in = null;
				reader = null;
			} catch (IOException e) {
				Log.error("socket关闭输入流出现问题。设备：" + config.getIp(), e);
			}
		}
	}
	
	/**
	 * 数据发送,将传入的byte字节发送给被采集端。
	 * 
	 * @param data 数据
	 * @throws LMSocketException
	 */
	public synchronized void send(byte[] data) throws NMSocketException {
		try {
			for (int i = 0; i < data.length; i++) {
				out.write(data[i]);
			}
			out.flush();
		} catch (IOException ex) {
			throw new NMSocketException("Socket发送命令失败！", ex);
		}
	}
	
	/**
	 * 数据发送,将传入的字符串发送给被采集端
	 * 
	 * @param data 数据
	 * @throws LMSocketException
	 */
	public synchronized void send(String data) throws NMSocketException {
		try {
			writer.write(data);
			writer.flush();
		} catch (IOException ex) {
			throw new NMSocketException("Socket发送命令失败！", ex);
		}
	}

	/**
	 * 接收函数。负载等待和接收代理端返回的字节数组。
	 * 
	 * @return 代理端执行数据
	 * @throws IOException IO异常
	 * @throws NMSocketException 
	 * @throws NMFormateException 
	 */
	public synchronized byte[] receive() throws NMSocketException {
		int length = 0;
		byte[] bufferBytes = new byte[1024];
		try {
			while (length != -1 || length >= bufferBytes.length) {
				length = in.read(bufferBytes);
			}
		} catch (IOException e) {
			throw new NMSocketException(e);
		} catch (Exception e) {
			throw new NMSocketException(e);
		}
		
		return bufferBytes;
	}
	
	/**
	 * 接收字符串数据
	 * 
	 * @return 接收字符串
	 * @throws NMSSocketException 接收数据过程中异常
	 */
//	public synchronized String receiveStr() throws NMSocketException{
//		int length = 0;
//		char[] charArrays = new char[1024];
//		try {
//			while (length != -1 || length >= charArrays.length) {
//				length = reader.read(charArrays);
//			}
//			
//		} catch (IOException e) {
//			disconnect();
//			throw new NMSocketException(e);
//		}
//		
//		//读取字符数组，转化为字符串；
//		return charArrays.toString();
//	}
	
	public synchronized String receiveStr() throws NMSocketException{
		try {
			return reader.readLine();
		} catch (IOException e) {
			disconnect();
			throw new NMSocketException(e);
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
