package com.neusoft.gbw.cp.collect.recording.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamRecord;
import com.neusoft.np.arsf.base.bundle.Log;

public class V8RecordProcess implements IRecord{
	
	protected static final Logger log = Logger.getLogger("V8RecordingProcess");
	
	private Socket client = null;
	private StreamParam info = null;
	private OutputStream output = null;
	private InputStream input = null;
	private String ip = null;
	private String port = null;
	private String channel = null;
	private boolean isStop = false;
	

	@Override
	public void init(StreamParam info) {
		String streamMediaUrl = info.getUrl();
		if(streamMediaUrl.startsWith("vsaudio")){
			streamMediaUrl = streamMediaUrl.replace("vsaudio://", "");
			this.ip = streamMediaUrl.split(":")[0];
			this.port = streamMediaUrl.split(":")[1].split("/")[0];
			this.channel = streamMediaUrl.split(":")[1].split("/")[1];
		}else if(streamMediaUrl.startsWith("Rttp")){
			streamMediaUrl = streamMediaUrl.replace("Rttp://", "");
			this.ip = streamMediaUrl.split(":")[0];
			this.port = streamMediaUrl.split(":")[1].split("/")[0];
			this.channel = streamMediaUrl.split(":")[1].split("/")[1];
		}else if(streamMediaUrl.startsWith("Rtcp")){
			streamMediaUrl = streamMediaUrl.replace("Rtcp://", "");
			this.ip = streamMediaUrl.split(":")[0];
			this.port = streamMediaUrl.split(":")[1].split("/")[0];
			this.channel = streamMediaUrl.split(":")[1].split("/")[1];	
		}else if(streamMediaUrl.startsWith("ltp")){
			streamMediaUrl = streamMediaUrl.replace("ltp://", "");
			this.ip = streamMediaUrl.split(":")[0];
			this.port = streamMediaUrl.split(":")[1].replace("/", "");
			this.channel = "0";
		}else if(streamMediaUrl.startsWith("TTCP")){
			streamMediaUrl = streamMediaUrl.replace("TTCP://", "");
			this.ip = streamMediaUrl.split(":")[0];
			this.port = streamMediaUrl.split(":")[1].replace("/", "");
			if(streamMediaUrl.split(":")[1].split("/").length>1)
				this.channel = streamMediaUrl.split(":")[1].split("/")[1];
			else this.channel = "s";
		}
		this.info = info;
	}
	
	@Override
	public StreamRecord start() {
		int recordLength = 0;
		int recordStatus = 0;
		List<byte[]> cutFileByte = new ArrayList<byte[]>();
		try {
			Log.debug("[录音操作]启动录音线程,设备IP=" + ip + ",url=" + info.getUrl());
			client = new Socket(ip, Integer.parseInt(port));
			client.setSoTimeout(5000);//客户端读取服务的数据超时时间
			output = client.getOutputStream();
			output.write(channel.getBytes());
			output.flush();
			Log.debug("[录音操作]设备IP=" + ip + "，接收机通道" + channel + "设置完成......");
			input = client.getInputStream();
			long now = System.currentTimeMillis();
			long recordDuration = info.getRecordLength()*1000;
			Log.debug("[录音操作]接收机开始数据采集,录音时长=" + recordDuration);
			byte[] data_temp = new byte[176400];
			byte[] data = null;
			while (System.currentTimeMillis() < now + recordDuration) {
				if(isStop)
					break;
				int length = input.read(data_temp, 0,176400);
				data = new byte[length];
				if (length > 0) {
					System.arraycopy(data_temp, 0, data, 0, length);
					cutFileByte.add(data);
					recordLength += length;
				}
			}
			recordStatus = 1;
			Log.debug("[录音操作]接收机结束数据采集,param=" + info.toString() + ",recordLength=" + recordLength);
			data_temp = null;
			data = null;
		} catch (SocketException e) {
			recordStatus = 0;
			Log.error("[录音操作]读取站点音频流超时，此音频视为漏采,param=" + info.toString(),e);
		}catch(IOException e){
			recordStatus = 0;
			Log.error("[录音操作]连接站点网关超时，此音频视为漏采,param=" + info.toString(),e);
		}finally {
			try {
				closePut();
			} catch (IOException e) {
				Log.error("[录音操作]录音线程关闭出现异常,停止采集音频，此音频视为漏采,param=" + info.toString(),e);
			}
		}
		StreamRecord record = createRecord(cutFileByte, recordLength, recordStatus);
		cutFileByte = null;
		return record;
	}
	
	private StreamRecord createRecord(List<byte[]> stream, int recordLength, int status) {
		StreamRecord record = new StreamRecord();
		record.setRecordLength(recordLength);
		record.setRecordStatus(status);
		record.setStream(stream);
		return record;
	}
	
	private void closePut() throws IOException {
		if (client != null) {
			client.shutdownInput();
			client.close();
			client = null;
		}
		if (input != null) {
			input.close();
			input = null;
		}
		if (output != null) {
			output.close();
			output = null;
		}
	}

	@Override
	public void stop() {
		this.isStop = true;
	}
	
}
