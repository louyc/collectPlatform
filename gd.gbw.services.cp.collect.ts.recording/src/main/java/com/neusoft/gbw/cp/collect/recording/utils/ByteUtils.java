package com.neusoft.gbw.cp.collect.recording.utils;

import java.nio.ByteBuffer;

public class ByteUtils {
	
	public static byte[] getHeader(long id,int status,int size) {
		byte[] result = mergeBytes(getID(id), getStatus(status));
		result = mergeBytes(result, getSize(size));
		return getAllocateByte(result, 17);
	}
	
	public static byte[] getID(long id) {
		return longToBytes(id);
	}
	
	public static byte[] getStatus(int status) {
		byte b = 0;
		byte[] bytes = new byte[1];
		switch(status) {
		case 0:
			b = 0;
			break;
		case 1:
			b = 1;
			break;
		}
		bytes[0] = b;
		return bytes;
	}
	
	public static byte[] getSize(int size) {
		return intToBytes(size);
	}
	
	public static byte[] longToBytes(long n) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, n);  
        return buffer.array();  
	}
	
	public static int getLength(byte[] bytes) {
		byte[] lengthArray = getBytes(bytes, 4, 9);
		return bytesToInt(lengthArray);
	}

	/**
	 * int整型转换为byte数组
	 * 
	 * @param i 整型
	 * @return byte数组
	 */
	public static byte[] intToBytes(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

	
	/**
	 * 获取指定长度字节数组，但长度不够补充空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	private static byte[] getAllocateByte(byte[] src, int length) {
		byte[] bytes = new byte[length];
		if (src == null || src.equals("")) {
			return bytes;
		}
		for(int i = 0 ;i<src.length; i++){
			bytes[i] = src[i];
		}
		return bytes;
		
	}
	
	public static byte[] getBytes(byte[] head, int length, int src) {
		byte[] bytes = new byte[length];
		System.arraycopy(head, src, bytes, 0, length);
		return bytes;
	}
	
	/**
	 * 连接字节数组
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static byte[] mergeBytes(byte[] a1, byte[] a2) {
		byte[] result = new byte[a1.length + a2.length];
		System.arraycopy(a1, 0, result, 0, a1.length);
		System.arraycopy(a2, 0, result, a1.length, a2.length);
		return result;
	}
	
	private static int bytesToInt(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}

}
	
