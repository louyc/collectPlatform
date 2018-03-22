package com.neusoft.np.arsf.cluster.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;


public class ClusterSetVirtualIPAddress {
	
	public void platformSetIPAddress(String ipAddrSetInfo) {
		//判断此网卡名称对应的IP地址是否修改成功，如果失败是因为IP冲突的问题
		String interfaceName = ConfigVariable.PLATFORM_INTERFACE_NAME;
		String ip = ipAddrSetInfo.split(",")[0];
		int count = 1;
		while(true) {
			//修改指定网卡名称的IP地址
			changeIPAddress(ipAddrSetInfo);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			String ipAddr = getInterfaceSetIPaddr(interfaceName);
			if (!ipAddr.equals(ip)) {
				Log.warn("平台集群IP地址第" + (count++) +"次设置失败，InterfaceName=" + interfaceName + "，IPAddress=" + ipAddr + "，应设置IPAddress=" + ip);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				continue;
			}
			Log.info("平台集群网卡IP设置成功，InterfaceName=" + interfaceName + "," + getOutArray(ipAddrSetInfo.split(",")));
			break;
		}
	
	}
	
	private boolean changeIPAddress(String ipAddrSetInfo) {
		boolean result = false;
		String interfaceName = ConfigVariable.PLATFORM_INTERFACE_NAME;
		String[] setInfoArray = ipAddrSetInfo.split(",");
		try {
			execIPAddrCommand(interfaceName, setInfoArray);
			result = true;
		} catch (Exception e) {
			Log.error("切换IP信息出现异常", e);
		}
		
		return result;
	}
	
	public String getInterfaceSetIPaddr(String interfaceName) {
		String ipAddr = null;
		Process p = null;
		try {
			String command = "cmd.exe /c ipconfig";
			p = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(),"gbk"));
			String line = null;
			boolean isInterfaceName = false;
			while((line = reader.readLine()) != null) {
				Log.debug("获取网卡信息，line=" + line.trim());
				if (line.trim().indexOf(interfaceName) != -1) {
					//差找到指定网卡的名称
					isInterfaceName = true;
				}
				if (isInterfaceName) {
					if (line.trim().indexOf("IPv4") != -1) {
						ipAddr = line.trim().split(":")[1].trim();
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ipAddr;
	}
	
	private String getOutArray(String[] array) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[IP：" + array[0] + ",");
		buffer.append("Mask：" + array[1] + ",");
		buffer.append("Gateway：" + array[2] + "]");
		
		return buffer.toString();
	}
	
	protected void execIPAddrCommand(String name, String[] array) throws Exception {
		String ip = array[0];
		String mask = array[1];
		String gateway = array[2];
		String command = getUpdateIPCommand(name, ip, mask, gateway);
		Runtime run = Runtime.getRuntime();
		run.exec(command);
	}
	
	private String getUpdateIPCommand(String name, String ip, String mask, String gateway) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("netsh interface ip set address ");
		buffer.append("name=\"" + name + "\" source=static ");
		buffer.append("addr=" + ip + " mask=" + mask + " gateway=" + gateway);
		
		return buffer.toString();
	}
	
	public static void main(String[] args) throws Exception {
	}
}
