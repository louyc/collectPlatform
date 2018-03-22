package com.neusoft.np.arsf.core.transfer.socket.recieve;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;

public class ClientReceiver extends NMService {

	private String serviceName = null;

	private BlockingQueue<Object> queue = null;

	private Socket socket = null;

	/**
	 * 字符集
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * 输入流(字节)
	 */
	private BufferedReader reader = null;

	public ClientReceiver(Socket socket, BlockingQueue<Object> queue) throws IOException {
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET));
		this.queue = queue;
		this.socket = socket;
	}

	@Override
	public void run() {
		Object result = null;
		while (true) {
			try {
				result = receive();
				if (result == null) {
					//判断连接是否存在
					send();
					sleep(100);
					continue;
				}

				queue.put(result);
			} catch (InterruptedException e) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "线程:" + serviceName + "出现中断异常", e);
				return;
			} catch (Exception e) {
				if (e instanceof SocketException) {
					if (e.getMessage().equals("Socket is closed")) {
						Log.info(TransferConstants.REVCIEVE_SERVICE_NAME + "Socket连接断开，线程" + serviceName + "退出");
					}
				} else {
					Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "socket出现未知异常", e);
				}
				close();

				return;
			}
		}
	}

	/**
	 * 数据接收
	 * 
	 * @return
	 * @throws IOException 
	 */
	private String receive() throws Exception {
		return reader.readLine();
	}

	private void send() throws Exception {
		OutputStream socketOut = socket.getOutputStream();
		socketOut.write("1".getBytes());
		socketOut.flush();
		socketOut.close();
	}

	private void close() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "socket关闭连接出现问题", e);
			}
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				Log.error(TransferConstants.REVCIEVE_SERVICE_NAME + "socket关闭输出流出现问题", e);
			}
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {

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
