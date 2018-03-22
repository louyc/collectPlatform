package com.neusoft.np.arsf.core.transfer.socket.recieve;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;

/**
 * 
 * 项目名称: 处理平台<br>
 * 模块名称: 数据接收服务<br>
 * 功能描述: Socket客户端接口应用<br>
 * 创建日期: 2012-12-13 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-13       刘勃宏        创建 
 * </pre>
 */
public class SocketClient {
	
	/**
	 * 默认超时时间
	 */
	private static final int TIMEOUT = 10000;
	
	/**
	 * 默认重连休眠时间
	 */
	private static final int RECONNECT_SLEEP_TIME = 200;

	/**
	 * 客户端套接字
	 */
	private Socket client;

	/**
	 * 字节缓冲输出流
	 */
	private BufferedOutputStream bufOutputStream;

	/**
	 * 字节缓冲输入流
	 */
	private BufferedInputStream bufInputStream;
	
	/**
	 * 字符输出流
	 */
	private BufferedReader reader = null;
	
	/**
	 * 字符输入流
	 */
	private BufferedWriter writer = null;
	
	/**
	 * Socket配置信息
	 */
	private SocketConfig config = null;
	
	private String charset = "UTF-8";
	
	/**
	 * 初始化时，需要绑定IP地址和端口
	 * 
	 * @coustructor
	 */
	public SocketClient(SocketConfig config) {
		this.config = config;
	}

	/**
	 * 创建socket连接
	 * 
	 * @throws NMSocketException
	 */
	public void connect() throws NMSocketException {
		if (client == null || client.isClosed() || !client.isOutputShutdown()) {
			createSocket();
		}
		createStream();
	}

	/**
	 * 创建socket连接。
	 * 
	 * @throws NMSocketException socket异常
	 */
	private void createSocket() throws NMSocketException {
		boolean success = false;
		try {
			InetAddress addr = InetAddress.getByName(config.getIp());
			SocketAddress sockaddr = new InetSocketAddress(addr, config.getPort());
			client = new Socket();
			client.connect(sockaddr,TIMEOUT);
			client.setReceiveBufferSize(8192*1000);
			success = true;
		} catch (UnknownHostException e) {
			throw new NMSocketException("指示主机 IP 地址无法确定而抛出的异常", e);
		} catch (IOException e) {
			throw new NMSocketException("Socket连接出现IO异常", e);
		} catch (Exception e) {
			throw new NMSocketException("Socket连接出现未知异常", e);
		} finally {
			if (!success) {
				// 没有连接成功，关闭socket连接
				close();
			}
		}
	}

	/**
	 * 初始化输入流和输出流。
	 * 
	 * @throws NMSocketException socket异常
	 */
	private void createStream() throws NMSocketException {
		boolean success = false;
		try {
			bufOutputStream = new BufferedOutputStream(client.getOutputStream());
			bufInputStream = new BufferedInputStream(client.getInputStream());
			reader = new BufferedReader(new InputStreamReader(bufInputStream, charset));
			writer = new BufferedWriter(new OutputStreamWriter(bufOutputStream, charset));
			success = true;
		} catch (IllegalArgumentException e) {
			throw new NMSocketException("Socket缓存区创建失败", e);
		} catch (IOException e) {
			throw new NMSocketException("Socket缓存区创建失败，IO异常", e);
		} catch (Exception e) {
			throw new NMSocketException("Socket缓存区创建失败，未知异常", e);
		} finally {
			if (!success) {
				close();
			}
		}
	}

	/**
	 * 断开socket连接
	 */
	public void disconnect() {
		close();
	}

	/**
	 * 重新创建socket连接，根据配置的最大重连次数进行重连。
	 * 如果有一次重连成果返回true，三次都失败返回false。
	 * 
	 * @return 是否重连成果
	 */
	public void reConnect() {
		int nloopcount = 1;
		while (true) {
			try {
				connect();
				break;
			} catch (NMSocketException e) {
				Log.info("设备：" + config.getIp() + "进行重连 " + nloopcount + "次");
			}
			nloopcount++;
			try {
				Thread.sleep(RECONNECT_SLEEP_TIME);
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
	
	/**
	 * 数据发送,将传入的byte字节发送给被采集端。
	 * 
	 * @param data 数据
	 * @throws NMSocketException
	 */
	public void send(byte[] data) throws NMSocketException {
		try {
			for (int i = 0; i < data.length; i++) {
				bufOutputStream.write(data[i]);
			}
			bufOutputStream.flush();
		} catch (IOException ex) {
			throw new NMSocketException("Socket发送命令失败！", ex);
		}
	}
	
	/**
	 * 数据发送,将传入的字符串发送给被采集端
	 * 
	 * @param data 数据
	 * @throws NMSocketException
	 */
	public void send(String data) throws NMSocketException {
		try {
			writer.write(data);
			writer.flush();
		} catch (IOException ex) {
			throw new NMSocketException("Socket发送命令失败！", ex);
		}
	}

//	/**
//	 * 接收字符串
//	 * 
//	 * @return 接收字符串
//	 * @throws NMSSocketException 接收过程中异常
//	 */
//	public String receiveStr() throws NMSocketException{
//		int length = -1;
//		char[] charArrays = new char[CollectVariable.CHAR_ARRAY_LENGTH];
//		try {
//			//while (length != -1 || length >= charArrays.length) {
//			while (length == -1) {
//				length = reader.read(charArrays);
//			}
//		} catch (IOException e) {
//			close();
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
	
	/**
	 * 接收函数。负载等待和接收代理端返回的字节数组。
	 * 
	 * @return 代理端执行数据
	 * @throws IOException IO异常
	 * @throws NMSocketException 
	 * @throws NMFormateException 
	 */
	public byte[] receive() throws NMSocketException {
		int length = -1;
		byte[] bufferBytes = new byte[1024];
		try {
			length = bufInputStream.read(bufferBytes);
			if (length == 1024) {
				return bufferBytes;
			} else {
				byte[] bytes = new byte[length];
				System.arraycopy(bufferBytes, 0, bytes, 0, length);
				bufferBytes = null;
				return bytes;
			}
		} catch (IOException e) {
			throw new NMSocketException(e);
		} catch (Exception e) {
			throw new NMSocketException(e);
		}
	}
	
	/**
	 * 关闭socket相关连接，关闭socket的输入流和输出流。
	 */
	private void close() {
		if (client != null) {
			try {
				client.close();
				client = null;
			} catch (IOException e) {
				Log.error("socket关闭连接出现问题。设备：" + config.getIp(), e);
			}
		}
		if (bufOutputStream != null) {
			try {
				bufOutputStream.close();
				bufOutputStream = null;
				writer = null;
			} catch (IOException e) {
				Log.error("socket关闭输出流出现问题。设备：" + config.getIp(), e);
			}
		}
		if (bufInputStream != null) {
			try {
				bufInputStream.close();
				bufOutputStream = null;
				reader = null;
			} catch (IOException e) {
				Log.error("socket关闭输入流出现问题。设备：" + config.getIp(), e);
			}
		}
	}
}
