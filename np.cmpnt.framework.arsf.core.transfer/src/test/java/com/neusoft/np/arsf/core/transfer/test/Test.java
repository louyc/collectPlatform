package com.neusoft.np.arsf.core.transfer.test;

import com.neusoft.nms.common.net.jms.NMConnection;
import com.neusoft.nms.common.net.jms.NMConnectionFactory;
import com.neusoft.nms.common.net.jms.exception.NMMQException;

public class Test {
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			NMConnection connection = NMConnectionFactory.createConnection("admin", "admin", "127.0.0.1", 61616, "jh-PC-64388-1419686123595-0:1");
		} catch (NMMQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
