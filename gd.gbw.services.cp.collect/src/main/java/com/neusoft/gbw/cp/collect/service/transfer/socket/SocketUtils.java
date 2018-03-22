package com.neusoft.gbw.cp.collect.service.transfer.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import com.neusoft.np.arsf.base.bundle.Log;

public class SocketUtils {
	
  private Socket m_sock = null;
  private String m_strPostContent = null;
  public String m_strGetContent = null;
  private URL m_url = null;
  private int m_nPort;
  private InputStream in = null;
  private OutputStream out = null;

  public SocketUtils(String urlString) throws UnknownHostException, IOException{
      this.m_url = new URL(urlString);

      if (this.m_url.getPort() < 0)
        this.m_nPort = 80;
      else
        this.m_nPort = this.m_url.getPort();

      this.m_sock = new Socket(this.m_url.getHost(), this.m_nPort);
      this.m_sock.setSoTimeout(GetTimerOutPara());
  }
  
  public void postURLRequest(String strPostContent) throws IOException{
      BuildHttpPack(strPostContent);
      Log.info("[socket发送]任务发送完成  协议：" + this.m_strPostContent);
      this.m_sock.setSendBufferSize(this.m_strPostContent.length());
      out = this.m_sock.getOutputStream();
      byte[] byOutStream = this.m_strPostContent.getBytes("GB2312");
      out.write(byOutStream, 0, byOutStream.length);
  }

  public void getURLResponse() throws IOException{
	  in =  this.m_sock.getInputStream();
	  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	  String line;
	  while ((line = reader.readLine()) != null)
	  {
		  System.out.println("任务发送成功，返回：" + line);
	  }
  }
//      String strDescHost = this.m_url.getHost() + ":" + this.m_url.getPort();
//      in = this.m_sock.getInputStream();
//      byte[] ch = new byte[1000];
//      int nLen = 0;
//      int i = 0;
//      while (true) {
//        i++;
//        nLen = in.read(ch);
//        if (nLen == -1)
//          break;
//        if (nLen == 0)
//          continue;
//        
//        this.m_strGetContent += new String(ch, 0, nLen);
//      }
//    }

  public void close(){
    try{
      if(in != null)
    	  in.close();
      if(out != null)
    	  out.close();
      if(this.m_sock != null) {
        this.m_sock.shutdownOutput();
        this.m_sock.shutdownInput();
      }
      if (this.m_sock != null)
          this.m_sock.close();
    }catch (IOException e) {
    }
  }
  
  private void BuildHttpPack(String strContent){
	StringBuffer buffer = new StringBuffer();
	buffer.append("POST " + this.m_url.getPath() + " HTTP/1.1\r\n");
//	buffer.append("POST " + "xmlReceiver/upload" + " HTTP/1.1\r\n");
	buffer.append("Referer: " +  this.m_url.getHost()  + "\r\n");
	buffer.append("User-Agent: Mozila\r\n");
	buffer.append("Connection: close\r\n");
	buffer.append("Host: " +  this.m_url.getHost() + ":" + this.m_nPort + "\r\n");
//	buffer.append("Host: " +  "10.13.2.5" + ":" + "9003/" + "\r\n");
    try{
    	buffer.append("Content-Length: " + strContent.getBytes("GB2312").length + "\r\n\r\n");
		buffer.append(strContent);
    }
    catch (UnsupportedEncodingException ex){
    	Log.error("", ex);
    }
    this.m_strPostContent = buffer.toString();
  }

  private int GetTimerOutPara(){
	  return 10000;
  }
}
