package com.neusoft.np.arsf.common.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class NPAmfConverUtil {

	private static final ThreadLocal<NPAmfConverUtil> amfConverUtils = new ThreadLocal<NPAmfConverUtil>() {
		protected NPAmfConverUtil initialValue() {
			return new NPAmfConverUtil();
		}
	};
	
	private SerializationContext context = new SerializationContext();
	private Amf3Output amfOut = new Amf3Output(context);
	
	private NPAmfConverUtil(){}
	
	public static NPAmfConverUtil getInstance() {
		return amfConverUtils.get();
	}
	
	public byte[] convert(Object target) throws IOException  {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		DataOutputStream dataOutStream = new DataOutputStream(byteOutStream);
		amfOut.setOutputStream(dataOutStream);
		amfOut.writeObject(target);
		dataOutStream.flush();
		return byteOutStream.toByteArray();
	}
	
	public void close() throws IOException {
		amfOut.close();
	}
}
